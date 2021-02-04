package com.onetwothree.onetwothree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

public class GameOverFragment extends Fragment {

    private MainActivity mainActivity;
    public View mainView;

    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_ACHIEVEMENT_UI = 9003;

    public GameOverFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ott_end, container, false);

        TextView end_result = mainView.findViewById(R.id.ott_end_result);
        TextView end_ad_achievement = mainView.findViewById(R.id.ad_achievement);
        ImageButton end_replay = mainView.findViewById(R.id.ott_end_replay);
        ImageButton end_close = mainView.findViewById(R.id.ott_end_close);
        ImageButton end_achievement = mainView.findViewById(R.id.ott_start_achievement);
        ImageButton end_leaderboard = mainView.findViewById(R.id.ott_start_leaderboard);
        ImageButton end_continue = mainView.findViewById(R.id.ott_end_continue);
        ImageButton end_share = mainView.findViewById(R.id.ott_start_share);

        end_result.setText(Integer.toString((int)mainActivity.getResultScore()));
        end_ad_achievement.setText("광고보고 이어하기 ( " + (3 - mainActivity.replayCount) + "번 남음 )");

        end_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                mainActivity.replayCount = 0;
                mainActivity.replayScore = 0;
                mainActivity.switchFragment(1);
            }
        });
        end_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                mainActivity.finish();
            }
        });
        end_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                if(account != null) showLeaderboard();
            }
        });
        end_achievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                if(account != null) showAchievements();
            }
        });
        end_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                if(mainActivity.replayCount >= 3){

                    Toast.makeText(getActivity(), "3번의 기회를 모두 사용하셨어요. 재도전 해보세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                end_replay.setEnabled(false);
                end_achievement.setEnabled(false);
                end_close.setEnabled(false);
                end_leaderboard.setEnabled(false);
                end_share.setEnabled(false);


                mainActivity.mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mainActivity.mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        mainActivity.replayScore = (int)mainActivity.getResultScore();
                        mainActivity.replayCount++;

                        end_replay.setEnabled(true);
                        end_achievement.setEnabled(true);
                        end_close.setEnabled(true);
                        end_leaderboard.setEnabled(true);
                        end_share.setEnabled(true);

                        mainActivity.switchFragment(1);
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        Toast.makeText(getActivity(), "광고 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        if (mainActivity.mInterstitialAd.isLoaded()) {
                            mainActivity.mInterstitialAd.show();
                        }
                    }
                });
            }
        });
        end_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTouchSound();
                ScreenShot();
            }
        });



        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) {
            Games.getLeaderboardsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                    .submitScore(getString(R.string.leaderboard_id), (int) mainActivity.getResultScore());

            mainActivity.achievementManager.setAchievement((int)mainActivity.getResultScore());
            mainActivity.achievementManager.setIncrementAchievement();
        }

        return mainView;
    }

    private void buttonTouchSound(){
        mainActivity.soundManager.playSound(0);
    }

    //화면 캡쳐하기
    public void ScreenShot(){

        View view = getActivity().getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        //캐시를 비트맵으로 변환
        Bitmap screenBitmap = Bitmap.createBitmap(view.getDrawingCache());

        try {

            File cachePath = new File(getActivity().getApplicationContext().getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(getActivity().getApplicationContext().getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    "com.onetwothree.onetwothree.fileprovider", newFile);

            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
            Sharing_intent.setType("image/png");
            Sharing_intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(Sharing_intent, "Share image using"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showLeaderboard() {
        Games.getLeaderboardsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }


    private void showAchievements() {
        Games.getAchievementsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }
}

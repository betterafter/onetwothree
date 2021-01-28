package com.onetwothree.onetwothree;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameOverFragment extends Fragment {

    private MainActivity mainActivity;
    public View mainView;
    private static final int RC_LEADERBOARD_UI = 9004;

    public GameOverFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ott_end, container, false);

        TextView end_result = mainView.findViewById(R.id.ott_end_result);
        ImageButton end_replay = mainView.findViewById(R.id.ott_end_replay);
        ImageButton end_close = mainView.findViewById(R.id.ott_end_close);

        ImageButton start_leaderboard = mainView.findViewById(R.id.ott_start_leaderboard);
        start_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLeaderboard();
            }
        });

        end_result.setText(Integer.toString((int)mainActivity.getResultScore()));
        end_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.switchFragment(1);
            }
        });

        end_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.finish();
            }
        });


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account != null) Games.getLeaderboardsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .submitScore(getString(R.string.leaderboard_id), (int)mainActivity.getResultScore());

        return mainView;
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
}

package com.onetwothree.onetwothree;

import android.os.Bundle;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    double resultScore = 0;
    public int replayScore = 0;
    public int replayCount = 0;

    private final int REQCODE = 1001;

    public InterstitialAd mInterstitialAd;
    public boolean isAdShown = false;

    public AchievementManager achievementManager;
    public SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundManager = new SoundManager(this);
        soundManager.addSound(0, R.raw.button);
        soundManager.addSound(1, R.raw.correct);
        soundManager.addSound(2, R.raw.move);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5909086836185335/3643553736");

        achievementManager = new AchievementManager(this, findViewById(R.id.main_framelayout));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_framelayout, new GameStartFragment(this));
        fragmentTransaction.commit();

    }


    public void switchFragment(int current){
        Fragment fragment;
        if(current == 0){
            fragment = new GameStartFragment(this);
        }
        else if(current == 1){
            fragment = new GamePlayFragment(this);
        }
        else{
            fragment = new GameOverFragment(this);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, fragment);
        fragmentTransaction.commit();
    }

    public void setResultScore(double resultScore){
        this.resultScore = resultScore;
    }

    public double getResultScore(){
        return resultScore;
    }

    private void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                        System.out.println("signed out");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //signOut();
    }
}
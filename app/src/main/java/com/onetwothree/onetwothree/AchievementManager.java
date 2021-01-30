package com.onetwothree.onetwothree;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;

import androidx.appcompat.app.AppCompatActivity;

public class AchievementManager extends AppCompatActivity {

    MainActivity mainActivity;
    View view;

    public AchievementManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public AchievementManager(MainActivity mainActivity, View view){
        this.mainActivity = mainActivity;
        this.view = view;
    }

    public void setAchievement(int score){
        if(score >= 100){
            Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                    .unlock(mainActivity.getString(R.string.achievement_1));
        }

        if(score >= 1000){
            Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                    .unlock(mainActivity.getString(R.string.achievement_2));
        }

        if(score >= 5000){
            Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                    .unlock(mainActivity.getString(R.string.achievement_3));
        }

        if(score >= 10000){
            Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                    .unlock(mainActivity.getString(R.string.achievement_4));
        }

        if(score >= 50000){
            Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                    .unlock(mainActivity.getString(R.string.achievement_5));
        }

        GamesClient gamesClient = Games.getGamesClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity));
        gamesClient.setViewForPopups(view);
    }

    public void setIncrementAchievement(){

        Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                .increment(mainActivity.getString(R.string.achievement_6), 1);

        Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                .increment(mainActivity.getString(R.string.achievement_7), 1);

        Games.getAchievementsClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity))
                .increment(mainActivity.getString(R.string.achievement_8), 1);

        GamesClient gamesClient = Games.getGamesClient(mainActivity, GoogleSignIn.getLastSignedInAccount(mainActivity));
        gamesClient.setViewForPopups(view);
    }
}

package com.onetwothree.onetwothree;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameStartFragment extends Fragment {

    private MainActivity mainActivity;
    public View mainView;

    private static final int REQCODE = 100;

    public GameStartFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        signIn();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ott_start, container, false);

        ImageButton start_play_button = mainView.findViewById(R.id.ott_start_play_button);
        start_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.switchFragment(1);
            }
        });

        return mainView;
    }


    private void signIn() {

        GoogleSignInOptions signInOptions
                = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            System.out.println("Already signed in.");
            GoogleSignInAccount signedInAccount = account;
            GamesClient gamesClient = Games.getGamesClient(getActivity(), signedInAccount);
            gamesClient.setViewForPopups(mainView);

        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            System.out.println("Haven't been signed-in before. Try the silent sign-in first.");
            GoogleSignInClient signInClient = GoogleSignIn.getClient(getActivity(), signInOptions);
            signInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        // The signed in account is stored in the task's result.
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                        GamesClient gamesClient = Games.getGamesClient(getActivity(), signedInAccount);
                                        gamesClient.setViewForPopups(mainView);
                                    } else {
                                        System.out.println("failed");
                                        startSignInIntent();
                                    }
                                }
                            });
        }
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(getActivity(),
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, REQCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                GamesClient gamesClient = Games.getGamesClient(getActivity(), signedInAccount);
                gamesClient.setViewForPopups(mainView);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.onetwothree.onetwothree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GamePlayFragment extends Fragment {

    private MainActivity mainActivity;
    private View mainView;
    private timeChecker timeChecker;
    private changeNextNumber nextNumber;
    private boolean gameOver = false;
    private double time = -1;

    private String MAX = "99999999";

    private Button button1, button2, button3;
    private ToggleButton toggleButton1, toggleButton2, toggleButton3, toggleButton4;
    private ImageView button_highlight;
    private LinearLayout operator_layout;

    private int toggleType = 1;

    private ProgressBar progressBar;
    private TextView ott_play_score;

    private int[] position;
    private float posY;

    private boolean buttonSoundPlay = false;

    public GamePlayFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ott_play, container, false);

        progressBar = mainView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);

        ott_play_score = mainView.findViewById(R.id.ott_play_score);
        TextView ott_play_current = mainView.findViewById(R.id.ott_play_current);
        TextView ott_play_next =  mainView.findViewById(R.id.ott_play_next);

        int a = (int)(Math.random() * 3) + 1;
        int b = (int)(Math.random() * 3) + 1;
        int c = (int)(Math.random() * 3) + 1;

        int makeCurrentText = 1 * a + 2 * b + 3 * c;
        ott_play_score.setText(Integer.toString(mainActivity.replayScore));
        ott_play_next.setText(Integer.toString(makeCurrentText));

        operator_layout = mainView.findViewById(R.id.operator_layout);
        button_highlight = mainView.findViewById(R.id.button_highlight);

        operator_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(operator_layout.getHeight() != 0){
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, operator_layout.getHeight() / 4
                    );
                    button_highlight.setLayoutParams(params);

                    posY = operator_layout.getHeight() / 4;
                    position = new int[]{0, (int)posY, (int)posY * 2, (int)posY * 3};
                    operator_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        toggleButton1 = mainView.findViewById(R.id.button_add);
        toggleButton2 = mainView.findViewById(R.id.button_minus);
        toggleButton3 = mainView.findViewById(R.id.button_multiple);
        toggleButton4 = mainView.findViewById(R.id.button_division);

        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.soundManager.playSound(2);
            }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.soundManager.playSound(2);
            }
        });
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.soundManager.playSound(2);
            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.soundManager.playSound(2);
            }
        });

        MoveButtonHighlight moveButtonHighlight = new MoveButtonHighlight();
        moveButtonHighlight.start();

        // 초기화
        toggleButton2.setChecked(false);
        toggleButton3.setChecked(false);
        toggleButton4.setChecked(false);
        toggleButton1.setChecked(true);

        toggleType = 1;
        toggleButton1.setTextColor(getActivity().getResources().getColor(R.color.white));
        toggleButton2.setTextColor(getActivity().getResources().getColor(R.color.black));
        toggleButton3.setTextColor(getActivity().getResources().getColor(R.color.black));
        toggleButton4.setTextColor(getActivity().getResources().getColor(R.color.black));
        //..............................

        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                }
                else{
                    toggleButton1.setChecked(true);
                }
                toggleType = 1;
                setToggleButtonTextColor(1);
            }
        });
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toggleButton1.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                }
                else{
                    toggleButton2.setChecked(true);
                }
                toggleType = 2;
                setToggleButtonTextColor(2);
            }
        });
        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toggleButton1.setChecked(false);
                    toggleButton2.setChecked(false);
                    toggleButton4.setChecked(false);
                }
                else{
                    toggleButton3.setChecked(true);
                }
                toggleType = 3;
                setToggleButtonTextColor(3);
            }
        });
        toggleButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toggleButton1.setChecked(false);
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                }
                else{
                    toggleButton4.setChecked(true);
                }
                toggleType = 4;
                setToggleButtonTextColor(4);
            }
        });

        button1 = mainView.findViewById(R.id.ott_play_1_button);
        button2 = mainView.findViewById(R.id.ott_play_2_button);
        button3 = mainView.findViewById(R.id.ott_play_3_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(ott_play_current, ott_play_next, 2);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(ott_play_current, ott_play_next, 3);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(ott_play_current, ott_play_next, 5);
            }
        });


        time = 0;
        if(timeChecker != null) timeChecker = null;
        timeChecker = new timeChecker();
        timeChecker.start();
        return mainView;
    }

    private class MoveButtonHighlight extends Thread {
        public void run() {
            while(time < 5){
                if(position == null) continue;
                float move = posY / 12;
                try{
                    if(button_highlight.getY() < position[toggleType - 1]){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    button_highlight.setY(button_highlight.getY() + move);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else if(button_highlight.getY() > position[toggleType - 1]){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    button_highlight.setY(button_highlight.getY() - move);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if(Math.abs(button_highlight.getY() - position[toggleType - 1]) <= move * 10){
                        button_highlight.setY(position[toggleType - 1]);
                    }
                    sleep(1);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    private void setToggleButtonTextColor(int num){

        if(num == 1){
            toggleButton1.setTextColor(getActivity().getResources().getColor(R.color.white));
            toggleButton2.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton3.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton4.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        else if(num == 2){
            toggleButton1.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton2.setTextColor(getActivity().getResources().getColor(R.color.white));
            toggleButton3.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton4.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        else if(num == 3){
            toggleButton1.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton2.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton3.setTextColor(getActivity().getResources().getColor(R.color.white));
            toggleButton4.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        else if(num == 4){
            toggleButton1.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton2.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton3.setTextColor(getActivity().getResources().getColor(R.color.black));
            toggleButton4.setTextColor(getActivity().getResources().getColor(R.color.white));
        }
    }

    private void buttonClick(TextView current, TextView next, double i){

        mainActivity.soundManager.playSound(0);
        // 연산하는 부분
        double currentText = Double.parseDouble(current.getText().toString());
        double nextText =  Double.parseDouble(next.getText().toString());
        double calc;
        if(toggleType == 1) calc = currentText + i;
        else if(toggleType == 2) calc = currentText - i;
        else if(toggleType == 3) calc = currentText * i;
        else if(toggleType == 4) calc = currentText / i;
        else return;

        String res = Double.toString(calc);
        if((int) calc == calc) res = String.format("%.0f", calc);

        current.setText(res);

        // 숫자가 같아질 경우 = 옳은 답을 냈을 경우
        if(calc == nextText){
            if(time >= 4.999f) return;
            button1.setEnabled(false); button2.setEnabled(false); button3.setEnabled(false);

            mainActivity.soundManager.playSound(1);
            ott_play_score
                    .setText(Integer.toString(Integer.parseInt(ott_play_score.getText().toString()) + (int)calc));

            int a = (int)(Math.random() * 10) + 1;
            int b = (int)(Math.random() * 10) + 1;
            int c = (int)(Math.random() * 10) + 1;

            int makeCurrentText = 1 * a + 2 * b + 3 * c;
            int result;
            int type = (int)(Math.random() * 10) + 1;
            if(type < 4 && makeCurrentText < nextText || calc >= Integer.parseInt(MAX)) {
                result = (int)(nextText - makeCurrentText);
                next.setText(Integer.toString(Integer.parseInt(next.getText().toString()) + 1));
            }
            else {
                result = (int)(nextText + makeCurrentText);
                next.setText(Integer.toString(Integer.parseInt(next.getText().toString()) - 1));
            }

            nextNumber = new changeNextNumber(result, next);
            nextNumber.start();
            time = 0;
        }
    }

    private class changeNextNumber extends Thread{

        int next;
        TextView nextView;

        public changeNextNumber(int next, TextView nextView){
            this.next = next;
            this.nextView = nextView;
        }

        public void run(){
            while(Integer.parseInt(nextView.getText().toString()) != next){
                try {
                    int curr = Integer.parseInt(nextView.getText().toString());
                    if(curr < next) curr = curr + 1;
                    else if(curr > next) curr = curr - 1;
                    nextView.setText(Integer.toString(curr));
                    time = 0;
                    sleep(10);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(button1 != null && button2 != null && button3 != null)
                        button1.setEnabled(true); button2.setEnabled(true); button3.setEnabled(true);
                }
            });
            time = 0;
            nextNumber = null;
        }
    }

    private class timeChecker extends Thread{
        public void run(){
            while(!gameOver){
                try {
                    double tempTime = time;
                    if(Integer.parseInt(ott_play_score.getText().toString()) <= 1000) time += 0.001f;
                    else if(Integer.parseInt(ott_play_score.getText().toString()) <= 5000) time += 0.0015f;
                    else if(Integer.parseInt(ott_play_score.getText().toString()) <= 15000) time += 0.00175f;
                    else if(Integer.parseInt(ott_play_score.getText().toString()) <= 30000) time += 0.002f;
                    else if(Integer.parseInt(ott_play_score.getText().toString()) <= 50000) time += 0.0022f;
                    else time += 0.0025f;
                    if(mainActivity.isAdShown) time = tempTime;
                    progressBar.setProgress((int)((5 - time) * 20));
                    if(time >= 5){
                        timeChecker = null;
                        mainActivity.setResultScore(Double.parseDouble(ott_play_score.getText().toString()));
                        mainActivity.switchFragment(2);
                        break;
                    }
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

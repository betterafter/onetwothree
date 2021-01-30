package com.onetwothree.onetwothree;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.HashMap;

public class SoundManager {

    private SoundPool mSoundPool;
    private HashMap<Integer,Integer> mSoundPoolMap;
    private Context mContext;

    public SoundManager(Context context){
        this.mContext = context;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mSoundPool = new SoundPool.Builder().build();
        else mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        mSoundPoolMap = new HashMap<>();
    }

    public void addSound(int index, int sound_id){
        mSoundPoolMap.put(index, mSoundPool.load(mContext, sound_id, 1));
    }

    public int playSound(int index){
        return mSoundPool.play(mSoundPoolMap.get(index),1,1,1,0,1f);
    }

    public void stopSound(int streamId){
        mSoundPool.stop(streamId);
    }

    public void pauseSound(int streamId){
        mSoundPool.pause(streamId);
    }

    public void resumeSound(int streamId){
        mSoundPool.resume(streamId);
    }
}

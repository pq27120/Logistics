package com.huaren.logistics.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.huaren.logistics.R;

public class SoundPoolUtil {
  private Context context;
  private SoundPool soundPool;
  private int right;
  private int wrong;

  public SoundPoolUtil(Context context) {
    this.context = context;
    this.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
    right = this.soundPool.load(context, R.raw.righttip , 1);
    wrong = this.soundPool.load(context, R.raw.wrongtip , 1);
  }

  public void playRight() {
    soundPool.play(right, 1, 1, 0, 0, 1);
  }

  public void playWrong() {
    soundPool.play(wrong, 1, 1, 0, 0, 1);
  }

  public Context getContext() {
    return context;
  }

  public SoundPool getSoundPool() {
    return soundPool;
  }

  public int getRight() {
    return right;
  }

  public int getWrong() {
    return wrong;
  }
}

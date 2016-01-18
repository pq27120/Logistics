package com.huaren.logistics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity{
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void backClick(View view) {
    finish();
  }
}

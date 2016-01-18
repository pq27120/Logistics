package com.huaren.logistics.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.huaren.logistics.R;
import com.huaren.logistics.entity.UpdateInfo;
import com.huaren.logistics.login.LoginActivity;
import com.huaren.logistics.main.MainActivity;

public class SplashActivity extends Activity implements ISplashView {

  private SplashPresenter presenter;

  private ProgressDialog pd;    //进度条对话框

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    presenter = new SplashPresenter(this);
    presenter.checkUpdate();
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override public void showUpdateProgress() {
    if (pd == null) {
      pd = new ProgressDialog(this);
      pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      pd.setMessage("正在下载更新");
    }
    pd.show();
  }

  @Override public void hideProgress() {
    pd.dismiss();
  }

  @Override public void showUpdateDialog(final UpdateInfo updateInfo) {
    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
    builder.setTitle(updateInfo.getDescription());
    // 强制升级
    builder.setCancelable(false);
    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override public void onCancel(DialogInterface dialog) {
        // 进入主页面
        dialog.dismiss();
      }
    });

    builder.setMessage(updateInfo.getBak());
    builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        presenter.updateApk();
      }
    });
    if ("true".equals(updateInfo.getIsupdate())) {
      builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
          finishActivity();
        }
      });
    } else {
      builder.setNegativeButton("稍后升级", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          enterMain();
        }
      });
    }
    builder.show();
  }

  @Override public void finishActivity() {
    finish();
  }

  @Override public void changeProgress(int total) {
    pd.setProgress(total);
  }

  @Override public void enterMain() {
    startActivity(new Intent(this, MainActivity.class));
    this.finishActivity();
  }

  @Override public void enterLogin() {
    startActivity(new Intent(this, LoginActivity.class));
    this.finishActivity();
  }
}

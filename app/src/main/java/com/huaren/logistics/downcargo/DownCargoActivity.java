package com.huaren.logistics.downcargo;

import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicButtonsCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.customer.CustomProgress;

public class DownCargoActivity extends BaseActivity implements IDownCargoView {

    //private PullToRefreshListView ptrlvDateInfo = null;

    private ButtonRectangle updateButton;

    private DownCargoPresenter presenter;

    private MaterialListView mListView;

    private Dialog dialog;    //进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downcargo);
        //ptrlvDateInfo = (PullToRefreshListView) findViewById(R.id.down_cargo_list);
        mListView = (MaterialListView) findViewById(R.id.down_cargo_listview);
        updateButton = (ButtonRectangle) findViewById(R.id.btn_update);
        ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.down_cargo);
        presenter = new DownCargoPresenter(this);
        updateButton.setOnClickListener(new UpdateButtonClick());
        dialog = CustomProgress.show(this, "下载中...", false, null);
        downData();
    }

    /**
     * 用Handler来更新UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            mListView.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void setAdapter(DownCargoAdapter adapter) {
        //ptrlvDateInfo.setAdapter(adapter);
    }

    @Override
    public void hideUpdateView() {
    }

    @Override
    public void showUpdateView(String time, String info) {
        mListView.clearAll();
        Card card = new Card.Builder(this)
                .withProvider(BasicButtonsCardProvider.class)
                .setTitle("更新成功")
                .setDescription("更新时间：" + time + "," + info)
                .endConfig().build();
        mListView.add(card);
        handler.sendEmptyMessage(0);
        mListView.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
    }

    @Override
    public AssetManager getAssetManager() {
        return getAssets();
    }

    @Override
    public void finishActivity() {
        handler.sendEmptyMessage(0);
        finish();
    }

    private class UpdateButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mListView.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
            dialog = CustomProgress.show(DownCargoActivity.this, "下载中...", false, null);
            downData();
        }
    }

    private void downData() {
        presenter.downloadOrderData();
        presenter.downloadDictData();
        presenter.downloadDictValueData();
    }
}

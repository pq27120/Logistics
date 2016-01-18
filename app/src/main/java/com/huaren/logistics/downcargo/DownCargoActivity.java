package com.huaren.logistics.downcargo;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

public class DownCargoActivity extends BaseActivity implements IDownCargoView{

  private ListView downCargoListView;

  private DownCargoPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_downcargo);
    downCargoListView = (ListView) findViewById(R.id.down_cargo_list);
    ((TextView)findViewById(R.id.tv_common_title)).setText(R.string.down_cargo);
    presenter = new DownCargoPresenter(this);
    presenter.initList();
  }

  @Override public void showProgress() {

  }

  @Override public void hideProgress() {

  }

  @Override public void setAdapter(DownCargoAdapter adapter) {
    downCargoListView.setAdapter(adapter);
  }
}

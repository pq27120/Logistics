package com.huaren.logistics.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.huaren.logistics.R;
import com.huaren.logistics.entity.MainMenuItem;
import java.util.List;

public class MainGridViewAdapter extends BaseAdapter {

  private Context mContext;
  private List<MainMenuItem> mList;

  public MainGridViewAdapter(Context mContext, List<MainMenuItem> mList) {
    this.mContext = mContext;
    this.mList = mList;
  }

  @Override public int getCount() {
    return mList.size();
  }

  @Override public MainMenuItem getItem(int position) {
    return mList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grideview_main, null);

    Button button = (Button) convertView.findViewById(R.id.btn_item_main_function);
    TextView textView = (TextView) convertView.findViewById(R.id.tx_item_main_function);
    MainMenuItem mainMenuItem = mList.get(position);
    int draw = (mContext.getResources()
        .getIdentifier(mainMenuItem.getImageId(), "drawable", mContext.getPackageName()));
    String text = mainMenuItem.getName();
    button.setBackgroundResource(draw);
    textView.setText(text);
    return convertView;
  }
}

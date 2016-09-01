package com.huaren.logistics.myrecycleinput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

/**
 * Created by bj on 2016/9/1.
 */
public class MyRecycleInputDetailActivity extends BaseActivity implements IMyRecycleInputDetailView{
    private MyRecycleInputDetailPresent recycleInputDetailPresent;
    private MaterialListView mListView;
    private String orderBatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecycle_input_detail);
        init();
    }

    public static void actionStart(Context context, String orderBatchId) {
        Intent intent = new Intent(context, MyRecycleInputDetailActivity.class);
        intent.putExtra("orderBatchId", orderBatchId);
        context.startActivity(intent);
    }

    @Override
    public void backClick(View view) {
        MyRecycleInputActivity.actionStart(MyRecycleInputDetailActivity.this);
        super.backClick(view);
    }

    @Override
    public void addCard(Card card) {
        mListView.add(card);
    }

    public void init() {
        initUserInfo();
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mListView.clearAll();
        ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_myrecycle_input_title);
        Intent intent = getIntent();
        orderBatchId = intent.getStringExtra("orderBatchId");
        recycleInputDetailPresent = new MyRecycleInputDetailPresent(this);
        recycleInputDetailPresent.initRecycleInputList(orderBatchId);
    }
}

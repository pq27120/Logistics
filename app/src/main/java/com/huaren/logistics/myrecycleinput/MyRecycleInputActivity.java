package com.huaren.logistics.myrecycleinput;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;

/**
 * Created by bj on 2016/8/31.
 */
public class MyRecycleInputActivity extends BaseActivity implements IMyRecycleInputView {
    private MaterialListView mListView;

    private MyRecycleInputPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecycle_input);
        initUserInfo();
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(Card card, int position) {
                Log.d("CARD_TYPE", card.getTag().toString());
                Intent intent = new Intent(MyRecycleInputActivity.this, MyRecycleInputDetailActivity.class);
                intent.putExtra("orderBatchId", card.getTag().toString());
                startActivity(intent);
                finish();
            }

            @Override public void onItemLongClick(Card card, int position) {
                Log.d("LONG_CLICK", card.getTag().toString());
            }
        });
        ((TextView) findViewById(R.id.tv_common_title)).setText(R.string.activity_myrecycle_input_title);
        presenter = new MyRecycleInputPresenter(this);
        presenter.initCargoList();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyRecycleInputActivity.class);
        context.startActivity(intent);
    }

    @Override public void addCard(Card card) {
        mListView.add(card);
    }
}

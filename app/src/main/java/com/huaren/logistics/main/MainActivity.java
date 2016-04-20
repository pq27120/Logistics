/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.huaren.logistics.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.huaren.logistics.BaseActivity;
import com.huaren.logistics.R;
import com.huaren.logistics.cargo.CargoActivity;
import com.huaren.logistics.downcargo.DownCargoActivity;
import com.huaren.logistics.evaluation.EvaluationActivity;
import com.huaren.logistics.info.InfoActivity;
import com.huaren.logistics.recycleinput.RecycleInputActivity;
import com.huaren.logistics.recyclescan.RecycleScanDetailActivity;
import com.huaren.logistics.uncargo.UnCargoActivity;
import com.huaren.logistics.uploadcargo.UploadCargoActivity;

import java.util.List;

public class MainActivity extends BaseActivity implements MainView, AdapterView.OnItemClickListener {

    private GridView gridView;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.main_gridview);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        initUserInfo();
        (findViewById(R.id.common_back_btn)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tv_common_title)).setText(R.string.app_name);
        presenter = new MainPresenter(this);
        presenter.initGridView();
        gridView.setOnItemClickListener(new GridViewItemClick());
    }

    @Override protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
    }

    @Override public void hideProgress() {
    }

    @Override public void setItems(List<String> items) {
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void setGridViewAdapter(MainGridViewAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    @Override public void enterDownCargo() {
        startActivity(new Intent(this, DownCargoActivity.class));
    }

    @Override public void enterCargo() {
        startActivity(new Intent(this, CargoActivity.class));
    }

    @Override public void enterUnCargo() {
        startActivity(new Intent(this, UnCargoActivity.class));
    }

    @Override public void enterEvaluaton() {
        startActivity(new Intent(this, EvaluationActivity.class));
    }

    @Override public void enterInfo() {
        startActivity(new Intent(this, InfoActivity.class));
    }

    @Override public void enterRecycle() {
        startActivity(new Intent(this, RecycleInputActivity.class));
    }

    @Override public void enterRecycleScan() {
        startActivity(new Intent(this, RecycleScanDetailActivity.class));
    }

    @Override
    public void enterUploadRecord() {
        startActivity(new Intent(this, UploadCargoActivity.class));
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    private class GridViewItemClick implements AdapterView.OnItemClickListener {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            presenter.mainMenuItemClick(position);
        }
    }
}

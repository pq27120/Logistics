package com.huaren.logistics.downcargo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huaren.logistics.R;
import com.huaren.logistics.bean.Customer;
import java.util.List;

public class DownCargoAdapter extends BaseAdapter {

  private Context context;
  private List<Customer> customerList;

  public DownCargoAdapter(Context context, List<Customer> customerList) {
    this.context = context;
    this.customerList = customerList;
  }

  @Override public int getCount() {
    return customerList.size();
  }

  @Override public Customer getItem(int position) {
    return customerList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(context).inflate(R.layout.item_list_down, null);
    TextView customerIdView = (TextView) convertView.findViewById(R.id.customer_id);
    TextView customerNameView = (TextView) convertView.findViewById(R.id.customer_name);
    TextView unloadSizeView = (TextView) convertView.findViewById(R.id.unload_tv);
    TextView loadSizeView = (TextView) convertView.findViewById(R.id.load_tv);
    Customer customer = customerList.get(position);
    customerIdView.setText(customer.getCustomerId());
    customerNameView.setText(customer.getName());
    int unload = 0;
    int load = 0;
    //if (customer.getGoods() != null && !customer.getGoods().isEmpty()) {
    //  for (int i = 0; i < customer.getGoods().size(); i++) {
    //    Goods goods = customer.getGoods().get(i);
    //    if (goods.getIsLoad()) {
    //      load++;
    //    } else {
    //      unload++;
    //    }
    //  }
    //}
    unloadSizeView.setText(unload + "");
    loadSizeView.setText(load + "");
    return convertView;
  }

  public void addInfo(List<Customer> customerList) {
    this.customerList.addAll(customerList);
  }
}

package shop.smartbazaar.smartbazaar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import shop.smartbazaar.smartbazaar.Cities_model;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class Cities_adapter extends BaseAdapter {

    Context context;

    ArrayList<Cities_model> arrayList;
    public Cities_adapter(Context context,ArrayList<Cities_model> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getArea_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null,false);

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(arrayList.get(position).area_name);
        textView.setTypeface(Utiles.Main_font(context,"kufi-r"));

        view.setTag(arrayList.get(position).getArea_id());

        return  view;
    }
}

package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subhajitkar.commercial.projet_tulip.R;

import java.util.ArrayList;

public class DialogListAdapter extends BaseAdapter {

    private ArrayList<ListObject> list;
    private Context context;

    public DialogListAdapter(Context context, ArrayList<ListObject> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_dialog_list_item, parent, false);
            //widgets initialization
            viewHolder.text = convertView.findViewById(R.id.tv_dialog);
            viewHolder.icon = convertView.findViewById(R.id.iv_dialog);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //adding data
        viewHolder.text.setText(list.get(position).getText());
        viewHolder.icon.setImageResource(list.get(position).getIcon());
        viewHolder.icon.setTag(position);
        return convertView;
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
    }
}

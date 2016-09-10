package com.app.uni.betrack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cevincent on 10/09/2016.
 */
public class UtilsListAdapter extends BaseAdapter {
    private Context context;
    private List<UtilsListBean> ListElements;

    public UtilsListAdapter(Context context, List<UtilsListBean> ListElements) {
        this.context = context;
        this.ListElements = ListElements;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListBeanHolder holder = null;
        holder = new ListBeanHolder();
        holder.listBean = ListElements.get(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.survey_row_scrolling, parent, false);
        holder.survey_list_element = (TextView) row.findViewById(R.id.survey_list_element);


        row.setTag(holder);
        holder.survey_list_element.setText(holder.listBean.getListELement());

        int height = parent.getMeasuredHeight();
        row.setMinimumHeight(height/3);

        return row;
    }

    public static class ListBeanHolder {
        UtilsListBean listBean;
        TextView survey_list_element;
    }

    @Override
    public int getCount() {
        return ListElements.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

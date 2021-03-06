package com.james.zoo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 101716 on 2017/6/16.
 */

public class EquipmentAdapter extends BaseAdapter {
    private String TAG = EquipmentAdapter.class.getSimpleName();
    private ArrayList<EquipmentItem> mListItems;
    private LayoutInflater inflater;
    private Context mContext;
    private ImageView imageView;
    private ArrayList<EquipmentItem> mEquipData;
    private EquipmentAdapter mEquipAdapter;
    private ViewHolder holder;
    private EquipmentItem equitem;
    private int layoutResourceId;

    public EquipmentAdapter(Context context, int layoutResourceId, ArrayList<EquipmentItem> itemList) {
        this.mListItems = itemList;
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public EquipmentItem getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        EquipmentItem item = (EquipmentItem) mListItems.get(position);
        Long idInt = Long.valueOf(item.getId());
        //Long idInt = Long.parseLong(item.getId());
        return idInt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View row = inflater.inflate(R.layout.activity_equiment_layout, parent, false);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        holder = new ViewHolder();
        EquipmentItem items = mListItems.get(position);
        holder.imageView = convertView.findViewById(R.id.imageView_pic);
        holder.item = convertView.findViewById(R.id.tv_item);
        holder.item.setText(items.getS_item());
        holder.location = convertView.findViewById(R.id.tv_location);
        holder.location.setText(items.getS_location());
        holder.distance = convertView.findViewById(R.id.tv_distance);
        holder.distance.setText(items.getS_geo());
        holder.unit = convertView.findViewById(R.id.tv_unit);
        if (items.getKiller().contains("里")) {
            holder.unit.setText("公里");
        } else {
            holder.unit.setText("公尺");
        }
        holder.summary = convertView.findViewById(R.id.tv_summary);
        holder.summary.setText(items.getS_summary());
        if (!items.getS_pic_URL().toString().equals("")) {
            Picasso.with(mContext)
                    .load(items.getS_pic_URL().toString())
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            Picasso.with(mContext)
                    .load(R.mipmap.no_image_text)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView item, location, distance, summary, unit;
    }

}

package com.gap.androidtraining.ui.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gap.androidtraining.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<String> items;
    private LayoutInflater inflater = null;
    private Activity context;

    public class ViewHolder {
        ImageView imageViewPhoto;
    }

    public ImageAdapter(Activity context, List<String> items) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_grid_item, parent, false);

            holder = new ViewHolder();
            holder.imageViewPhoto = (ImageView) convertView.findViewById(R.id.image_view_photo);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        Picasso.with(context).load(items.get(position)).resize(200, 200).centerCrop().into(holder.imageViewPhoto);

        return (convertView);
    }
}

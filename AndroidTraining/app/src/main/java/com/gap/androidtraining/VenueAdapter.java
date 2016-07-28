package com.gap.androidtraining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gap.androidtraining.BaseAPI.FoursquareSearch;

import java.util.List;

public class VenueAdapter extends BaseAdapter {

    private List<FoursquareSearch.response.Venue> items;
    private LayoutInflater inflater = null;
    private Activity context;

    public class ViewHolder {
        TextView textViewName;
    }

    public VenueAdapter(Activity context, List<FoursquareSearch.response.Venue> items) {
        this.items = items;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<FoursquareSearch.response.Venue> getItems() {
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
            convertView = inflater.inflate(R.layout.venue_row, parent, false);

            holder = new ViewHolder();
            holder.textViewName = (TextView) convertView.findViewById(R.id.text_view_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        holder.textViewName.setText(items.get(position).name);

        return (convertView);
    }
}

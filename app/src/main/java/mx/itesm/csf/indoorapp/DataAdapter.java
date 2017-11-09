package mx.itesm.csf.indoorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<Beacon> mArrayList;
    private ArrayList<Beacon> mFilteredList;
    Context context;

    public DataAdapter(ArrayList<Beacon> arrayList, Context context) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {
        String minor = mFilteredList.get(i).getMinor();
        if (minor.equals("null")) minor = "<font color='#EE0000'>missing</font>";

        String x = mFilteredList.get(i).getX();
        if (x.equals("null")) x = "<font color='#EE0000'>missing</font>";

        String y = mFilteredList.get(i).getY();
        if (y.equals("null")) y = "<font color='#EE0000'>missing</font>";

        viewHolder.tv_name.setText("Zone: " + mFilteredList.get(i).getId());
        viewHolder.tv_version.setText(Html.fromHtml("Minor: " + minor));
        viewHolder.tv_api_level.setText(Html.fromHtml("Pos X: " + x + "\t-\tPos Y: " + y));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String beacon = gson.toJson(mFilteredList.get(i));

                Intent intent = new Intent(context, BeaconInfoActivity.class);
                intent.putExtra("beacon", beacon);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                    Collections.sort(mFilteredList);
                } else {

                    ArrayList<Beacon> filteredList = new ArrayList<>();

                    for (Beacon beacon : mArrayList) {

                        if (beacon.getId().toLowerCase().contains(charString) || beacon.getX().toLowerCase().contains(charString) || beacon.getY().toLowerCase().contains(charString) || beacon.getMinor().toLowerCase().contains(charString)) {
                            filteredList.add(beacon);
                        }

                        // Condition added to display zones missing data if "missing" is typed in the search bar.
                        if (charString.equals("missing")) {
                            if (beacon.getMinor().equals("null") || beacon.getX().equals("null") || beacon.getY().equals("null")) {
                                filteredList.add(beacon);
                            }
                        }
                    }

                    mFilteredList = filteredList;
                    Collections.sort(mFilteredList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Beacon>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);

        }
    }

}
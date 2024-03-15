package com.example.database_demo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter implements Filterable {

    ArrayList<Model> original;
    ArrayList<Model> temp;
    Context context;
    customfilter cs;

    public CustomListAdapter(Context context, ArrayList<Model> m) {
        this.context = context;
        this.original = m;
        this.temp = m;

    }

    @Override
    public int getCount() {
        return temp.size();
    }

    @Override
    public Object getItem(int position) {
        return temp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView id_text, FName_text, LName_text, Gender_txt;
        ImageView edt_img, del_img;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(context);
//            v = vi.inflate(R.layout.activity_custom listview, null);
        ViewHolder holder;
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.activity_customlistview, null);
            holder = new ViewHolder();
            holder.id_text = v.findViewById(R.id.id);
            holder.FName_text = v.findViewById(R.id.txt_firstname);
            holder.LName_text = v.findViewById(R.id.txt_lastname);
            holder.Gender_txt = v.findViewById(R.id.txt_gender);
            holder.edt_img = v.findViewById(R.id.iv_edit);
            holder.del_img = v.findViewById(R.id.iv_delete);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Model model1 = temp.get(position);
        holder.id_text.setText(String.valueOf(position + 1));
        holder.FName_text.setText(model1.getFname());
        holder.LName_text.setText(model1.getLname());
        holder.Gender_txt.setText(model1.getGender());
        holder.edt_img.setImageResource(R.drawable.baseline_edit_24);
        holder.del_img.setImageResource(R.drawable.delete);

        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (cs == null) {
            cs = new customfilter();
        }
        return cs;
    }

    public class customfilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filterResults.values = original;
                filterResults.count = original.size();
            } else {
                ArrayList<Model> arrayList = new ArrayList<>();
                String filterstring = constraint.toString().toLowerCase().trim();

                for (Model i : original) {
                    if (i.getFname().toLowerCase().contains(filterstring)||
                            i.getLname().toLowerCase().contains(filterstring)||
                    i.getGender().toLowerCase().contains(filterstring)) {
                        arrayList.add(i);
                    }
                }
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
            }
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (constraint != null && constraint.length() > 0) {
                temp.clear();
                temp.addAll((ArrayList<Model>) results.values);
            }

            notifyDataSetChanged();
        }
    }
}

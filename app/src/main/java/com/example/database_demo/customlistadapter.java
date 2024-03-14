package com.example.database_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class customlistadapter extends ArrayAdapter<model> implements Filterable {

    ArrayList<model> original,temp;
    int resource;
    Context context;
    customfilter cs;

    public customlistadapter(Context context, int resource, List<model> m) {
        super(context, resource, m);
        this.context = context;
        this.resource = resource;
        //this.mod = m;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(resource,null);
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resource, null);
        }
        TextView id_text = v.findViewById(R.id.id);
        TextView fname_text = v.findViewById(R.id.txt_firstname);
        TextView lname_text = v.findViewById(R.id.txt_lastname);
        TextView gender_txt = v.findViewById(R.id.txt_gender);
        ImageView edt_img = v.findViewById(R.id.iv_edit);
        ImageView del_img = v.findViewById(R.id.iv_delete);

        model model1 = getItem(position);

        id_text.setText("" + (position + 1));

        fname_text.setText(model1.getFname());
        lname_text.setText(model1.getLname());
        gender_txt.setText(model1.getGender());
        edt_img.setImageResource(R.drawable.baseline_edit_24);
        del_img.setImageResource(R.drawable.delete);

        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (cs == null){
            cs = new customfilter();
        }
        return cs;
    }


    public class customfilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<model> arrayList = new ArrayList<>();
                for (model model : original) {
                    if (model.getFname().toUpperCase().contains(constraint)) {
                        arrayList.add(model);
                    }
                }
                filterResults.count = arrayList.size();
                filterResults.values = arrayList;
            }
            else {
                filterResults.count = temp.size();
                filterResults.values = temp;
            }
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                    original = (ArrayList<model>) results.values;
                    notifyDataSetChanged();
        }
    }
}

package com.example.database_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter implements Filterable {

    ArrayList<Model> original = new ArrayList<>();
    ArrayList<Model> temp = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    customfilter cs;
    String searchTextFirstName = "", searchTextLastName = "", searchTextGender = "";
    SqlDb db;

    public CustomListAdapter(Context context, ArrayList<Model> m, LayoutInflater layoutInflater) {
        this.context = context;
        this.original.addAll(m);
        this.temp.addAll(m);
        this.layoutInflater = layoutInflater;
        db = new SqlDb(context);
    }

    public void performFiler(String searchText, int searchOn) {
        if (cs == null) {
            cs = new customfilter();
        }
        if (searchOn == 1) {
            searchTextFirstName = searchText;
        } else if (searchOn == 2) {
            searchTextLastName = searchText;
        } else if (searchOn == 3) {
            searchTextGender = searchText;
        }
        cs.filter(searchText);
    }

    public void addData(Model model) {
        original.add(model);
        notifyDataSetChanged();
    }

    public void deleteData(int position) {
        Model modelToRemove = temp.get(position);
        temp.remove(position);

        for (int i = 0; i < original.size(); i++) {
            if (original.get(i).getId() == modelToRemove.getId()) {
                original.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(Model model, int position) {
        temp.set(position, model);

        for (int i = 0; i < original.size(); i++) {
            if (original.get(i).getId() == temp.get(position).getId()) {
                original.set(i, model);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return temp.size();
    }

    @Override
    public Model getItem(int position) {
        return temp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvId, tvFirstName, tvLastName, tvGender;
        ImageView ivEdit, ivDelete;
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
//            LayoutInflater inflater = LayoutInflater.from(context);
            v = layoutInflater.inflate(R.layout.activity_customlistview, null);
            holder = new ViewHolder();
            holder.tvId = v.findViewById(R.id.id);
            holder.tvFirstName = v.findViewById(R.id.txt_firstname);
            holder.tvLastName = v.findViewById(R.id.txt_lastname);
            holder.tvGender = v.findViewById(R.id.txt_gender);
            holder.ivEdit = v.findViewById(R.id.iv_edit);
            holder.ivDelete = v.findViewById(R.id.iv_delete);
            holder.ivEdit.setOnClickListener(editClickListener);
            holder.ivDelete.setOnClickListener(deleteOnClickListener);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Model model1 = temp.get(position);

        holder.tvId.setText(String.valueOf(model1.getId() + 1));
        holder.tvFirstName.setText(model1.getFname());
        holder.tvLastName.setText(model1.getLname());
        holder.tvGender.setText(model1.getGender());
        holder.ivEdit.setImageResource(R.drawable.baseline_edit_24);
        holder.ivDelete.setImageResource(R.drawable.delete);
        holder.ivEdit.setTag(position);
        holder.ivDelete.setTag(position);
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

            if (searchTextGender.length() == 0 && searchTextFirstName.length() == 0 && searchTextLastName.length() == 0) {
                filterResults.values = original;
                filterResults.count = original.size();
            } else {
                ArrayList<Model> arrayList = new ArrayList<>();

                for (Model item : original) {
                    if ((searchTextGender.length() == 0 || item.getGender().trim().equalsIgnoreCase(searchTextGender)) &&
                            item.getFname().toLowerCase().trim().contains(searchTextFirstName) &&
                            item.getLname().toLowerCase().trim().contains(searchTextLastName)) {
                        arrayList.add(item);
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
            } else {
                temp.clear();
                temp.addAll(original);
            }
            notifyDataSetChanged();
        }
    }

    View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            //update data from database..
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.alertdialog));
//                    LayoutInflater inflater = convertView.getLayoutInflater();
            v = layoutInflater.inflate(R.layout.activity_pupupaddbtn, null);
            final EditText firstname = v.findViewById(R.id.edtfirstname);
            final EditText lastname = v.findViewById(R.id.edtlastname);
            final RadioButton rbmale = v.findViewById(R.id.rbmale);
            final RadioButton rbfemale = v.findViewById(R.id.rbfemale);
            builder.setView(v);
            Model Model = temp.get(position);
            firstname.setText(Model.getFname());
            lastname.setText(Model.getLname());
            if (Model.getGender().equals("male")) {
                rbmale.setChecked(true);
            } else {
                rbfemale.setChecked(true);
            }
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fn = firstname.getText().toString();
                    String ln = lastname.getText().toString();
                    String name;
                    if (rbmale.isChecked()) {
                        name = "male";
                    } else {
                        name = "female";
                    }
                    db.UpdateData(fn, ln, name);
                    Model tempModel = getItem(position);
                    updateData(new Model(tempModel.getId(), fn, ln, name), position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    };

    View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.alertdialog));
            builder.setTitle("Are you sure you want to delete it..?");
            Model Model = temp.get(position);
            builder.setMessage(Model.getFname() + " " + Model.getLname());
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fn = Model.getFname();
                    db.Deletedata(fn);
                    deleteData(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    };
}

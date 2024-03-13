package com.example.database_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addbtn;
    ArrayList<model> m1;
    customlistadapter adapter;

    ListView listView;
    SqlDb db = new SqlDb(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbtn = findViewById(R.id.Addbtn);
        listView = findViewById(R.id.listview);
        m1 = new ArrayList<>();

        adapter = new customlistadapter(this, R.layout.customlistview, m1);
        listView.setAdapter(adapter);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alertdialog));
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.pupupaddbtn, null);
                final EditText firstname = v.findViewById(R.id.edtfirstname);
                final EditText lastname = v.findViewById(R.id.edtlastname);
                builder.setView(v);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fn = firstname.getText().toString();
                        String ln = lastname.getText().toString();
                        if (fn.isEmpty() || ln.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please Enter details..", Toast.LENGTH_SHORT).show();
                        } else {
                            db.Insertdata(fn, ln);
                            updatelistview();
                            Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                builder.show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView editimageview = view.findViewById(R.id.iv_edit);

                editimageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //update data from database..
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alertdialog));
                        LayoutInflater inflater = getLayoutInflater();
                        v = inflater.inflate(R.layout.pupupaddbtn, null);
                        final EditText firstname = v.findViewById(R.id.edtfirstname);
                        final EditText lastname = v.findViewById(R.id.edtlastname);
                        builder.setView(v);
                        model Model = m1.get(position);
                        firstname.setText(Model.getFname());
                        lastname.setText(Model.getLname());
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fn = firstname.getText().toString();
                                String ln = lastname.getText().toString();
                                db.UpdateData(fn, ln);
                                updatelistview();
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
                });

                //Delete data from database..
                ImageView deleteimageview = view.findViewById(R.id.iv_delete);
                deleteimageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alertdialog));
                        builder.setTitle("Are you sure you want to delete it..?");
                        model Model = m1.get(position);
                        builder.setMessage(Model.getFname() + " " + Model.getLname());
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fn = Model.getFname();
                                db.Deletedata(fn);
                                updatelistview();
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
                });
            }
        });
        updatelistview();
    }

    public void updatelistview() {
        m1.clear();
        ArrayList<model> data = db.Displaydata();
        m1.addAll(data);
        adapter.notifyDataSetChanged();

    }
}

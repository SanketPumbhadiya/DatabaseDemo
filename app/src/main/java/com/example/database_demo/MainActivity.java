package com.example.database_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addbtn;
    ArrayList<Model> m1;
    CustomListAdapter adapter;

    ListView listView;
    EditText SearcheditTextfname, SearcheditTextlname, SearcheditTextgender;
    SqlDb db = new SqlDb(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbtn = findViewById(R.id.Addbtn);
        listView = findViewById(R.id.listview);
        m1 = new ArrayList<>();

        SearcheditTextfname = findViewById(R.id.search_edtfname);
        SearcheditTextlname = findViewById(R.id.search_edtlname);
        SearcheditTextgender = findViewById(R.id.search_edtgender);


        SearcheditTextfname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.performFiler(s.toString(),1);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        SearcheditTextlname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.performFiler(s.toString(),2);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        SearcheditTextgender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.performFiler(s.toString(),3);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alertdialog));
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.activity_pupupaddbtn, null);
                final EditText firstname = v.findViewById(R.id.edtfirstname);
                final EditText lastname = v.findViewById(R.id.edtlastname);
                final RadioButton rbmale = v.findViewById(R.id.rbmale);
                builder.setView(v);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fn = firstname.getText().toString();
                        String ln = lastname.getText().toString();
                        String s;
                        if (rbmale.isChecked()) {
                            s = "male";
                        } else {
                            s = "female";
                        }
                        if (fn.isEmpty() || ln.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please Enter details..", Toast.LENGTH_SHORT).show();
                        } else {
                            db.Insertdata(fn, ln, s);
                            Model model = new Model(adapter.getCount(),fn,ln,s);
                            adapter.addData(model);
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
        updatelistview();
    }

    public void updatelistview() {
        m1.clear();
        ArrayList<Model> data = db.Displaydata();
        m1.addAll(data);
        adapter = new CustomListAdapter(this, m1, getLayoutInflater());
        listView.setAdapter(adapter);
    }
}

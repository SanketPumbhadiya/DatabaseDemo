package com.example.database_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText firstname, lastname;
    Button submitbtn;
    ArrayAdapter adapter;

    ListView listView;

    SqlDb db = new SqlDb(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstname = findViewById(R.id.edtfirstname);
        lastname = findViewById(R.id.edtlastname);
        submitbtn = findViewById(R.id.submitbtn);
        listView = findViewById(R.id.listview);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = firstname.getText().toString();
                String ln = lastname.getText().toString();

                    if (submitbtn.getText() == "Update") {
                        //Update Data from listview..
                        db.UpdateData(fn, ln);
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        firstname.setText("");
                        lastname.setText("");
                        submitbtn.setText("Submit");
                        updatelistview();
                    } else {
                        if (firstname.length() ==0 || lastname.length() == 0) {
                            Toast.makeText(MainActivity.this, "This fill is required", Toast.LENGTH_SHORT).show();
                        } else {
                        //Insert Data from listview..
                        db.Insertdata(fn, ln);
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                        firstname.setText("");
                        lastname.setText("");
                        updatelistview();
                    }
                }
            }
        });

        //Splitting data from listview..
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                String[] splitt = s.split(" ");
                if (splitt.length >= 1) {
                    String fn = splitt[0];
                    firstname.setText(fn);
                }
                if (splitt.length >= 2) {
                    String ln = splitt[1];
                    lastname.setText(ln);
                }
                submitbtn.setText("Update");
            }
        });

        //Delete Data from listview..

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String fn = (String) adapter.getItem(position);
                String[] splitt = fn.split(" ");

                adapter.remove(fn);
                adapter.notifyDataSetChanged();
                if (splitt.length >= 1) {
                    String sp = splitt[0];
                    db.Deletedata(sp);
                }
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        updatelistview();
    }

    //Display data from listview..
    public void updatelistview() {
        adapter.clear();
        ArrayList<String> data = db.Displaydata();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }
}

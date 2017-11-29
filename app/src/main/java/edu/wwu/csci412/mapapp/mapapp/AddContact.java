package edu.wwu.csci412.mapapp.mapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = new Database(this);
    }

    public void add(View v) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        Contact c = new Contact(0, name, phone);
        db.insert(c);
        Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        finish();
    }
}

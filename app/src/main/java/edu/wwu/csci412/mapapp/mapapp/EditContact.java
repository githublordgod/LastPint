package edu.wwu.csci412.mapapp.mapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditContact extends AppCompatActivity {

    private Database db;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        db = new Database(this);
        id = getIntent().getIntExtra("id", 0);
        Contact c = db.select(id);
        if (c == null) finish();
        ((EditText) findViewById(R.id.name)).setText(c.getName());
        ((EditText) findViewById(R.id.phone)).setText(c.getPhone());
    }

    public void confirm(View v) {
        String name = ((EditText) findViewById(R.id.name)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        db.update(id, name, phone);
        Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}

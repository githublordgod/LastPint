package edu.wwu.csci412.mapapp.mapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    private Database db;
    private ScrollView sv;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        db = new Database(this);
        sv = findViewById(R.id.sv);
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        sv.removeAllViewsInLayout();
        ArrayList<Contact> list = db.selectAll();
        if (list.size() > 0) {
            GridLayout grid = new GridLayout(this);
            grid.setRowCount(list.size());
            grid.setColumnCount(1);
            ContactButton[] contacts = new ContactButton[list.size()];
            ButtonHandler h = new ButtonHandler();
            int i = 0;
            for (Contact c : list) {
                contacts[i] = new ContactButton(this, c);
                contacts[i].setText(contacts[i].toString());
                contacts[i].setOnClickListener(h);
                grid.addView(contacts[i], GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
                i++;
            }
            sv.addView(grid);
        }
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            /*db.delete(v.getId());
            Toast.makeText(ContactList.this, "Contact erased", Toast.LENGTH_SHORT).show();
            update();*/
            PopupMenu popup = new PopupMenu(ContactList.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.contact, popup.getMenu());
            currentId = v.getId();

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.m_edit:
                            Intent i = new Intent(ContactList.this, EditContact.class);
                            i.putExtra("id", currentId);
                            ContactList.this.startActivity(i);
                            break;
                        case R.id.m_delete:
                            db.delete(currentId);
                            Toast.makeText(ContactList.this, "Contact erased", Toast.LENGTH_SHORT).show();
                            update();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });

            popup.show();
        }
    }

    public void add(View v) {
        Intent i = new Intent(this, AddContact.class);
        this.startActivity(i);
    }
}

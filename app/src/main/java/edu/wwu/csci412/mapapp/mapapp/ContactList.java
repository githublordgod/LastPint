package edu.wwu.csci412.mapapp.mapapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
    private static final int SMS_PERM = 1;

    private Database db;
    private ScrollView sv;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        db = new Database(this);
        sv = findViewById(R.id.sv);
        currentId = 0;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkSMSPermission();
        }
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
                //contacts[i].getBackground().setColorFilter(0xBBBBBB00, PorterDuff.Mode.MULTIPLY);
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
                        case R.id.m_help:
                            Toast.makeText(ContactList.this, "Requesting help...", Toast.LENGTH_SHORT).show();
                            checkPerms();
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

    public void checkPerms() {
        if (currentId != 0) {
            Log.i("","sending to " + db.select(currentId).getPhone());
            trueSendHelp();
        }
    }

    public void checkSMSPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ContactList.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactList.this,
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ContactList.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        SMS_PERM);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERM: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(ContactList.this, "Permission cleared.", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ContactList.this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    void trueSendHelp() {
        SmsManager smsManager = SmsManager.getDefault();
        String phone = db.select(currentId).getPhone();
        smsManager.sendTextMessage(phone, null, getString(R.string.helpmsg), null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }
}

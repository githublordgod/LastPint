package edu.wwu.csci412.mapapp.mapapp;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class ContactButton extends AppCompatButton {
    private Contact contact;

    public ContactButton(Context context, Contact c) {
        super(context);
        contact = c;
    }

    public int getId() { return contact.getId(); }

    public String toString() { return contact.getName() + " - " + contact.getPhone(); }
}

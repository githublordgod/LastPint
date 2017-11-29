package edu.wwu.csci412.mapapp.mapapp;

public class Contact {
    private int id;
    private String name;
    private String phone;

    public Contact(int id, String name, String phone) {
        setId(id);
        setName(name);
        setPhone(phone);
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}

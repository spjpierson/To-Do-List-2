package com.piersonapps.todolist;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.HashMap;


public class DaoToDoList {

    private DatabaseReference databaseReference;
    private String myKey;

    public DaoToDoList(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(ToDoList.class.getSimpleName());

    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public Task<Void> addRow(ToDoList row){
        myKey = databaseReference.child(row.getList()).push().getKey();
        row.setKey(myKey);

        return databaseReference.child(row.getList()).child(myKey).setValue(row);
    }

    public Task<Void> update(ToDoList list, String key ,HashMap<String,Object> hashMap){

        return databaseReference.child(list.getList()).child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String list, String key){
        return databaseReference.child(list).child(key).removeValue();
    }

    public Query getKeys() {
        return databaseReference.orderByValue();
    }

    public String getMyKey() {
        return myKey;
    }
}

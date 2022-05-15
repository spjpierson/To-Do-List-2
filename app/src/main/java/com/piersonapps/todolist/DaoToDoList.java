package com.piersonapps.todolist;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;


public class DaoToDoList {

    private DatabaseReference databaseReference;


    public DaoToDoList(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(ToDoList.class.getSimpleName());

    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public Task<Void> addList(String list, ToDoList header){
        header.setKey(databaseReference.push().getKey());
        return databaseReference.child(list).setValue(header);
    }

    public Task<Void> addRow(ToDoList row){
        row.setKey(databaseReference.push().getKey());
        return databaseReference.child(row.getList()).child(row.getKey()).push().setValue(row);
    }

    public Task<Void> update(String list, String key ,HashMap<String,Object> hashMap){
        return databaseReference.child(list).child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String list, String key){
        return databaseReference.child(list).child(key).removeValue();
    }

    public String getKeys() {
        return databaseReference.orderByKey().toString();
    }
}

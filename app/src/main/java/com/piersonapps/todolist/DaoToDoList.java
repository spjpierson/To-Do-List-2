package com.piersonapps.todolist;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.HashMap;


public class DaoToDoList {

    private DatabaseReference databaseReference;
    private String myKey;
    private FirebaseAuth auth;
    private String FriendsKey;


    public DaoToDoList(FirebaseAuth auth){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.auth = auth;
        databaseReference = db.getReference(ToDoList.class.getSimpleName());

    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public Task<Void> addRow(ToDoList row){
        myKey = databaseReference.child(auth.getUid()).child(row.getList()).push().getKey();
        row.setKey(myKey);

        return databaseReference.child(auth.getUid()).child(row.getList()).child(myKey).setValue(row);
    }

    public Task<Void> addFriendsRows(ToDoList row){
        myKey = databaseReference.child(row.getList()).push().getKey();
        row.setKey(FriendsKey);

        return databaseReference.child(auth.getUid()).child(row.getList()).child(myKey).setValue(row);
    }

    public Task<Void> update(ToDoList list, String key ,HashMap<String,Object> hashMap){

        return databaseReference.child(auth.getUid()).child(list.getList()).child(key).updateChildren(hashMap);
    }

    public Task<Void> updateFriends(ToDoList list, String key ,HashMap<String,Object> hashMap){

        return databaseReference.child(list.getList()).child(FriendsKey).updateChildren(hashMap);
    }

    public Task<Void> deleteList(String list){
        return databaseReference.child(auth.getUid()).child(list).removeValue();
    }

    public Task<Void> deleteFriends(String list){
        return databaseReference.child(FriendsKey).child(list).removeValue();
    }

    public Task<Void> remove(String list, String key){
        return databaseReference.child(auth.getUid()).child(list).child(key).removeValue();
    }

    public Task<Void> removeFriends(String list, String key){
        return databaseReference.child(FriendsKey).child(list).child(key).removeValue();
    }

    public Query getKeys() {
        return databaseReference.orderByValue();
    }

    public String getMyKey() {
        return myKey;
    }


    public Query getFriendsKeys() {
        return databaseReference.orderByValue();
    }

    public String getFriendsKeyKey() {
        return FriendsKey;
    }


}

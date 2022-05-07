package com.piersonapps.todolist;

import androidx.room.Insert;

public interface RowDataDao {
    @Insert
    void insert();
}

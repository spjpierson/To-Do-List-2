package com.piersonapps.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RowData {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "list")
    public String list;

    @ColumnInfo(name = "is_header")
    public boolean isHeader;

    @ColumnInfo(name = "check")
    public boolean check;

    @ColumnInfo(name = "detail_1")
    public String detail1;

    @ColumnInfo(name = "detail_2")
    public String detail2;

    @ColumnInfo(name = "detail_3")
    public String detail3;

    @ColumnInfo(name = "detail_4")
    public String detail4;

    @ColumnInfo(name = "address_1")
    public String address1;

    @ColumnInfo(name = "address_2")
    public String address2;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "state")
    public String state;

    @ColumnInfo(name = "zip")
    public int zip;

    @ColumnInfo(name = "phone")
    public int phone;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "store_type")
    public String store_type;

}

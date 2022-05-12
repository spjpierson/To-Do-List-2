package com.piersonapps.todolist;


import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;


import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.TypedValue;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.HashMap;

public class QuickViewList extends AppCompatActivity implements View.OnClickListener {

    private Button addRowButton;
    private LinearLayout container;

    private ArrayList<CheckBox> checkboxs;

    private ArrayList<EditText> editText1s;
    private ArrayList<EditText> editText2s;
    private ArrayList<EditText> editText3s;
    private ArrayList<EditText> editText4s;

    private ArrayList<ImageButton> editButtons;
    private ArrayList<ImageButton> saveButtons;
    
    private ArrayList<String> lists;

    private ArrayList<ToDoList> rowData;

    private ArrayList<Integer> rowIndexing;

    private Spinner listsSpinner;

    private int rowId = 0;
    private int rowSaveButtonId = 999999999;

    DaoToDoList dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_view_list);

        dao = new DaoToDoList();

        rowIndexing = new ArrayList<Integer>();

        lists = new ArrayList<String>();
        lists.add("Add New List");

        addRowButton = findViewById(R.id.quick_view_add_row_button);
        container = findViewById(R.id.quick_view_layout_container);

        checkboxs = new ArrayList<CheckBox>();

        editText1s = new ArrayList<EditText>();
        editText2s = new ArrayList<EditText>();
        editText3s = new ArrayList<EditText>();
        editText4s = new ArrayList<EditText>();

        editButtons = new ArrayList<ImageButton>();

        saveButtons = new ArrayList<ImageButton>();

        listsSpinner = findViewById(R.id.quick_view_lists_spinner);

        ArrayAdapter<String> spinnerListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lists);

        listsSpinner.setAdapter(spinnerListArrayAdapter);


        addRowButton.setOnClickListener(this);

        listsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().toString().equals("Add New List")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuickViewList.this);
                    alertDialog.setTitle("List");
                    alertDialog.setMessage("Please Enter In the new List Below");

                    EditText input = new EditText(QuickViewList.this);
                    input.setHint("List Name");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);

                    alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                           String listName = input.getText().toString();
                           lists.add(listName);

                            listsSpinner.setSelection(lists.size()-1);

                            addRowLayout(listName);

                        }
                    });

                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        if(view.getId() == addRowButton.getId()){
            addRowLayout(listsSpinner.getSelectedItem().toString());
        }else{
            int index = 0;

            do {

                if(editButtons.get(index).getId() == view.getId()){
                  editText1s.get(index).setEnabled(true);
                  editText2s.get(index).setEnabled(true);
                  editText3s.get(index).setEnabled(true);
                  editText4s.get(index).setEnabled(true);

                  index = (editButtons.size()+1);
                }

                index++;
            }while(index < editButtons.size());

            index = 0;

            do{
                if(saveButtons.get(index).getId() == view.getId()){
                    editText1s.get(index).setEnabled(false);
                    editText2s.get(index).setEnabled(false);
                    editText3s.get(index).setEnabled(false);
                    editText4s.get(index).setEnabled(false);

                    index = (saveButtons.size()+1);
                }

                index++;
            }while(index < saveButtons.size());

        }

    }

        private void addRowLayout(String listName){
            LinearLayout row = new LinearLayout(this.getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBackgroundColor(Color.BLACK);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            Resources r = this.getResources();


            int dp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1,
                    r.getDisplayMetrics()
            );

            LayoutParams params1Weight = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,1);
            params1Weight.setMargins(dp,dp,dp,dp);

            LayoutParams params2Weight = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,2);
            params2Weight.setMargins(dp,dp,dp,dp);

            LayoutParams params5Weight = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,5);
            params5Weight.setMargins(dp,dp,dp,dp);

            CheckBox checkBox = new CheckBox(this.getApplicationContext());
            checkBox.setLayoutParams(params1Weight);
            checkBox.setBackgroundColor(Color.WHITE);


            EditText editText1 = new EditText(this.getApplicationContext());
            editText1.setLayoutParams(params2Weight);
            editText1.setBackgroundColor(Color.WHITE);
            editText1.setText("Detail 1");
            editText1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText1.setEnabled(false);


            EditText editText2 = new EditText(this.getApplicationContext());
            editText2.setLayoutParams(params2Weight);
            editText2.setBackgroundColor(Color.WHITE);
            editText2.setText("Detail 2");
            editText2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText2.setEnabled(false);

            EditText editText3 = new EditText(this.getApplicationContext());
            editText3.setLayoutParams(new LayoutParams(params2Weight));
            editText3.setBackgroundColor(Color.WHITE);
            editText3.setText("Detail 3");
            editText3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText3.setEnabled(false);

            EditText editText4 = new EditText(this.getApplicationContext());
            editText4.setLayoutParams(params5Weight);
            editText4.setBackgroundColor(Color.WHITE);
            editText4.setText("Detail 4");
            editText4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText4.setEnabled(false);


            ImageButton editButton = new ImageButton(this.getApplicationContext());
            editButton.setLayoutParams(params1Weight);
            editButton.setBackgroundColor(Color.WHITE);
            editButton.setImageResource(android.R.drawable.ic_menu_edit);
            editButton.setId(rowId);



            ImageButton saveButton = new ImageButton(this.getApplicationContext());
            saveButton.setLayoutParams(params1Weight);
            saveButton.setBackgroundColor(Color.WHITE);
            saveButton.setImageResource(android.R.drawable.ic_menu_save);
            saveButton.setId(rowSaveButtonId);

            row.addView(checkBox);

            row.addView(editText1);
            row.addView(editText2);
            row.addView(editText3);
            row.addView(editText4);

            row.addView(editButton);
            row.addView(saveButton);

            container.addView(row);



            editButton.setOnClickListener(this);
            saveButton.setOnClickListener(this);

            checkboxs.add(checkBox);

            editText1s.add(editText1);
            editText2s.add(editText2);
            editText3s.add(editText3);
            editText4s.add(editText4);

            editButtons.add(editButton);
            saveButtons.add(saveButton);

            String column1 = editText1.getText().toString();
            String column2 = editText2.getText().toString();
            String column3 = editText3.getText().toString();
            String column4 = editText4.getText().toString();

            ToDoList list;

            if(checkboxs.size() == 1){
               list = new ToDoList(listName, rowId, true,
                        false, column1,column2,column3,column4,
                        null,null,null,null,0,0,null,null);

                    dao.addRow(list).addOnSuccessListener(suc->{
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(err->{
                        Toast.makeText(this,"Fail: " + err.getMessage(),Toast.LENGTH_LONG).show();
                    });
            }else if(checkboxs.size() > 1 && checkboxs.size() != 0){
                list = new ToDoList(listName, rowId, true,
                        false, column1,column2,column3,column4,
                        null,null,null,null,0,0,null,null);

                dao.addRow(list).addOnSuccessListener(suc->{
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
                }).addOnFailureListener(err->{
                    Toast.makeText(this,"Fail: " + err.getMessage(),Toast.LENGTH_LONG).show();
                });

            /*
                 list = new ToDoList(listName, rowId, false,
                        false, column1,column2,column3,column4,
                        null,null,null,null,0,0,null,null);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("list",list.getList());
                hashMap.put("isHeader",false);
                hashMap.put("check",list.isCheck());
                hashMap.put("index",list.getIndex());

                hashMap.put("column1",list.getColumn1());
                hashMap.put("column2",list.getColumn2());
                hashMap.put("column3",list.getColumn3());
                hashMap.put("column4",list.getColumn4());

                hashMap.put("address1",list.getAddress1());
                hashMap.put("address2",list.getAddress2());
                hashMap.put("city",list.getCity());
                hashMap.put("state",list.getState());
                hashMap.put("zip",list.getZip());

                hashMap.put("phone",list.getPhone());
                hashMap.put("email",list.getEmail());
                hashMap.put("store",list.getStore_type());

                dao.update(listName,hashMap);


             */

             //   String key = dao.getData().get().getResult().getValue().toString();

            //    Toast.makeText(this,"key: " + key, Toast.LENGTH_LONG).show();
            //    editText1.setText(key);
             /*

                dao.addRow(list).addOnSuccessListener(suc->{
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
                }).addOnFailureListener(err->{
                    Toast.makeText(this,"Fail: " + err.getMessage(),Toast.LENGTH_LONG).show();
                }); */

                Toast.makeText(this,"Not first row",Toast.LENGTH_LONG).show();
            }


            rowId++;
            rowSaveButtonId--;

        }



    }

package com.piersonapps.todolist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class QuickViewList extends AppCompatActivity implements View.OnClickListener {


    private ImageButton addRowButton;
    private ImageButton editAllButton;
    private ImageButton saveAllButton;
    private ImageButton deleteRowButton;
    private ImageButton alaramButton;
    private ImageButton deleteListButton;

    private ImageButton logutButton;


    private LinearLayout container;

    private ArrayList<String> searchFields;
    private ArrayList<CheckBox> checkboxes;

    private ArrayList<EditText> editText1s;
    private ArrayList<EditText> editText2s;
    private ArrayList<EditText> editText3s;
    private ArrayList<EditText> editText4s;



    private ArrayList<ImageButton> editButtons;
    private ArrayList<ImageButton> saveButtons;

    private ArrayList<String> lists;


    private Spinner listsSpinner;
    private Spinner searchFieldsSpinner;

    private int rowId = 0;
    private int rowSaveButtonId = 999999999;

    private ArrayList<ToDoList> toDoLists;

    private DaoToDoList dao;

    private Button searchButton;
    private EditText searchInput;

   ArrayAdapter<String> spinnerListArrayAdapter;

   ArrayAdapter<String> spinnerSearchFieldsArrayAdapter;

   private final String selectField = "Select Field";

   private int screenWidth;

    private int columnWidth;
    private int buttonWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_view_list);
        Intent intent = getIntent();

        String userId = intent.getStringExtra("user_id");
        String email_id = intent.getStringExtra("email_id");
        String name_id = intent.getStringExtra("name_id");


        dao = new DaoToDoList(FirebaseAuth.getInstance());

        lists = new ArrayList<>();

        // grab current database list
        dao.getDatabaseReference().get().addOnCompleteListener(task -> {


            task.getResult().getChildren().iterator();
            for(DataSnapshot child: task.getResult().child(FirebaseAuth.getInstance().getUid()).getChildren()){
                lists.add(child.getKey());

            }

        });


        lists.add("Please Select Your List Or Create a New One");
        // to allow user to add new database
        lists.add("Add New List");
        // to allow user to create a friends share list
        lists.add("Create New Share List");

        addRowButton = findViewById(R.id.quick_view_add_row_button);
        saveAllButton = findViewById(R.id.quickview_save_all_button);
        editAllButton = findViewById(R.id.quick_view_edit_all_button);

        deleteRowButton = findViewById(R.id.quick_view_delete_button);
        deleteListButton = findViewById(R.id.quick_view_delete_list_button);
        alaramButton = findViewById(R.id.quick_view_set_alarm_button);
        logutButton = findViewById(R.id.quick_view_logutButton_button);


        container = findViewById(R.id.quick_view_layout_container);
        searchInput = findViewById(R.id.quick_view_search_input);

        searchFields = new ArrayList<>();

        checkboxes = new ArrayList<>();

        editText1s = new ArrayList<>();
        editText2s = new ArrayList<>();
        editText3s = new ArrayList<>();
        editText4s = new ArrayList<>();

        editButtons = new ArrayList<>();

        saveButtons = new ArrayList<>();

        toDoLists = new ArrayList<>();

        listsSpinner = findViewById(R.id.quick_view_lists_spinner);
        searchFieldsSpinner = findViewById(R.id.quick_view_field_spinner);

        searchFields.add(selectField);

        spinnerListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lists);
        spinnerListArrayAdapter.notifyDataSetChanged();
        listsSpinner.setAdapter(spinnerListArrayAdapter);

        spinnerSearchFieldsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,searchFields);
        spinnerSearchFieldsArrayAdapter.notifyDataSetChanged();
        searchFieldsSpinner.setAdapter(spinnerSearchFieldsArrayAdapter);

        searchButton = findViewById(R.id.quick_view_search_button);

        addRowButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        editAllButton.setOnClickListener(this);
        saveAllButton.setOnClickListener(this);
        alaramButton.setOnClickListener(this);
        logutButton.setOnClickListener(this);
        deleteRowButton.setOnClickListener(this);
        deleteListButton.setOnClickListener(this);


        searchFieldsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().toString().equals(selectField)){
                    if(checkboxes.size() > 0){
                        for(int j = 0; j <= checkboxes.size()-1; ++j){
                            addRowLayoutWithSearch(j);
                            searchInput.setText("");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(adapterView.getSelectedItem().toString().equals("Add New List")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuickViewList.this);
                    alertDialog.setTitle("List");
                    alertDialog.setMessage("Please Enter In the new List Below");
                //popup
                    EditText input = new EditText(QuickViewList.this);
                    input.setHint("List Name");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);

                    alertDialog.setPositiveButton("Enter", (dialogInterface, i1) -> {

                       String listName = input.getText().toString();
                       lists.add(listName);

                        listsSpinner.setSelection(lists.size()-1);

                        addRowLayout(listName,true);

                    });


                    alertDialog.setNegativeButton("Cancel",
                            (dialog, which) -> dialog.cancel());

                    alertDialog.show();

                }else if(!adapterView.getSelectedItem().toString().equals("Please Select Your List Or Create a New One")){

                   dao.getDatabaseReference().child(FirebaseAuth.getInstance().getUid()).child(adapterView.getSelectedItem().toString()).get().addOnCompleteListener(task -> {

                       container.removeAllViews();

                       checkboxes = new ArrayList<>();

                       editText1s = new ArrayList<>();
                       editText2s = new ArrayList<>();
                       editText3s = new ArrayList<>();
                       editText4s = new ArrayList<>();

                       editButtons = new ArrayList<>();
                       saveButtons = new ArrayList<>();


                       toDoLists = new ArrayList<>();

                        rowId = 0;
                        rowSaveButtonId = 999999999;

                       int i12 = 0;
                       task.getResult().getChildren().iterator();
                       for(DataSnapshot child: task.getResult().getChildren()) {


                           ToDoList list = new ToDoList();

                           list.setList(adapterView.getSelectedItem().toString());
                           list.setIndex(rowId);
                           list.setHeader(Boolean.getBoolean(Objects.requireNonNull(child.child("header").getValue()).toString()));

                           if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("true")) {
                               list.setCheck(true);
                           }else if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("false")){
                               list.setCheck(false);
                           }

                           list.setColumn1(Objects.requireNonNull(child.child("column1").getValue()).toString());
                           list.setColumn2(Objects.requireNonNull(child.child("column2").getValue()).toString());
                           list.setColumn3(Objects.requireNonNull(child.child("column3").getValue()).toString());
                           list.setColumn4(Objects.requireNonNull(child.child("column4").getValue()).toString());
                           list.setKey(child.getKey());
                           toDoLists.add(list);


                           if(i12 == 0){



                                   spinnerSearchFieldsArrayAdapter.clear();

                                   searchFields = new ArrayList<>();
                                   searchFields.add(0,selectField);
                                   searchFields.add( 1,list.getColumn1());
                                   searchFields.add( 2,list.getColumn2());
                                   searchFields.add( 3,list.getColumn3());
                                   searchFields.add( 4,list.getColumn4());

                                  spinnerSearchFieldsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,searchFields);
                                  spinnerSearchFieldsArrayAdapter.notifyDataSetChanged();
                                 searchFieldsSpinner.setAdapter(spinnerSearchFieldsArrayAdapter);


                           }

                           addRowLayout(adapterView.getSelectedItem().toString(),false);

                           checkboxes.get(i12).setChecked(list.isCheck());
                           editText1s.get(i12).setText(list.getColumn1());
                           editText2s.get(i12).setText(list.getColumn2());
                           editText3s.get(i12).setText(list.getColumn3());
                           editText4s.get(i12).setText(list.getColumn4());
                           i12++;
                       }



                   });
               }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;

        //for rows width size
        int cell = (screenWidth/10);
        columnWidth = cell * 2;
        buttonWidth = ((cell*2)/3)+10;
        //TODO
        dao.getDatabaseReference().child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
             if(checkboxes.size() > 0) {


                int index;
                String list = listsSpinner.getSelectedItem().toString();
                 dataSnapshot.child(list).getChildren().iterator();
                 for(DataSnapshot child: dataSnapshot.child(list).getChildren()){

                    String in =  Objects.requireNonNull(child.child("index").getValue()).toString();

                    try{
                        index = Integer.parseInt(in);

                        if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("true")){
                            checkboxes.get(index).setChecked(true);
                        }else if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("false")){
                            checkboxes.get(index).setChecked(false);
                        }

                        editText1s.get(index).setText(Objects.requireNonNull(child.child("column1").getValue()).toString());
                        editText2s.get(index).setText(Objects.requireNonNull(child.child("column2").getValue()).toString());
                        editText3s.get(index).setText(Objects.requireNonNull(child.child("column3").getValue()).toString());
                        editText4s.get(index).setText(Objects.requireNonNull(child.child("column4").getValue()).toString());
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Sorry Something went wrong you might what to reselect the list",Toast.LENGTH_SHORT).show();
                    }


                 }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
             //   Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }


    @Override
    public void onClick(View view) {

        if(alaramButton.getId() == view.getId()){
            // Intent get an activity content
            Intent calendarActivity = new Intent(this,CalendarActivity.class);
            //start an activity
            startActivity(calendarActivity);
        }else if(deleteListButton.getId() == view.getId()){

        if(!listsSpinner.getSelectedItem().equals("Add New List") && !listsSpinner.getSelectedItem().equals("Please Select Your List Or Create a New One")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuickViewList.this);
            builder.setMessage("ARE YOUR SURE YOU WANT TO DELETE " + listsSpinner.getSelectedItem().toString() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dao.deleteList(listsSpinner.getSelectedItem().toString());
                    lists.remove(listsSpinner.getSelectedItem().toString());
                    listsSpinner.setSelection(0);
                    container.removeAllViews();

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();
        }

        } else if(deleteRowButton.getId() == view.getId()){


            if(checkboxes.size() > 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(QuickViewList.this);
                builder.setMessage("ARE YOUR SURE YOU WANT TO DELETE ALL CHECK ROWS?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j < toDoLists.size(); ++j){
                            if(checkboxes.get(j).isChecked()){
                                dao.remove(toDoLists.get(j).getList(),toDoLists.get(j).getKey());
                            }
                        }

                        //TODO
                        dao.getDatabaseReference().child(FirebaseAuth.getInstance().getUid()).child(listsSpinner.getSelectedItem().toString()).get().addOnCompleteListener(task -> {

                            container.removeAllViews();

                            checkboxes = new ArrayList<>();

                            editText1s = new ArrayList<>();
                            editText2s = new ArrayList<>();
                            editText3s = new ArrayList<>();
                            editText4s = new ArrayList<>();

                            editButtons = new ArrayList<>();
                            saveButtons = new ArrayList<>();


                            toDoLists = new ArrayList<>();

                            rowId = 0;
                            rowSaveButtonId = 999999999;

                            int i12 = 0;
                            task.getResult().getChildren().iterator();
                            for(DataSnapshot child: task.getResult().getChildren()) {


                                ToDoList list = new ToDoList();

                                list.setList(listsSpinner.getSelectedItem().toString());
                                list.setIndex(rowId);
                                list.setHeader(Boolean.getBoolean(Objects.requireNonNull(child.child("header").getValue()).toString()));

                                if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("true")) {
                                    list.setCheck(true);
                                }else if(Objects.requireNonNull(child.child("check").getValue()).toString().equals("false")){
                                    list.setCheck(false);
                                }

                                list.setColumn1(Objects.requireNonNull(child.child("column1").getValue()).toString());
                                list.setColumn2(Objects.requireNonNull(child.child("column2").getValue()).toString());
                                list.setColumn3(Objects.requireNonNull(child.child("column3").getValue()).toString());
                                list.setColumn4(Objects.requireNonNull(child.child("column4").getValue()).toString());
                                list.setKey(child.getKey());
                                toDoLists.add(list);


                                if(i12 == 0){



                                    spinnerSearchFieldsArrayAdapter.clear();

                                    searchFields = new ArrayList<>();
                                    searchFields.add(0,selectField);
                                    searchFields.add( 1,list.getColumn1());
                                    searchFields.add( 2,list.getColumn2());
                                    searchFields.add( 3,list.getColumn3());
                                    searchFields.add( 4,list.getColumn4());

                                    spinnerSearchFieldsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,searchFields);
                                    spinnerSearchFieldsArrayAdapter.notifyDataSetChanged();
                                    searchFieldsSpinner.setAdapter(spinnerSearchFieldsArrayAdapter);


                                }

                                addRowLayout(listsSpinner.getSelectedItem().toString(),false);

                                checkboxes.get(i12).setChecked(list.isCheck());
                                editText1s.get(i12).setText(list.getColumn1());
                                editText2s.get(i12).setText(list.getColumn2());
                                editText3s.get(i12).setText(list.getColumn3());
                                editText4s.get(i12).setText(list.getColumn4());
                                i12++;
                            }



                        });





                    }




                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                    }
                });

                builder.show();

            }

        } else if(editAllButton.getId() == view.getId()){

         if(checkboxes.size() > 0) {
             int i = 0;
             do {
                 editText1s.get(i).setEnabled(true);
                 editText2s.get(i).setEnabled(true);
                 editText3s.get(i).setEnabled(true);
                 editText4s.get(i).setEnabled(true);
                 i += 1;
             } while (i < checkboxes.size());
         }

        }else if(saveAllButton.getId() == view.getId()){
            if(checkboxes.size() > 0) {
                int i = 0;
                do {
                    editText1s.get(i).setEnabled(false);
                    editText2s.get(i).setEnabled(false);
                    editText3s.get(i).setEnabled(false);
                    editText4s.get(i).setEnabled(false);

                    toDoLists.get(i).setCheck(checkboxes.get(i).isChecked());

                    toDoLists.get(i).setColumn1(editText1s.get(i).getText().toString());
                    toDoLists.get(i).setColumn2(editText2s.get(i).getText().toString());
                    toDoLists.get(i).setColumn3(editText3s.get(i).getText().toString());
                    toDoLists.get(i).setColumn4(editText4s.get(i).getText().toString());


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("list", toDoLists.get(i).getList());
                    hashMap.put("check", toDoLists.get(i).isCheck());
                    hashMap.put("index", toDoLists.get(i).getIndex());

                    hashMap.put("column1", toDoLists.get(i).getColumn1());
                    hashMap.put("column2", toDoLists.get(i).getColumn2());
                    hashMap.put("column3", toDoLists.get(i).getColumn3());
                    hashMap.put("column4", toDoLists.get(i).getColumn4());

                    dao.update(toDoLists.get(i), toDoLists.get(i).getKey(), hashMap);


                    if (i == 0) {
                        spinnerSearchFieldsArrayAdapter.clear();

                        searchFields = new ArrayList<>();
                        searchFields.add(0, selectField);
                        searchFields.add(1, editText1s.get(0).getText().toString());
                        searchFields.add(2, editText2s.get(0).getText().toString());
                        searchFields.add(3, editText3s.get(0).getText().toString());
                        searchFields.add(4, editText4s.get(0).getText().toString());

                        spinnerSearchFieldsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, searchFields);
                        spinnerSearchFieldsArrayAdapter.notifyDataSetChanged();
                        searchFieldsSpinner.setAdapter(spinnerSearchFieldsArrayAdapter);
                    }

                    i += 1;
                } while (i < checkboxes.size());
            }

        } else if(view.getId() == addRowButton.getId()){
            addRowLayout(listsSpinner.getSelectedItem().toString(),true);
        }else if(view.getId() == searchButton.getId()){

             if(checkboxes.size() != 0){
                 container.removeAllViews();
                 addRowLayoutWithSearch(0);

                 String chosenColumn = searchFieldsSpinner.getSelectedItem().toString();

                 int searchColumn = 0;

                 if(editText1s.get(0).getText().toString().equals(chosenColumn)){
                     searchColumn = 1;

                 } else if(editText2s.get(0).getText().toString().equals(chosenColumn)){
                     searchColumn = 2;
                 } else if(editText3s.get(0).getText().toString().equals(chosenColumn)){
                     searchColumn = 3;
                 }else if(editText4s.get(0).getText().toString().equals(chosenColumn)){
                     searchColumn = 4;
                 }



                 if(searchColumn == 1){

                     for(int i = 1; i <= checkboxes.size()-1; ++i){
                         if(editText1s.get(i).getText().toString().contains(searchInput.getText().toString().trim())){
                             addRowLayoutWithSearch(i);

                         }
                     }

                 }else if(searchColumn == 2){

                     for(int i = 1; i <= checkboxes.size()-1; ++i){
                         if(editText2s.get(i).getText().toString().contains(searchInput.getText().toString())){
                             addRowLayoutWithSearch(i);

                         }
                     }

                 }else if(searchColumn == 3){

                     for(int i = 1; i <= checkboxes.size()-1; ++i){
                         if(editText3s.get(i).getText().toString().contains(searchInput.getText().toString().trim())){
                             addRowLayoutWithSearch(i);

                         }
                     }

                 }else if(searchColumn == 4){

                     for(int i = 1; i <= checkboxes.size()-1; ++i){
                         if(editText4s.get(i).getText().toString().contains(searchInput.getText().toString().trim())){
                             addRowLayoutWithSearch(i);

                         }
                     }

                 }


             }
        } else if(logutButton.getId() == view.getId()){
            FirebaseAuth.getInstance().signOut();
            finish();

        } else{
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

                    toDoLists.get(index).setCheck(checkboxes.get(index).isChecked());

                    toDoLists.get(index).setColumn1(editText1s.get(index).getText().toString());
                    toDoLists.get(index).setColumn2(editText2s.get(index).getText().toString());
                    toDoLists.get(index).setColumn3(editText3s.get(index).getText().toString());
                    toDoLists.get(index).setColumn4(editText4s.get(index).getText().toString());


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("list",toDoLists.get(index).getList());
                    hashMap.put("check",toDoLists.get(index).isCheck());
                    hashMap.put("index",toDoLists.get(index).getIndex());

                    hashMap.put("column1",toDoLists.get(index).getColumn1());
                    hashMap.put("column2",toDoLists.get(index).getColumn2());
                    hashMap.put("column3",toDoLists.get(index).getColumn3());
                    hashMap.put("column4",toDoLists.get(index).getColumn4());



                    dao.update(toDoLists.get(index), toDoLists.get(index).getKey(),hashMap);


                    if(index == 0){
                        spinnerSearchFieldsArrayAdapter.clear();

                        searchFields = new ArrayList<>();
                        searchFields.add(0,selectField);
                        searchFields.add( 1,editText1s.get(0).getText().toString());
                        searchFields.add( 2,editText2s.get(0).getText().toString());
                        searchFields.add( 3,editText3s.get(0).getText().toString());
                        searchFields.add( 4,editText4s.get(0).getText().toString());

                        spinnerSearchFieldsArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,searchFields);
                        spinnerSearchFieldsArrayAdapter.notifyDataSetChanged();
                        searchFieldsSpinner.setAdapter(spinnerSearchFieldsArrayAdapter);
                    }

                    index = (saveButtons.size()+1);




                }

                index++;
            }while(index < saveButtons.size());

        }

    }

    private void addRowLayoutWithSearch(int index){


        LinearLayout row = new LinearLayout(getApplicationContext());
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

        ((ViewGroup)checkboxes.get(index).getParent()).removeView(checkboxes.get(index));
        row.addView(checkboxes.get(index));

        ((ViewGroup)editText1s.get(index).getParent()).removeView(editText1s.get(index));
        row.addView(editText1s.get(index));

        ((ViewGroup)editText2s.get(index).getParent()).removeView(editText2s.get(index));
        row.addView(editText2s.get(index));

        ((ViewGroup)editText3s.get(index).getParent()).removeView(editText3s.get(index));
        row.addView(editText3s.get(index));

        ((ViewGroup)editText4s.get(index).getParent()).removeView(editText4s.get(index));
        row.addView(editText4s.get(index));


        ((ViewGroup)editButtons.get(index).getParent()).removeView(editButtons.get(index));
        row.addView(editButtons.get(index));

        ((ViewGroup)saveButtons.get(index).getParent()).removeView(saveButtons.get(index));
        row.addView(saveButtons.get(index));



       container.addView(row);
    }

    private void addRowLayout(String listName, boolean newRow){
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

            checkBox.setMinWidth((buttonWidth));
            checkBox.setMaxWidth(buttonWidth);


            EditText editText1 = new EditText(this.getApplicationContext());
            editText1.setLayoutParams(params2Weight);
            editText1.setBackgroundColor(Color.WHITE);
            editText1.setText(R.string.column_1);
            editText1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText1.setEnabled(false);

            editText1.setMinWidth(columnWidth);
            editText1.setMaxWidth(columnWidth);


        EditText editText2 = new EditText(this.getApplicationContext());
            editText2.setLayoutParams(params2Weight);
            editText2.setBackgroundColor(Color.WHITE);
            editText2.setText(R.string.column_2);
            editText2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText2.setEnabled(false);

            editText2.setMinWidth(columnWidth);
            editText2.setMaxWidth(columnWidth);

            EditText editText3 = new EditText(this.getApplicationContext());
            editText3.setLayoutParams(new LayoutParams(params2Weight));
            editText3.setBackgroundColor(Color.WHITE);
            editText3.setText(R.string.column_3);
            editText3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText3.setEnabled(false);

            editText3.setMinWidth(columnWidth);
            editText3.setMaxWidth(columnWidth);

            EditText editText4 = new EditText(this.getApplicationContext());
            editText4.setLayoutParams(params5Weight);
            editText4.setBackgroundColor(Color.WHITE);
            editText4.setText(R.string.column_4);
            editText4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            editText4.setEnabled(false);

            editText4.setMinWidth(columnWidth);
            editText4.setMaxWidth(columnWidth);


            ImageButton editButton = new ImageButton(this.getApplicationContext());
            editButton.setLayoutParams(params1Weight);
            editButton.setBackgroundColor(Color.WHITE);
            editButton.setImageResource(android.R.drawable.ic_menu_edit);
            editButton.setId(rowId);

            editButton.setMaxWidth(buttonWidth);
            editButton.setMaxWidth(buttonWidth);

            ImageButton saveButton = new ImageButton(this.getApplicationContext());
            saveButton.setLayoutParams(params1Weight);
            saveButton.setBackgroundColor(Color.WHITE);
            saveButton.setImageResource(android.R.drawable.ic_menu_save);
            saveButton.setId(rowSaveButtonId);

            saveButton.setMaxWidth(buttonWidth);
            saveButton.setMinimumWidth(buttonWidth);

            row.addView(checkBox);

            row.addView(editText1);
            row.addView(editText2);
            row.addView(editText3);
            row.addView(editText4);

            row.addView(editButton);
            row.addView(saveButton);


            container.addView(row);

            editText1.setMaxWidth((screenWidth/10)*2);
            editText2.setMinWidth((screenWidth/10)*2);



            editButton.setOnClickListener(this);
            saveButton.setOnClickListener(this);

            checkboxes.add(checkBox);

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



            if(checkboxes.size() == 1 && newRow ){
              ToDoList list = new ToDoList(listName, rowId, true,
                        false, column1,column2,column3,column4);

              toDoLists.add(list);

                    dao.addRow(list).addOnSuccessListener(suc-> Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()).addOnFailureListener(err-> Toast.makeText(this,"Fail: " + err.getMessage(),Toast.LENGTH_LONG).show());


            }else if(checkboxes.size() > 0 && newRow){
              ToDoList  list = new ToDoList(listName, rowId, false,
                        false, column1,column2,column3,column4);
                toDoLists.add(list);



                dao.addRow(list).addOnSuccessListener(suc-> Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()).addOnFailureListener(err-> Toast.makeText(this,"Fail: " + err.getMessage(),Toast.LENGTH_LONG).show());

            }



            rowId++;
            rowSaveButtonId--;

        }



    }

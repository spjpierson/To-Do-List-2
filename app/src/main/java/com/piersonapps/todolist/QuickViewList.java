package com.piersonapps.todolist;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.Resources;
import android.graphics.Color;

import android.os.Bundle;
import android.util.TypedValue;

import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


import java.util.ArrayList;

public class QuickViewList extends AppCompatActivity implements View.OnClickListener {

    private Button addRowButton;
    private LinearLayout container;

    private ArrayList<CheckBox> checkboxs;

    private ArrayList<EditText> editText1s;
    private ArrayList<EditText> editText2s;
    private ArrayList<EditText> editText3s;
    private ArrayList<EditText> editText4s;

    private ArrayList<ImageButton> editbuttons;
    private ArrayList<ImageButton> savebuttons;

    private int rowId = 0;
    private int rowSaveButtonId = 999999999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_view_list);

        addRowButton = findViewById(R.id.quick_view_add_row_button);
        container = findViewById(R.id.quick_view_layout_container);

        checkboxs = new ArrayList<CheckBox>();

        editText1s = new ArrayList<EditText>();
        editText2s = new ArrayList<EditText>();
        editText3s = new ArrayList<EditText>();
        editText4s = new ArrayList<EditText>();

        editbuttons = new ArrayList<ImageButton>();

        savebuttons = new ArrayList<ImageButton>();


        addRowLayout();
        addRowButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        if(view.getId() == addRowButton.getId()){
            addRowLayout();
        }else{
            int index = 0;

            do {

                if(editbuttons.get(index).getId() == view.getId()){
                  editText1s.get(index).setEnabled(true);
                  editText2s.get(index).setEnabled(true);
                  editText3s.get(index).setEnabled(true);
                  editText4s.get(index).setEnabled(true);

                  index = (editbuttons.size()+1);
                }

                index++;
            }while(index < editbuttons.size());

            index = 0;

            do{
                if(savebuttons.get(index).getId() == view.getId()){
                    editText1s.get(index).setEnabled(false);
                    editText2s.get(index).setEnabled(false);
                    editText3s.get(index).setEnabled(false);
                    editText4s.get(index).setEnabled(false);

                    index = (savebuttons.size()+1);
                }

                index++;
            }while(index < savebuttons.size());

        }

    }




        private void addRowLayout(){
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

            editbuttons.add(editButton);
            savebuttons.add(saveButton);

            rowId++;
            rowSaveButtonId--;

        }



    }

package com.piersonapps.todolist;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    *  Here wants my dad wrote
    * To Do List
    * Shopping List By store, food, meds, personal care, supply
    * Place to store information Like: windows measurement filter sizes room sizes FILTER Sizes Room Sizes Parts Need, Number Maker
    *
    * Shopping List: By Store Type example Hardware, Food, Supply
    * Repair List Of People Or Company TO Fix And For What
    *
    * I think personal it be better off if we let him create his own spread sheet
    *
    * */

    /*
    * @Monica
    *   add the feature when the user type in a new list and click Add button will create a widgets under main_container_lists_layout.
    *   The widget should look like a business card
    *   show what type of list it is. the type can be show as icon
    *   place for phone number, email, address, website allow the user to be edit and save
    *   when click on width or arrow button start the QuickViewList intent remember later we will have to pass data
    *
    * */
    //The place the user is inputting the text

    EditText main_listInput;
    Button test_button;
    //New list added button M.P.
    Button button_add;
    //Dialog box for popup Window M.P.
    Dialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //creating the ID paths for new list and add button M.P.
        main_listInput = (EditText)findViewById(R.id.main_new_list_input);
        button_add = (Button) findViewById(R.id.main_button_add);
        //onclick listener when input added M.P.
        button_add.setOnClickListener(this);
        mDialog = new Dialog(this);
 



    // The button is only so I can get to the quick view
        test_button = findViewById(R.id.main_test_button);
        test_button.setOnClickListener(this);

}

    //  private void showToast(String main_newList) {
    //     Toast.makeText(MainActivity.this, main_newList, Toast.LENGTH_SHORT).show();
    // }

    public void onClick(View view){

        if(view.getId() == test_button.getId()) {
            Intent intent = new Intent(this, QuickViewList.class);
            startActivity(intent);
        }
        else if(view.getId() == button_add.getId()){
            // main_newList = main_listInput.getText().toString();
            // showToast(main_newList);

            //Popup Window M.P
            mDialog.setContentView(R.layout.newlist);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.show();
        }
}
}
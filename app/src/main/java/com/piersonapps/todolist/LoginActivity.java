package com.piersonapps.todolist;

import androidx.annotation.ColorLong;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.collection.LLRBNode;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String viewState;
    private TextView headerDisplay;
    private EditText emailInputText;
    private EditText passwordInputText;
    private EditText reEnterPasswordInputText;
    private EditText nameInputText;
    private Button loginButton;
    private Button newAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewState = "LOGIN_STATE";
        headerDisplay = findViewById(R.id.login_header_display_text);
        emailInputText = findViewById(R.id.login_email_input);
        passwordInputText = findViewById(R.id.login_password_input);
        reEnterPasswordInputText = findViewById(R.id.login_reenter_password_input);
        nameInputText = findViewById(R.id.login_name_input);
        loginButton = findViewById(R.id.login_login_button);
        newAccountButton = findViewById(R.id.login_create_new_account_button);

        loginButton.setOnClickListener(this);
        newAccountButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == loginButton.getId()){
            if(viewState.equals("LOGIN_STATE")){
                String email = emailInputText.getText().toString().trim();
                String password = passwordInputText.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        FirebaseUser firebaseUser;
                        firebaseUser = task.getResult().getUser();

                        Intent intent = new Intent(getApplicationContext(),QuickViewList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("user_id",firebaseUser.getUid());
                        intent.putExtra("email_id",firebaseUser.getEmail());
                        intent.putExtra("name_id",firebaseUser.getDisplayName());

                        startActivity(intent);
                        finish();
                    });
                }

            }else if(viewState.equals("NEW_ACCOUNT_STATE")){
                headerDisplay.setText("LOGIN");
                reEnterPasswordInputText.setVisibility(View.INVISIBLE);
                nameInputText.setVisibility(View.INVISIBLE);
                loginButton.setText("Login");
                viewState = "LOGIN_STATE";
            }

        }else if(view.getId() == newAccountButton.getId()){
            if(viewState.equals("LOGIN_STATE")){
                headerDisplay.setText("REGISTER");
                reEnterPasswordInputText.setVisibility(View.VISIBLE);
                nameInputText.setVisibility(View.VISIBLE);
                loginButton.setText("BACK TO LOGIN");
                viewState = "NEW_ACCOUNT_STATE";
            }else if(viewState.equals("NEW_ACCOUNT_STATE")){
                 String email = emailInputText.getText().toString().trim();
                 String reEnterPassword = reEnterPasswordInputText.getText().toString().trim();
                 String password = passwordInputText.getText().toString().trim();
                 String fullname = nameInputText.getText().toString().trim();

                 if(password.equals(reEnterPassword) && !email.isEmpty() && !fullname.isEmpty() ){

                     FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                             task -> {
                                 FirebaseUser firebaseUser;
                                 firebaseUser = task.getResult().getUser();
                                 firebaseUser.sendEmailVerification();


                                 UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                                 firebaseUser.updateProfile(profileUpdate);


                                 Intent intent = new Intent(getApplicationContext(),QuickViewList.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                 intent.putExtra("user_id",firebaseUser.getUid());
                                 intent.putExtra("email_id",firebaseUser.getEmail());
                                 intent.putExtra("name_id",firebaseUser.getDisplayName());

                                 startActivity(intent);
                                 finish();
                             }
                     );

                 }else if(email.isEmpty()){
                     emailInputText.setBackgroundColor(Color.RED);
                     headerDisplay.setText("Email is empty");
                }else if(fullname.isEmpty()){
                     emailInputText.setBackgroundColor(Color.WHITE);
                     nameInputText.setBackgroundColor(Color.RED);
                     headerDisplay.setText("Name is empty");
                 } else if(!password.equals(reEnterPassword)){
                     emailInputText.setBackgroundColor(Color.WHITE);
                     nameInputText.setBackgroundColor(Color.WHITE);
                     passwordInputText.setBackgroundColor(Color.RED);
                     reEnterPasswordInputText.setBackgroundColor(Color.RED);
                     headerDisplay.setText("Password does not match!");
                 }

            }
        }
    }
}
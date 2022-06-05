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
import android.widget.Toast;

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
    private int attemps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        attemps = 0;
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
                      if(task.isSuccessful()) {
                          FirebaseUser firebaseUser;
                          firebaseUser = task.getResult().getUser();

                          Intent intent = new Intent(getApplicationContext(), QuickViewList.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                          intent.putExtra("user_id", firebaseUser.getUid());
                          intent.putExtra("email_id", firebaseUser.getEmail());
                          intent.putExtra("name_id", firebaseUser.getDisplayName());

                          startActivity(intent);
                          finish();
                      }else{
                          if(task.getException().toString().contains("password") && attemps < 3){
                              attemps += 1;
                              Toast.makeText(getApplicationContext(),"Your password is incorrect. Attempts: "+attemps,Toast.LENGTH_LONG).show();

                          }else if(task.getException().toString().contains("password") && attemps > 2 ){
                              viewState = "RESET_PASSWORD";
                              passwordInputText.setVisibility(View.INVISIBLE);
                              newAccountButton.setVisibility(View.INVISIBLE);
                              loginButton.setText("REST PASSWORD");
                              headerDisplay.setText("REST PASSWORD");
                          }
                      }

                    });
                }

            }else if(viewState.equals("RESET_PASSWORD")){
                String email = emailInputText.getText().toString();
                if(!TextUtils.isEmpty(email)){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(
                            task -> {
                                Toast.makeText(getApplicationContext(),"A password reset email was sent to you",Toast.LENGTH_LONG).show();
                                viewState = "LOGIN_STATE";
                                passwordInputText.setVisibility(View.VISIBLE);
                                newAccountButton.setVisibility(View.VISIBLE);

                                loginButton.setText("LOGIN");
                                headerDisplay.setText("LOGIN");
                            }
                    );
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

                 if(password.equals(reEnterPassword) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(fullname)){

                     FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                             task -> {
                                 FirebaseUser firebaseUser;
                                 firebaseUser = task.getResult().getUser();
                                 firebaseUser.sendEmailVerification();
                                 if(task.isSuccessful()){

                                 UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                                 firebaseUser.updateProfile(profileUpdate);


                                 Intent intent = new Intent(getApplicationContext(),QuickViewList.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                 intent.putExtra("user_id",firebaseUser.getUid());
                                 intent.putExtra("email_id",firebaseUser.getEmail());
                                 intent.putExtra("name_id",firebaseUser.getDisplayName());

                                 startActivity(intent);
                                 finish(); }
                             }
                     );

                 }else if(TextUtils.isEmpty(email)){
                     emailInputText.setBackgroundColor(Color.RED);
                     headerDisplay.setText("Email is empty");
                }else if(TextUtils.isEmpty(fullname)){
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
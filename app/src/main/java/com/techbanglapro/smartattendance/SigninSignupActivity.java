package com.techbanglapro.smartattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SigninSignupActivity extends AppCompatActivity {

    private EditText instute_name, department_name,name,user_email, user_password;
    private Button  signup_button;

    private TextView have_account;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        IntitilizeAllVariable();

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupUser();
            }
        });

        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });


    }

    private void IntitilizeAllVariable()
    {
        instute_name = findViewById(R.id.institute_name);
        department_name = findViewById(R.id.department_name);
        name = findViewById(R.id.teacher_name);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);

        signup_button = findViewById(R.id.signup_button);

        have_account = findViewById(R.id.have_account);
    }

    private void SignupUser() {
        final String institute = instute_name.getText().toString();
        final String department = department_name.getText().toString();
        final String teacher_name = name.getText().toString();

        final String email = user_email.getText().toString().trim();
        final String password = user_password.getText().toString().trim();

        if (TextUtils.isDigitsOnly((institute))|| TextUtils.isDigitsOnly((department)) || TextUtils.isDigitsOnly((teacher_name)) ||
                TextUtils.isDigitsOnly((email)) || TextUtils.isDigitsOnly((password)))
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else if(password.length() <6)
        {
            Toast.makeText(this, "Minimum 6 word password required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                          /*  Intent intent = new Intent(SigninSignupActivity.this,MainActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
                                firebaseUser = mAuth.getCurrentUser();
                                userId = firebaseUser.getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);


                                HashMap<String,String> hashMap = new HashMap<>();

                                hashMap.put("institute",institute);
                                hashMap.put("department",department);
                                hashMap.put("teacher_name",teacher_name);
                                hashMap.put("email",email);
                                hashMap.put("password",password);

                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            //Toast.makeText(SigninSignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                           /* Intent intent = new Intent(SigninSignupActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);*/

                                        }
                                        else
                                        {
                                            String message = task.getException().toString();
                                            //Toast.makeText(SigninSignupActivity.this, "Error : "+message, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }

                            else
                            {
                                String message = task.getException().toString();
                               // Toast.makeText(SigninSignupActivity.this, "Error : "+message, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
    }

}
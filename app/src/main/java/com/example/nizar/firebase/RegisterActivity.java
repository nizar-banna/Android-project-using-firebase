package com.example.nizar.firebase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

   private EditText mNameField;
   private EditText mEmailField;
   private EditText mPassword;
   private Button mButtonRegister;

   private FirebaseAuth mAuth;
   private ProgressDialog mProgress;
   private DatabaseReference mDataBase;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameField = findViewById(R.id.name);
        mEmailField = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mButtonRegister = findViewById(R.id.register_btn);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress = new ProgressDialog(this);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
                mProgress.setMessage("Signing Up ...");
                mProgress.show();

            }
        });

    }

    private void startRegister() {

        final String name = mNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String password = mPassword.getText().toString();


        if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(password) ){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser_db = mDataBase.child(user_id);
                        currentUser_db.child("name").setValue(name);
                        currentUser_db.child("image").setValue("default");
                    }

                    mProgress.dismiss();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Fill the fields",Toast.LENGTH_LONG).show();
        }


    }
}

package com.example.nizar.firebase;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private Button sign_up_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        sign_up_btn = (Button)findViewById(R.id.sign_up_btn);
        mLoginBtn = (Button)findViewById(R.id.sign_in_btn);
        mLoginEmailField = findViewById(R.id.email_login);
        mLoginPasswordField = findViewById(R.id.password_login);


        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddPostActivity.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

    }

    private void startLogin() {
       String email = mLoginEmailField.getText().toString();
       String password = mLoginPasswordField.getText().toString();
       if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       checkUserExist();

                   }
               }
           });
       }else{
           Toast.makeText(getApplicationContext(),"Fill the fields",Toast.LENGTH_LONG).show();
       }



    }

    private void checkUserExist() {
      final String user_id = mAuth.getCurrentUser().getUid();

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){

                    Intent intent = new Intent(MainActivity.this,UserProfile.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(),"You Need To Sign Up",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

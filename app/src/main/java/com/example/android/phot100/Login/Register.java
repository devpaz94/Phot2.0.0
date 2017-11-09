package com.example.android.phot100.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.phot100.MainActivity;
import com.example.android.phot100.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private TextInputLayout mRegName;
    private TextInputLayout mRegEmail;
    private TextInputLayout mRegPassword;
    private Button mRegBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = (Toolbar) findViewById(R.id.reg_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register New User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegName = (TextInputLayout) findViewById(R.id.reg_name);
        mRegEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mRegPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mRegBtn = (Button) findViewById(R.id.reg_button);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String display_name = mRegName.getEditText().getText().toString();
                String email = mRegEmail.getEditText().getText().toString();
                String password = mRegPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    register_newUser(display_name, email, password);
                }
            }
        });
    }

    private void register_newUser(final String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("Image", "default");
                            userMap.put("thumb_image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent mainIntent = new Intent(Register.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Register.this, "Error Creating new User", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}

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

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private TextInputLayout mEmailLogin;
    private TextInputLayout mPasswordLogin;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mLoginBtn = (Button) findViewById(R.id.login_button);
        mEmailLogin = (TextInputLayout) findViewById(R.id.email_login);
        mPasswordLogin = (TextInputLayout) findViewById(R.id.password_login);

        mToolbar = (Toolbar) findViewById(R.id.login_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = mEmailLogin.getEditText().getText().toString();
                String password = mPasswordLogin.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Signing In, Please check form and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

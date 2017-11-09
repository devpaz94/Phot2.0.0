package com.example.android.phot100.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.phot100.R;

public class StartActivity extends AppCompatActivity {

    private Button mRegButn;
    private Button mSigninBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSigninBtn = (Button) findViewById(R.id.sign_in_btn);
        mRegButn = (Button) findViewById(R.id.need_account_button);

        mRegButn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent regIntent = new Intent(StartActivity.this, Register.class);
                startActivity(regIntent);
            }
        });
        mSigninBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent signinIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(signinIntent);
            }
        });
    }
}

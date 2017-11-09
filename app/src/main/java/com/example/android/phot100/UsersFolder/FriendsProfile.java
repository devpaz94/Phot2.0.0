package com.example.android.phot100.UsersFolder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.phot100.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsProfile extends AppCompatActivity {

    private TextView mDisplayName;
    private Button mSendRequestButton;
    private CircleImageView mProfileImageView;
    private String mUid;

    private DatabaseReference mUsersDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        mUid = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);

        mProfileImageView = findViewById(R.id.profile_page_image);
        mDisplayName = findViewById(R.id.profile_name);
        mSendRequestButton = findViewById(R.id.send_request_btn);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("Image").getValue().toString();

                mDisplayName.setText(display_name);
                Picasso.with(FriendsProfile.this).load(image).placeholder(R.drawable.default_image).into(mProfileImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

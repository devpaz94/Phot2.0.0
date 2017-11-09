package com.example.android.phot100.UsersFolder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.phot100.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsers extends AppCompatActivity {


    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersdatabase;
    private Button mLinkButton;
    private DatabaseReference mSentReqDatabase;
    private DatabaseReference mReceivedReqDatabase;
    private int mCurrentState;
    private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar) findViewById(R.id.all_users_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(AllUsers.this));

        mUsersdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mReceivedReqDatabase = FirebaseDatabase.getInstance().getReference().child("received_requests");
        mSentReqDatabase = FirebaseDatabase.getInstance().getReference().child("sent_requests");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();



            FirebaseRecyclerAdapter<Users, UsersViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                    Users.class,
                    R.layout.users_layout,
                    UsersViewHolder.class,
                    mUsersdatabase
            ){
                @Override
                protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position){
                    usersViewHolder.setDisplayName(users.getName());
                    usersViewHolder.setUserImage(users.getThumb_image(), getApplicationContext());

                    final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    final String user_id = getRef(position).getKey();
                    mLinkButton = usersViewHolder.mView.findViewById(R.id.link_button);



                    mLinkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mCurrentState == 0){
                                mSentReqDatabase.child(mCurrentUser.getUid()).child(user_id).child("sent")
                                        .setValue(currentDateTimeString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mReceivedReqDatabase.child(user_id).child(mCurrentUser.getUid()).child("received").setValue(currentDateTimeString);
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(AllUsers.this, "Hello", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            };
            mUsersList.setAdapter(recyclerAdapter);
        }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.name_text_view);
            userName.setText(name);

        }

        public void setUserImage(String thumb_image, Context context){
            CircleImageView circleImageView = (CircleImageView) mView.findViewById(R.id.circleImageView);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.default_image).into(circleImageView);
        }
    }


}

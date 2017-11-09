package com.example.android.phot100.MainFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.phot100.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Requests extends Fragment {

    private RecyclerView mRequestsList;
    private DatabaseReference mRequestsDatabase;
    private DatabaseReference mUsersDatabase;
    private String mCurrent_user_id;
    private View mMainView;
    private FirebaseUser mAuth;
    private ImageView mAcceptBtn;
    private ImageView mDeclineBtn;
    private DatabaseReference mLinkedDatabase;
    private DatabaseReference mSentDatabse;


    public Requests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);

        mRequestsList = (RecyclerView) mMainView.findViewById(R.id.requests_list);

        mSentDatabse = FirebaseDatabase.getInstance().getReference().child("sent_requests");
        mLinkedDatabase = FirebaseDatabase.getInstance().getReference().child("linked_users");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRequestsList.setHasFixedSize(true);
        mRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));


        return mMainView;
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuth != null){mCurrent_user_id = mAuth.getUid();}
        else{mCurrent_user_id = "default";};

        mRequestsDatabase = FirebaseDatabase.getInstance().getReference().child("received_requests");

        FirebaseRecyclerAdapter<RequestObject, RequestsViewHolder> requestsAdapter = new FirebaseRecyclerAdapter<RequestObject, RequestsViewHolder>(
                RequestObject.class,
                R.layout.request_list_item,
                RequestsViewHolder.class,
                mRequestsDatabase.child(mCurrent_user_id)
        ) {
            @Override
            protected void populateViewHolder(final RequestsViewHolder viewHolder, RequestObject model, int position) {
                    viewHolder.setDate(model.getReceived());

                    final String list_user_id = getRef(position).getKey();

                    mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userName = dataSnapshot.child("name").getValue().toString();
                            String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                            viewHolder.setName(userName);
                            viewHolder.setImage(getContext(), thumbImage);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mAcceptBtn = viewHolder.mView.findViewById(R.id.accept_btn);
                    mDeclineBtn = viewHolder.mView.findViewById(R.id.decline_btn);

                    mAcceptBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acceptRequest(list_user_id);
                        }
                    });

                    mDeclineBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delcineRequest(list_user_id);
                        }
                    });

            }
        };

        mRequestsList.setAdapter(requestsAdapter);
    }
    public static class RequestsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public RequestsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setDate(String date){
            TextView email_view = mView.findViewById(R.id.email_text_view);
            email_view.setText(date);
        }

        public void setName(String name){
            TextView name_view = mView.findViewById(R.id.name_text_view);
            name_view.setText(name);
        }

        public void setImage(Context context, String thumb_image){
            CircleImageView circleImageView = (CircleImageView) mView.findViewById(R.id.request_image);
            Picasso.with(context).load(thumb_image).placeholder(R.drawable.default_image).into(circleImageView);
        }
    }

    private void acceptRequest(final String list_uid){

        mLinkedDatabase.child(mCurrent_user_id).setValue(list_uid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mLinkedDatabase.child(list_uid).setValue(mCurrent_user_id);
                }
            }
        });

        delcineRequest(list_uid);
    }

    private void delcineRequest(final String list_uid){

        mRequestsDatabase.child(mCurrent_user_id).child(list_uid).removeValue();
        mSentDatabse.child(list_uid).child(mCurrent_user_id).removeValue();

    }
}

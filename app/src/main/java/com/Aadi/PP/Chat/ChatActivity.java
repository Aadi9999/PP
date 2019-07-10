package com.Aadi.PP.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Aadi.PP.Matches.MatchesFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Aadi.PP.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ChatActivity extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    TextView mMatchName, userLastSeen;

    private CircularImageView mMatchImage;
    private NestedScrollView mScrollview;

    FirebaseUser fuser;

    DatabaseReference reference, Userreport;

    Intent intent;

    ValueEventListener seenListener;

    private TextView last_msg;

    private EditText mSendEditText;

    private Button mSendButton;

    private ImageView sportsicon;

    private FirebaseAuth mAuth;

    private String currentUserID, matchId, chatId, matchName;

    DatabaseReference mDatabaseUser, mDatabaseChat, usersDb, RootRef, mDatabaseUser1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_chat, container, false);

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        userLastSeen = view.findViewById(R.id.user_last_seen);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolBar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backicon);


        sportsicon = view.findViewById(R.id.sporticon3);
        mMatchName = view.findViewById(R.id.MatchName);
        mMatchImage = view.findViewById(R.id.MatchImage);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        RootRef = FirebaseDatabase.getInstance().getReference();
        matchId = getActivity().getIntent().getExtras().getString("matchId");

        matchName = getActivity().getIntent().getExtras().getString("name");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseUser1 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        DisplayUserInfo();
        DisplayRecieverInfo();



        Userreport = RootRef.child("Users").child(matchId).child("Report");
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), getActivity());
        mRecyclerView.setAdapter(mChatAdapter);


         mScrollview = view.findViewById(R.id.scroll_view);

        mSendEditText = view.findViewById(R.id.message);
        mSendButton = view.findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

                mScrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }}, 100);
            }
        });


        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
    }



    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){

            DatabaseReference newMessageDb = mDatabaseChat.push();

            Calendar calForTimeStamp = Calendar.getInstance();
            SimpleDateFormat timeStamp = new SimpleDateFormat("hh:mm a");
            String currentTimeStamp = timeStamp.format(calForTimeStamp.getTime());

            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessage.put("timestamp", currentTimeStamp);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void DisplayRecieverInfo ()
    {

        RootRef.child("Users").child(matchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String profileImageUrl = "";

                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    Glide.with(getActivity().getApplicationContext()).load(profileImageUrl).into(mMatchImage);

                    matchName = dataSnapshot.child("name").getValue().toString();
                    mMatchName.setText(matchName);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.chat_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    // Check whether user is online or not
    private void DisplayUserInfo()
    {
        RootRef.child("Users").child(matchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){


                    final String type = dataSnapshot.child("userState").child("type").getValue().toString();
                    final String lastDate = dataSnapshot.child("userState").child("date").getValue().toString();
                    final String lastTime = dataSnapshot.child("userState").child("time").getValue().toString();

                    if (type.equals("online"))
                    {
                        userLastSeen.setText("online");
                    }
                    else
                    {
                        userLastSeen.setText("last seen:  " + lastTime + "  " + lastDate);
                    }


                    if (dataSnapshot.child("sports").getValue()!=null) {
                        String sports = dataSnapshot.child("sports").getValue().toString();


                        if (sports.equals("Badminton")) {
                            sportsicon.setImageResource(R.drawable.badminton);
                        }

                        if (sports.equals("Basketball")) {
                            sportsicon.setImageResource(R.drawable.basketball);
                        }

                        if (sports.equals("Football")) {
                            sportsicon.setImageResource(R.drawable.soccer);
                        }

                        if (sports.equals("Table tennis")) {
                            sportsicon.setImageResource(R.drawable.pingpong);
                        }

                        if (sports.equals("Exercising")) {
                            sportsicon.setImageResource(R.drawable.gym);
                        }

                        if (sports.equals("Baseball")) {
                            sportsicon.setImageResource(R.drawable.baseball);
                        }

                        if (sports.equals("Tennis")) {
                            sportsicon.setImageResource(R.drawable.tennis);
                        }

                        if (sports.equals("Archery")) {
                            sportsicon.setImageResource(R.drawable.archery);
                        }

                        if (sports.equals("Snooker")) {
                            sportsicon.setImageResource(R.drawable.eightball);
                        }

                        if (sports.equals("Cycling")) {
                            sportsicon.setImageResource(R.drawable.cycling);
                        }

                        if (sports.equals("Golf")) {
                            sportsicon.setImageResource(R.drawable.golf);
                        }

                        if (sports.equals("Rugby")) {
                            sportsicon.setImageResource(R.drawable.rugby);
                        }

                        if (sports.equals("Running")) {
                            sportsicon.setImageResource(R.drawable.shoe);
                        }

                        if (sports.equals("American Football")) {
                            sportsicon.setImageResource(R.drawable.americanfootball);
                        }

                        if (sports.equals("Volleyball")) {
                            sportsicon.setImageResource(R.drawable.volleyball);
                        }

                        if (sports.equals("Cricket")) {
                            sportsicon.setImageResource(R.drawable.cricket);
                        }

                        if (sports.equals("default")) {
                            sportsicon.setImageResource(R.drawable.shoe);
                        }
                    }

            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateUserStatus(String state)
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time",saveCurrentTime);
        currentStateMap.put("date",saveCurrentDate);
        currentStateMap.put("type",state);


        usersDb.child(currentUserID).child("userState")
                .updateChildren(currentStateMap);
    }



    @Override
    public void onResume() {
        super.onResume();

        mScrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }}, 100);

        updateUserStatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();

        updateUserStatus("offline");
    }



    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), MatchesFragment.class);
        startActivity(intent);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), MatchesFragment.class);
                startActivity(intent);
                return true;
            case R.id.Report:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Report this contact to SportConnect?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Userreport.setValue("Reported");
                                Toasty.info(getActivity(), "User Reported", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                return true;

            case R.id.Block:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Block this Contact?")
                        .setMessage("Blocked contacts will be permanently removed from Connects page, and you will not be able to message them.")
                        .setPositiveButton("Block", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabaseUser1.removeValue();
                                Intent intent = new Intent(getActivity(), MatchesFragment.class);
                                startActivity(intent);
                                return;
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

        }
        return super.onOptionsItemSelected(item);

    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    mScrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }}, 700);

                    String message = null;
                    String createdByUser = null;
                    String timestamp = null;
                    String LastSeen = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }

                    if(dataSnapshot.child("timestamp").getValue()!=null){
                        timestamp = dataSnapshot.child("timestamp").getValue().toString();
                    }



                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }

                    if(message!=null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, timestamp, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}
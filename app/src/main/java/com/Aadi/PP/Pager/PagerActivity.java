package com.Aadi.PP.Pager;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.Aadi.PP.ProfileActivity;
import com.Aadi.PP.R;
import com.Aadi.PP.mActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PagerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private EditText mSportsField;
    private String sports, userId;
    private FirebaseAuth mAuth;
    private FrameLayout mFramelayout;
    private DatabaseReference mUserDatabase;

    private boolean mShowingFragments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        mViewPager = findViewById(R.id.viewPager);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);


        animateIn();

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.drawable.soccer));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.drawable.basketball));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.drawable.tennis));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.drawable.cricket));
        mCardAdapter.addCardItem(new CardItem(R.string.title_5, R.drawable.volleyball));
        mCardAdapter.addCardItem(new CardItem(R.string.title_6, R.drawable.baseball));
        mCardAdapter.addCardItem(new CardItem(R.string.title_7, R.drawable.hockey));
        mCardAdapter.addCardItem(new CardItem(R.string.title_8, R.drawable.americanfootball));
        mCardAdapter.addCardItem(new CardItem(R.string.title_9, R.drawable.pingpong));
        mCardAdapter.addCardItem(new CardItem(R.string.title_10, R.drawable.shoe));
        mCardAdapter.addCardItem(new CardItem(R.string.title_11, R.drawable.rugby));
        mCardAdapter.addCardItem(new CardItem(R.string.title_12, R.drawable.badminton));
        mCardAdapter.addCardItem(new CardItem(R.string.title_13, R.drawable.golf));
        mCardAdapter.addCardItem(new CardItem(R.string.title_14, R.drawable.gym));
        mCardAdapter.addCardItem(new CardItem(R.string.title_15, R.drawable.waterpolo));
        mCardAdapter.addCardItem(new CardItem(R.string.title_16, R.drawable.cycling));
        mCardAdapter.addCardItem(new CardItem(R.string.title_17, R.drawable.eightball));
        mCardAdapter.addCardItem(new CardItem(R.string.title_18, R.drawable.archery));

        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);


        mCardShadowTransformer.enableScaling(true);
        mFragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);


        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

    }

    @Override
    public void onClick(View view) {
        animateOut();
        saveUserInformation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(PagerActivity.this, mActivity.class);
                startActivity(intent1);
                finish();
            }
        }, 700);


    }


    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("sports") != null) {
                        sports = map.get("sports").toString();
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void animateIn() {
        // start translationY - 2000
        // final translationY - 0
        // Duration - 1 sec
        CustomAnimationsUtils.animateY(mViewPager, 2000, 0, 1000);
    }


    private void animateOut() {

        CustomAnimationsUtils.animateY(mViewPager, 0, 2000, 1000);
    }




    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void saveUserInformation() {

        int position = mViewPager.getCurrentItem();
        Map userInfo = new HashMap();

        switch(position) {

            case 0:
                userInfo.put("sports", "Football");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 1:
                userInfo.put("sports", "Basketball");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 2:
                userInfo.put("sports", "Tennis");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 3:
                userInfo.put("sports", "Cricket");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 4:
                userInfo.put("sports", "Volleyball");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 5:
                userInfo.put("sports", "Baseball");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 6:
                userInfo.put("sports", "Hockey");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 7:
                userInfo.put("sports", "American Football");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 8:
                userInfo.put("sports", "Table Tennis");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 9:
                userInfo.put("sports", "Running");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 10:

                userInfo.put("sports", "Rugby");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 11:

                userInfo.put("sports", "Badminton");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 12:

                userInfo.put("sports", "Golf");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 13:

                userInfo.put("sports", "Exercising");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 14:

                userInfo.put("sports", "Swimming");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 15:

                userInfo.put("sports", "Cycling");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 16:

                userInfo.put("sports", "Snooker");
                mUserDatabase.updateChildren(userInfo);
                return;

            case 17:

                userInfo.put("sports", "Archery");
                mUserDatabase.updateChildren(userInfo);
                return;


        }

    }






    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

}

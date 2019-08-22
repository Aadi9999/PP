package com.Aadi.PP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.Aadi.PP.Pager.CardFragmentPagerAdapter;
import com.Aadi.PP.Pager.CardItem;
import com.Aadi.PP.Pager.CardPagerAdapter;
import com.Aadi.PP.Pager.CustomAnimationsUtils;
import com.Aadi.PP.Pager.PagerActivity;
import com.Aadi.PP.Pager.ShadowTransformer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SignupFragment3 extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_pager, container, false);
        mViewPager = rootView.findViewById(R.id.viewPager);
        mButton = rootView.findViewById(R.id.button);


        ProgressBar progressBar = rootView.findViewById(R.id.my_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.color1), PorterDuff.Mode.SRC_IN );

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

        mFragmentCardAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                dpToPixels(2, getContext()));

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

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                animateOut();
                saveUserInformation();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fragment fragment = new SignupFragment4();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(android.R.id.content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                }, 700);
            }
        });


        return rootView;
    }




    public void onClick(View view) {
        animateOut();
        saveUserInformation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent1);
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

package com.Aadi.PP;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class PhonetwoActivity extends AppCompatActivity {

    private String mVerificationId;
    private FirebaseAuth mAuth;
    private OtpTextView otpTextView;
    private Toolbar toolbar;
    private String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonetwo);
        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);
        toolbar = findViewById(R.id.toolBar);
        setActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_18);
        getActionBar().setTitle("");


        otpTextView = findViewById(R.id.otp_view);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {

                String code = otpTextView.getOTP();
                if (code.isEmpty() || code.length() < 6) {
                    otpTextView.showError();

                }
                //verifying the code entered manually
                verifyVerificationCode(otp);
            }
        });



    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpTextView.setOTP(code);
                otpTextView.showSuccess();
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {


        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {try {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }catch (Exception e){
        otpTextView.showError();
        Toast toast = Toasty.error(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
        toast.show();
    }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PhonetwoActivity.this, PhoneoneActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), PhoneoneActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhonetwoActivity.this, task -> {
                    if (task.isSuccessful()) {

                    SharedPreferences.Editor editor = getSharedPreferences("ph_num", MODE_PRIVATE).edit();
                    editor.putString("myNum", mobile);
                    editor.apply();


                    Intent intent2 = new Intent(PhonetwoActivity.this, BetweenActivity.class);
                    startActivity(intent2);
                    finish();
                    return;

                    }

                     else {

                        //verification unsuccessful.. display an error message
                        otpTextView.showError();
                        String message = "Something is wrong, we will fix it soon";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered";
                        }

                    }
                });
    }

}
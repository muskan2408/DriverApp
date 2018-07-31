package in.equipshare.driverway;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.utils.SessionManagement;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMobile extends AppCompatActivity {

    private static final String TAG = "MobileAuthActivity";

    String phoneNumber;

    private ConstraintLayout phoneLayout,codeLayout;
    private TextInputLayout phoneET,codeET;
    private ProgressBar phoneProgress,codeProgress;
    private Button sendCodeBT;
    private TextView errorText;
    String token;
    Model model;
    SessionManagement session;

    private FirebaseAuth mAuth;

    private int btnType = 0;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    protected PhoneAuthProvider.ForceResendingToken mResendToken;

    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_auth);



        phoneLayout = findViewById(R.id.phoneLayout);
        codeLayout  = findViewById(R.id.codeLayout);

        phoneET = findViewById(R.id.phoneET);
        codeET = findViewById(R.id.codeET);

        phoneProgress = findViewById(R.id.phoneProgress);
        codeProgress = findViewById(R.id.codeProgress);

        sendCodeBT = findViewById(R.id.sendCodeBT);

        errorText = findViewById(R.id.errorText);
        FirebaseApp.initializeApp(this);

        session=new SessionManagement(this);
        mAuth = FirebaseAuth.getInstance();


        sendCodeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendCodeBT.setError(null);

                if (btnType==0) {

                     phoneNumber = "+91"+phoneET.getEditText().getText().toString();

                    if(phoneNumber.length()==13){

                        phoneProgress.setVisibility(View.VISIBLE);
                        phoneET.setEnabled(false);
                        //sendCodeBT.setVisibility(View.GONE);

                        Call<Model> call=retrofitInterface.checkMobile(phoneNumber);

                        call.enqueue(new Callback<Model>() {
                            @Override
                            public void onResponse(Call<Model> call, Response<Model> response) {
                                model=response.body();
                                if(model.getMsg().equals("mobile number already exist")){
                                    phoneET.setError("Mobile number already exist...");
                                    phoneET.setEnabled(true);

                                }
                                else{
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            phoneNumber,
                                            30,
                                            TimeUnit.SECONDS,
                                            EditMobile.this,
                                            mCallbacks);
                                }

                            }

                            @Override
                            public void onFailure(Call<Model> call, Throwable t) {
                                phoneET.setError("Mobile number already exist, enter different mobile number");

                            }
                        });



                    }else {
                        phoneET.setError("Please enter a valid number...");
                    }



                }else {

                    String code = codeET.getEditText().getText().toString();

                    if(code.length()!=6){

                        codeET.setError("Please enter a valid code");

                    }else {

                        sendCodeBT.setEnabled(false);
                        codeProgress.setVisibility(View.VISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,code);

                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }



                }
            }
        });



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorText.setText(e.getMessage());
                    Log.d("error",e.getLocalizedMessage());
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    errorText.setText("Invalid mobile number");
                    Log.d("error",e.getLocalizedMessage());
                } else {
                    errorText.setText(e.getMessage());
                    Log.d("error",e.getLocalizedMessage());
                }

                errorText.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

                phoneProgress.setVisibility(View.INVISIBLE);

                codeLayout.setVisibility(View.VISIBLE);

                btnType = 1;

                sendCodeBT.setEnabled(true);
                sendCodeBT.setText("Verify Code");




            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            String contactNo = user.getPhoneNumber();
                            HashMap<String, String> hashMap = session.getUserDetails();

                            token=hashMap.get(SessionManagement.KEY_TOKEN);
                            Call<Model> call= retrofitInterface.updateMobile(token,contactNo);

                            call.enqueue(new Callback<Model>() {
                                @Override
                                public void onResponse(Call<Model> call, Response<Model> response) {
                                    model=response.body();
                                    if (model.getMsg().equals("mobile updated")) {
                                        new AlertDialog.Builder(EditMobile.this).setTitle("Mobile updated Successfully")
                                                .setMessage("Please Login to Continue")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(EditMobile.this, Login.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).show();
                                    }
                                    else{
                                        Toast.makeText(EditMobile.this,"Something went wrong please try again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Model> call, Throwable t) {

                                }
                            });


//       Toast.makeText(getApplicationContext(),"valid",Toast.LENGTH_SHORT).show();
//                            Toast.makeText(MobileAuthActivity.this,"Code sent", Toast.LENGTH_SHORT).show();
//


                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                codeProgress.setVisibility(View.INVISIBLE);

                                errorText.setText("The sms verification code is invalid. Please check the code");
                                Log.d(TAG,task.getException().getLocalizedMessage());

                                errorText.setVisibility(View.VISIBLE);
                                sendCodeBT.setEnabled(true);


                            }
                        }
                    }
                });
    }





}


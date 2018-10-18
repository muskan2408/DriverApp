package in.equipshare.driverway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.List;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.utils.SessionManagement;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements Serializable{


    PrefManager prefManager;
    TextInputLayout mobileno, pinno;
    Button login , account;
    TextView forget;
    ProgressDialog progressDialog;
    Model model;
    String  token;
    SessionManagement session;
    private static final String TAG="Login";
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    public static Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         mobileno=(TextInputLayout)findViewById(R.id.mobileno);
         pinno=(TextInputLayout)findViewById(R.id.pin);
         forget=(TextView)findViewById(R.id.forget);
         account=(Button)findViewById(R.id.create);
         session=new SessionManagement(getApplicationContext());

         forget.setVisibility(View.GONE);
        prefManager=new PrefManager(getApplicationContext());
//        Toast.makeText(getApplicationContext(),
//        "User Login Status: " + prefManager.isUserLoggedIn(),
//        Toast.LENGTH_LONG).show();
        login=(Button)findViewById(R.id.login);



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent (Login.this,Forgetpin.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,MobileAuthActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(Login.this);
                progressDialog.setTitle("Log In");
                progressDialog.setMessage("Please wait while we check your credentials");
                progressDialog.setCancelable(false);
                progressDialog.show();
                   String mobilenumber=mobileno.getEditText().getText().toString();
                   String pinnumber=pinno.getEditText().getText().toString();
                startLogin(mobilenumber,pinnumber);
//                Intent i=new Intent(Login.this,MainActivity.class);
//                startActivity(i);
            }
        });
    }

    private void startLogin(final String mobile, final String pin) {
        if(pinno.getEditText().getText().toString().isEmpty()){
            progressDialog.dismiss();
            pinno.setError("Mobile is required");
            pinno.requestFocus();
            return;
        }
        if(pinno.getEditText().getText().toString().length()!=4)
        {
            progressDialog.dismiss();
            pinno.setError("pin must be of 4 digits");
            pinno.requestFocus();
            return;
        }
        if(mobileno.getEditText().getText().toString().length()<10)
        {
            progressDialog.dismiss();
            mobileno.setError("Enter valid mobile number");
            mobileno.requestFocus();
            return;
        }
        Call<Model> call=retrofitInterface.login(mobile,pin);

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, retrofit2.Response<Model> response) {

                progressDialog.dismiss();
                model=response.body();

                Log.d("check",String.valueOf(model.getSuccess()));
                if(model.getMsg()!=null&& model.getMsg().equals("wrong password"))
                {
                    progressDialog.cancel();
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    pinno.setError("Incorrect Pin");
                    pinno.requestFocus();
                    return ;
                }

                if(model.getMsg()!=null&&model.getMsg().equals("user with contact number does not exist"))
                {
                    progressDialog.cancel();
                    mobileno.setError("Invalid Username");
                    mobileno.requestFocus();
                    return;
                }
                boolean check=false;
                check= model.getSuccess();
                Log.d("check",String.valueOf(check));


                if(check){
                    progressDialog.cancel();
                    Toast.makeText(Login.this, "Signin Successful", Toast.LENGTH_SHORT).show();
                    session.createLoginSession(mobile, pin,model.getToken());

                    token=model.getToken();
                    Log.e(TAG,token);
                    Intent i=new Intent(Login.this,MainActivity.class);
                   // i.putExtra("allvalues", model);
                    i.putExtra("token",token);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this, "Some Error occured, Please try Again After Some Time! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                Toast.makeText(Login.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("TAG", "error: " + t);
                progressDialog.cancel();
            }
        });

    }

}

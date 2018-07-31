package in.equipshare.driverway;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.utils.SessionManagement;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePin extends AppCompatActivity {

    SessionManagement session;
    TextInputEditText oldpin,newpin,confirmpin;
    Button changepin;
    Model model;
    String token;
    ProgressDialog progressDialog;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        session=new SessionManagement(this);
        oldpin=(TextInputEditText)findViewById(R.id.oldpin);
        newpin=(TextInputEditText)findViewById(R.id.pin);
        confirmpin=(TextInputEditText)findViewById(R.id.confirmpin);
        progressDialog=new ProgressDialog(this);
        changepin=(Button)findViewById(R.id.changepin);
        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Update Pin");
                progressDialog.setMessage("Please wait while we update your pin");
                progressDialog.setCancelable(false);
                progressDialog.show();
                changepin(oldpin.getEditableText().toString(),newpin.getEditableText().toString(),confirmpin.getEditableText().toString());
                }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId()==R.id.main_logout_btn)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }


        if(item.getItemId()==R.id.main_rate)
        {
            final String appPackageName =getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }



        return true;
    }

    public void changepin(final String oldpin, String newpin, String confirmpin)
    {

        if(oldpin.isEmpty()){
            progressDialog.dismiss();
            this.oldpin.setError("old pin is required");
            this.oldpin.requestFocus();
            return;
        }
        if(newpin.isEmpty()){
            progressDialog.dismiss();
            this.newpin.setError("old pin is required");
            this.newpin.requestFocus();
            return;
        }
        if(confirmpin.isEmpty()){
            progressDialog.dismiss();
            this.confirmpin.setError("old pin is required");
            this.confirmpin.requestFocus();
            return;
        }
        if(!newpin.equals(confirmpin)){
            progressDialog.dismiss();
            this.confirmpin.setError("newpin and confirmpin does not match");
            this.confirmpin.requestFocus();
            return;

        }
        HashMap<String, String> hashMap = session.getUserDetails();

        token=hashMap.get(SessionManagement.KEY_TOKEN);
        Call<Model> call= retrofitInterface.changePin(token,oldpin,newpin);

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                progressDialog.cancel();
                model=response.body();
                if (model.getSuccess()) {
                    new AlertDialog.Builder(ChangePin.this).setTitle("Pin Updated Successfully")
                            .setMessage("Please login to continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ChangePin.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                }
                else{
                    Toast.makeText(ChangePin.this,"Old pin is not correct",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }
        });

    }
}

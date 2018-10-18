package in.equipshare.driverway;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditProfile extends AppCompatActivity {
       Button changepin,editmobile,update;
      // TextInputLayout name,email;
       Model modelview;
       TextInputEditText name,email;
       Spinner bloodgroup;
       SessionManagement session;
       Model model;
    String token,nameset,emailset,bloodgroupset;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent=getIntent();
         nameset=intent.getStringExtra("name");
        emailset=intent.getStringExtra("email");
        bloodgroupset=intent.getStringExtra("bloodgroup");
        session=new SessionManagement(this);
        changepin=(Button)findViewById(R.id.changePin);
        editmobile=(Button)findViewById(R.id.editmobile);
        update=(Button)findViewById(R.id.updateprofile);
        name=(TextInputEditText) findViewById(R.id.name);
        email=(TextInputEditText) findViewById(R.id.emailid);
        bloodgroup=(Spinner)findViewById(R.id.spinner);

       name.setText(nameset);
       email.setText(emailset);
        bloodgroup.setSelection(getIndex(bloodgroup, bloodgroupset));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = session.getUserDetails();

                token=hashMap.get(SessionManagement.KEY_TOKEN);
                Call<Model> call= retrofitInterface.updateProfile(token,name.getEditableText().toString(),email.getEditableText().toString(),bloodgroup.getSelectedItem().toString());

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        model=response.body();
                        if (model.getSuccess()) {
                            new AlertDialog.Builder(EditProfile.this).setTitle("Profile Updated Successfully")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                        }
                        else{
                            Toast.makeText(EditProfile.this,"Something went wrong please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                    }
                });
            }
        });



        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditProfile.this,ChangePin.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        editmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent=new Intent(EditProfile.this,EditMobile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    public int getIndex(Spinner bg, String sbg){

        int index = 0;

        for (int i=0;i<bg.getCount();i++){
            if (bg.getItemAtPosition(i).equals(sbg)){
                index = i;
            }
        }
        return index;
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

}

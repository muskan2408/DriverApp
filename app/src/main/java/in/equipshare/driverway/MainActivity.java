package in.equipshare.driverway;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
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

public class MainActivity extends AppCompatActivity {
         BottomNavigationView bottomNavigationView;
         private ActionBar toolbar;
         SessionManagement session;
         boolean check;
    Model model;
    String token;
         private static final String TAG="MainActivity";
    private static final int ERROR_DAILOG_REQUEST=9001;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent=getIntent();
       // model=intent.getParcelableExtra("allvalues");
        session=new SessionManagement(this);
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        // token=model.getToken();
        toolbar = getSupportActionBar();
       // check=model.getSuccess();

        if(isServiceOk()){

        }
        bottomNavigationView = findViewById(R.id.navigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("Maps");
        loadFragment(new MapsFragment());

        profile_getrequest();


         bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    Fragment fragment;

                switch(item.getItemId()){

                    case R.id.navigation_qr:
                        toolbar.setTitle("Generate QR Code");
                        fragment = new QRFragment();
                        loadFragment(fragment);
                        return true;
                       // Toast.makeText(MainActivity.this, "Hello QR", Toast.LENGTH_SHORT).show();
                    case R.id.navigation_maps:
                       // Toast.makeText(MainActivity.this, "Hello Maps", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Maps");
                        fragment = new MapsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_profile:
                      //  Toast.makeText(MainActivity.this, "Hello profile", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("My Profile");
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        return true;



                }

                return false;
            }
        });



    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        Bundle bundle = new Bundle();
        //bundle.putParcelable("allvalues", (Parcelable) model);
        bundle.putParcelable("profile",model);
// set Fragmentclass Arguments
        ProfileFragment fragobj = new ProfileFragment();
        fragobj.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean isServiceOk()
    {
        Log.d(TAG,"isServiceOk: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available== ConnectionResult.SUCCESS)
        {
            //everything is fine
            Log.d(TAG,"Google play services working properly");
            return true;
        }
        else
        {
            if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){

                Log.d(TAG,"isServiceOk: an error occured but we can fix it");
                Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DAILOG_REQUEST);
                dialog.show();

            }
            else
            {
                Toast.makeText(MainActivity.this, "You cant connect reuest", Toast.LENGTH_SHORT).show();
            }
            return false;

        }

    }

    public Model getModel() {
        return model;
    }

    private void profile_getrequest()
    {
        Call<Model> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                model=response.body();
                Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());

            }


        });
    }

}

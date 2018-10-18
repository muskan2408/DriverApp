package in.equipshare.driverway;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.equipshare.driverway.model.Model;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
Button register;
ProgressDialog progressDialog;
TextInputLayout fullname,email,pin,confirmpin;
Spinner bloodgroup;
String bloodGroup;
Model model;


      private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
              .addConverterFactory(GsonConverterFactory.create());

      public static Retrofit retrofit=builder.build();

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=(Button)findViewById(R.id.register);
        fullname=(TextInputLayout)findViewById(R.id.fullname);
        email=(TextInputLayout) findViewById(R.id.email);
        pin=(TextInputLayout)findViewById(R.id.pin);
        confirmpin=(TextInputLayout)findViewById(R.id.confirmpin);
        bloodgroup=(Spinner)findViewById(R.id.qual_spinner);
         Intent i=getIntent();
         final String contact=i.getStringExtra("mobileno");
         bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgroup.setSelection(i);
                bloodGroup=bloodgroup.getSelectedItem().toString();

             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(Register.this);
                progressDialog.setTitle("Creating Account");
                progressDialog.setMessage("Please wait while we creating your account");
                progressDialog.setCancelable(false);
                progressDialog.show();
//                Intent i=new Intent(Register.this,Login.class);
//                startActivity(i);

                startRegister(fullname.getEditText().getText().toString(),
                        email.getEditText().getText().toString(),
                        pin.getEditText().getText().toString(),
                        confirmpin.getEditText().getText().toString(),
                        contact,bloodGroup);

            }
        });
    }
    private void startRegister(String name, String email,String pin,String confirmpin,String contact,String bloodGroup) {

        if(name.isEmpty()){
            progressDialog.dismiss();
                fullname.setError("Name is required");
                fullname.requestFocus();
                return;
        }
        if(email.isEmpty()){
            progressDialog.dismiss();
            this.email.setError("Name is required");
            this.email.requestFocus();
            return;
        }
        if(pin.isEmpty()){
            progressDialog.dismiss();
            this.pin.setError("Name is required");
            this.pin.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Please enter a valid email or password");
            this.email.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(confirmpin.isEmpty()){
            progressDialog.dismiss();
          this. confirmpin.setError("Name is required");
       this.confirmpin.requestFocus();
            return;
        }
       if(pin.length()!=4)
       {
           progressDialog.dismiss();
           this.pin.setError("pin must be of 4 digits");
           this.pin.requestFocus();
           return;
       }
       if(!confirmpin.equals(pin))
       {

           progressDialog.dismiss();
           this.confirmpin.setError("pin does not match");
           this.confirmpin.requestFocus();
           return;
       }
       RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);

         Call<Model> call=retrofitInterface.register(contact,pin,email,name,bloodGroup);
         call.enqueue(new Callback<Model>() {
             @Override
             public void onResponse(Call<Model> call, Response<Model> response) {
                 progressDialog.dismiss();
                 Model model=response.body();
                 if(model.getMsg().equals("mobile number already exist")){
                     new AlertDialog.Builder(Register.this).setTitle("Enter Another Mobile No.")
                             .setMessage("Mobile number already exist")
                             .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     Intent intent = new Intent(Register.this, MobileAuthActivity.class);
                                     startActivity(intent);
                                     finish();
                                 }
                             }).show();

                 }
                 if(model.getMsg().equals("user created")){
                     new AlertDialog.Builder(Register.this).setTitle("User Created Successfully")
                             .setMessage("Please Login to Continue")
                             .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     Intent intent=new Intent(Register.this,Login.class);
                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                     startActivity(intent);
                                     finish();
                                 }
                             }).show();
             }
             else
                 {
                     Toast.makeText(Register.this, "Some Error occured, Please try Again After Some Time! ", Toast.LENGTH_SHORT).show();
                 }}

             @Override
             public void onFailure(Call<Model> call, Throwable t) {
                 progressDialog.dismiss();
                 Toast.makeText(Register.this, "Error !!!!!!", Toast.LENGTH_SHORT).show();
             }
         });
    }
}

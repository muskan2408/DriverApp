package in.equipshare.driverway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MobileOTP extends AppCompatActivity {
     Button Continue;
   EditText mobileno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);
        Continue=(Button)findViewById(R.id.button);
        mobileno=(EditText)findViewById(R.id.mobile);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MobileOTP.this,Verfication.class);
                i.putExtra("mobile",mobileno.getText().toString());
                startActivity(i);
            }
        });
    }
}

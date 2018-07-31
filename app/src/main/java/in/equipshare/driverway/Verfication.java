package in.equipshare.driverway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Verfication extends AppCompatActivity {
Button verify;
TextView mobiletext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);
        verify=(Button)findViewById(R.id.verify);
        mobiletext=(TextView)findViewById(R.id.mobile);
        Bundle bundle=getIntent().getExtras();
        String mobile=bundle.getString("mobile");
        mobiletext.setText(mobile);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Verfication.this,Register.class);
                startActivity(i);
            }
        });

    }
}

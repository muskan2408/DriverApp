package in.equipshare.driverway;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

public class Forgetpin extends AppCompatActivity {
        TextInputLayout mobile, newpin, confirmpin;
        Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpin);

        mobile=(TextInputLayout)findViewById(R.id.mobileno);
        newpin=(TextInputLayout)findViewById(R.id.newpin);
        confirmpin=(TextInputLayout)findViewById(R.id.confirmpin);
        login=(Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Forgetpin.this,Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });














    }
}

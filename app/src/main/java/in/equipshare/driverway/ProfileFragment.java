package in.equipshare.driverway;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.model.User;
import in.equipshare.driverway.utils.SessionManagement;


public class ProfileFragment extends Fragment {
    Model modelview;
    private TextView name;
    private TextView email;
    private TextView mobile;
    private TextView bloodgroup;
    SessionManagement session;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        session=new SessionManagement(getActivity());
       // Bundle bundle=getArguments();
       // model=bundle.getParcelable("profile");
        modelview=((MainActivity)getActivity()).model;
        final User u=modelview.getUser();

        name=(TextView)view.findViewById(R.id.name);
        email=(TextView)view.findViewById(R.id.emailid);
        mobile=(TextView)view.findViewById(R.id.mobile);
        bloodgroup=(TextView)view.findViewById(R.id.bloodgroup);

       name.setText(u.getName().toUpperCase());
        email.setText(u.getEmail());
        mobile.setText("Mobile Number : "+u.getContact());
        bloodgroup.setText("Blood Group : "+u.getBloodGroup());

        FloatingActionButton actionButton=view.findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),EditProfile.class);
                i.putExtra("name", u.getName().toString());
                i.putExtra("email",u.getEmail().toString());
                i.putExtra("bloodgroup",u.getBloodGroup().toString());
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ( (AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId()==R.id.main_logout_btn)
        {
            new AlertDialog.Builder(getActivity())
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
            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }



        return true;
    }

}

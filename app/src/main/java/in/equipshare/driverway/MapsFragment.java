//package in.equipshare.driverway;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class MapsFragment extends Fragment implements OnMapReadyCallback {
//    private static final String TAG = "MapActivity";
//    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    private boolean mLocationPermissionGranted = false;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private GoogleMap mMap;
//    private FusedLocationProviderClient mfusedLocationProviderClient;
//    private static final float DEFAULT_ZOOM = 15f;
////widgets
//
//    private EditText mSearchText;
//    private ImageView mgps;
//
//    public MapsFragment() {
//        // Required empty public constructor
//    }
//
//    public static MapsFragment newInstance(String param1, String param2) {
//        MapsFragment fragment = new MapsFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view=inflater.inflate(R.layout.fragment_maps, container, false);
//        setHasOptionsMenu(false);
//        mSearchText=(EditText)view.findViewById(R.id.input_search);
//        mgps=(ImageView)view.findViewById(R.id.ic_gps);
//        getLocationPermission();
//        return view;
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        ( (AppCompatActivity) getActivity()).getSupportActionBar().show();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(getActivity(), "map is ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "map is ready");
//        mMap = googleMap;
//
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();
//            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//            init();
//
//
//        }
//
//    }
//    public void init(){
//
//        Log.d(TAG,"initializing");
//
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if(actionId== EditorInfo.IME_ACTION_SEARCH|| actionId==EditorInfo.IME_ACTION_DONE
//                        || event.getAction()==KeyEvent.ACTION_DOWN
//                        ||event.getAction()==KeyEvent.KEYCODE_ENTER){
//                    //execute our method for searching
//                    geoLocate();
//                    hideSoftKeyboard();
//                   return true;
//
//                }
//                return false;
//            }
//        });
//        mgps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG,"get gps location");
//                getDeviceLocation();
//                hideSoftKeyboard();
//            }
//        });
//
//    }
//
//    public void geoLocate(){
//        Log.d(TAG,"geolocating");
//
//        String searchString=mSearchText.getText().toString();
//        Geocoder geocoder=new Geocoder(getActivity());
//        List<Address> list=new ArrayList<>();
//        try{
//            list=geocoder.getFromLocationName(searchString,1);
//        }catch(Exception e)
//        {
//            Log.e(TAG,"geoLocate: IOException"+e.getMessage());
//        }
//
//        if(list.size()>0)
//        {
//            Address address=list.get(0);
//            Log.d(TAG,address.toString());
//            //  Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//
//            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
//        }
//
//    }
//
//    private void getDeviceLocation(){
//        Log.d(TAG,"getting current device location");
//
//        mfusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
//        try{
//            if(mLocationPermissionGranted){
//
//                com.google.android.gms.tasks.Task location=mfusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
//                        if(task.isSuccessful())
//                        {
//                            Log.d(TAG,"found location!");
//                            Location currentLocation=task.getResult();
//                            getLocationPermission();
//                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
//                        }
//                        else{
//                            Log.d(TAG,"current location is null");
//                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }catch(SecurityException e)
//        {
//
//            Log.d(TAG,"GETDEVICELOCATION : securityException"+ e.getMessage());
//        }
//
//    }
//
//    private void moveCamera(LatLng latLng,float zoom,String title){
//        Log.d(TAG,"move the camera to: lat:"+latLng.latitude+" long:"+latLng.longitude);
////        getLocationPermission();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
//        if(!title.equals("My Location")) {
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);
//            mMap.addMarker(options);
//        }
//    }
//    private void initMap(){
//
//        Log.d(TAG,"initialize a map");
//        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
//        //this is one of the way if we didnt want to implement onMapCallBack interface
////        mapFragment.getMapAsync(new OnMapReadyCallback() {
////            @Override
////            public void onMapReady(GoogleMap googleMap) {
////                mMap=googleMap;
////            }
////        });
//mapFragment.getMapAsync(new OnMapReadyCallback() {
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//      //  Toast.makeText(getActivity(), "map is ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "map is ready");
//        mMap = googleMap;
//
//        if (mLocationPermissionGranted) {
//            getDeviceLocation();
//            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//getLocationPermission();
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//            init();
//
//
//        }
//
//    }
//});
//    }
//
//
//    private void getLocationPermission(){
//        Log.d(TAG,"get Location Permission");
//        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        if(ContextCompat.checkSelfPermission(getActivity(),FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
//            if(ContextCompat.checkSelfPermission(getActivity(),COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED)
//            {
//                mLocationPermissionGranted=true;
//                initMap();
//            }
//            else{
//
//                ActivityCompat.requestPermissions(getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
//
//            }
//
//        }
//        else{
//
//            ActivityCompat.requestPermissions(getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
//
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        Log.d(TAG,"called");
//        mLocationPermissionGranted=false;
//        switch (requestCode){
//
//            case LOCATION_PERMISSION_REQUEST_CODE:
//            {
//                if(grantResults.length>0)
//                {
//                    for(int i=0;i<grantResults.length;i++){
//
//                        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                            mLocationPermissionGranted=false;
//                            Log.d(TAG,"permission failed");
//                            return;
//                        }
//                    }
//                    Log.d(TAG,"permission granted");
//                    mLocationPermissionGranted=true;
//                    //initialize our map
//                    initMap();
//                }
//            }
//        }
//    }
//
//    public void hideSoftKeyboard(){
//        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//
//       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//}





package in.equipshare.driverway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.widget.Button;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.equipshare.driverway.model.PlaceInfo;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


public class MapsFragment extends Fragment implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener,RoutingListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    private AutoCompleteTextView mSearchText;
    private ImageView mgps, mInfo, mPlacePicker;
    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int LOCATION_REQUEST=500;
    Button navigation;
    ProgressDialog progressDialog;
    Location currentLocation;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorAccent,R.color.black};



    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    private GoogleApiClient mGoogleApiClient;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlaceInfo mPlace;
    private Marker mMarker;
    int type=1;


    public MapsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(int type) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
      args.putInt("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getArguments().getInt("type");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        setHasOptionsMenu(false);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_search);
        mgps = (ImageView) view.findViewById(R.id.ic_gps);
       mInfo = (ImageView) view.findViewById(R.id.place_info);
        mPlacePicker = (ImageView) view.findViewById(R.id.place_picker);
        navigation=(Button)view.findViewById(R.id.navigation);
        getLocationPermission();
        polylines = new ArrayList<>();

        mgps.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(getActivity(), "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               // ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST );
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();


        }

    }



    public void init() {

        Log.d(TAG,"initializing");
        mSearchText.setOnItemClickListener(mAutoCompleteListener);
        mplaceAutocompleteAdapter =new PlaceAutocompleteAdapter(getActivity(),
                Places.getGeoDataClient(getActivity(),null),LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mplaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId== EditorInfo.IME_ACTION_SEARCH|| actionId==EditorInfo.IME_ACTION_DONE
                        || event.getAction()==KeyEvent.ACTION_DOWN
                        ||event.getAction()==KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                    hideSoftKeyboard();
                    mgps.setVisibility(View.VISIBLE);
                    return true;

                }
                return false;
            }
        });
        mgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "get gps location");
                getDeviceLocation();
               hideSoftKeyboard();
            }
        });
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cliccked place info");
                try {

                    if (mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    } else {
                        Log.d(TAG, "onClick: placeinfo: " + mPlace.toString());
                        mMarker.showInfoWindow();
                    }

                } catch (NullPointerException e) {
                    Log.e(TAG, "onClick:NullPointerException" + e.getMessage());
                }
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Onclick: GooglePlayServicesRepairableException" + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Onclick: GooglePlayServicesNotAvailableException" + e.getMessage());

                }
            }
        });
        //
        // hideSoftKeyboard();
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=22.7195680,75.857727");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    public void geoLocate() {
        Log.d(TAG,"geolocating");

        String searchString=mSearchText.getText().toString();
        Geocoder geocoder=new Geocoder(getActivity());
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch(Exception e)
        {
            Log.e(TAG,"geoLocate: IOException"+e.getMessage());
        }

        if(list.size()>0)
        {
            Address address=list.get(0);
            Log.d(TAG,address.toString());
            //  Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }

    }

    private void getDeviceLocation() {
        Log.d(TAG, "getting current device location");

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (mLocationPermissionGranted) {

                com.google.android.gms.tasks.Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "found location!");
                            currentLocation = task.getResult();
                            Log.d("current location",String.valueOf(currentLocation.getLatitude()+currentLocation.getLongitude()));
                            LatLng pickUpLatLng=new LatLng(22.7195680,75.857727);
                            MarkerOptions options1 = new MarkerOptions()
                     .position(pickUpLatLng)
                .title("Construction Site");
                     mMarker = mMap.addMarker(options1);
                            //getLocationPermission();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");


                            LatLng start=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                       getRouteMarker(pickUpLatLng,start);
                        } else {
                            Log.d(TAG, "current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        MarkerOptions options1 = new MarkerOptions()
//                .position(latLng)
//                .title(placeInfo.getName())
//                .snippet(snippet);
//        mMarker = mMap.addMarker(options1);
            }
        } catch (SecurityException e) {

            Log.d(TAG, "GETDEVICELOCATION : securityException" + e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "move the camera to: lat:" + latLng.latitude + " long:" + latLng.longitude);
//        getLocationPermission();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        else if(title.equals("My Location")){
            {
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(title);
                mMap.addMarker(options);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG,"move the camera to: lat:"+latLng.latitude+" long:"+latLng.longitude);
//        getLocationPermission();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        mMap.clear();

        if(placeInfo!=null)
        {
            try{
                String snippet="Address"+placeInfo.getAddress()+"\n" +
                        "Phone Number"+placeInfo.getPhoneNumber()+"\n" +
                        "Website"+placeInfo.getWebsiteUri()+"\n" ;
                  String title="My Location";

                        MarkerOptions options = new MarkerOptions()
                                .position(latLng)
                                .title(title);
                        mMap.addMarker(options);
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                erasePolylines();
                getRouteMarker(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),placeInfo.getLatLng());


            }catch(NullPointerException e)
            {
                Log.e(TAG,"moveCamera: NullPointerException"+e.getMessage());
            }

        }
        else{

            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();

    }

    private void getRouteMarker(LatLng pickUpLatLng, LatLng start) {

//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "Fetching route information.", true);
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(start, pickUpLatLng)
                .build();
        routing.execute();
    }

    private void initMap() {

        Log.d(TAG, "initialize a map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //this is one of the way if we didnt want to implement onMapCallBack interface
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                mMap=googleMap;
//            }
//        });
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Toast.makeText(getActivity(), "map is ready", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "map is ready");
                mMap = googleMap;

                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //getLocationPermission();
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    init();


                }

            }
        });
    }


    private void getLocationPermission() {
        Log.d(TAG, "get Location Permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {

                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }

        } else {

            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "called");
        mLocationPermissionGranted = false;
        switch (requestCode) {

            case LOCATION_REQUEST:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;

            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {

                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "permission granted");
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
     * ---------------------------------------------Google places api------------------------------------
     * */

    private AdapterView.OnItemClickListener mAutoCompleteListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = mplaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<? super PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {

                Log.d(TAG, "places query did not complete successfrully " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            try {

                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                //   mPlace.setAttributions(place.getName().toString());
                mPlace.setId(place.getId().toString());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                mPlace.setRating(place.getRating());

                Log.d(TAG, "OnResult: place details" + mPlace.toString());

            } catch (NullPointerException e) {
                Log.e(TAG, "OnResult: NullPointerException" + e.getMessage());

            }
            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude),
                    DEFAULT_ZOOM, mPlace);

//            Log.d(TAG,"OnResult:place details: "+place.getAttributions());
//            Log.d(TAG,"OnResult:place details: "+place.getViewport());
//            Log.d(TAG,"OnResult:place details: "+place.getAddress());
//            Log.d(TAG,"OnResult:place details: "+place.getPhoneNumber());
//            Log.d(TAG,"OnResult:place details: "+place.getLatLng());
//            Log.d(TAG,"OnResult:place details: "+place.getId());
//            Log.d(TAG,"OnResult:place details: "+place.getWebsiteUri());
//            Log.d(TAG,"OnResult:place details: "+place.getRating());
//
            places.release();

        }
    };

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
         //   Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestIndexRoute) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

          //  Toast.makeText(getActivity(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolylines(){

        for(Polyline line:polylines)
        {
            line.remove();
        }
        polylines.clear();
    }







}



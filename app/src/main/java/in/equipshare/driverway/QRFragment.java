package in.equipshare.driverway;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

import in.equipshare.driverway.model.Model;
import in.equipshare.driverway.utils.SessionManagement;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class QRFragment extends Fragment {
    Button qr;
    ImageView i;
    ProgressDialog progressDialog;
    String token;
    public final static int QRcodeWidth = 500 ;
    SessionManagement session;
    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    Bitmap bitmap ;
    Model model;
    MultiFormatWriter multiFormatWriter;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);



    public QRFragment() {
        // Required empty public constructor
    }

    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_qr, container, false);
        setHasOptionsMenu(true);
        session=new SessionManagement(getActivity());
          progressDialog =new ProgressDialog(getActivity());
        qr=(Button)view.findViewById(R.id.qr_button);
        i=(ImageView)view.findViewById(R.id.iv);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Generating QR Code");
                progressDialog.setMessage("Please wait while we generating your qr code");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.d("fsddddddddddsfffff","heloooooooo");
                        HashMap<String, String> hashMap = session.getUserDetails();
            multiFormatWriter =new MultiFormatWriter();

                        token=hashMap.get(SessionManagement.KEY_TOKEN);
                        Call<Model> call= retrofitInterface.qrcode(token);
                         call.enqueue(new Callback<Model>() {
                             @Override
                             public void onResponse(Call<Model> call, Response<Model> response) {
                                 model=response.body();
                                 if(model.getSuccess()){
                                     String qr =model.getMsg();
                                     try {
                                         BitMatrix bitMatrix=multiFormatWriter.encode(model.getMsg(),
                                                 BarcodeFormat.QR_CODE,200,200);
                                         BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                                         bitmap=barcodeEncoder.createBitmap(bitMatrix);
                                         i.setImageBitmap(bitmap);
                                       //  bitmap = TextToImageEncode(model.getMsg());
                                         progressDialog.dismiss();
                                     } catch (WriterException e) {
                                         e.printStackTrace();
                                     }

                                    // i.setImageBitmap(bitmap);

                                 }
                                 else{
                                     if((model.getMsg()).equals("No Order Found"))
                                     {
                                         progressDialog.cancel();
                                         new AlertDialog.Builder(getActivity()).setTitle("You don't have any order")
                                                 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialogInterface, int i) {
                                                         progressDialog.dismiss();
                                                     }
                                                 }).show();
                                     }
                                  if(model.getMsg().equals("No Supplier Found")) {
                                      progressDialog.cancel();
                                      new AlertDialog.Builder(getActivity()).setTitle("Not Registered with Supplier")
                                              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                      progressDialog.dismiss();
                                                  }
                                              }).show();
                                  }
                                 }
                             }

                             @Override
                             public void onFailure(Call<Model> call, Throwable t) {

                             }
                         });



//                        bitmap = TextToImageEncode("equipshare");
//                        i.setImageBitmap(bitmap);

//                        String path = saveImage(bitmap);  //give read write permission
//                        Toast.makeText(MainActivity.this, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {



        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }
    @Override
    public void onResume() {
        super.onResume();
        ( (AppCompatActivity) getActivity()).getSupportActionBar().show();
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

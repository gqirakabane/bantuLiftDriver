package com.bantu.lift.driver.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bantu.lift.driver.MainActivity;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.adapter.CarTypeAdapter;
import com.bantu.lift.driver.adapter.SpinnerDropdownAdapter;
import com.bantu.lift.driver.constant.FunctionHelper;
import com.bantu.lift.driver.implementer.CreateLiftPresenterImplementer;
import com.bantu.lift.driver.interFace.FragmentInterface;
import com.bantu.lift.driver.modelclass.GetCarTypeModel.CarTypeModelclass;
import com.bantu.lift.driver.modelclass.GetCarTypeModel.Datum;
import com.bantu.lift.driver.retrofit.ApiUtils;
import com.bantu.lift.driver.retrofit.IRestInterfaces;
import com.bantu.lift.driver.service.GPSTracker;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.bantu.lift.driver.view.ICreateLiftView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLiftFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, ICreateLiftView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private double mParam1;
    private double mParam2;
    private double mParam3;
    private double mParam4;
    private String mParam5;
    private String mParam6;
    private String mParam7;
    private String mParam8;
    private GoogleMap mMap;
    String title;
    LinearLayout finishBtn;
    public int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    double lat;
    double lang1;
    CheckBox sighupCheckbox;
    RelativeLayout carType1;
    LinearLayout uploadPhoto;
    EditText et_startDate, et_carType;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    Spinner spinner_carType, spinner_luggage;
    CircleImageView img;
    SharedPreferences sharedPreferences;
    SpinnerDropdownAdapter spinnerDropdownAdapter, spinnerDropdownAdapter1;

    CarTypeAdapter carTypeAdapter;
    private FragmentInterface fragmentInterface;
    List<Datum> cartypeList = new ArrayList<>();
    String carId;
    List<String> luggagetypeList = new ArrayList<>();
    CreateLiftPresenterImplementer createLiftPresenterImplementer;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    SupportMapFragment mapFragment;
    String imagepath = "checkdata";
    String carType = "";
    String luggage = "";

    public CreateLiftFragment() {
    }

    public static CreateLiftFragment newInstance(double mParam1, double mParam2, double mParam3, double mParam4, String date, String pickupaddrss, String dropaddress, String distance) {
        CreateLiftFragment fragment = new CreateLiftFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, mParam1);
        args.putDouble(ARG_PARAM2, mParam2);
        args.putDouble(ARG_PARAM3, mParam3);
        args.putDouble(ARG_PARAM4, mParam4);
        args.putString(ARG_PARAM5, date);
        args.putString(ARG_PARAM6, pickupaddrss);
        args.putString(ARG_PARAM7, dropaddress);
        args.putString(ARG_PARAM8, distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            mParam3 = getArguments().getDouble(ARG_PARAM3);
            mParam4 = getArguments().getDouble(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
            mParam7 = getArguments().getString(ARG_PARAM7);
            mParam8 = getArguments().getString(ARG_PARAM8);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_lift_amount, container, false);
        createLiftPresenterImplementer = new CreateLiftPresenterImplementer(this, view, getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = view.findViewById(R.id.img);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        uploadPhoto = view.findViewById(R.id.uploadPhoto);
        uploadPhoto.setOnClickListener(this);

        GPSTracker gps = new GPSTracker(getActivity());
        et_startDate = view.findViewById(R.id.et_startDate);
        spinner_carType = view.findViewById(R.id.spinner_carType);
        spinner_luggage = view.findViewById(R.id.spinner_luggage);
        finishBtn = view.findViewById(R.id.finishBtn);
        carType1 = view.findViewById(R.id.carType1);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lat = latitude;
            lang1 = longitude;
            mapFragment.getMapAsync(this);
            Log.e("data==", String.valueOf(latitude));
            Log.e("data==1", String.valueOf(longitude));

        } else {

            Log.e("UpdateHistoryService", "gps not");
        }
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        luggagetypeList.clear();
        luggagetypeList.add("Luggage");
        luggagetypeList.add("Small");
        luggagetypeList.add("large");


        spinnerDropdownAdapter1 = new SpinnerDropdownAdapter(getActivity(), luggagetypeList);
        spinner_luggage.setAdapter(spinnerDropdownAdapter1);
        spinner_luggage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                luggage = luggagetypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        finishBtn.setOnClickListener(this);
        MainActivity.tootlbarheader.setVisibility(View.GONE);
        MainActivity.text_toolbarTitle.setVisibility(View.VISIBLE);
        MainActivity.text_toolbarTitle.setText("Create Lift");
        getCarTypeList();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.uploadPhoto) {
            //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start((Activity) getContext());
            showPictureDialog();
        }
        if (id == R.id.finishBtn) {

            createLiftPresenterImplementer.sendRequest(imagepath, mParam1, mParam2, mParam3, mParam4, mParam5, carType, luggage, mParam6, mParam7,mParam8,carId);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(lat, lang1);
        MarkerOptions marker = new MarkerOptions().position(sydney);
        marker.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drop_loc)));
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addCircle(new CircleOptions()
                .center(sydney)
                .radius(5.0f)
                .strokeWidth(2f).strokeColor(0x55B5CFEB)
                .fillColor(0x55B5CFEB));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface = (FragmentInterface) context;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        if (PackageManager.PERMISSION_GRANTED
                == ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) && PackageManager.PERMISSION_GRANTED
                == ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY);
        } else {
            requestWritePermission(getActivity());
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY);
        }
    }

    private void takePhotoFromCamera() {
        if (PackageManager.PERMISSION_GRANTED
                == ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) && PackageManager.PERMISSION_GRANTED
                == ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        } else {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
            requestWritePermission(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == getActivity().RESULT_OK) {


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        //  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);

                    imagepath = getPathFromURI(contentURI);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(thumbnail);

            imagepath = getPathFromURI(getImageUri(getActivity(), thumbnail));

            //  imageprofile.setImageBitmap(thumbnail);
            // saveImage(thumbnail);

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void OnLoginSuccess() {
        //fragmentInterface.fragmentResult(DashboardFragment.newInstance("",""), "Police Department");
        //addFragment(DashboardFragment.newInstance("", ""), "Dashboard");
        sharedPreferences.edit().putString(SharedPreferenceConstants.checkPoll, "1").apply();
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("Group Details", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void OnLoginError() {

    }

    @Override
    public void OnInitView(View view) {

    }

    private static void requestWritePermission(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
            new android.app.AlertDialog.Builder(context)
                    .setMessage("This app needs permission to use The phone Camera in order to activate the Scanner")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 1);
                        }
                    }).show();

        } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new android.app.AlertDialog.Builder(context)
                    .setMessage("This app needs permission to use storage to save the clicked Image")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    }).show();

        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void addFragment(Fragment fragment, final String title1) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, title1);
        fragmentTransaction.commit();
        // drawer.closeDrawer(GravityCompat.START);
        //imgTitle.setImageResource(R.drawable.toolbarimage);
    }


    public void getCarTypeList() {
        FunctionHelper.showDialog(getActivity(), "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<CarTypeModelclass> signInModelclassCall = iRestInterfaces.getAllCarTypes(sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        signInModelclassCall.enqueue(new Callback<CarTypeModelclass>() {
            @Override
            public void onResponse(Call<CarTypeModelclass> call, Response<CarTypeModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        cartypeList.addAll(response.body().getData());
                        // Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        carTypeAdapter = new CarTypeAdapter(getActivity(), response.body().getData());
                        spinner_carType.setAdapter(carTypeAdapter);
                        getData();
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<CarTypeModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    public void getData() {
        spinner_carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                carType = cartypeList.get(position).getCarTypeName();
                carId = cartypeList.get(position).getCarTypeId();
                if (carType.equalsIgnoreCase("Other")) {
                    carType1.setVisibility(View.VISIBLE);
                } else {
                    carType1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_item_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}


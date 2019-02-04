package com.example.saurabh.driver.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saurabh.driver.R;
import com.example.saurabh.driver.implementer.LicenseUploadPresenterImplementer;
import com.example.saurabh.driver.implementer.SignUpPresenterImplementer;
import com.example.saurabh.driver.view.ILicenseUploadView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadLicenceActivity extends AppCompatActivity implements View.OnClickListener, ILicenseUploadView {
    CircleImageView img;
    public int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    LinearLayout uploadPhoto;
    Button finishBtn;
    EditText et_uploadId, et_uploadlicense;
    TextInputLayout txtInputuploadId, txtInputuploadlicense;
    Button browseId, browseLicense;
    private int flag = 0;
    public static final String BASE_DIR_PATH = Environment.getExternalStorageDirectory() + "/.Carpal";
    private static final String IMAGE_DIRECTORY = "/demonuts";
    ImageView back;
    String photoimagepath = "photo", photoimageurl;
    String uploadidpath = "photoid", uploadidurl;
    String uploadlicensepath = "photolicense", uploadlicenseurl;
    public static final String IMAGE_DIR_PATH = BASE_DIR_PATH + "/Image";
    LicenseUploadPresenterImplementer licenseUploadPresenterImplementer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_activity);
        View rootView = getWindow().getDecorView().getRootView();
        licenseUploadPresenterImplementer = new LicenseUploadPresenterImplementer(this, rootView, UploadLicenceActivity.this);
        img = findViewById(R.id.img);
        finishBtn = findViewById(R.id.finishBtn);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        txtInputuploadId = findViewById(R.id.txtInputuploadId);
        txtInputuploadlicense = findViewById(R.id.txtInputuploadlicense);
        et_uploadlicense = findViewById(R.id.et_uploadlicense);
        et_uploadId = findViewById(R.id.et_uploadId);
        browseId = findViewById(R.id.browseId);
        back = findViewById(R.id.back);
        browseLicense = findViewById(R.id.browseLicense);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        txtInputuploadId.setTypeface(typeface);
        txtInputuploadlicense.setTypeface(typeface);
        finishBtn.setOnClickListener(this);
        browseId.setOnClickListener(this);
        browseLicense.setOnClickListener(this);
        uploadPhoto.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.browseId) {
            if (checkPermissionForCamera()) {
                flag = 1;
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(UploadLicenceActivity.this);
            }
        }
        if (id == R.id.browseLicense) {
            if (checkPermissionForCamera()) {
                flag = 2;
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(UploadLicenceActivity.this);
            }
        }
        if (id == R.id.uploadPhoto) {
            if (checkPermissionForCamera()) {
                flag = 0;
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(UploadLicenceActivity.this);
            }
        }
        if (id == R.id.finishBtn) {
            licenseUploadPresenterImplementer.sendSignUpRequest(getIntent().getStringExtra("fullName"),
                    getIntent().getStringExtra("mobile"),
                    getIntent().getStringExtra("email"),
                    getIntent().getStringExtra("gender"),
                    getIntent().getStringExtra("work"),
                    getIntent().getStringExtra("homecity"),
                    getIntent().getStringExtra("password"),photoimagepath,uploadidpath,uploadlicensepath);
        }
        if (id==R.id.back)
        {
            licenseUploadPresenterImplementer.sendbackRequest();
        }

    }

    public boolean checkPermissionForCamera() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(UploadLicenceActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UploadLicenceActivity.this, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UploadLicenceActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Access Camera permission is necessary to upload profile pic.");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadLicenceActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(UploadLicenceActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if (flag == 0) {
                    ((ImageView) findViewById(R.id.img)).setImageURI(result.getUri());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                        Uri uri = getImageUri1(UploadLicenceActivity.this, bitmap);
                        Log.e("Uri===", "" + uri);

                        photoimageurl = createPath(uri, UploadLicenceActivity.this);


                        photoimagepath = String.valueOf(photoimageurl);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                        imageurl= getRealPathFromURI(result.getUri());

                    //galleryUpload();


                } else if (flag == 1) {
                    String uploadString = String.valueOf(result.getUri());
                    int lastIndex = uploadString.lastIndexOf("/");
                    String s2 = uploadString.substring(lastIndex + 1);
                    et_uploadId.setText(s2);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                        Uri uri = getImageUri1(UploadLicenceActivity.this, bitmap);
                        Log.e("Uri===", "" + uri);
                        uploadidurl = createPath(uri, UploadLicenceActivity.this);
                        uploadidpath = String.valueOf(uploadidurl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    String uploadString = String.valueOf(result.getUri());
                    int lastIndex = uploadString.lastIndexOf("/");
                    String s2 = uploadString.substring(lastIndex + 1);

                    et_uploadlicense.setText(s2);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                        Uri uri = getImageUri1(UploadLicenceActivity.this, bitmap);
                        Log.e("Uri===", "" + uri);
                        uploadlicenseurl = createPath(uri, UploadLicenceActivity.this);
                        uploadlicensepath = String.valueOf(uploadlicenseurl);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String createPath(Uri selectedImageUri, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            return cursor.getString(columnIndex);
        }
        return null;
    }

    @Override
    public void OnLoginSuccess() {

    }

    @Override
    public void OnLoginError() {

    }

    @Override
    public void OnInitView(View view) {

    }

    public static Uri getImageUri1(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path == null) {
            path = getUriPath(inContext, inImage);
        }
        if (path != null) {
            return Uri.parse(path);
        }

        return null;
    }

    public static String getUriPath(Context context, Bitmap bitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(
                    IMAGE_DIR_PATH + File.separator + "IMG_" + timeStamp + ".jpg"
            );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();
            return mediaFile.getPath();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }

}

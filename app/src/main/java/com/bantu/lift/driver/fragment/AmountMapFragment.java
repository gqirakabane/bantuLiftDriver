package com.bantu.lift.driver.fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantu.lift.driver.MainActivity;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.interFace.FragmentInterface;
import com.bantu.lift.driver.service.GPSTracker;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AmountMapFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private double mParam1;
    private double mParam2;
    private double mParam3;
    private double mParam4;
    private String mParam5;
    private String mParam6;
    private GoogleMap mMap;
    String distance="0 km";
    ArrayList markerPoints = new ArrayList();
    double lat;
    double lang1;
    EditText et_startDate;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    LinearLayout cotinuelift;
    ImageView cal;
    GPSTracker gps;
    TextView realTime;
    private FragmentInterface fragmentInterface;
    CustomDateTimePicker custom;
    String start_date;
    SupportMapFragment mapFragment;
    SharedPreferences sharedPreferences;

    public AmountMapFragment() {
    }

    public static AmountMapFragment newInstance(double param1, double param2, double l2, double t2, String pickupaddrss, String dropaddress) {
        AmountMapFragment fragment = new AmountMapFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putDouble(ARG_PARAM3, l2);
        args.putDouble(ARG_PARAM4, t2);
        args.putString(ARG_PARAM5, pickupaddrss);
        args.putString(ARG_PARAM6, dropaddress);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapwithamount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        GPSTracker gps = new GPSTracker(getActivity());
        cal = view.findViewById(R.id.cal);
        et_startDate = view.findViewById(R.id.et_startDate);
        realTime = view.findViewById(R.id.realTime);
        cotinuelift = view.findViewById(R.id.cotinuelift);
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
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom.showDialog();
            }
        });
        cotinuelift.setOnClickListener(this);

        MainActivity.tootlbarheader.setVisibility(View.GONE);
        MainActivity.text_toolbarTitle.setVisibility(View.VISIBLE);
        MainActivity.text_toolbarTitle.setText("Select Time");
        custom = new CustomDateTimePicker(getActivity(),
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        //                        ((TextInputEditText) findViewById(R.id.edtEventDateTime))
                        //edtEventDateTime.setText("");
                        int a = monthNumber;
                        String str;
                        if (a < 9) {
                            a++;
                            str = "0" + a;
                        } else {
                            a++;
                            str = String.valueOf(a);

                        }
                        int hour2 = hour24;
                        String hour;
                        if (hour2 < 9) {

                            hour = "0" + hour2;
                        } else {

                            hour = String.valueOf(hour2);

                        }

                        int min1 = min;

                        if (min1 < 9) {
                            min1 = 10;
                        }

                        int sec1 = sec;

                        if (sec1 < 9) {
                            sec1 = 10;
                        }
                        start_date = year + "-" + str + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + Integer.parseInt(hour) + ":" + min1
                                + ":" + sec1;
                        et_startDate.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + "-" + str + "-" + year
                                + " " + Integer.parseInt(hour) + ":" + min1
                                + ":" + sec1);

                    }

                    @Override
                    public void onCancel() {

                    }
                });
        realTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                start_date = sdf.format(c.getTime());
                et_startDate.setText(start_date);

            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(SharedPreferenceConstants.checkPoll, "").equalsIgnoreCase("1")) {
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cotinuelift) {
            //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            if (validationLoginCheck() == true) {

                if (distance.equalsIgnoreCase("0 km"))
                {
                    Toast.makeText(getActivity(), "You are searching for out of state location.", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }else
                {
                    fragmentInterface.fragmentResult(CreateLiftFragment.newInstance(mParam1, mParam2, mParam3, mParam4, start_date, mParam5, mParam6,distance), "Group Details");

                }

            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        Long l1= Long.valueOf(mParam1);
//        Long l2= Long.valueOf(mParam3);
//        Long t1= Long.valueOf(mParam2);
//        Long t2= Long.valueOf(mParam4);
        LatLng sydney = new LatLng(mParam1, mParam2);

        LatLng sydney1 = new LatLng(mParam3, mParam4);
        markerPoints.add(sydney);
        builder = new LatLngBounds.Builder();

        drawMarker(new LatLng(mParam1, mParam2), "");
        drawMarker1(new LatLng(mParam3, mParam4), "");

        bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(cu);

       /*MarkerOptions marker = new MarkerOptions().position(sydney);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_loc));
        googleMap.addMarker(marker);

        MarkerOptions marker1 = new MarkerOptions().position(sydney1);
        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green));
        googleMap.addMarker(marker1);*/
       /* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                sydney).zoom(10).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
       /* builder.include(marker1.getPosition());

        builder = new LatLngBounds.Builder();

        bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.animateCamera(cu);*/
        String url = getDirectionsUrl(sydney, sydney1);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface = (FragmentInterface) context;
    }

    public boolean validationLoginCheck() {
        boolean check = false;
        String drop_city = et_startDate.getText().toString();
        if (check == false) {
            if (drop_city.equals("")) {
                Toast.makeText(getActivity(), "Please enter start date ", Toast.LENGTH_SHORT).show();
            } else {

                check = true;
            }
        }
        return check;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyB30y4oyqSfThUXJII35FbMFLj2lgBy5Q8";
        Log.d("data=", url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result==", result);
            try {
                JSONObject jsonObject=new JSONObject(result);

                JSONArray jsonArray=jsonObject.getJSONArray("routes");
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    JSONArray jsonArray1=jsonObject1.getJSONArray("legs");
                    for (int j = 0; j <jsonArray1.length() ; j++) {
                        JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                        JSONObject jsonObject3=jsonObject2.getJSONObject("distance");
                        distance=jsonObject3.getString("text");
                       // Toast.makeText(getActivity(), jsonObject3.getString("text"), Toast.LENGTH_SHORT).show();

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.color(Color.BLACK);

            }

            if (lineOptions != null && mMap != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines draw");
            }           //mMap.addPolyline(lineOptions);
        }
    }


    private void drawMarker(LatLng point, String text) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(text).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drop_loc)));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

    }

    private void drawMarker1(LatLng point, String text) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(text).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_green)));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

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


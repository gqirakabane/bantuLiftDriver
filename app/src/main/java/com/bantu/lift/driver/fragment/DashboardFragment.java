package com.bantu.lift.driver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bantu.lift.driver.MainActivity;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.interFace.FragmentInterface;
import com.bantu.lift.driver.service.GPSTracker;
import com.bantu.lift.driver.service.GPSTracker1;
import com.bantu.lift.driver.utils.SharedPreferenceConstants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Locale;

public class DashboardFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    String title;
    double lat;
    double lang1;
    String pickupAddress, dropAddress;
    double l1 = 0, l2 = 0, t1 = 0, t2 = 0;
    LinearLayout pickUp, dropcity;
    LinearLayout continue_map;
    TextInputLayout txtInputpickUp, txtInputdropcity;
    GPSTracker gps;
    EditText et_pickup, et_dropcity;
    String currentMap;
    private int flagpickup = 0, flagdrop = 0;

    private FragmentInterface fragmentInterface;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE1 = 12;
    SupportMapFragment mapFragment;
    int enableFlag = 0;

    ArrayList markerPoints = new ArrayList();
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    SharedPreferences sharedPreferences;
    LocationManager locationManager;
    Location location;
    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        txtInputpickUp = view.findViewById(R.id.txtInputpickUp);
        txtInputdropcity = view.findViewById(R.id.txtInputdropcity);
        continue_map = view.findViewById(R.id.continue_map);
        et_dropcity = view.findViewById(R.id.et_dropcity);
        et_pickup = view.findViewById(R.id.et_pickup);
        dropcity = view.findViewById(R.id.dropcity);
        pickUp = view.findViewById(R.id.pickUp);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        txtInputpickUp.setTypeface(typeface);
        txtInputdropcity.setTypeface(typeface);
        continue_map.setOnClickListener(this);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        et_pickup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Do what you want
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    CommonMeathod.hideKeyboard(getActivity());
                    try {
                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(Place.TYPE_BANK)
                                .setTypeFilter(Place.TYPE_STADIUM)
                                .setTypeFilter(Place.TYPE_SHOPPING_MALL)
                                .setTypeFilter(Place.TYPE_BUS_STATION)
                                .build();
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(autocompleteFilter)
                                        .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                    }
                    return true;
                }
                return false;
            }
        });
        et_dropcity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Do what you want
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    CommonMeathod.hideKeyboard(getActivity());
                    try {
                        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(Place.TYPE_BANK)
                                .setTypeFilter(Place.TYPE_STADIUM)
                                .setTypeFilter(Place.TYPE_SHOPPING_MALL)
                                .setTypeFilter(Place.TYPE_BUS_STATION)
                                .build();
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(autocompleteFilter)
                                        .build(getActivity());
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE1);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {

                    }
                    return true;
                }
                return false;
            }
        });


        GPSTracker gps = new GPSTracker(getActivity());
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
       /* FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i <=fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
*/
        GPSTracker1 gpsTracker = new GPSTracker1(getActivity());
        gpsTracker.canGetLocation();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.continue_map) {
            if (validationLoginCheck() == true) {
                fragmentInterface.fragmentResult(AmountMapFragment.newInstance(l1, t1, l2, t2, pickupAddress, dropAddress), "dashboard");

            }
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
                sydney).zoom(25).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addCircle(new CircleOptions()
                .center(sydney)
                .radius(5.0f)
                .strokeWidth(2f).strokeColor(0x55B5CFEB)
                .fillColor(0x55B5CFEB));

        getDescAddress(lat,lang1);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface = (FragmentInterface) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (et_pickup.getText().toString().length() > 0 && et_dropcity.getText().toString().length() > 0) {
            et_dropcity.setCursorVisible(true);
            et_dropcity.requestFocus();
            et_dropcity.setSelection(et_dropcity.getText().length());
        } else if (et_pickup.getText().toString().length() > 0) {
            et_dropcity.setCursorVisible(true);
            et_dropcity.requestFocus();
        } else if (et_dropcity.getText().toString().length() > 0) {
            et_pickup.setCursorVisible(true);
            et_pickup.requestFocus();
        }
        MainActivity.tootlbarheader.setVisibility(View.VISIBLE);
        MainActivity.text_toolbarTitle.setVisibility(View.GONE);
        if (sharedPreferences.getString(SharedPreferenceConstants.checkPoll, "").equalsIgnoreCase("1")) {
            getActivity().onBackPressed();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                l1 = place.getLatLng().latitude;
                t1 = place.getLatLng().longitude;
               /* Log.e("name=", String.valueOf(l1));
                Log.e("name=", String.valueOf(t1));*/
                if (place.getAddress().length() > 35)
                    et_pickup.setText(place.getAddress().toString().substring(0, 33) + "...");
                else et_pickup.setText(place.getAddress().toString());
                pickupAddress = place.getName().toString();
                flagpickup = 1;
                if (flagpickup == 1 && flagdrop == 1) {
                    drawTwoMarker(l1, t1, l2, t2);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i("", status.getStatusMessage());
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE1) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                if (place.getAddress().length() > 35)
                    et_dropcity.setText(place.getAddress().toString().substring(0, 33) + "...");
                else et_dropcity.setText(place.getAddress());
                l2 = place.getLatLng().latitude;
                t2 = place.getLatLng().longitude;
            /*    Log.e("name=", String.valueOf(l2));
                Log.e("name=", String.valueOf(t2));*/
                dropAddress = place.getName().toString();
                flagdrop = 1;
                if (flagpickup == 1 && flagdrop == 1) {
                    drawTwoMarker(l1, t1, l2, t2);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i("", status.getStatusMessage());
            }
        }

    }

    public boolean validationLoginCheck() {
        boolean check = false;
        String drop_city = et_dropcity.getText().toString();
        String pickUp = et_pickup.getText().toString();
        if (check == false) {
            if (drop_city.equals("")) {
                Toast.makeText(getActivity(), "Please enter pickup address ", Toast.LENGTH_SHORT).show();
            } else if (pickUp.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "please enter drop address", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }

    public void drawTwoMarker(double l1, double t1, double l2, double t2) {
        if (mMap != null)
            mMap.clear();
        LatLng sydney = new LatLng(l1, t1);

        LatLng sydney1 = new LatLng(l2, t2);
        markerPoints.add(sydney);
        builder = new LatLngBounds.Builder();

        drawMarker(new LatLng(l1, t1), "");
        drawMarker1(new LatLng(l2, t2), "");

        bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(cu);
        String url = getDirectionsUrl(sydney, sydney1);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
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

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_item_custom_marker, null);
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
    public void getDescAddress(Double ll1, Double tt1) {
        // Get the location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            Geocoder gcd = new Geocoder(getActivity(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(ll1,
                        tt1, 1);
                if (addresses.size() > 0) {
                    String address, locality = null, add = null, subLocality = null, state, country, postalCode, knownName = null;

                    for (int i = 0; i < addresses.size(); i++) {
                        address = addresses.get(i).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        locality = addresses.get(i).getLocality();
                        subLocality = addresses.get(i).getSubLocality();
                        state = addresses.get(i).getAdminArea();
                        country = addresses.get(i).getCountryName();
                        postalCode = addresses.get(i).getPostalCode();
                        knownName = addresses.get(i).getAddressLine(i);

                    }
                    flagpickup = 1;

                    enableFlag += 1;
                    l1=lat;
                    t1=lang1;
                    et_dropcity.requestFocus();

                    if (subLocality != null) {

                        Log.e("add--", locality + "," + subLocality + " , " + add + " , " + knownName);
                        et_pickup.setText(knownName);
                        pickupAddress = knownName;

                        //currentLocation.setText(knownName);

                    } else {
                        et_pickup.setText(knownName);
                        //currentLocation.setText(knownName);
                        pickupAddress = knownName;

                        Log.e("add--", locality + " , " + add + " , " + knownName);
                    }
                    Log.e("add--", subLocality);
                }

            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getActivity(), SyncStateContract.Constants.ToastConstatnts.ERROR_FETCHING_LOCATION, Toast.LENGTH_SHORT).show();
            }
        }
    }
}


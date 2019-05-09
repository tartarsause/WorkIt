package com.example.workit.Activities;


import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workit.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;
import static android.provider.SettingsSlicesContract.KEY_LOCATION;



public class Map_view_fragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {

    //Hardcoded office locations
    private static final LatLng WeWork_City_House = new LatLng(1.281293, 103.850049); //Address: 36 Robinson Rd, Singapore 068877
    private static final LatLng JustCo = new LatLng(1.281797, 103.851504); //Address: 6 Raffles Quay, #16-01, Singapore 048580
    private static final LatLng Coworkyard_Office = new LatLng(1.283436, 103.852680); //Address: 11 Collyer Quay, Singapore 049319

    //Hardcoded office prices
    private int price1 = 30;
    private int price2 = 30;
    private int price3 = 25;

    private Marker mWeWork;
    private Marker mJustCo;
    private Marker mCoworkyard;



    private static final int MY_REQUEST_INT = 177;

    GoogleMap mMap;
    TextView receivedCityText;
    final static String DATA_RECEIVE = "data_receive";


    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private boolean mLocationPermissionGranted = true;

    // A default location, Singapore and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(1.290270, 103.851959);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public Map_view_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_view_fragment, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            receivedCityText.setText(args.getString(DATA_RECEIVE));
            receivedCityText = getView().findViewById(R.id.CityTextView);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        receivedCityText = view.findViewById(R.id.CityTextView);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle("Map");

    }

    protected void displayReceivedData(String message) {
        receivedCityText.setText(message);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //If bundle is present, geolocate query
        if (receivedCityText != null) {
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();

            //execute method to find city and move camera
            geoLocate();
        } else {
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        }
        LatLng singapore = new LatLng(1.290270, 103.851959);
        googleMap.addMarker(new MarkerOptions().position(singapore)
                .title("Singapore"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 15));

        // Add some markers to the map, and add a data object to each marker.
        mWeWork = mMap.addMarker(new MarkerOptions()
                .position(WeWork_City_House)
                .title("WeWork City House")
                .snippet("$" +price1 + "/day")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mWeWork.setTag(0);
        mWeWork.showInfoWindow();

        mCoworkyard = mMap.addMarker(new MarkerOptions()
                .position(Coworkyard_Office)
                .title("Coworkyard Office")
                .snippet("$" +price2 + "/day")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mCoworkyard.setTag(0);
        mCoworkyard.showInfoWindow();

        mJustCo = mMap.addMarker(new MarkerOptions()
                .position(JustCo)
                .title("JustCo")
                .snippet("$" +price3 + "/day")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mJustCo.setTag(0);
        mJustCo.showInfoWindow();

        //TODO: Create custom marker for offices to display price,
        // refer to https://stackoverflow.com/questions/50246711/google-map-with-number-markers-from-server

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            //Toast.makeText(getContext(), marker.getTitle() + " has been clicked " + clickCount + " times.",
                    //Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


//    attachment method to implement the interface of sending office data
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try{
//            mOfficeDataCallBack = (OnInfoWindowClickedListener) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().toString() +
//                    "onInfoWindowClickedListener has not been implemented correctly");
//        }
//    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //Saves state of map when activity is paused
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current Location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "My Location button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        //Gets the current location of the device, and positions the map's camera.
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (receivedCityText == null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void geoLocate() {
        String query = receivedCityText.getText().toString();
        Log.d(TAG, "geoLocate: Query is " + query);
        Log.d(TAG, "geoLocate: geolocating");


        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(query, 1);
        } catch (IOException e)
        {
            Log.e(TAG, "geoLocate: IOException" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 10);
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getSnippet() != null ) {
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            String officeName = marker.getTitle();
            Integer officePrice = 25;
            intent.putExtra("officeName", officeName);
            intent.putExtra("officePrice", price2);
            startActivity(intent);
        }
    }
}

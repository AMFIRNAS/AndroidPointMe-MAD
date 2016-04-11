package firenew.aleemfirnas21.mapproject;


import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps .model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    GoogleMap mMap;
    private static final double
            SLIIT_LAT = 6.915285,
            SLIIT_LNG = 79.975246,
            DIYAWANNAWA_LAT = 6.886945,
            DIYAWANNAWA_LNG = 79.933734,
            KADUWELA_LAT = 6.930830,
            KADUWELA_LNG = 79.984218;


    private GoogleApiClient mLocationClient;
    private LocationListener mListener;
    //private Marker marker;
    private Marker marker1, marker2;
    private Polyline line;

    Marker marker;
    Circle shape;

    private static final int POLYGON_POINTS = 5;

    List<Marker> markers = new ArrayList<>();
    //private Polygon shape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);


        if (servicesOK()) {
            setContentView(R.layout.activity_map);

            if (initMap()) {
//                Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
                gotoLocation(SLIIT_LAT, SLIIT_LNG, 15);



                //enable this for use current location button
//                mLocationClient = new GoogleApiClient.Builder(this)
//                        .addApi(LocationServices.API)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//                        .build();
//
//                mLocationClient.connect();

                  mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
            }

        } else {
            setContentView(R.layout.activity_main);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Add menu handling code
        switch (id) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }


        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    public boolean servicesOK() {

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();


            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        TextView tvLocality = (TextView) v.findViewById(R.id.tvLocality);
                        TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
                        TextView tvLng = (TextView) v.findViewById(R.id.tvLng);
                        TextView tvSnippet = (TextView) v.findViewById(R.id.tvSnippet);

                        LatLng latLng = marker.getPosition();
                        tvLocality.setText(marker.getTitle());
                        tvLat.setText("Latitude: " +latLng.latitude);
                        tvLng.setText("Longitude: " +latLng.longitude);
                        tvSnippet.setText(marker.getSnippet());

                        return v;
                    }
                });

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        Geocoder gc = new Geocoder(MainActivity.this);
                        List<Address> list = null;

                        try {
                            list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        Address add = list.get(0);
                        MainActivity.this.addMarker(add, latLng.latitude, latLng.longitude);
                    }
                });


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String msg = marker.getTitle() + " (" +
                                marker.getPosition().latitude + ", " +
                                marker.getPosition().longitude + ")";
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Geocoder gc = new Geocoder(MainActivity.this);
                        List<Address> list = null;
                        LatLng ll = marker.getPosition();
                        try {
                            list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        Address add = list.get(0);
                        marker.setTitle(add.getLocality());
                        marker.setSnippet(add.getCountryName());
                        marker.showInfoWindow();
                    }
                });



            }
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void geoLocate(View v) throws IOException {

        hideSoftKeyboard(v);

        TextView tv = (TextView) findViewById(R.id.editText1);
        String searchString = tv.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            Address add = list.get(0);
            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, 15);

            if (marker != null) {
                marker.remove();
            }

            addMarker(add, lat, lng);


//            final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
//            Marker melbourne = mMap.addMarker(new MarkerOptions()
//                    .position(MELBOURNE)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//
//
//            MarkerOptions options = new MarkerOptions()
//                    .position(new LatLng(25.2744, 133.7751))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
//
//            String country = add.getCountryName();
//            if (country.length() > 0) {
//                options.snippet(country);
//            }
//
//            marker = mMap.addMarker(options);
//


        }

    }


    private void addMarker(Address add, double lat, double lng) {
//        MarkerOptions options = new MarkerOptions()
//                .title(add.getLocality())
//                .position(new LatLng(lat, lng))
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.defaultMarker());
//              //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
//
//        String country = add.getCountryName();
//        if (country.length() > 0) {
//            options.snippet(country);
//        }
//
//        //marker = mMap.addMarker(options);
//
//
//        if (marker1 == null) {
//            marker1 = mMap.addMarker(options);
//        } else if (marker2 == null) {
//            marker2 = mMap.addMarker(options);
//            drawLine();
//        } else {
//            removeEverything();
//            marker1 = mMap.addMarker(options);
//        }


//        if (markers.size() == POLYGON_POINTS) {
//            removeEverything();
//        }
//
//        MarkerOptions options = new MarkerOptions()
//                .title(add.getLocality())
//                .position(new LatLng(lat, lng))
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.defaultMarker());
////              .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
//        String country = add.getCountryName();
//        if (country.length() > 0) {
//            options.snippet(country);
//        }
//
//        markers.add(mMap.addMarker(options));
//
//        if (markers.size() == POLYGON_POINTS) {
//            drawPolygon();
//        }




        if (marker != null) {
            removeEverything();
        }

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions()
                .title(add.getLocality())
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker());
//              .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        String country = add.getCountryName();
        if (country.length() > 0) {
            options.snippet(country);
        }

        marker = mMap.addMarker(options);

        CircleOptions circleOptions = new CircleOptions()
                .strokeWidth(3)
                .fillColor(0x330000FF)
                .strokeColor(Color.BLUE)
                .center(latLng)
                .radius(1000);
        shape = mMap.addCircle(circleOptions);

    }


//    private void drawPolygon() {
//        PolygonOptions options = new PolygonOptions()
//                .fillColor(0x330000FF)
//                .strokeWidth(3)
//                .strokeColor(Color.BLUE);
//        for (int i = 0; i < POLYGON_POINTS; i++) {
//            options.add(markers.get(i).getPosition());
//        }
//
//        shape = mMap.addPolygon(options);
//    }

//
//    private void drawLine() {
//        PolylineOptions lineOptions = new PolylineOptions()
//                .add(marker1.getPosition())
//                .add(marker2.getPosition());
//        line = mMap.addPolyline(lineOptions);
//    }

    private void removeEverything() {
//        marker1.remove();
//        marker1 = null;
//        marker2.remove();
//        marker2 = null;
//        if (line != null) {
//            line.remove();
//            line = null;
//        }
//        for (Marker marker : markers) {
//            marker.remove();
//        }
//        markers.clear();
//        if (shape != null) {
//            shape.remove();
//            shape = null;
//        }

        marker.remove();
        marker = null;
        shape.remove();
        shape = null;
    }

    public void showCurrentLocation(MenuItem item) {

//        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
//        if (currentLocation == null) {
//            Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT).show();
//        } else {
//            LatLng latLng = new LatLng(
//                    currentLocation.getLatitude(),
//                    currentLocation.getLongitude()
//            );
//            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
//                    latLng, 15
//            );
//            mMap.animateCamera(update);
//        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();

        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MainActivity.this,
                        "Location changed: " + location.getLatitude() + ", " +
                                location.getLongitude(), Toast.LENGTH_SHORT).show();
                gotoLocation(location.getLatitude(), location.getLongitude(), 15);
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mLocationClient, request, mListener
        );
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mLocationClient, mListener
        );
    }
}

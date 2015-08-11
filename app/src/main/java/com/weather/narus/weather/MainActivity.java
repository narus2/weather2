package com.weather.narus.weather;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.narus.weather.model.model;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    AutoCompleteTextView contry;
    private GoogleMap mMap;
    Callback<model> Callback;
    Marker mark;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private model mWeather;

    private static final String TAG = MainActivity.class.getSimpleName();
    // init retrofit
    final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://api.openweathermap.org/")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    final JsonApiService jsonApiService = restAdapter.create(JsonApiService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();
        setUpMapIfNeeded();

        contry = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);

  /* //This is auto complit
         AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        ArrayAdapter<String> adapter =
               new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);
*/
        Button btn_show = (Button) findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SityText = String.valueOf(contry.getText());
                if (!SityText.isEmpty()) {
                    jsonApiService.get_countries(SityText, "metric", Callback);
                }
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
           mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            final UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setCompassEnabled(true);
            uiSettings.setAllGesturesEnabled(true);

            mMap.setInfoWindowAdapter(new WeatherInfoWindowAdapter());
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Callback = new Callback<model>() {

            @Override
            public void success(final model model, final Response response) {
                LatLng userLocation = new LatLng(model.getCoord().getLat(), model.getCoord().getLon());

                MarkerOptions marker = new MarkerOptions()
                .position(userLocation)
                        .snippet("t:" + model.getMain().getTemp())
                .title(model.getName());
                mMap.clear();
                mark = mMap.addMarker(marker);
                showWeatherInfoWindow(model);

            }


            @Override
            public void failure(final RetrofitError error) {
                ShowDialog(R.string.city_not_found);
            }

        };

//        LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = service.getBestProvider(criteria, false);
//        Location location = service.getLastKnownLocation(provider);

//        if (location != null){
//            LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
//            mMap.setMyLocationEnabled(true);
//        } else if (mGoogleApiClient != null){


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.setMyLocationEnabled(true);

        } else {
            ShowDialog(R.string.Not_get_GPS);
        }
    }

    public void ShowDialog(int id) {
        Context context = getApplicationContext();
        CharSequence text = getResources().getText(id);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    /* Connect to Google Play Services
    */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void showWeatherInfoWindow(model weather) {
        mWeather = weather;
        mMap.clear();

        LatLng cityCoordinates = new LatLng(weather.getCoord().getLat(), weather.getCoord().getLon());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cityCoordinates, 10.0f));
        Marker cityMarker = mMap.addMarker(new MarkerOptions()
                .position(cityCoordinates));

        cityMarker.showInfoWindow();
    }

    class WeatherInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mContentsView;

        WeatherInfoWindowAdapter() {
            mContentsView = getLayoutInflater().inflate(R.layout.weather_info_vertical, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (mWeather == null) {
                return null;
            }

            new InfoWindowFiller(getApplicationContext(), mContentsView, mWeather).getFilledView();

            return mContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }

}

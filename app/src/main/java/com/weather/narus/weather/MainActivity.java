package com.weather.narus.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.narus.weather.model.Weather;
import com.weather.narus.weather.model.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {

    String[] countries;
    AutoCompleteTextView contry;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Callback<model> Callback;
    MarkerOptions marker = new MarkerOptions();

    private static final String TAG = MainActivity.class.getSimpleName();

    final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://api.openweathermap.org/")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    final JsonApiService jsonApiService = restAdapter.create(JsonApiService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMapIfNeeded();

        contry = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
         AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
 //       ArrayAdapter<String> adapter =
 //              new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
//        textView.setAdapter(adapter);

//
        Button btn_show = (Button) findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonApiService.get_countries(contry.getText().toString(), Callback );
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
                mMap.clear();
                LatLng userLocation = new LatLng(model.getCoordlt(), model.getCoordln());

                Weather r = model.getWeather().get(0);
                String PICT_URL = "http://openweathermap.org/img/w/" + r.getIcon()+".png";

                new DownloadImageTask().execute(PICT_URL);
                marker = new MarkerOptions()
                        .position(userLocation)
                        .title(model.getName());
                new DownloadImageTask().execute(PICT_URL);


                //MarkerOptions marker =new MarkerOptions().position(userLocation).snippet("Snippet").title(model.getName());
                mMap.addMarker(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(userLocation)
                        .zoom(10)
                        .bearing(45)
                        .tilt(20)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);

            }


            @Override
            public void failure(final RetrofitError error) {
                Context context = getApplicationContext();
                CharSequence text = getResources().getText(R.string.city_not_found);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        };


        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.setMyLocationEnabled(true);


        jsonApiService.get_countries_LatLng(location.getLatitude(), location.getLongitude(), Callback);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       // mMap = googleMap;
//                final UiSettings uiSettings = mMap.getUiSettings();
//                 uiSettings.setZoomControlsEnabled(true);
//                 uiSettings.setCompassEnabled(true);
//                 uiSettings.setAllGesturesEnabled(true);
//
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = service.getBestProvider(criteria, false);
//        Location location = service.getLastKnownLocation(provider);
//        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
//
//                 mMap.addMarker(
//                         new MarkerOptions().position(new LatLng(userLocation.latitude, userLocation.longitude)).
//                                 snippet("Snippet").title("London"));
//
//
//
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            publishProgress(new Void[]{}); //or null

            String url = "";
            if( params.length > 0 ){
                url = params[0];
            }

            InputStream input = null;
            try {
                URL urlConn = new URL(url);
                input = urlConn.openStream();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return BitmapFactory.decodeStream(input);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            marker.icon(BitmapDescriptorFactory.fromBitmap(result));
            mMap.clear();
            mMap.addMarker(marker);
        }
    }
}

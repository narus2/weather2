package com.weather.narus.weather;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.narus.weather.model.model;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements OnMapReadyCallback {

    String[] countries;
    AutoCompleteTextView contry;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

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

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mMap = mapFragment.getMap();
//        if (mMap == null) {
//            finish();
//        }

        contry = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        Button btn_show = (Button) findViewById(R.id.btn_show);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
//        textView.setAdapter(adapter);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonApiService.get_countries(contry.getText().toString(), new Callback<model>() {
                    @Override
                    public void success(final model model, final Response response) {
                        Log.i(TAG, model.toString());
                    }


                    @Override
                    public void failure(final RetrofitError error) {
                        Log.w(TAG, "Error: " + error.getKind());
                    }

                });
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
         //   mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
         //           .getMap();

//            <fragment
//            android:layout_width="wrap_content"
//            android:layout_height="wrap_content"
//            android:name="com.google.android.gms.maps.MapFragment"
//            android:id="@+id/map"
//            android:layout_below="@+id/btn_show"
//            android:layout_alignParentLeft="true"
//            android:layout_alignParentStart="true"
//            android:layout_alignParentBottom="true"
//            android:layout_alignParentRight="true"
//            android:layout_alignParentEnd="true" />

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
                final UiSettings uiSettings = mMap.getUiSettings();
                 uiSettings.setZoomControlsEnabled(true);
                 uiSettings.setCompassEnabled(true);
                 uiSettings.setAllGesturesEnabled(true);


                 mMap.addMarker(
                         new MarkerOptions().position(new LatLng(51.5286416, -0.1015987)).
                                 snippet("Snippet").title("London"));



    }
}

package com.example.jenki.solenteatout;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> MarkerArray;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(50.913639, -1.411781));

        MarkerArray = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), null);
        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemLongPress(int index, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };
     ///the save all files thing goes here, on start

    }

    public void onLocationChanged(Location newLoc) {
        double latitude = newLoc.getLatitude();
        double longitude = newLoc.getLongitude();

        mv.getController().setCenter(new GeoPoint(latitude, longitude));
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addrestaurant) {
            Intent intent = new Intent(this, AddRestaurantActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
          else if (item.getItemId() == R.id.savetofile) {
            try
              {
                  PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/savedRestaurants.csv", true));

                  for(int i = 0; i< MarkerArray.size(); i++)
                  {
                      String restaurantname = MarkerArray.getItem(i).getTitle();
                      String restaurantsnippet = MarkerArray.getItem(i).getSnippet();
                      Double restaurantlatitude = MarkerArray.getItem(i).getPoint().getLatitude();
                      Double restaurantlongitude = MarkerArray.getItem(i).getPoint().getLongitude();

                      pw.println(restaurantname + "," + restaurantsnippet + "," + restaurantlatitude + "," + restaurantlongitude);
                  }
                  pw.close();

                  Toast.makeText(this,"Restaurants Saved" , Toast.LENGTH_LONG).show();
              }
              catch(IOException e)
              {
                  Toast.makeText(this, "Error Saving", Toast.LENGTH_LONG).show();
              }
          }

            else if (item.getItemId() == R.id.load) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/savedRestaurants.csv"));
                String line;
                while ((line = reader.readLine()) != null )  {
                    String[] comp = line.split(",");
                    if(comp.length == 6)  {
                        Double lat = Double.valueOf(comp[4]);
                        Double lon = Double.valueOf(comp[5]);
                        MarkerArray = new ItemizedIconOverlay<>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                        OverlayItem arrayitem = new OverlayItem(comp[0], comp[1]+comp[2]+comp[3], new GeoPoint(lat, lon));
                        try {
                            MarkerArray.addItem(arrayitem);
                            mv.getOverlays().add(MarkerArray);
                        }
                        catch (Exception e)  {
                            new AlertDialog.Builder(this).setMessage(e.toString()).setPositiveButton("good", null).show();
                        }
                    }
                }
            }
            catch (IOException e) {

            }
        }
            return false;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String restaurantNameString = extras.getString("com.example.jenki.solenteatout.name");
                String restaurantAddressString = extras.getString("com.example.jenki.solenteatout.address");
                String restaurantCuisineString = extras.getString("com.example.jenki.solenteatout.cuisine");
                String restaurantRatingString = extras.getString("com.example.jenki.solenteatout.rating");
                MarkerArray = new ItemizedIconOverlay<>(this, new ArrayList<OverlayItem>(), markerGestureListener);
                OverlayItem mapMarker = new OverlayItem(restaurantNameString, restaurantAddressString + "," + restaurantCuisineString + "," + restaurantRatingString, mv.getMapCenter());
                MarkerArray.addItem(mapMarker);
                mv.getOverlays().add(MarkerArray);

                ///make it so the preference being clicked makes this happen.


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean autosave = prefs.getBoolean("autosave", true);

                if (autosave)
                {
                    try
                    {
                        PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/savedRestaurants.csv", true));

                        for(int i = 0; i< MarkerArray.size(); i++)
                        {
                            String restaurantname = MarkerArray.getItem(i).getTitle();
                            String restaurantsnippet = MarkerArray.getItem(i).getSnippet();
                            Double restaurantlatitude = MarkerArray.getItem(i).getPoint().getLatitude();
                            Double restaurantlongitude = MarkerArray.getItem(i).getPoint().getLongitude();

                            pw.println(restaurantname + "," + restaurantsnippet + "," + restaurantlatitude + "," + restaurantlongitude);
                        }
                        pw.close();

                        Toast.makeText(this,"Restaurants Saved" , Toast.LENGTH_LONG).show();
                    }
                    catch(IOException e)
                    {
                        Toast.makeText(this, "Error Saving", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
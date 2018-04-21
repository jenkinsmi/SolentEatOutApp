package com.example.jenki.solenteatout;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class MainActivity extends AppCompatActivity implements LocationListener
{
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);

        LocationManager mgr=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

        mv = (MapView)findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(50.913639,-1.411781));
    }

    public void onLocationChanged(Location newLoc)
    {
        double latitude = newLoc.getLatitude();
        double longitude = newLoc.getLongitude();

        mv.getController().setCenter(new GeoPoint(latitude,longitude));
    }

    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider,int status,Bundle extras)
    {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.addrestaurant)
        {
            Intent intent = new Intent(this,AddRestaurantActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        else if (item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

}

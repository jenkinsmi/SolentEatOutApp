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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class AddRestaurantActivity extends Activity implements View.OnClickListener{
}
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant;
        Button regular = (Button)findViewById(R.id.bt1);
        regular.setOnClickListener(this);
    }
    public void onClick(View view)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        EditText nam = (EditText)findViewById(R.id.et1);
        EditText add = (EditText)findViewById(R.id.et2);
        EditText cui = (EditText)findViewById(R.id.et3);
        EditText rat = (EditText)findViewById(R.id.et4);
        double name = Double.parseDouble(nam.getText().toString());
        double address = Double.parseDouble(add.getText().toString());
        double cuisine = Double.parseDouble(cui.getText().toString());
        double rating = Double.parseDouble(rat.getText().toString());

        bundle.putDouble("com.example.jenki.solenteatout.name",name);
        bundle.putDouble("com.example.jenki.solenteatout.address",address);
        bundle.putDouble("com.example.jenki.solenteatout.cuisine",cuisine);
        bundle.putDouble("com.example.jenki.solenteatout.rating",rating);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
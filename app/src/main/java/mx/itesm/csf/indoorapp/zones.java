package mx.itesm.csf.indoorapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.Region;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.packets.ConfigurableDevice;
import com.estimote.coresdk.recognition.packets.DeviceType;
import com.estimote.coresdk.service.BeaconManager;

import java.util.UUID;

public class zones extends AppCompatActivity {
    private BeaconManager beaconManager; //The beacon manager who is going to monitor and range the beacons
    private BeaconRegion region;    //The template of region that will follow specific beacons according to the features of it
    private static final int NUM_BEACONS = 12;  //Number of beacons to use in the warehouse, in next delivery will be pulled from DB maybe
    BeaconRegion[] BeaconRegions = new BeaconRegion[NUM_BEACONS];   //Array of BeaconRegions to monitor while the app is working (Zones)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EstimoteSDK.initialize(getApplicationContext(), "warehouse-ge-gmail-com-s-b-dpm", "3a35682f21bd1cad60f8ecd9e2a4fc70");  //Initialize application context from estimote

        beaconManager = new BeaconManager(getApplicationContext()); //Estimote beacons manager to manage beacons

        for(int i = 0; i < NUM_BEACONS; i++)
            BeaconRegions[i] = new BeaconRegion("ranged region " + (i+1), UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 12, i+1);

        region = new BeaconRegion("ranged region",UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null); //Setting the features for the ranging region


        setContentView(R.layout.activity_zones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registered Zones: ");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor( -16776961);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zones, menu);
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
}

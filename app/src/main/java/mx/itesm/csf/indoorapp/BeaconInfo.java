package mx.itesm.csf.indoorapp;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconInfo extends AppCompatActivity implements BeaconConsumer {

    Zone zone;
    TextView majorTextView;
    EditText minorEditText;
    EditText posxEditText;
    EditText posyEditText;
    TextView beaconTextView;

    // AltBeacon SDK Objects for Ranging
    private BeaconManager beaconManager;
    //private BeaconRegion region;
    private Beacon highestBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_info);

        // Parse zone object
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("zone");
        zone = gson.fromJson(strObj, Zone.class);

        // Set the activity title
        getSupportActionBar().setTitle("Zone " + zone.getId());

        initializeTextViews();

        beaconTextView = (TextView) findViewById(R.id.beaconTextView);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    // Beacon Ranging with specific Region UUID
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                if (!beacons.isEmpty()) {
                    highestBeacon = beacons.iterator().next();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (highestBeacon.getRssi() >= -65)
                                beaconTextView.setText("Minor: " + highestBeacon.getId3().toString() + "  RSSI: "  + highestBeacon.getRssi());
                        }
                    });
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("4e6ed5ab-b3ed-4e10-8247-c5f5524d4b21"), null, null));
        } catch (RemoteException e) {    }
    }

    public void initializeTextViews() {
        Spanned missing = Html.fromHtml("<font color='#EE0000'>missing</font>");

        // Bind layout elements to variables
        majorTextView = (TextView) findViewById(R.id.majorTextView);
        minorEditText = (EditText) findViewById(R.id.minorEditText);
        posxEditText = (EditText) findViewById(R.id.posxEditText);
        posyEditText = (EditText) findViewById(R.id.posyEditText);


        // Assign values
        majorTextView.setText(zone.getMajor().equals("null") ? missing : zone.getMajor());          // MAJOR
        minorEditText.setHint(zone.getMinor().equals("null") ? missing : zone.getMinor());          // MINOR
        posxEditText.setHint(zone.getX().equals("null") ? missing : zone.getX());                   // X
        posyEditText.setHint(zone.getY().equals("null") ? missing : zone.getY());                   // Y
    }
}

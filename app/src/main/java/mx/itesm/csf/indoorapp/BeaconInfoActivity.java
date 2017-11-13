package mx.itesm.csf.indoorapp;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconInfoActivity extends AppCompatActivity implements BeaconConsumer {

    Button updateZoneButton;
    Button selectClosestBeaconButton;
    RadioButton leftRadioButton;
    RadioButton rightRadioButton;
    //TextView majorTextView;
    EditText minorEditText;
    TextView beaconTextView;
    Context context;

    // AltBeacon SDK Objects for Ranging
    private Request request = new Request();
    private BeaconManager beaconManager;
    private org.altbeacon.beacon.Beacon highestBeacon;
    private String position;
    private String highestMinor;
    private Beacon beacon;
    private String minor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_info);
        context = this;

        // Parse beacon object
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("beacon");
        beacon = gson.fromJson(strObj, Beacon.class);
        minor = beacon.getMinor();
        position = beacon.getPosition();

        // Set the activity title
        getSupportActionBar().setTitle("Zone " + beacon.getId());
        initializeTextViews();

        beaconTextView = findViewById(R.id.beaconTextView);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        updateZoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // We check which values changed and we update only those
                if (!minor.equals(minorEditText.getText().toString()) && minorEditText.getText().toString().length() > 0) {
                    request.updateMinor(beacon.getId(), minorEditText.getText().toString(), context);
                }

                if (!position.equals(beacon.getPosition())) {
                    request.updatePosition(beacon.getId(), position, context);
                }
            }
        });

        selectClosestBeaconButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (highestBeacon.getId3().toString().length() > 0) {
                minorEditText.setText(highestMinor);
            }
            }
        });
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
            public void didRangeBeaconsInRegion(final Collection<org.altbeacon.beacon.Beacon> beacons, Region region) {
            if (!beacons.isEmpty()) {
                highestBeacon = beacons.iterator().next();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    highestMinor = highestBeacon.getId3().toString();
                    String display = "Minor: " + highestMinor + "  RSSI: "  + highestBeacon.getRssi();
                    beaconTextView.setText(display);
                    }
                });
            }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("4e6ed5ab-b3ed-4e10-8247-c5f5524d4b21"), null, null));
        } catch (RemoteException e) {
            // catch error
        }
    }

    public void initializeTextViews() {
        Spanned missing = Html.fromHtml("<font color='#EE0000'>missing</font>");

        // Bind layout elements to variables
        //majorTextView = findViewById(R.id.majorTextView);
        minorEditText = findViewById(R.id.minorEditText);
        updateZoneButton = findViewById(R.id.updateZoneButton);
        selectClosestBeaconButton = findViewById(R.id.selectClosestBeaconButton);
        leftRadioButton = findViewById(R.id.left);
        rightRadioButton = findViewById(R.id.right);

        // Assign values
        //majorTextView.setText(beacon.getMajor().equals("null") ? missing : beacon.getMajor());          // MAJOR
        minorEditText.setHint(beacon.getMinor().equals("null") ? missing : beacon.getMinor());          // MINOR

        if (position.equals("Left")) leftRadioButton.setChecked(true);
        if (position.equals("Right")) rightRadioButton.setChecked(true);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.left:
                if (checked)
                    position = "Left";
                    break;
            case R.id.right:
                if (checked)
                    position = "Right";
                    break;
        }
    }
}

package mx.itesm.csf.indoorapp;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    Context context;
    EditText minorEditText;
    Button updateZoneButton;
    Button selectClosestBeaconButton;
    Button resetBeaconButton;
    RadioButton leftRadioButton;
    RadioButton rightRadioButton;
    TextView beaconTextView;
    //TextView majorTextView;

    private Beacon beacon;                                      // Beacon passed in as intentExtra
    private String position;                                    // RadioButtonGroup value
    private String highestMinor;                                // Closest beacon's MINOR
    private Request request = new Request();                    // Requests to the server
    private BeaconManager beaconManager;                        // Beacon SDK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_info);
        context = this;

        // Parse beacon object
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("beacon");
        beacon = gson.fromJson(strObj, Beacon.class);
        position = beacon.getPosition();

        // Set the activity title
        getSupportActionBar().setTitle("Zone " + beacon.getId());
        initializeTextViews();

        // Declare beacon region to be scanned
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        // Sets the Beacon ID field = the closest beacon's MINOR
        selectClosestBeaconButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (highestMinor.length() > 0) {
                minorEditText.setText(highestMinor);
            }
            }
        });

        // Update zone's values on the server
        // We check which values have changed and we update only those
        updateZoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (!beacon.getMinor().equals(minorEditText.getText().toString()) && minorEditText.getText().toString().length() > 0) {
                request.updateMinor(beacon.getId(), minorEditText.getText().toString(), context);
            }

            if (!position.equals(beacon.getPosition())) {
                request.updatePosition(beacon.getId(), position, context);
            }
            }
        });

        // Sets the zone's beacon's MINOR = null on the server
        resetBeaconButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            request.updateMinor(beacon.getId(), "null", context);
            }
        });

        // Touch Listener to hide keyboard if you press outside of an EditText
        findViewById(R.id.linearLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
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
                // We save the beacon closest to the device
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    highestMinor = beacons.iterator().next().getId3().toString();
                    String display = "Minor: " + highestMinor + "  RSSI: "  + beacons.iterator().next().getRssi();
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
        beaconTextView = findViewById(R.id.beaconTextView);
        leftRadioButton = findViewById(R.id.left);
        rightRadioButton = findViewById(R.id.right);
        updateZoneButton = findViewById(R.id.updateZoneButton);
        resetBeaconButton = findViewById(R.id.resetBeaconButton);
        selectClosestBeaconButton = findViewById(R.id.selectClosestBeaconButton);

        // Assign values
        //majorTextView.setText(beacon.getMajor().equals("null") ? missing : beacon.getMajor());          // MAJOR
        minorEditText.setHint(beacon.getMinor().equals("null") ? missing : beacon.getMinor());            // MINOR
        if (position.equals("Left")) leftRadioButton.setChecked(true);                                    // LEFT
        if (position.equals("Right")) rightRadioButton.setChecked(true);                                  // RIGHT
    }

    // Read values from the RadioButtonGroup
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

package chaos.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.estimote.coresdk.recognition.packets.Beacon;

import com.google.gson.Gson;

public class beaconInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_info);

        final TextView textView3 = (TextView) findViewById(R.id.textView3);

        // We pull the beacon object
        Gson gson = new Gson();
        String beaconDataObjectAsAString = getIntent().getStringExtra("BeaconObjectAsString");
        textView3.setText(beaconDataObjectAsAString);
        //Beacon beaconDataObject = gson.fromJson(beaconDataObjectAsAString, Beacon.class);
    }
}

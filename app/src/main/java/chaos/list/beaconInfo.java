package chaos.list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class beaconInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_info);

        final TextView minorTv = (TextView) findViewById(R.id.minorTv);
        final TextView majorTv = (TextView) findViewById(R.id.majorTv);

        final EditText zoneEt = (EditText) findViewById(R.id.zonaEt);

        Button saveBtn = (Button) findViewById(R.id.saveBtn);

        // We pull the beacon object
        final int minor = getIntent().getIntExtra("minorInt", 0);
        final int major = getIntent().getIntExtra("majorInt", 0);

        majorTv.setText(String.valueOf(major));
        minorTv.setText(String.valueOf(minor));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Beacon information is sent back to MainActivity through an Intent.
                // Once the information is received in the MainActivity, the new beacon is added to the list
                Log.d("beaconInfo", "Save Button Pressed");
                Intent intent = new Intent(beaconInfo.this, MainActivity.class);

                String zoneString = zoneEt.getText().toString();
                int zone = -1;
                if (zoneString.length() > 0) {
                    zone = Integer.parseInt(zoneString);
                }

                intent.putExtra("zoneInt", zone);
                intent.putExtra("minorInt", minor);
                startActivity(intent);
            }
        });


    }
}

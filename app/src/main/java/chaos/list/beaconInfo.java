package chaos.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class beaconInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_info);

        final TextView minorTv = (TextView) findViewById(R.id.minorTv);
        final TextView majorTv = (TextView) findViewById(R.id.majorTv);

        // We pull the beacon object
        int minor = getIntent().getIntExtra("minorInt", 0);
        int major = getIntent().getIntExtra("majorInt", 0);

        majorTv.setText(String.valueOf(major));
        minorTv.setText(String.valueOf(minor));
    }
}

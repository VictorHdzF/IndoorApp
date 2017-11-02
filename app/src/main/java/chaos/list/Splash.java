package chaos.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

/**
 * Created by danflovier on 10/09/2017.
 */

public class Splash extends AppCompatActivity {

    ProgressBar mProgress; // Progressbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // Session class instance
        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        // Show splash screen and login session if the user is not logged in
            startSplash();
    }

    private void startSplash() {
        // Start a thread for set the progressbar value
        new Thread(new Runnable() {
            public void run() {
                for (int progress = 0; progress <= 100; progress++) {
                    try {
                        // Set the velocity of the value progress
                        Thread.sleep(30);
                        mProgress.setProgress(progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Start next activity
                startLogin();
                finish();
            }
        }).start();
    }

    private void startLogin(){
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);
        finish();
    }
}

package chaos.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

/**
 * Created by danflovier on 10/09/2017.
 */

public class Login extends AppCompatActivity {
    private String pass = "password";
    private String id = "0";
    private LoginPreferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText form = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button1);

        // Session Manager
        session = new LoginPreferences(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(form.getText().toString(), pass.toString())) {
                    id = "1";
                    session.createLoginSession(id);
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                   Message.message(Login.this, "Wrong Password!");
                }
            }
        });

    }
    /*
    // Get the ID of the lifttruck to show the data in other places of the app
    public static String getVariable(){
        return id_lifttruck;
    }*/
}



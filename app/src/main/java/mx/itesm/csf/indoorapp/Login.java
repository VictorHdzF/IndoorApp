package mx.itesm.csf.indoorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by danflovier on 10/09/2017.
 */

public class Login extends AppCompatActivity {
    private String pass = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText form = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                /*if (Objects.equals(form.getText().toString(), pass.toString())) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this, "Wrong Password!", Toast.LENGTH_LONG).show();
                }*/
            }
        });

    }
}



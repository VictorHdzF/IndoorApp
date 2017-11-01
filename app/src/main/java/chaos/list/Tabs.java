package chaos.list;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Created by danflovier on 10/09/2017.
 */

public class Tabs extends AppCompatActivity implements Tab3_Shipload.OnFragmentInteractionListener {

    public static final String PREFS = "examplePrefs";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final int ZXING_CAMERA_PERMISSION = 1;

    // ViewPager that will host the section contents.
    private ViewPager mViewPager;

    static Tabs activity;

    // SQLite Database
    SQLiteAdapter helper;

    // AlertDialog
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    // Network receiver
    NetworkReceiver networkReceiver;

    // String to store the id of the liftruck
    //String id_lt = LoginLiftTruck.getVariable();
    static String id_lt;

    // Shared preferences to make an autoincrement id for the products list
    SharedPreferences sharedPreferenceID;
    Editor editor;
    String updateID;
    String stringID;
    int id = 0;


    public static final String PAGE_URL = "https://webservice-warehouse.run.aws-usw02-pr.ice.predix.io/index.php";


    // Shared Preferences for session login
    LoginPreferences session;

    // Shared Preferences for list items
    ProductsPreferences list_sP;

    Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initialize activity
        activity = this;

        // Shared Preferences class instance for Session manager
        session = new LoginPreferences(getApplicationContext());
        list_sP = new ProductsPreferences(getApplicationContext());
        // Components to use the SQLite database
        helper = new SQLiteAdapter(this);

        // AlertDialog
        builder = new AlertDialog.Builder(this);

        // Network receiver
        networkReceiver = new NetworkReceiver();

        request = new Request(getApplicationContext());

        networkBroadcast();
        new newProgressDialog().execute();

        HashMap<String, String> user = session.getLiftTruckDetail();
        id_lt = user.get(LoginPreferences.KEY_LIFTTRUCK);

        // Check if the permission to use the CAMERA is activated on the device
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }

        // Check connectivity to Internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            connectivityState(true);
            try {
                helper.deleteData("products_db");
                request.RequestProducts("listaProducto");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            connectivityState(false);
        }



        //Message.message(getApplicationContext(),"ENTRE: " + size_table);
        // SharedPreferences for the ID's of the products
        //sharedPreferenceID = this.getSharedPreferences(PREFS, id);
        //updateID = sharedPreferenceID.getString("local_ID", "0");
        //id = Integer.parseInt(updateID);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// NETWORK ///////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Set the connectivity
    public void connectivityState(boolean ans){
        int size_table = helper.getSize();
        // Validate if there's connection to Internet
        if (ans == true){
            // Validate if there's any content on the table of database
            if (size_table > 0){
                // Send saved data to the server
                sendSQLiteData();
            }


        }
    }

    // Network broadcast for Nougat
    private void networkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    // Unregister possibly network changes
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// MENU /////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Inflate the menu - Add items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.missing_products) {
            showDatabaseInfo();
            return true;
        }

        if (id == R.id.list_products) {
            showProductsDatabaseInfo();
            return true;
        }

        if (id == R.id.logout) {
            list_sP.removeList();
            session.logoutLiftTruck();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Returns a fragment corresponding to one of the tabs.
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Tab1_Principal.newInstance();
                case 1:
                    return Tab2_Products.newInstance();
                case 2:
                    return Tab3_Shipload.newInstance();
            }

            return null;
        }

        // Obtain the number of tabs showed in the bar(3)
        @Override
        public int getCount() {
            return 3;
        }

        // Set the title of each tab
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PRINCIPAL";
                case 1:
                    return "PRODUCTS";
                case 2:
                    return "SHIPLOAD";
            }
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////// GETTING DATA ////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Instance to use the method
    public static Tabs getInstance(){
        return  activity;
    }

    // Activity obtains the value of the EAN registered
    @Override
    public void getProductData(String ean, String productName) {

        // Counter of id and number of the product
        id++;
        stringID = Integer.toString(id);

        // Name of the product with the number of the ID
        //String product_name = productName + stringID;

        // Send the data to the database
        sendToPredix (ean, id_lt, "1", "1");

        // Create a product with the properly parameters
        Products product = new Products(id,ean,productName,id_lt, "1", "1");

        // Send the data of the interface to the function of
        // the other fragment to manipulate the info and add a product to the list
        String tag = "android:switcher:" + R.id.container + ":" + 1;
        Tab2_Products f = (Tab2_Products) getSupportFragmentManager().findFragmentByTag(tag);
        f.addProduct(product);
    }


    // Get the ID of the lifttruck to show the data in other places of the app
    public static String getVariable(){
        return id_lt;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// EXTERNAL DATABASE ///////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // Connection to the database

    public void sendToPredix(final String ean, final String mont, final String status, final String section) {

        final String[] respuesta = new String[1];

        RequestQueue queue = Volley.newRequestQueue(Tabs.this);

        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, PAGE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                respuesta[0] = response;
                if(respuesta[0].equals("001")) {
                    Toast.makeText(Tabs.this, "Registered (" + respuesta[0] +")\n EAN: " + ean , Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Tabs.this, "Registered error (" + respuesta[0] + ")", Toast.LENGTH_LONG).show();
                    helper.insertData(ean, mont, status, section);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                respuesta[0] = "100";
                Toast.makeText(Tabs.this, "Error to connect (" + respuesta[0] + ")", Toast.LENGTH_LONG).show();
                helper.insertData(ean, mont, status, section);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ean", ean);
                params.put("mnt", mont);
                params.put("s", status);
                params.put("sec", section);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String encodedString = Base64.encodeToString(String.format("%s:%s", "app_client", "prueba123").getBytes(), Base64.NO_WRAP);
                String infoAut = String.format("Basic %s", encodedString);
                headers.put("Authorization", infoAut);
                return headers;
            }

        };

        //AppController.getInstance().addToRequestQueue(sr);
        queue.add(sr);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// INTERNAL DATABASE //////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void sendSQLiteData(){
        final int size_table = helper.getSize();

        final ArrayList<String> ean, id_lift_truck, status, section;

        // Retrieve data from database in the array lists
        ean = helper.getResults("Ean");
        id_lift_truck = helper.getResults("LoginLiftTruck");
        status = helper.getResults("Status");
        section = helper.getResults("Section");

        int tam = section.size();
        // Delete data from the table of the database
        helper.deleteData("saved_products_db");

        new Runnable(){
            @Override
            public void run() {
                try {
                    // Send data to the server
                    for (int i = 0; i < size_table; i++) {
                        sendToPredix(ean.get(i),id_lift_truck.get(i), status.get(i), section.get(i));
                        //Message.message(getApplicationContext(),"P:" + ean.get(i) +"--" + id_lift_truck.get(i) + "--" + status.get(i) + "--" + section.get(i) +"\n");
                        Thread.sleep(200);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.run();

        // Remove data from array lists
        ean.clear();
        id_lift_truck.clear();
        status.clear();
        section.clear();
    }


    public void showDatabaseInfo() {
        String database = helper.getData();

        if (!Objects.equals(database, "")) {
            builder.setTitle("DATABASE CONTENT")
                    .setMessage("ID \t\t\tEAN(BARCODE)\t\t\tLT\tST \t\tSE\n\n" + database)
                    .setPositiveButton("ACCEPT",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
            alertDialog = builder.create();
            alertDialog.getWindow().setLayout(600, 400);
            alertDialog.show();
        }
        else{
            Message.message(getApplicationContext(), "No records found");
        }
    }

    public void showProductsDatabaseInfo() {
        String database = helper.getProductsData();

        if (!Objects.equals(database, "")) {
            builder.setTitle("DATABASE CONTENT")
                    .setMessage("ID \t\t\tEAN(BARCODE)\t\t\tNAME\n\n" + database)
                    .setPositiveButton("ACCEPT",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
            alertDialog = builder.create();
            alertDialog.getWindow().setLayout(600, 400);
            alertDialog.show();
        }
        else{
            Message.message(getApplicationContext(), "No records found");
        }
    }

    /*
    // onPause() will be called whenever you leave your    activity, temporary or permanently.
    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferenceID.edit();
        editor.putString("local_ID", stringID);
        editor.apply();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// ASYNCTASK //////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    private class newProgressDialog extends AsyncTask<Void,Void,Void> {
        // Initialize a new instance of progress dialog
        private ProgressDialog pd = new ProgressDialog(Tabs.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            // Set the title
            pd.setTitle("Processing");
            // Set a message
            pd.setMessage("Loading...");
            // Set a style of the progress
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // Sets whether the dialog is cancelable or not
            pd.setCancelable(false);
            // Show the progress dialog
            pd.show();
        }

        @Override
        protected Void doInBackground(Void...args){
            // Start Operation in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                        try {
                            // Set time of showing ProgressDialog
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Close the ProgressDialog
                        pd.dismiss();
                }
            }).start(); // Start the operation

            return null;
        }
    }


}




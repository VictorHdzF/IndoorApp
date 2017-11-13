package mx.itesm.csf.indoorapp;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.support.v7.widget.RecyclerView;

/**
 * Created by magnus on 11/13/17.
 */

public class Request {

    public static final String BASE_URL = "https://webservice-warehouse.run.aws-usw02-pr.ice.predix.io/index.php/";
    private DataAdapter mAdapter;
    private String status;
    ArrayList<Beacon> mArrayList;

    // Make JSON array request with ([)
    public void getZones(final Context context, final RecyclerView mRecyclerView) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, BASE_URL, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    // Transform the response into JSONArray
                    JSONArray array = new JSONArray(response);
                    ArrayList tempArrayList = new ArrayList<Beacon>();

                    // Go through every index of the array
                    // for reading the data and save it in the JSONObject
                    for (int i = 0; i < array.length(); i++) {

                        // Save info of the array in the JSONObject
                        JSONObject beacons = (JSONObject) array.get(i);

                        String has_beacon = beacons.getString("has_beacon");

                        if (has_beacon.equals("t")) {
                            // Obtain the info inside of every
                            // attribute inside of the object
                            String minor = beacons.getString("minor");
                            String major = beacons.getString("major");
                            String id = beacons.getString("id");
                            String position = beacons.getString("position");

                            tempArrayList.add(new Beacon(id, minor, major, position));
                        }
                    }
                    mArrayList = new ArrayList<>(tempArrayList);
                    Collections.sort(mArrayList);
                    mAdapter = new DataAdapter(mArrayList, context);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MainActivity", "Error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("s", "beacons");
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
        queue.add(sr);
    }

    // Filters values on RecyclerView List when a query is typed in
    public void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    // Update MINOR for specific ID
    public void updateMinor(final String id, final String minor, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, BASE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    // Go through every index of the array
                    // for reading the data and save it in the JSONObject
                    JSONObject jsonResponse = new JSONObject(response);
                    status = jsonResponse.getString("status");

                    if(!status.equals("001")) //Message.message(context,"Minor Successfully Updated");
                    Message.message(context,"Error Updating Minor " + status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status = "100";
                Message.message(context, "Error to connect (" + status + ")");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("minor", minor);
                params.put("s", "updateMinor");
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
        queue.add(sr);
    }

    // Update POSITION for specific ID
    public void updatePosition(final String id, final String position, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, BASE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    // Go through every index of the array
                    // for reading the data and save it in the JSONObject
                    JSONObject jsonResponse = new JSONObject(response);
                    status = jsonResponse.getString("status");

                    if(!status.equals("001")) //Message.message(context,"Pos Y Successfully Updated");
                    Message.message(context,"Error Updating Position " + status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                status = "100";
                Message.message(context, "Error to connect (" + status + ")");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("position", position);
                params.put("s", "setPosition");
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
        queue.add(sr);
    }
}

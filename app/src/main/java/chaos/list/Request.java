package chaos.list;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danflovier on 23/10/2017.
 */

public class Request {
    // URL of the server
    private String URL = "https://webservice-warehouse.run.aws-usw02-pr.ice.predix.io/index.php";

    // TAG to make a Log
    private static String TAG = Request.class.getSimpleName();

    // Context of the class
    Context context;

    // SQLite adapter
    SQLiteAdapter db;

    // Constructor of the class
    Request(Context context){
        this.context = context;
        db = new SQLiteAdapter(this.context);
    }

    // Connection to the external database (Predix)
    public void sendToPredix(final String ean, final String mont, final String status, final String section) {
        final String[] respuesta = new String[1];

        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                respuesta[0] = response;
                if(respuesta[0].equals("001")) {
                    Message.message(context,"Registered (" + respuesta[0] +")\n EAN: " + ean );
                }
                else {
                    Message.message(context,"Registered error (" + respuesta[0] + ")" );
                    db.insertData("upload_pending",ean,"", mont, status, section);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                respuesta[0] = "100";
                Message.message(context, "Error to connect (" + respuesta[0] + ")");
                db.insertData("upload_pending",ean,"", mont, status, section);
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
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
        VolleySingleton.getInstance().addToRequestQueue(sr);
    }

    // Make JSON array request with ([)
    public void RequestProducts(final String status) {
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    // Transform the response into JSONArray
                    JSONArray array = new JSONArray(response);

                    // Go through every index of the array
                    // for reading the data and save it in the JSONObject
                    for (int i = 0; i < array.length(); i++) {

                        // Save info of the array in the JSONObject
                        JSONObject products = (JSONObject) array.get(i);

                        // Obtain the info inside of every
                        // attribute inside of the object
                        String name = products.getString("name");
                        String ean = products.getString("ean");

                        // Insert the information of the products in the internal database
                        db.insertData("products_list",ean, name,"","","");

                        /*
                        if (ans > 0){
                            Message.message(context,"SI SE PUDO!");
                        }*/
                    }

                    int size = db.getSize(SQLiteHelper.PRODUCTS_LIST);
                    //Message.message(context, "TAMANIO: " + size);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message.message(context, "Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Message.message(context, "Error to connect" );
                //Toast.makeText(context, "Error to connect" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("s", status);
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
        VolleySingleton.getInstance().addToRequestQueue(sr);
    }



}

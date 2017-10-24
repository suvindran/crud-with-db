package example.in.foodconnectdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoaderActivity extends AppCompatActivity {
    DBhelper db;
    SharedPreferences sp;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        db = new DBhelper(this);
        db.tabledelete();
        sp = getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);
        getfood();

    }


    public void getfood() {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/food";

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {
                            JSONArray data = response.getJSONArray("data");

                            for(int i=0;i<data.length();i++) {
                                JSONObject json = data.getJSONObject(i);
                                db.add(new Model( json.get("name").toString(),Integer.valueOf(json.get("id").toString()),Double.valueOf(json.get("price").toString())));
                            }

                            Intent intent = new Intent(LoaderActivity.this,MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                // hide the progress dialog
                hidepDialog();
            }
        }) {
            //@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+sp.getString("token",""));
                return headers;

            }

        };
// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}

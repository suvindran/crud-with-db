package example.in.foodconnectdb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {
    EditText name,rate;
    Button create;
    SharedPreferences sp;
    DBhelper db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        sp=getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);

        db = new DBhelper(this);

        name = (EditText) findViewById(R.id.name1);
        rate = (EditText) findViewById(R.id.rate1);

        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(name.getText().toString().equals("")) && !(rate.getText().toString().equals("")));
                login(name.getText().toString(),rate.getText().toString());
                db.add(new Model(name.getText().toString(),id,Double.valueOf(rate.getText().toString())));

            }
        });
    }

    public void login(final String name1_text, final String rate1_text) {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/food/create";


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        HashMap<String, String> params = new HashMap<String, String>();

        params.put("name", name1_text);
        params.put("price", rate1_text);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            if((response.get("status").toString()).equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }

        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Accept", "application/json");
                header.put("Authorization", "Bearer " + sp.getString("token", ""));
                return header;
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}

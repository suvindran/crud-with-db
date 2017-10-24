package example.in.foodconnectdb;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    EditText namefood,ratefood;
    Button btn;
    int id;
    DBhelper db;
    List<Model> list;
    SharedPreferences sp;
    Context context;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String name = extras.getString("name");
        String rate = extras.getString("rate");
        final String id = extras.getString("id");

        sp=getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);



        namefood = (EditText) findViewById(R.id.namefood);
        namefood.setText(extras.getString("name"));

        ratefood = (EditText) findViewById(R.id.ratefood);
        ratefood.setText(extras.getString("rate"));

        db = new DBhelper(this);


        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(namefood.getText().toString().equals("")) && !(ratefood.getText().toString().equals(""))) ;
                Edit(id,namefood.getText().toString(),ratefood.getText().toString());
            }
        });


    }
    public void Edit(final String id,final String namefood_text, final String ratefood_text) {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/food/update";


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        HashMap<String, String> params = new HashMap<String, String>();

        params.put("id", id);
        params.put("name", namefood_text);
        params.put("price", ratefood_text);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            if((response.get("status").toString()).equals("1")) {

                                db.update(new Model(namefood_text,Integer.valueOf(id),Double.valueOf(ratefood_text)), id);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
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

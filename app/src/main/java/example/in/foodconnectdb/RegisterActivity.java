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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, cpass;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        cpass = (EditText) findViewById(R.id.cpass);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (!(name.getText().toString()).equals("") && !(email.getText().toString()).equals("") && !(password.getText().toString()).equals("") && !(cpass.getText().toString()).equals(password.getText().toString())) {
                    register(name.getText().toString(), email.getText().toString(), password.getText().toString());
                //}

            }
        });
    }

    public void register(final String name_text, final String email_text, final String password_text) {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/register";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("name", name_text);
        params.put("email", email_text);
        params.put("password", password_text);
        params.put("c_password", password_text);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            if ((response.get("status").toString()).equals("1")) {
                                JSONObject data = (JSONObject) response.get("data");
                                SharedPreferences sp = getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("id", data.get("id").toString());
                                editor.putString("name", data.get("name").toString());
                                editor.putString("token", data.get("token").toString());
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();}
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
}

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

public class LoginActivity extends AppCompatActivity {
    EditText mail, pass;
    Button login,signup;
    ProgressDialog pDialog;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(LoginActivity.this);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        mail = (EditText) findViewById(R.id.mail);
        pass = (EditText) findViewById(R.id.pass);

        sp = getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);


        if (!(sp.getString("token", "").equals(""))) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mail.getText().toString()).equals("") && !(pass.getText().toString()).equals("")) {
                    login(mail.getText().toString(), pass.getText().toString());
                }

            }
        });
    }

    public void login(final String email_text, final String password_text) {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/login";


        //pDialog.setMessage("Loading...");
        //pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("email", email_text);
        params.put("password", password_text);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            {

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("token", response.get("token").toString());
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), LoaderActivity.class);
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
                //pDialog.hide();
            }
        });

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

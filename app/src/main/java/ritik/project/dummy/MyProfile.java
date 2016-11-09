package ritik.project.dummy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    private String roll;
    private EditText name,roll_et,phone,email;
    private ProgressDialog pDialog;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle extras=getIntent().getExtras();
        if (extras != null) {

            roll = extras.getString("roll");

        }
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        name=(EditText)findViewById(R.id.name_et);
        roll_et=(EditText)findViewById(R.id.roll_no_et);
        phone=(EditText)findViewById(R.id.phone_et);
        email=(EditText)findViewById(R.id.email_et);
        update=(Button)findViewById(R.id.update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetProfile(getCurrentFocus());
            }
        });
        GetProfile(roll,getCurrentFocus());
    }


    private void GetProfile(final String roll, final View view) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Fetching Details...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_MYPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Myprofile Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {



                        JSONObject user=jObj.getJSONObject("user");
                        name.setText(user.getString("name"));
                        roll_et.setText(roll);
                        phone.setText(user.getString("phone"));
                        email.setText(user.getString("email"));

                    } else {
                        Snackbar.make(view, "Error getting Details ", Snackbar.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.d(Constants.TAG, "MainActivity: Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Login Error: " + error.getMessage());
                Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("roll", roll);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void SetProfile(final View view) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Updating Details...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_MYPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Myprofile Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {



                        JSONObject user=jObj.getJSONObject("user");
                        name.setText(user.getString("name"));
                        roll_et.setText(roll);
                        phone.setText(user.getString("phone"));
                        email.setText(user.getString("email"));

                    } else {
                        Snackbar.make(view, "Error getting Details ", Snackbar.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.d(Constants.TAG, "MainActivity: Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Profile Update Error: " + error.getMessage());
                Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("roll", roll);
                params.put("name",name.getText().toString().trim());
                params.put("phone",phone.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

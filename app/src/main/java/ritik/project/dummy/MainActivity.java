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

/**
 * Created by SuperUser on 09-11-2016.
 */
public class MainActivity extends AppCompatActivity {
    public SessionManager session;
    private Button login,linktoRegister,skip;
    private EditText inputroll,inputpass;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Log.d(Constants.TAG, "MainActivity: already logged in with "+session.getUser().toString());
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("user_roll",session.getUser().get("roll"));
            startActivity(intent);
            finish();
        }


        inputroll=(EditText)findViewById(R.id.login_roll_et) ;
        inputpass=(EditText)findViewById(R.id.login_password_et);

        login=(Button)findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(Constants.TAG, "MainActivity: Login button pressed");
                String roll = inputroll.getText().toString().trim();
                String password = inputpass.getText().toString().trim();
                if (!roll.isEmpty() || !password.isEmpty()) {
                               Log.d(Constants.TAG, "MainActivity: ET not empty");
                    if(helper.isRoll(roll))
                        if(password.length()>5)
                        checkLogin(roll, password,view);
                        else
                            inputpass.setError("Password too short");
                    else
                        inputroll.setError("Invalid Roll No.");
                } else {
                    Log.d(Constants.TAG, "onClick: ET empty");
                    Snackbar.make(view, "Please enter the credentials!", Snackbar.LENGTH_LONG).show();

                }
            }

        });

        linktoRegister=(Button)findViewById(R.id.register_btn);
        linktoRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(Constants.TAG, "MainActivity: Register button pressed");
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        skip=(Button)findViewById(R.id.skip_btn);
        skip.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d(Constants.TAG, "MainActivity: Guest Login");
                session.setLogin(false);
                Log.d(Constants.TAG, "onResponse: Session set");


                session.setUser("Guest"," "," "," ",false);
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("user_roll","Guest");
                startActivity(intent);
                finish();

            }
        });
    }







    private void checkLogin(final String roll, final String password, final View view) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Login Response: " + response.toString());
               hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                        session.setLogin(true);

                        JSONObject user=jObj.getJSONObject("user");
                        Log.d(Constants.TAG, "MainActivity verified status: "+user.getString("verified"));
                        Boolean verified=(!user.getString("verified").equals("none"));
                        session.setUser(user.getString("name"),roll,user.getString("phone"),user.getString("email"),verified);

                        Intent intent = new Intent(MainActivity.this,
                                SearchActivity.class);
                        intent.putExtra("user_verified",user.getString("verified"));
                        intent.putExtra("user_roll",roll);
                        startActivity(intent);
                        finish();
                    } else {
                         Snackbar.make(view, "Incorrect Credentials ", Snackbar.LENGTH_LONG).show();

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
                params.put("password", password);

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

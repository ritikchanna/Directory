package ritik.project.dummy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class RegisterActivity extends AppCompatActivity {
    private SessionManager session;
    private Button register_btn;
    private EditText name_et,roll_et,password_et,phone_et,email_et;
    private ProgressDialog pDialog;
    AlertDialog.Builder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(Constants.TAG, "RegisterActivity: Created");

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Log.d(Constants.TAG, "RegisterActivity: Session logged in ");
                 Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        name_et=(EditText) findViewById(R.id.register_name);
        roll_et=(EditText) findViewById(R.id.register_roll);
        password_et=(EditText)findViewById(R.id.register_pass);
        phone_et=(EditText)findViewById(R.id.register_phone);
        email_et=(EditText)findViewById(R.id.register_email);
        alertDialogBuilder = new AlertDialog.Builder(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Log.d(Constants.TAG, "RegisterActivity: 1");
        register_btn=(Button)findViewById(R.id.register);
        Log.d(Constants.TAG, "RegisterActivity: 2");
        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(Constants.TAG, "RegisterActivity: 3");
                String name = name_et.getText().toString().trim();
                String roll=roll_et.getText().toString().trim();
                String phone=phone_et.getText().toString().trim();
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();


                if (!roll.isEmpty() || !password.isEmpty() || !name.isEmpty()) {

                   if(!name.isEmpty())
                    if(helper.isRoll(roll))
                        if(password.length()>5)
                            registerUser(name, email,phone,roll, password);
                        else
                            password_et.setError("Password too short");
                        else
                            roll_et.setError("Invalid Roll No.");
                    else
                        name_et.setError("Name Required");
                } else {
                                Log.d(Constants.TAG, "onClick: ET empty");
                    Snackbar.make(view, "Please enter the credentials!", Snackbar.LENGTH_LONG).show();

                }
            }
        });
        Log.d(Constants.TAG, "onCreate: 4");
    }





    private void registerUser(final String name, final String email,final String phone,final String roll,
                              final String password) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        alertDialogBuilder.setTitle("Success");
                        alertDialogBuilder.setMessage("Registration Successful , Try Login now ");
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                        alertDialogBuilder.show();

                    } else {
                        Log.d(Constants.TAG, "Registration: Error from server");
                        String error_msg = jObj.getString("error_msg");

                        alertDialogBuilder.setTitle("Oops!!");
                        alertDialogBuilder.setMessage(error_msg);
                        alertDialogBuilder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                            }
                        });
                        alertDialogBuilder.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("roll",roll);
                params.put("phone",phone);
                params.put("email", email);
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

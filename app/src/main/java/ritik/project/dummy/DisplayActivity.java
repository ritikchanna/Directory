package ritik.project.dummy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
public class DisplayActivity extends AppCompatActivity {
String name,roll,phone,email,verified_by;
    TextView namet,rollt,phonet,emailt;
    ImageView icn_verified;
    SessionManager session;
    private String user_roll;
    AlertDialog.Builder alertDialogBuilder,alertDialogBuilder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Log.d(Constants.TAG, "DisplayActivity created");
        session = new SessionManager(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            roll=extras.getString("roll");
            phone=extras.getString("phone");
            email=extras.getString("email");
            verified_by=extras.getString("verified_by");
            user_roll=extras.getString("user_roll");

        }
        Log.d(Constants.TAG, "user roll is: "+user_roll);
        namet=(TextView)findViewById(R.id.Disp_name);
        rollt=(TextView)findViewById(R.id.Disp_roll);
        phonet=(TextView)findViewById(R.id.Disp_phone);
        emailt=(TextView)findViewById(R.id.Disp_email);
        icn_verified=(ImageView)findViewById(R.id.img_verifiedv);

        namet.setText(name);
        rollt.setText(roll);
        phonet.setText(phone);
        emailt.setText(email);
        if (!verified_by.equals("none"))
            icn_verified.setVisibility(View.VISIBLE);


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder2 = new AlertDialog.Builder(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.verify:
                if(!user_roll.equals("Guest")) {
                    if (verified_by.equals("none")) {
                        alertDialogBuilder.setTitle("Verify User");
                        alertDialogBuilder.setMessage("Please make sure that all the details are correct , Your identity will be recorded for future reference");
                        alertDialogBuilder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                verify(roll);
                            }
                        });
                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    } else {
                        alertDialogBuilder.setTitle("Already Verified");
                        alertDialogBuilder.setMessage("Oops !! User is already verified");
                        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                    }
                    alertDialogBuilder.show();
                }
                else
                {
                    Log.d(Constants.TAG, "Guest trying to verify");
                    alertDialogBuilder.setTitle("Guest Login");
                    alertDialogBuilder.setMessage("Login to verify other users");
                    alertDialogBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alertDialogBuilder.show();
                }
                return true;
            case R.id.report:
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, Constants.CONTACT_EMAIL);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.CONTACT_EMAIL_SUBJECT);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Kindly look into the details of Roll No. "+roll);


            emailIntent.setType("message/rfc822");

            try {
                startActivity(Intent.createChooser(emailIntent,
                        "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Log.d(Constants.TAG, "Error in reporting: ");
            }



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void verify(final String roll_verify){

        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_VERIFY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Verify Response " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String status=jObj.getString("status");
                        Log.d(Constants.TAG, "Status value "+status);
                        if(status.equals("done")){
                            Log.d(Constants.TAG, "verify status: done");
                            alertDialogBuilder2.setMessage("User has been verified");
                            alertDialogBuilder2.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }else if (status.equals("not verified")){
                            Log.d(Constants.TAG, "verify status: not verified");
                            alertDialogBuilder2.setMessage("To verify other users , You need to be verified first . " +
                                    "To get verified contact any verified user or system admin");
                            alertDialogBuilder2.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }else{Log.d(Constants.TAG, "verify status: error");
                            alertDialogBuilder2.setMessage("Server Error , Please try again later");
                            alertDialogBuilder2.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                        }
                        Log.d(Constants.TAG, "creating alert");
                        alertDialogBuilder2.show();

                    } else {

                        Log.d(Constants.TAG, "Error on verify.php: post parameters missing");
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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("roll_verify", roll_verify);
                params.put("roll",user_roll);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}

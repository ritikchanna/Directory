package ritik.project.dummy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SuperUser on 09-11-2016.
 */
public class DisplayList extends AppCompatActivity {
    private String query1="null1234";
    private String query2="null1234";
    private String user_verified="none";
    private String user_roll;
    private List<student> studentList = new ArrayList<student>();

    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, studentList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

               student clicked=studentList.get(position);
                display(clicked);

            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            query1 = extras.getString("query1");
            query2=extras.getString("query2");
            user_verified=extras.getString("user_verified");
            user_roll=extras.getString("user_roll");
        }
        new GetStudents().execute(query1,query2);
        }



    private class GetStudents extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... arg) {
            getStudents(arg[0],arg[1],"0");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public void getStudents(final String query1,final String query2,final String last) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_SEARCH_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, "Search Response: " + response.toString());
                //hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    JSONObject user = jObj.getJSONObject("user");


                    if (!error) {
                        student student = new student();
                        student.setName(user.getString("name").trim());
                        student.setRoll(user.getString("roll").trim());
                        student.setPhone(user.getString("phone").trim());
                        student.setEmail(user.getString("email").trim());
                        student.setVerified_by(user.getString("verified_by").trim());




                        studentList.add(student);
                        getStudents(query1,query2,user.getString("roll").trim());
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Log.d(Constants.TAG, "onResponse: "+errorMsg);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.d(Constants.TAG, "onResponse: "+e.getMessage());
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("query1", query1);
                params.put("query2",query2);
                params.put("last",last);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void display(student student) {
        Intent intent = new Intent(DisplayList.this, DisplayActivity.class);

            intent.putExtra("name", student.getName());
            intent.putExtra("roll", student.getRoll());
            intent.putExtra("phone", student.getPhone());
            intent.putExtra("email", student.getEmail());
            intent.putExtra("verified_by",student.getVerified_by());
            intent.putExtra("user_roll",user_roll);
        startActivity(intent);

    }
}



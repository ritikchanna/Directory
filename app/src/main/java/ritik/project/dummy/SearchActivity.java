package ritik.project.dummy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.StringTokenizer;

/**
 * Created by SuperUser on 09-11-2016.
 */

public class SearchActivity extends AppCompatActivity {
    private Button search;
    private EditText search_et;
    SessionManager session;
    private String user_verified="none";
    private String user_roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle extras=getIntent().getExtras();
        if (extras != null) {
            user_verified = extras.getString("user_verified");
            user_roll = extras.getString("user_roll");

            }

        session = new SessionManager(getApplicationContext());
        search_et=(EditText)findViewById(R.id.search_et);
        search=(Button)findViewById(R.id.search);
        search_et=(EditText)findViewById(R.id.search_et);
        search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String Search_String=search_et.getText().toString().trim();
                if(!Search_String.isEmpty())
                    DisplayList(Search_String);
                else
                    search_et.setError("No search Query");

            }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                session.setLogin(false);
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(SearchActivity.this,About.class));
                return true;
            case R.id.profile:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void DisplayList(String rawquery){
        StringTokenizer st=new StringTokenizer(rawquery);
        if(st.countTokens()>2)
            search_et.setError("Only 2 tokens allowed");
        else {
            Intent intent = new Intent(SearchActivity.this, DisplayList.class);

            intent.putExtra("user_verified", user_verified);
            intent.putExtra("user_roll", user_roll);
            intent.putExtra("query1", st.nextToken());
            if (st.hasMoreTokens())
                intent.putExtra("query2", st.nextToken());
            else
                intent.putExtra("query2", "n1u2l3l4");

            startActivity(intent);

        }


    }
}

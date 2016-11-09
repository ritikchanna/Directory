package ritik.project.dummy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by SuperUser on 09-11-2016.
 */
public class About extends AppCompatActivity {
    Animation animFadeIn;
    TextView tittle,name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        tittle=(TextView)findViewById(R.id.tittle);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        tittle.setVisibility(View.VISIBLE);
        tittle.startAnimation(animFadeIn);
        name.setVisibility(View.VISIBLE);
        name.startAnimation(animFadeIn);
        email.setVisibility(View.VISIBLE);
        email.startAnimation(animFadeIn);
        phone.setVisibility(View.VISIBLE);
        phone.startAnimation(animFadeIn);
    }
}

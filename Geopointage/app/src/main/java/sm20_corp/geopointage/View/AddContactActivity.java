package sm20_corp.geopointage.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sm20_corp.geopointage.R;

/**
 * Created by gun on 03/11/2016.
 * Geopointage
 */

public class AddContactActivity extends AppCompatActivity {

    private ImageView arrowBack;
    private ImageView done;
    private TextView lastName;
    private TextView firstName;
    private TextView id;
    private int choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Intent intent = getIntent();
        if (intent != null) {
            choose = intent.getIntExtra("choose", 1);
        }

        arrowBack = (ImageView) findViewById(R.id.activity_add_contact_imageview_arrow_back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddContactActivity.this, ContactActivity.class);
                i.putExtra("choose", choose);
                startActivity(i);
            }
        });

        done = (ImageView) findViewById(R.id.activity_add_contact_imageview_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddContactActivity.this, ContactActivity.class);
                i.putExtra("choose", choose);
                startActivity(i);
            }
        });
        lastName = (TextView) findViewById(R.id.activity_add_contact_edittext_last_name);
        firstName = (TextView) findViewById(R.id.activity_add_contact_edittext_first_name);
        id = (TextView) findViewById(R.id.activity_add_contact_edittext_id);


    }


    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }
}

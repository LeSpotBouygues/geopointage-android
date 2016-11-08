package sm20_corp.geopointage.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sm20_corp.geopointage.R;

/**
 * Created by gun on 01/11/2016.
 * Geopointage
 */

public class ChooseActivity extends AppCompatActivity {


    private TextView name;
    private Button button;
    private int choose;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choose);

        Intent intent = getIntent();
        if (intent != null) {
            choose = intent.getIntExtra("choose", 1);
        }

        name = (TextView) findViewById(R.id.activity_choose_textview_name);

        button = (Button) findViewById(R.id.activity_choose_button_open_contact);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, ContactActivity.class);
                i.putExtra("choose", choose);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }

}

package sm20_corp.geopointage.View;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.R;

/**
 * Created by gun on 03/11/2016.
 * Geopointage
 */

public class AddContactActivity extends AppCompatActivity {

    private ImageView arrowBack;
    private ImageView done;
    private ImageView micLastName;
    private ImageView micId;

    private TextView lastName;
    private TextView firstName;
    private TextView id;
    private int choose;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


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

        micLastName= (ImageView) findViewById(R.id.activity_add_contact_imageview_mic_lastname);
        micLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.lastname_and_firstname));
                startActivityForResult(intent, 1);
            }
        });


        micId= (ImageView) findViewById(R.id.activity_add_contact_imageview_mic_id);
        micId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.identity));
                startActivityForResult(intent, 2);
            }
        });

        lastName = (TextView) findViewById(R.id.activity_add_contact_edittext_last_name);
        firstName = (TextView) findViewById(R.id.activity_add_contact_edittext_first_name);
        id = (TextView) findViewById(R.id.activity_add_contact_edittext_id);


        done = (ImageView) findViewById(R.id.activity_add_contact_imageview_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (lastName.getText().length() > 0 && firstName.getText().length() > 0 && id.getText().length() > 0) {

                    User user = new User(lastName.getText().toString(), firstName.getText().toString(), id.getText().toString());
                    DatabaseHandler.getInstance(getApplicationContext()).addUser(user);
                    Intent i = new Intent(AddContactActivity.this, ContactActivity.class);
                    i.putExtra("choose", choose);
                    startActivity(i);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.error_missing_champ), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            TextView lastName = (TextView) findViewById(R.id.activity_add_contact_edittext_last_name);
            TextView firstName = (TextView) findViewById(R.id.activity_add_contact_edittext_first_name);

            String[] tmp = matches.get(0).toString().split(" ");

            lastName.setText(tmp[0]);
            if (tmp.length > 1)
            firstName.setText(tmp[1]);

            System.out.println("last and first = " + matches.get(0));
        }
        else  if (requestCode == 2 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            TextView id = (TextView) findViewById(R.id.activity_add_contact_edittext_id);
            id.setText(matches.get(0));
            System.out.println("id  = " + matches.get(0));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

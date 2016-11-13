package sm20_corp.geopointage.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import sm20_corp.geopointage.Adapter.MyPagerAdapter;
import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.Module.SlidingTabLayout;
import sm20_corp.geopointage.R;

/**
 * Created by gun on 01/11/2016.
 * Geopointage
 */

public class ContactActivity extends FragmentActivity {

    private FloatingActionButton floatingActionButton;
    private ImageView arrowBack;
    private ImageView done;
    private CoordinatorLayout coordinatorLayout;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private int choose;
    private User chef;
    private ArrayList<User> arrayListCollaborateur = new ArrayList<>();

    public void setChef(User chef) {
        this.chef = chef;
    }

    public int getChoose() {
        return choose;
    }

    public void setArrayListCollaborateur(ArrayList<User> arrayListCollaborateur) {
        this.arrayListCollaborateur = arrayListCollaborateur;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Intent intent = getIntent();
        if (intent != null) {
            choose = intent.getIntExtra("choose", 1);
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_contact_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mPager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_contact_fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ContactActivity.this, AddContactActivity.class);
                i.putExtra("choose", choose);
                startActivity(i);
            }
        });


        arrowBack = (ImageView) findViewById(R.id.activity_contact_imageview_arrow_back);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (choose == 0) {
                    Intent i = new Intent(ContactActivity.this, MainActivity.class);
                    //  i.putExtra("user_extra", mUser);
                    startActivity(i);
                } else if (choose == 1) {
                    Intent i = new Intent(ContactActivity.this, ChooseActivity.class);
                    //i.putExtra("user_extra", mUser);
                    startActivity(i);
                }
            }
        });

        done = (ImageView) findViewById(R.id.activity_contact_imageview_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContactActivity.this, MainActivity.class);

                if (choose == 1) {
                    if (chef != null) {
                        DatabaseHandler.getInstance(getApplicationContext()).updateChef(chef);
                        startActivity(i);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.error_select_user), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    if (!arrayListCollaborateur.isEmpty()) {
                        i.putExtra("user_extra", arrayListCollaborateur);
                        startActivity(i);
                    }
                    else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.error_select_user), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }

}

package sm20_corp.geopointage.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import sm20_corp.geopointage.Adapter.MyPagerAdapter;
import sm20_corp.geopointage.Model.Chantier;
import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.Module.SlidingTabLayout;
import sm20_corp.geopointage.R;

/**
 * Created by gun on 01/11/2016.
 * Geopointage
 */

public class ContactActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FloatingActionButton floatingActionButton;
    private ImageView arrowBack;
    private ImageView done;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

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
    private String mMessage;
    private User chef;
    private ArrayList<User> arrayListCollaborateur = new ArrayList<>();
    private Chantier mChantier;

    public void setChef(User chef) {
        this.chef = chef;
    }

    public int getChoose() {
        return choose;
    }

    public void setArrayListCollaborateur(ArrayList<User> arrayListCollaborateur) {
        this.arrayListCollaborateur = arrayListCollaborateur;
    }
    public void addArrayListCollaborateur(User user) {
        this.arrayListCollaborateur.add(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Intent intent = getIntent();
        if (intent != null) {
            choose = intent.getIntExtra("choose", 1);
            mChantier = intent.getParcelableExtra("chantier_extra");
            mMessage = intent.getStringExtra("message");
            if (mMessage != null && !mMessage.isEmpty())
            {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, mMessage, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), ContactActivity.this);
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
                    i.putExtra("user_extra", arrayListCollaborateur);
                    i.putExtra("chantier_extra", mChantier);
                    startActivity(i);
                } else if (choose == 1) {
                    Intent i = new Intent(ContactActivity.this, ChooseActivity.class);
                    //i.putExtra("user_extra", mUser);
                    startActivity(i);
                }
            }
        });

        done = (ImageView) findViewById(R.id.activity_contact_imageview_done);
        if (choose == 0)
            done.setVisibility(View.GONE);
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
                        i.putExtra("chantier_extra", mChantier);
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
        mPager.setCurrentItem(1);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.activityResumed();
    }

   /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mContactAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mContactAdapter.filter(newText);
                return true;
            }
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

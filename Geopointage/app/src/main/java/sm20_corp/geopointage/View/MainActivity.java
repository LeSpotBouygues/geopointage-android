package sm20_corp.geopointage.View;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm20_corp.geopointage.Adapter.ContactAdapter;
import sm20_corp.geopointage.Api.ApiManager;
import sm20_corp.geopointage.Api.ModelApi.Message;
import sm20_corp.geopointage.Model.Chantier;
import sm20_corp.geopointage.Model.Tach;
import sm20_corp.geopointage.Model.TimeStamp;
import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.Module.RecyclerItemClickListener;
import sm20_corp.geopointage.Module.TimeStampManager;
import sm20_corp.geopointage.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ContactAdapter contactAdapter;
    private ArrayList<User> arrayListUser;
    private Button guideButton;
    private Button selectAdressButton;
    private ArrayList<User> tmp = null;
    private Chantier mChantier;
    private TextView addressTextView;
    private TextView mIotpTextView;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionMenu mFloatingActionMenu;
    private FloatingActionButton mFloatingActionButtonPlay;
    private FloatingActionButton mFloatingActionButtonPause;
    private FloatingActionButton mFloatingActionButtonStop;
    private Boolean pause = false;
    private String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        arrayListUser = new ArrayList<User>();
        arrayListUser.add(DatabaseHandler.getInstance(this).getChef());
        Intent intent = getIntent();
        if (intent != null) {
            tmp = intent.getParcelableArrayListExtra("user_extra");
            if (tmp != null) {
                System.out.println("tmp size =" + tmp.size());
                arrayListUser.addAll(tmp);

                for (int i = 0; i < arrayListUser.size(); i++) {
                    if (i != 0 && arrayListUser.get(i).getId().equals(DatabaseHandler.getInstance(this).getChef().getId())) {
                        System.out.println("remove " + i);
                        arrayListUser.remove(i);
                    }
                }
            }
            mChantier = intent.getParcelableExtra("chantier_extra");
            mMessage = intent.getStringExtra("message");
            if (mMessage != null && !mMessage.isEmpty()) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, mMessage, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        } else
            System.out.println("intent null");

        try {
            syncro();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addressTextView = (TextView) findViewById(R.id.content_main_textview_adresse);
        mIotpTextView = (TextView) findViewById(R.id.content_main_textview_iotp);
        if (mChantier != null) {
            addressTextView.setText(mChantier.getAddress());
            mIotpTextView.setText(mChantier.getIotp());
        }
        //DatabaseHandler.getInstance(getApplicationContext()).removeCuentTach();
        if (DatabaseHandler.getInstance(getApplicationContext()).getCurentTach() != null) {
            final AlertDialog dialog;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final View dialogView = inflater.inflate(R.layout.dialog_main_activity, null);
            alertDialogBuilder.setView(dialogView);
            alertDialogBuilder.setTitle(getString(R.string.tach_found));

            TextView address = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_adress);
            TextView iotp = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_iotp);
            TextView collaborateur = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_collaborateur);
            TextView comment = (TextView) dialogView.findViewById(R.id.dialog_main_activity_edittext_comment);
            comment.setVisibility(View.GONE);

            Tach tach = DatabaseHandler.getInstance(getApplicationContext()).getCurentTach();
            mChantier = new Chantier();
            mChantier.setAddress(tach.getAddress());
            mChantier.setIotp(tach.getIotp());
            arrayListUser = parseStr(tach.getIdCollaborateur());
            System.out.println("after parse str" + arrayListUser.toString());

            address.setText(mChantier.getAddress());
            iotp.setText(getString(R.string.iotp) + "  " + mChantier.getIotp());
            String tmp;
            String collaborateur2 = "";
            // System.out.println("size = " + arrayListUser.size());
            for (int i = 0; i < arrayListUser.size(); i++) {
                tmp = arrayListUser.get(i).getLastName() + " " + arrayListUser.get(i).getFirstName() + " " + arrayListUser.get(i).getId() + "\n";
                System.out.println("tmp = " + tmp);
                collaborateur2 = collaborateur2.concat(tmp);
            }
            collaborateur.setText(collaborateur2);

            final String finalCollaborateur = collaborateur2;
            alertDialogBuilder.setPositiveButton(getString(R.string.resume), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    mFloatingActionMenu.setVisibility(View.VISIBLE);
                    mFloatingActionButtonPlay.setVisibility(View.GONE);
                    createNotification("Debut à");
                    TimeStamp timeStamp = new TimeStamp("pause", System.currentTimeMillis() / 1000);
                    DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);
                    timeStamp = new TimeStamp("play", System.currentTimeMillis() / 1000);
                    DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);
                }

            });
            alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    DatabaseHandler.getInstance(getApplicationContext()).removeTimeStamp();
                    DatabaseHandler.getInstance(getApplicationContext()).removeCuentTach();
                    mChantier = null;
                }

            });

            dialog = alertDialogBuilder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                }
            });
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.app_bar_main_fab_menu);

        mFloatingActionButtonPlay = (FloatingActionButton) findViewById(R.id.content_main_fab_play);
        mFloatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mChantier != null
                        && !mChantier.getAddress().isEmpty()
                        && !mChantier.getIotp().isEmpty()
                        && arrayListUser != null
                        && !arrayListUser.isEmpty()) {
                    mFloatingActionMenu.setVisibility(View.VISIBLE);
                    mFloatingActionButtonPlay.setVisibility(View.GONE);
                    createNotification("Debut à");

                    TimeStamp timeStamp = new TimeStamp("play", System.currentTimeMillis() / 1000);
                    DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);
                    String tmp = "";
                    for (int i = 0; i < arrayListUser.size(); i++) {
                        tmp = tmp.concat(arrayListUser.get(i).getId() + "|");
                    }
                    if (!tmp.isEmpty())
                        tmp = tmp.substring(0, tmp.length() - 1);
                    System.out.println("tmp = " + tmp);
                    Tach tach = new Tach(mChantier.getAddress(), mChantier.getIotp(), tmp, "-1");
                    DatabaseHandler.getInstance(getApplicationContext()).addCurentTach(tach);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.no_adresse_select), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


        mFloatingActionButtonPause = (FloatingActionButton) findViewById(R.id.content_main_fab_pause);
        mFloatingActionButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionMenu.close(true);
                if (!pause) {
                    mFloatingActionButtonPause.setImageResource(R.drawable.ic_fab_play);
                    createNotification("Pause à");
                    pause = true;
                    TimeStamp timeStamp = new TimeStamp("pause", System.currentTimeMillis() / 1000);
                    DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);

                } else {
                    mFloatingActionButtonPause.setImageResource(R.drawable.ic_fab_pause);
                    createNotification("Debut à");
                    TimeStamp timeStamp = new TimeStamp("play", System.currentTimeMillis() / 1000);
                    DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);
                    pause = false;
                }
            }
        });

        mFloatingActionButtonStop = (FloatingActionButton) findViewById(R.id.content_main_fab_stop);
        mFloatingActionButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog dialog;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final View dialogView = inflater.inflate(R.layout.dialog_main_activity, null);
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setTitle(getString(R.string.resum_of_activity));

                TextView address = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_adress);
                TextView iotp = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_iotp);
                TextView collaborateur = (TextView) dialogView.findViewById(R.id.dialog_main_activity_textview_collaborateur);
                final TextView comment = (TextView) dialogView.findViewById(R.id.dialog_main_activity_edittext_comment);

                address.setText(mChantier.getAddress());
                iotp.setText(getString(R.string.iotp) + "  " + mChantier.getIotp());
                String tmp;
                String collaborateur2 = "";
                for (int i = 0; i < arrayListUser.size(); i++) {
                    tmp = arrayListUser.get(i).getLastName() + " " + arrayListUser.get(i).getFirstName() + " " + arrayListUser.get(i).getId() + "\n";
                    collaborateur2 = collaborateur2.concat(tmp);
                }
                collaborateur.setText(collaborateur2);

                final String finalCollaborateur = collaborateur2;
                alertDialogBuilder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.out.println("address = " + mChantier.getAddress());
                        System.out.println("iotp = " + mChantier.getIotp());
                        System.out.println("collaborateur = " + finalCollaborateur);
                        System.out.println("comment = " + comment.getText());
                        if (!comment.getText().toString().isEmpty())
                            sendComment(comment.getText().toString(), arrayListUser.get(0).getLastName(), arrayListUser.get(0).getFirstName(), mChantier.getIotp());

                        mFloatingActionMenu.setVisibility(View.GONE);
                        mFloatingActionButtonPlay.setVisibility(View.VISIBLE);
                        mFloatingActionMenu.close(true);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(1);
                        TimeStamp timeStamp = new TimeStamp("stop", System.currentTimeMillis() / 1000);
                        DatabaseHandler.getInstance(getApplicationContext()).addTime(timeStamp);
                        long resulat = TimeStampManager.timeStampToSecond(DatabaseHandler.getInstance(getApplicationContext()).getAllTimeStamp());
                        System.out.println("result  =  " + resulat);
                        DatabaseHandler.getInstance(getApplicationContext()).removeTimeStamp();

                        Tach tach = DatabaseHandler.getInstance(getApplicationContext()).getCurentTach();
                        tach.setTimeStamp(String.valueOf(resulat));

                        DatabaseHandler.getInstance(getApplicationContext()).addTach(tach);
                        DatabaseHandler.getInstance(getApplicationContext()).removeCuentTach();
                        try {
                            syncro();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

                alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                dialog = alertDialogBuilder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                });
                dialog.show();
            }
        });

        guideButton = (Button) findViewById(R.id.content_main_button_guider);
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChantier != null) {
                    if (!mChantier.getAddress().isEmpty()) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mChantier.getAddress());
                        gmmIntentUri = Uri.parse("geo:0,0?q=" +mChantier.getAddress());

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        //mapIntent.setPackage("com.google.android.apps.maps");

                       /*mapIntent.setData(Uri.parse("geo:" + mChantier.getLat() + "," +
                               mChantier.getLng() + "?q=" + getStreet() + "+" +
                                getHousenumber() + "+" + getPostalcode() + "+" +
                                getCity()));*/
                        startActivity(mapIntent);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.no_adresse_select), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.no_adresse_select), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        selectAdressButton = (Button) findViewById(R.id.content_main_button_select_adress);
        selectAdressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.content_main_recycler_view_colaborateur);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        contactAdapter = new ContactAdapter(arrayListUser, 1, this);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println("pos = " + position);
                        System.out.println("pos = " + arrayListUser.get(position).toString());
                        if (position == arrayListUser.size() - 1) {
                            Intent i = new Intent(MainActivity.this, ContactActivity.class);
                            i.putExtra("choose", 0);
                            i.putExtra("chantier_extra", mChantier);
                            i.putExtra("arrylist",arrayListUser);
                            startActivity(i);
                        }
                    }
                })
        );

        mRecyclerView.setAdapter(contactAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView

                System.out.println("pos = " + viewHolder.getAdapterPosition());
                System.out.println("size = " + arrayListUser.size());

                contactAdapter.remove(viewHolder.getAdapterPosition());
                System.out.println("name = " + arrayListUser.get(viewHolder.getAdapterPosition()).getLastName());
                arrayListUser.remove(viewHolder.getAdapterPosition());
                contactAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                contactAdapter.notifyItemRangeChanged(0, contactAdapter.getItemCount());
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition() == 0 || viewHolder.getAdapterPosition() == arrayListUser.size() - 1 || DatabaseHandler.getInstance(getApplicationContext()).getCurentTach() != null)
                    return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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

        if (id == R.id.nav_logout) {
            System.out.println("boolen = " + DatabaseHandler.getInstance(this).removeChef());
            DatabaseHandler.getInstance(this).removeTimeStamp();
            DatabaseHandler.getInstance(this).removeCuentTach();
            Intent i = new Intent(MainActivity.this, ChooseActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createNotification(String title) {
        Bitmap icon = ((BitmapDrawable) getResources().getDrawable(R.drawable.logo)).getBitmap();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        title = title.concat(" " + currentDateandTime);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setLargeIcon(icon)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setOngoing(true)
                        .setContentText(title);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
    }

    public ArrayList<User> parseStr(String str) {
        StringTokenizer parser = new StringTokenizer(str, "|", true);
        String token = null;
        ArrayList<User> arrayListTmp = new ArrayList<User>();
        User tmp = null;
        while (parser.hasMoreTokens()) {
            token = parser.nextToken("|");
            System.out.println("parse  = " + parser);
            System.out.println("token  = " + token);

            tmp = DatabaseHandler.getInstance(getApplicationContext()).getUser("", "", token, 0);
            System.out.println("tmp user = " + tmp.toString());
            arrayListTmp.add(tmp);
            parser.nextToken("|");
        }
        return arrayListTmp;
    }

    // [{login: 'id001', address: '1 rue blabla', date:'10-12-2016', numberOfHours: '2', worker: { firstName:'Samuel', lastName: 'JOSET'} }, {login: 'id002', address: '6 rue ha', date:'2-10-2006', numberOfHours: '4', worker: { firstName:'David', lastName: 'Haga'} }]
    //[{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"2","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"3","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"1","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"3","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"1","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"1","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"1","worker":{"firstName":"redouane","LastName":"Messara"}},{"login":"1234567890","address":"27 Rue Serpente, 92700 Colombes","date":"18-12-2016","numberOfHours":"2","worker":{"firstName":"redouane","LastName":"Messara"}}]

    public void syncro() throws JSONException {
        JSONArray body = new JSONArray();
        JSONObject tmp = new JSONObject();
        JSONArray tmpWorker = new JSONArray();
        JSONObject  tmpUser = new JSONObject();

        System.out.println("parse str");

        ArrayList<Tach> arrayListTach = DatabaseHandler.getInstance(getApplicationContext()).getAllTach();
        if (arrayListTach != null && arrayListTach.size() > 0) {
            Date date;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateandTime = sdf.format(new Date());
            for (int i = 0; i < arrayListTach.size(); i++) {
                ArrayList<User> arrayListUser = parseStr(arrayListTach.get(i).getIdCollaborateur());
                tmp.put("login", arrayListTach.get(i).getIotp());
                tmp.put("address", arrayListTach.get(i).getAddress());
                tmp.put("date", currentDateandTime);
                tmp.put("numberOfHours", arrayListTach.get(i).getTimeStamp());
                for(int y = 0; y < arrayListUser.size() ; y++) {
                    tmpUser.put("firstName", arrayListUser.get(y).getFirstName());
                    tmpUser.put("lastName", arrayListUser.get(y).getLastName());
                    tmpWorker.put(tmpUser);
                    tmpUser = new JSONObject();
                }
                tmp.put("workers", tmpWorker);
                body.put(tmp);
                tmp = new JSONObject();
                tmpWorker = new JSONArray();
            }
            System.out.println("body = " + body.toString());
            sendTach(body.toString());
        }
    }

    private void sendComment(String comment, String lastName, String firstName, String iotp) {
        ApiManager.get().sendComment(iotp, comment, firstName, lastName).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                System.out.println("code = " + response.raw().toString());
                if (response.isSuccessful()) {
                    System.out.println("comment = " + response.body().toString());

                } else {
                    System.out.println("Not sucess SendComment = " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.error_send_comment), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                System.out.println("On failure sendComment : " + t.getMessage());
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void sendTach(String body) {
        ApiManager.get().sendTach(body).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                System.out.println("code = " + response.raw().toString());
                if (response.isSuccessful()) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.send_tach_ok), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    DatabaseHandler.getInstance(getApplicationContext()).removeCuentTach();
                    DatabaseHandler.getInstance(getApplicationContext()).removeTach();
                    DatabaseHandler.getInstance(getApplicationContext()).removeTimeStamp();
                } else {
                    System.out.println("not sucess SendTach = " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.error_send_tach), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                System.out.println("On failure sendTach : " + t.getMessage());
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

}

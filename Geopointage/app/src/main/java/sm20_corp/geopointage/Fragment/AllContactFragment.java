package sm20_corp.geopointage.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm20_corp.geopointage.Adapter.ContactAdapter;
import sm20_corp.geopointage.Api.ApiManager;
import sm20_corp.geopointage.Api.ModelApi.UserList;
import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.Module.RecyclerItemClickListener;
import sm20_corp.geopointage.R;
import sm20_corp.geopointage.View.ContactActivity;

import static android.app.Activity.RESULT_OK;


/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class AllContactFragment extends Fragment {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;
    private ArrayList<User> mArrayListUser;
    private ImageView micId;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.all_contact_fragment, container, false);

        getUSerList();
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_contact_fragment_recycler_view_contact_all);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mArrayListUser = DatabaseHandler.getInstance(getActivity().getApplicationContext()).getAllUser();
        mContactAdapter = new ContactAdapter(mArrayListUser, 0, getActivity());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        User user;
                        //0 = main activity
                        //1 = choose activity
                        if (((ContactActivity) getActivity()).getChoose() == 0) {
                            ((ContactActivity) getActivity()).setArrayListCollaborateur(mContactAdapter.getSelectedItem(position, 0));
                            DatabaseHandler.getInstance(getActivity()).addRecent(mContactAdapter.getUserSelected(position));

                            //getActivity().get
                            //RecentContactFragment.getContactAdapter()
                        }
                        else {
                            System.out.println("setchef");
                            user = mContactAdapter.getSelectedItem(position, 1).get(0);
                            if (user != null)
                                ((ContactActivity) getActivity()).setChef(user);

                        }
                        mContactAdapter.notifyDataSetChanged();

                    }
                })
        );
        mRecyclerView.setAdapter(mContactAdapter);


        micId = (ImageView) getActivity().findViewById(R.id.activity_contact_imageview_mic_lastname);
        micId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.name_or_id));
                startActivityForResult(intent, 1);
            }
        });


        return rootView;
    }

   /* public void noitfyContactAdpater()
    {
        contactAdapter.notifyDataSetChanged();
    }*/

    // newInstance constructor for creating fragment with arguments
    public static AllContactFragment newInstance() {
        AllContactFragment allContactFragment = new AllContactFragment();
        //Bundle args = new Bundle();
        //args.putInt("someInt", page);
        //  args.putString("someTitle", title);
        // recentContactFragment.setArguments(args);
        return allContactFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUSerList() {
        ApiManager.get().getUser().enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                System.out.println("code = " + response.raw().toString());

                if (response.isSuccessful()) {
                    System.out.println("user = " + response.body().getUser().toString());
                    ArrayList<User> tmp = response.body().getUser();
                    for (int i = 0; i < tmp.size(); i++) {
                        System.out.println("user = " + tmp.get(i).toString());
                        tmp.get(i).setVisibility(true);
                        if (DatabaseHandler.getInstance(getContext()).getUser("", "", tmp.get(i).getId(), 0) == null) {
                            System.out.println("user add");
                            DatabaseHandler.getInstance(getContext()).addUser(tmp.get(i));
                            //((ContactActivity) getActivity()).addArrayListCollaborateur(tmp.get(i));
                            mContactAdapter.addUser(tmp.get(i));
                        } else
                            System.out.println("user pas ajouter");
                        //mContactAdapter.notifyDataSetChanged();
                        mContactAdapter.notifyDataSetChanged();
                    }
                } else
                    System.out.println("not sucess getUser = " + response.code());
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                System.out.println("On failure getUSer : " + t.getMessage());
            }
        });
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
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
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            searchView.setQuery(matches.get(0),true);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

}

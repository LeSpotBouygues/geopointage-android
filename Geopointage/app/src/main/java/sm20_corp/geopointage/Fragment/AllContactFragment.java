package sm20_corp.geopointage.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sm20_corp.geopointage.Adapter.ContactAdapter;
import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.Module.DatabaseHandler;
import sm20_corp.geopointage.Module.RecyclerItemClickListener;
import sm20_corp.geopointage.R;
import sm20_corp.geopointage.View.ContactActivity;


/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class AllContactFragment extends Fragment {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.all_contact_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_contact_fragment_recycler_view_contact_all);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        contactAdapter = new ContactAdapter(DatabaseHandler.getInstance(getActivity().getApplicationContext()).getAllUser(), 0, getActivity());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        User user;
                        //0 = main activity
                        //1 = chpse activity
                        if (((ContactActivity) getActivity()).getChoose() == 0)
                            ((ContactActivity) getActivity()).setArrayListCollaborateur(contactAdapter.getSelectedItem(position, 0));
                        else {
                            user = contactAdapter.getSelectedItem(position, 1).get(0);
                            ((ContactActivity) getActivity()).setChef(user);
                        }

                        /*Intent i = new Intent(getActivity(), MainActivity.class);
                        i.putExtra("userId", user.getId());
                        startActivity(i);
                        startActivity(i);*/


                        contactAdapter.notifyDataSetChanged();

                    }
                })
        );
        mRecyclerView.setAdapter(contactAdapter);


        return rootView;
    }


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
}

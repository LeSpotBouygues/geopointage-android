package sm20_corp.geopointage.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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

public class RecentContactFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;
    private ArrayList<User> arrayList = new ArrayList<User>();

    public ContactAdapter getContactAdapter() {
        return mContactAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        // System.out.println("hey");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.recent_contact_fragment, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recent_contact_fragment_recycler_view_contact_recent);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        arrayList = DatabaseHandler.getInstance(getActivity()).getAllRecent();
        if (arrayList == null) {
            arrayList = new ArrayList<User>();
        }

        mContactAdapter = new ContactAdapter(arrayList, 0, getActivity());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        User user;
                        //0 = main activity
                        //1 = choose activity
                        if (((ContactActivity) getActivity()).getChoose() == 0) {
                            ((ContactActivity) getActivity()).setArrayListCollaborateur(mContactAdapter.getSelectedItem(position, 0));
                            //DatabaseHandler.getInstance(getActivity()).addRecent(mContactAdapter.getUserSelected(position));

                            //getActivity().get
                            //RecentContactFragment.getContactAdapter()
                        } else {
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
        return rootView;
    }


    // newInstance constructor for creating fragment with arguments
    public static RecentContactFragment newInstance() {
        RecentContactFragment recentContactFragment = new RecentContactFragment();
        //Bundle args = new Bundle();
        //args.putInt("someInt", page);
        //  args.putString("someTitle", title);
        // recentContactFragment.setArguments(args);
        return recentContactFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}

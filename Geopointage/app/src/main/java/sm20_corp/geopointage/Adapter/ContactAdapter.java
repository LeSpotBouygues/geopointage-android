package sm20_corp.geopointage.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.User;
import sm20_corp.geopointage.R;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {
    private ArrayList<User> mDatasetAdapter;
    private ArrayList<User> mDatasetAdapterCopy;
    private Context mContext;
    private int mOption = -1;
    private ArrayList<Integer> posSelected = new ArrayList<Integer>();
    ArrayList<User> users = new ArrayList<User>();
    // Provide a suitable constructor (depends on the kind of dataset)
    //option 0 = contactAtivity
    //       1 = mainactivity

    public ContactAdapter(ArrayList<User> myDataset, int option, Context context) {
        mContext = context;
        mOption = option;
        if (myDataset != null) {
            if (mOption == 1)
            {
                User tmp = new User("","","");
                myDataset.add(tmp);
            }
            mDatasetAdapter = new ArrayList<>(myDataset);
            mDatasetAdapterCopy = new ArrayList<>(myDataset);
            // this.filterDataset();
        }
        else
            mDatasetAdapter = new ArrayList<>();
    }



    public ArrayList<User> getSelectedItem(int position , int option) {
        String idSelected = mDatasetAdapter.get(position).getId();

        int pos = 0;
        for (int i = 0; i < mDatasetAdapterCopy.size(); i++) {
            if (mDatasetAdapterCopy.get(i).getId().equals(idSelected)) {
                pos = i;
                break;
            }
        }
        boolean find = false;
        int i = 0;
        if (option == 1)
        {
            posSelected.clear();
        }
        while (!posSelected.isEmpty() && i < posSelected.size()) {
            if (posSelected.get(i) == pos) {
                posSelected.remove(i);
                users.remove(i);
                find = true;
            }
            i++;
        }
        if (!find) {
            posSelected.add(pos);
            users.add(mDatasetAdapterCopy.get(pos));
           // System.out.println("add = " + pos);
        }

        System.out.println("user size = " + users.size());
        int y = 0;
        while (!users.isEmpty() && y < users.size())
        {
            System.out.println("user last name = " + users.get(y).getLastName());
            y++;
        }
        return (users);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contact, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.date.setText(mDataset.get(position).getDate());




            holder.lastName.setText(mDatasetAdapterCopy.get(position).getLastName());
            holder.firstNname.setText(mDatasetAdapterCopy.get(position).getFirstName());
            holder.id.setText(mDatasetAdapterCopy.get(position).getId());


            if (mOption == 1 && position == 0) {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(R.string.chef_chantier);
            } else if (mOption == 1 && position == 1) {
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(R.string.collaborateur);
            } else
                holder.title.setVisibility(View.GONE);
            holder.cardViewUser.setCardBackgroundColor(mContext.getColor(R.color.White));


            int i = 0;
            while (!posSelected.isEmpty() && i < posSelected.size()) {
                if (posSelected.get(i) == position) {
                    holder.cardViewUser.setCardBackgroundColor(mContext.getColor(R.color.OrangeLight));
                }
                i++;

            }
      if (getItemCount() == position + 1 && mOption == 1){
            holder.linearLayoutContact.setVisibility(View.GONE);
            holder.linearLayoutAdd.setVisibility(View.VISIBLE);
        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetAdapter.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView lastName;
        public TextView firstNname;
        public TextView id;
        public CardView cardViewUser;
        public LinearLayout linearLayoutContact;
        public LinearLayout linearLayoutAdd;


        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.adapter_contact_textView_title);
            lastName = (TextView) v.findViewById(R.id.adapter_contact_textView_last_name);
            firstNname = (TextView) v.findViewById(R.id.adapter_contact_textView_first_name);
            id = (TextView) v.findViewById(R.id.adapter_contact_textView_id);
            cardViewUser = (CardView) v.findViewById(R.id.adapter_contact_card_view_list_user);
            linearLayoutContact = (LinearLayout) v.findViewById(R.id.adapter_contact_linearlayout_contact);
            linearLayoutAdd = (LinearLayout) v.findViewById(R.id.adapter_contact_linearlayout_add);

        }
    }

   /* public void filterDataset() {
        Collections.sort(mDatasetAdapter, new Comparator<User>() {
            public int compare(User o1, User o2) {


                return date2.compareTo(date1);

            }
        });
    }*/

    public boolean checkProductFilter(User item, String text) {
        /*for (int i = 0; i < item.getProductArrayList().size(); i++) {
            if (item.getProductArrayList().get(i).getName().toLowerCase().contains(text)) {
                return (true);
            }
        }*/
        return (false);
    }

 /*   public void filter(String text) {

        if (text.isEmpty()) {
            for (User item : mDatasetAdapter) {
                item.setVisibility(true);
            }
        } else {
            text = text.toLowerCase();
            for (Receipt item : mDatasetAdapter) {
                if (item.getCompanyName().toLowerCase().contains(text) || item.getDay().toLowerCase().contains(text) || item.getPrice().toLowerCase().contains(text)
                        || item.getPrice().concat(currencyMap.get(item.getCurrency())).toLowerCase().contains(text)) {
                    item.setVisibility(true);
                } else if (checkProductFilter(item, text)) {
                    item.setVisibility(true);
                } else {
                    item.setVisibility(false);
                }
            }
        }
        this.notifyDataSetChanged();
    }*/

   /* public void filterCheckbox(boolean common, boolean hobbies, boolean food, boolean misc, boolean health, boolean bill, boolean important) {
        for (User item : mDatasetAdapter) {
            if ((!common && item.getTag() == 0) || (!food && item.getTag() == 1) || (!hobbies && item.getTag() == 4) ||
                    (!misc && item.getTag() == 2) || (!health && item.getTag() == 5) || (!bill && item.getTag() == 3)||
                    (!important && item.getImportant() == 1))
                item.setVisibility(false);
            else
                item.setVisibility(true);
        }
        this.notifyDataSetChanged();
    }*/




   /* public void mDatasetAdapterIsNull(ArrayList<Receipt> dataset) {
        if (mDatasetAdapter.isEmpty()) {
            mDatasetAdapter = new ArrayList<>(dataset);
            mDatasetAdapterCopy = new ArrayList<>(dataset);
            this.notifyDataSetChanged();

        }
    }*/

}

package sm20_corp.geopointage.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sm20_corp.geopointage.Fragment.AllContactFragment;
import sm20_corp.geopointage.Fragment.RecentContactFragment;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return RecentContactFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return AllContactFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
            return ("Recent collaborateur");
        else
            return ("Tous les collaborateurs");

    }

}

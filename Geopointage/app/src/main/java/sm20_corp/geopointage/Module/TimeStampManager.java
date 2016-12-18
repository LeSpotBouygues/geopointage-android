package sm20_corp.geopointage.Module;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.TimeStamp;

/**
 * Created by gun on 17/12/2016.
 * Geopointage
 */

public class TimeStampManager {

    public static long timeStampToSecond(ArrayList<TimeStamp> arrayListTimeStamp) {
        int i = 0;
        long tmp = 0;
        for (i = 0; i < arrayListTimeStamp.size(); i++) {
            if (arrayListTimeStamp.size() > i + 1) {
                if (arrayListTimeStamp.get(i).getStatus().equals("pause") && arrayListTimeStamp.get(i + 1).getStatus().equals("stop"))
                    return tmp;
                if (arrayListTimeStamp.get(i + 1).getStatus().equals("pause") || arrayListTimeStamp.get(i + 1).getStatus().equals("stop")) {
                    tmp = tmp + arrayListTimeStamp.get(i + 1).getTimeStamp() - arrayListTimeStamp.get(i).getTimeStamp();
                }
            }
        }
        return tmp;
    }
}

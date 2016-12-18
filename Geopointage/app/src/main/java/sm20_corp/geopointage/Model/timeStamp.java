package sm20_corp.geopointage.Model;

/**
 * Created by gun on 17/12/2016.
 * Geopointage
 */

public class TimeStamp {
    private String Status;
    private long timeStamp;

    public TimeStamp(String status, long timeStamp) {
        Status = status;
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "Status='" + Status + '\'' +
                ", TimeStamp=" + timeStamp +
                '}';
    }
}

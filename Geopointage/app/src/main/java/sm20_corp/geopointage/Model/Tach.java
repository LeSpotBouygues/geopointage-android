package sm20_corp.geopointage.Model;

/**
 * Created by gun on 17/12/2016.
 * Geopointage
 */

public class Tach {
    private String address;
    private String iotp;
    private String IdCollaborateur;
    private String timeStamp;

    public Tach(String address, String iotp, String idCollaborateur, String timeStamp) {
        this.address = address;
        this.iotp = iotp;
        IdCollaborateur = idCollaborateur;
        this.timeStamp = timeStamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIotp() {
        return iotp;
    }

    public void setIotp(String iotp) {
        this.iotp = iotp;
    }

    public String getIdCollaborateur() {
        return IdCollaborateur;
    }

    public void setIdCollaborateur(String idCollaborateur) {
        IdCollaborateur = idCollaborateur;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Tach{" +
                "address='" + address + '\'' +
                ", iotp='" + iotp + '\'' +
                ", IdCollaborateur='" + IdCollaborateur + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

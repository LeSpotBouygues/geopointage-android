package sm20_corp.geopointage.Api.ModelApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.Chantier;

/**
 * Created by gun on 07/12/2016.
 * Geopointage
 */

public class ChantierList {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private ArrayList<Chantier> mChantier;

    public ChantierList() {
    }

    public ChantierList(String status, ArrayList<Chantier> chantiers) {
        this.status = status;
        this.mChantier = chantiers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Chantier> getChantier() {
        return mChantier;
    }

    public void setChantier(ArrayList<Chantier> chantier) {
        this.mChantier = chantier;
    }


}

package modeles;

import java.io.InputStream;

public class ShowUsersModel {

    String MILITARYID, IDNUMBER, NAME, RANK, PHONNUMBER, SPECIALTY, NOTE;
    int SQUNCE;
    InputStream image;

    public ShowUsersModel(String MILITARYID, String IDNUMBER, String NAME, String RANK, String PHONNUMBER, String SPECIALTY, String NOTE, int SQUNCE, InputStream image) {
        this.MILITARYID = MILITARYID;
        this.IDNUMBER = IDNUMBER;
        this.NAME = NAME;
        this.RANK = RANK;
        this.PHONNUMBER = PHONNUMBER;
        this.SPECIALTY = SPECIALTY;
        this.NOTE = NOTE;
        this.SQUNCE = SQUNCE;
        this.image = image;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public ShowUsersModel() {

    }

    public int getSQUNCE() {
        return SQUNCE;
    }

    public void setSQUNCE(int SQUNCE) {
        this.SQUNCE = SQUNCE;
    }

    public String getMILITARYID() {
        return MILITARYID;
    }

    public void setMILITARYID(String MILITARYID) {
        this.MILITARYID = MILITARYID;
    }

    public String getIDNUMBER() {
        return IDNUMBER;
    }

    public void setIDNUMBER(String IDNUMBER) {
        this.IDNUMBER = IDNUMBER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getPHONNUMBER() {
        return PHONNUMBER;
    }

    public void setPHONNUMBER(String PHONNUMBER) {
        this.PHONNUMBER = PHONNUMBER;
    }

    public String getSPECIALTY() {
        return SPECIALTY;
    }

    public void setSPECIALTY(String SPECIALTY) {
        this.SPECIALTY = SPECIALTY;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }

}

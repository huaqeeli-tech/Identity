
package modeles;


public class PersonalImagesModel {
    String ID, militaryId,documentName, documentType, expirationDate;
    int squens;

    public PersonalImagesModel() {
    }

    public PersonalImagesModel(String ID, String militaryId, String documentName, String documentType, String expirationDate, int squens) {
        this.ID = ID;
        this.militaryId = militaryId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.expirationDate = expirationDate;
        this.squens = squens;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMilitaryId() {
        return militaryId;
    }

    public void setMilitaryId(String militaryId) {
        this.militaryId = militaryId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getSquens() {
        return squens;
    }

    public void setSquens(int squens) {
        this.squens = squens;
    }
    
}

package modeles;

public class DocumentFileModel {

    String ID, militaryId, documentTopic, documentType;
    int squens;

    public DocumentFileModel() {
    }

    public DocumentFileModel(String ID, String militaryId, String documentTopic, String documentType, int squens) {
        this.ID = ID;
        this.militaryId = militaryId;
        this.documentTopic = documentTopic;
        this.documentType = documentType;
        this.squens = squens;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public String getDocumentTopic() {
        return documentTopic;
    }

    public void setDocumentTopic(String documentTopic) {
        this.documentTopic = documentTopic;
    }

    public int getSquens() {
        return squens;
    }

    public void setSquens(int squens) {
        this.squens = squens;
    }

}

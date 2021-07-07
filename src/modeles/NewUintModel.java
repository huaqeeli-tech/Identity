
package modeles;


public class NewUintModel {
    String uintId,uintName;

    public NewUintModel(String uintId, String uintName) {
        this.uintId = uintId;
        this.uintName = uintName;
    }

    public String getUintId() {
        return uintId;
    }

    public void setUintId(String uintId) {
        this.uintId = uintId;
    }

    public String getUintName() {
        return uintName;
    }

    public void setUintName(String uintName) {
        this.uintName = uintName;
    }

    @Override
    public String toString() {
        return "NewUintModel{" + "uintId=" + uintId + ", uintName=" + uintName + '}';
    }
    
}

package modeles;

public class PersonalModel {

    private String militaryId;
    private String rank;
    private String name;
    private String personalid;
    private String unit;
    private String birthDate;
    private String socialStatus;
    private String weight;
    private String lenght;

    public PersonalModel(String militaryId, String rank, String name, String personalid, String unit, String birthDate, String socialStatus, String weight, String lenght) {
        this.militaryId = militaryId;
        this.rank = rank;
        this.name = name;
        this.personalid = personalid;
        this.unit = unit;
        this.birthDate = birthDate;
        this.socialStatus = socialStatus;
        this.weight = weight;
        this.lenght = lenght;
    }

    public PersonalModel(String militaryId, String rank, String name, String personalid, String unit) {
        this.militaryId = militaryId;
        this.rank = rank;
        this.name = name;
        this.personalid = personalid;
        this.unit = unit;
    }

    

    public String getMilitaryId() {
        return militaryId;
    }

    public void setMilitaryId(String militaryId) {
        this.militaryId = militaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPersonalid() {
        return personalid;
    }

    public void setPersonalid(String personalid) {
        this.personalid = personalid;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    @Override
    public String toString() {
        return "PersonalModel{" + "militaryId=" + militaryId + ", name=" + name + ", rank=" + rank + ", unit=" + unit + ", personalid=" + personalid + ", birthDate=" + birthDate + ", socialStatus=" + socialStatus + ", weight=" + weight + ", lenght=" + lenght + '}';
    }

}

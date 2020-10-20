package com.huaqeeli.training.models;


public class PersonalModel {
    
    private String militaryId;
    private String name;
    private String rank;
    private String unit;

    public PersonalModel(String militaryId, String name, String rank, String unit) {
        this.militaryId = militaryId;
        this.name = name;
        this.rank = rank;
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

    @Override
    public String toString() {
        return "PersonalModel{" + ", militaryId=" + militaryId + ", name=" + name + ", rank=" + rank + ", unit=" + unit + '}';
    }

    
}

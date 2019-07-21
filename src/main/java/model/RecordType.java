package model;

public enum RecordType{

    INCOME("Income"), COST("Outcome"), MOVEMENT("Movement");

    private String translation;

    @Override
    public String toString() {
        return translation;
    }

    RecordType(String translation){ this.translation = translation;}

    public static RecordType getRecordTypeByCode(int recordType){
        if (recordType==0) return COST;
        else if (recordType ==1) return INCOME;
        else return MOVEMENT;
    }

}

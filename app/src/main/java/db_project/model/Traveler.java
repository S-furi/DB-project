package db_project.model;

// Viaggiatore
public class Traveler {

    private final String travelerCode;
    private final String firstName;
    private final String lastName;
    private final int phone;
    private final String residence;
    private final boolean isGroup;


    public Traveler(String travelerCode, String firstName, String lastName, int phone, String residence, boolean isGroup){
        this.travelerCode = travelerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.residence = residence;
        this.isGroup = isGroup;
    }


    public String getTravelerCode() {
        return travelerCode;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public int getPhone() {
        return phone;
    }


    public String getResidence() {
        return residence;
    }


    public boolean isGroup() {
        return isGroup;
    }

    

}

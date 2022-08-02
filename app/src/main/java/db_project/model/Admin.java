package db_project.model;

import java.util.Date;
// Amministratore
public class Admin {
    private final int id;
    private final Date contractYear;
    private final String firstName;
    private final String lastName;
    private final int telephone;
    private final String email;
    private final String residence;

    public Admin(
        final int id,
        final Date contractYear,
        final String firstName,
        final String lastName,
        final int telephone,
        final String email,
        final String residence
    ) {
        this.id = id;
        this.contractYear = contractYear;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.residence = residence;
    }

    public int getId() {
        return id;
    }

    public Date getContractYear() {
        return contractYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getResidence() {
        return residence;
    }

    @Override
    public String toString() {
        return String.format(
            "(%d, %s, %s, %s, %d, %s, %s)",
                    this.id,
                    this.firstName,
                    this.lastName,
                    this.contractYear.toString(),
                    this.telephone,
                    this.email,
                    this.residence);
    }    

}

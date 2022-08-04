package db_project.model;

// Classe
public class CarClass {

    private final int classType;
    private final int availableSeats;

    public CarClass(int classType, int availableSeats){
        this.classType = classType;
        this.availableSeats = availableSeats;
    }

    int getClassType(){
        return this.classType;
    }

    int getAvailableSeats(){
        return this.availableSeats;
    }

}

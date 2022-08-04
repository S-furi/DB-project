package db_project.model;

// Classe
public class CarClass {

    private int classType;
    private int availableSeats;

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

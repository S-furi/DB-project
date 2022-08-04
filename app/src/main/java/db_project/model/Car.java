package db_project.model;


// Carrozza
public class Car {

    private final int classType;
    private final String trainCode;
    private int position;               //Dovrebbe esssere numeroCarrozza ma non so cosa scrivere
    private final int seats;
    private final boolean toilet;

    public Car(int classType, String trainCode, int position, int seats, boolean toilet){
        this.classType = classType;
        this.trainCode = trainCode;
        this.position = position;
        this.seats = seats;
        this.toilet = toilet;
    }

    public int getClassType() {
        return classType;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public int getPosition() {
        return position;
    }

    public int getSeats() {
        return seats;
    }

    public boolean isToilet() {
        return toilet;
    }


    
}

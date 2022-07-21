package db_project.utils.pathFind.dataHandling;

public class LoaderTest {
    private static StationsLoader stLoad;
    public static void main(String[] args) {
        stLoad = new StationsLoader();
        printStations();
        //stLoad.getDistancesData();
    }

    private static void printStations() {
        stLoad.getStations().forEach(System.out::println);
    }
}

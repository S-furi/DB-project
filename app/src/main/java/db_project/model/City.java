package db_project.model;

//Città
public class City {
    private final String name;
    private final String region;
    private final String province;

    public City(final String name, final String region, final String province) {
        this.name = name;
        this.region = region;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return String.format(
            "%s (%s)",
            this.name,
            this.province
        );
    }
}

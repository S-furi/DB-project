package db_project.view.viewUtils;

public enum Cities {
    BARI_CENTRALE("BI", "BARI CENTRALE"),
    BARLETTA("BA", "BARLETTA"),
    BENEVENTO("BV", "BENEVENTO"),
    BERGAMO("BE", "BERGAMO"),
    BOLOGNA("BO", "BOLOGNA C.LE"),
    BOLZANO("BZ", "BOLZANO"),
    BRESCIA("BR", "BRESCIA"),
    CASERTA("CA", "CASERTA"),
    DESENZANO("DE", "DESENZANO"),
    FERRARA("FE", "FERRARA"),
    FIRENZE("FI", "FIRENZE S.M.N."),
    FOGGIA("FO", "FOGGIA"),
    GENOVA("GE", "GENOVA"),
    LAMEZIA("LA", "LAMEZIA"),
    MILANO("MI", "MILANO CENTRALE"),
    NAPOLI("NA", "NAPOLI CENTRALE"),
    PADOVA("PA", "PADOVA"),
    PESCHIERA("PE", "PESCHIERA"),
    REGGIO_CALABRIA("RC", "REGGIO CALABRIA"),
    REGGIO_EMILIA("RE", "REGGIO EMILIA"),
    ROMA("RO", "ROMA TERMINI"),
    ROVIGO("RV", "ROVIGO"),
    SALERNO("SA", "SALERNO"),
    TORINO("TO", "TORINO"),
    TRENTO("TN", "TRENTO"),
    TREVISO("TV", "TREVISO"),
    TRIESTE("TR", "TRIESTE"),
    UDINE("UD", "UDINE"),
    VENEZIA("VE", "VENEZIA SANTA LUCIA"),
    VERONA("VR", "VERONA"),
    VICENZA("VI", "VICENZA");

    private final String initial;
    private final String stationName;
    private Cities(final String initial, final String stationName) {
        this.initial = initial;
        this.stationName = stationName;
    }

    public String getInitial() {
        return this.initial;
    }

    public String getStationName() {
        return this.stationName;
    }

    public boolean equals(final Cities c2) {
        return this.initial.equals(c2.getInitial());
    }
}

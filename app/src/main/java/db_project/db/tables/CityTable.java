package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.City;
import db_project.utils.AbstractJsonReader;

public class CityTable extends AbstractTable<City, String> implements JsonReadeable<City> {
  public static final String TABLE_NAME = "CITTA";
  public static final String PRIMARY_KEY = "nome";

  public CityTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("regione", "provincia"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final City city) {
    return new Object[] {city.getName(), city.getRegion(), city.getProvince()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(final City city) {
    return new Object[] {city.getRegion(), city.getProvince(), city.getName()};
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table CITTA ( "
            + "nome varchar(40) not null, "
            + "regione varchar(40) not null, "
            + "provincia varchar(2) not null, "
            + "constraint ID_CITTA_ID primary key (nome));";

    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<City> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<City> cities = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String name = (String) row.get("nome");
              final String region = (String) row.get("regione");
              final String province = (String) row.get("provincia");
              cities.add(new City(name, region, province));
            });

    return cities;
  }

  @Override
  public List<City> readFromFile() {
    return new AbstractJsonReader<City>() 
      {}.setFileName("DbCities.json")
      .retreiveData(City.class);
  }
}

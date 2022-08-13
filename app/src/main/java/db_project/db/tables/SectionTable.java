package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Section;
import db_project.utils.AbstractJsonReader;

public class SectionTable extends AbstractTable<Section, String> implements JsonReadeable<Section> {
  public static final String TABLE_NAME = "TRATTA";
  public static final String PRIMARY_KEY = "codTratta";
  private final Logger logger;

  public SectionTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("distanza", "codStazionePartenza", "codStazioneArrivo"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Section section) {
    return new Object[] {
      section.getSectionCode(),
      section.getDistance(),
      section.getStartStation(),
      section.getEndStation()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Section section) {
    return new Object[] {
      section.getDistance(),
      section.getStartStation(),
      section.getEndStation(),
      section.getSectionCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table TRATTA ( "
            + "codTratta varchar(5) not null, "
            + "distanza int not null, "
            + "codStazionePartenza varchar(10) not null, "
            + "codStazioneArrivo varchar(10) not null, "
            + "constraint ID_TRATTA_ID primary key (codTratta)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Section> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Section> sections = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String startStation = (String) row.get("codStazionePartenza");
              final String endStation = (String) row.get("codStazioneArrivo");
              final String sectionCode = (String) row.get("codTratta");
              final int distance = (int) row.get("distanza");
              sections.add(new Section(startStation, endStation, sectionCode, distance));
            });
    return sections;
  }

  @Override
  public List<Section> readFromFile() {
    return new AbstractJsonReader<Section>() {}.setFileName("DbSection.json")
        .retreiveData(Section.class);
  }
}

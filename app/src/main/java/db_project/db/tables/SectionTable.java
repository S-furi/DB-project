package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Section;

public class SectionTable extends AbstractTable<Section, String> {
    public static final String TABLE_NAME = "TRATTA";
    public static final String PRIMARY_KEY = "codTratta";

    public SectionTable(final Connection connection) {
        super(TABLE_NAME, connection);
        super.setPrimaryKey(PRIMARY_KEY);
        super.setTableColumns(List.of("distanza", "codStazionePartenza", "codStazioneArrivo"));
    }

    @Override
    protected Object[] getSaveQueryParameters(final Section section) {
        return new Object[] { section.getSectionCode(), section.getDistance(), section.getStartStation(),
                section.getEndStation() };
    }

    @Override
    protected Object[] getUpdateQueryParameters(final Section section) {
        return new Object[] { section.getDistance(), section.getStartStation(), section.getEndStation(),
                section.getSectionCode() };
    }

    @Override
    protected List<Section> getPrettyResultFromQueryResult(final QueryResult result) {
        if (result.getResult().isEmpty()) {
            return Collections.emptyList();
        }
        List<Section> sections = new ArrayList<>();
        result.getResult().get().forEach(row -> {
            System.out.println(row.toString());
            final String startStation = (String) row.get("codStazionePartenza");
            final String endStation = (String) row.get("codStazioneArrivo");
            final String sectionCode = (String) row.get("codTratta");
            final int distance = (int) row.get("distanza");
            sections.add(new Section(startStation, endStation, sectionCode, distance));
        });
        return sections;
    }

}

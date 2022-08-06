package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Admin;

public class AdminTable extends AbstractTable<Admin, String> {
    public static final String TABLE_NAME = "AMMINISTRATORE";
    public static final String PRIMARY_KEY = "adminID";

    public AdminTable(final Connection connection) {
        super(TABLE_NAME, connection);
        super.setPrimaryKey(PRIMARY_KEY);
        super.setTableColumns(List.of("annoContratto", "nome", "cognome", "telefono", "email", "residenza"));
    }

    @Override
    protected Object[] getSaveQueryParameters(final Admin admin) {
        return new Object[] { admin.getId(), admin.getContractYear(), admin.getFirstName(), admin.getLastName(),
                admin.getTelephone(), admin.getEmail(), admin.getResidence() };
    }

    @Override
    protected Object[] getUpdateQueryParameters(final Admin admin) {
        return new Object[] { admin.getContractYear(), admin.getFirstName(), admin.getLastName(), admin.getTelephone(),
                admin.getEmail(), admin.getResidence(), admin.getId() };
    }

    @Override
    protected List<Admin> getPrettyResultFromQueryResult(final QueryResult result) {
        if (result.getResult().isEmpty()) {
            return Collections.emptyList();
        }
        final List<Admin> admList = new ArrayList<>();
        result.getResult().get().forEach(row -> {
            System.out.println(row.toString());
            final String id = (String) row.get("adminID");
            final Date contractYear = (Date) row.get("annoContratto");
            final String firstName = (String) row.get("nome");
            final String lastName = (String) row.get("cognome");
            final int telephone = (int) row.get("telefono");
            final String email = (String) row.get("email");
            final String residence = (String) row.get("residenza");
            admList.add(new Admin(id, contractYear, firstName, lastName, telephone, email, residence));
        });
        return admList;
    }
}

package ru.said.orm.next.core.db;

public class SQLiteDatabaseType implements DatabaseType {

    @Override
    public void appendPrimaryKey(StringBuilder sql, boolean generated) {
        sql.append(" PRIMARY KEY");
        if (generated) {
            sql.append(" AUTOINCREMENT");
        }
    }
}

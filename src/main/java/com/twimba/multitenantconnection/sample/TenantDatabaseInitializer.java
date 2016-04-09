package com.twimba.multitenantconnection.sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.twimba.hibernate.AbstractDatabaseInitializer;



public class TenantDatabaseInitializer extends AbstractDatabaseInitializer {

    public static final String CREATE_PERSON_TABLE = "CREATE TABLE Person ( " +
                                                     "  name varchar(200), " +
                                                     "  email varchar(200), " +
                                                     "  id int(11) unsigned NOT NULL AUTO_INCREMENT, " +
                                                     "  PRIMARY KEY (id) " +
                                                     ") ENGINE=InnoDB ";

    @Override
    public void intializeDatabase(Connection conn) {
        try (PreparedStatement cptPs = conn.prepareStatement(CREATE_PERSON_TABLE)) {
            cptPs.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getInitializingTableNameToCheck() {
        return "Person";
    }

}

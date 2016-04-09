package com.twimba.multitenantconnection.sample;

import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.mysql.jdbc.Driver;
import com.twimba.hibernate.AbstractSimpletMultiTenantConnectionProvider;
import com.twimba.hibernate.DatabaseInitializer;



public class MultiTenantConnectionProvider extends AbstractSimpletMultiTenantConnectionProvider {

    private static final long serialVersionUID = 1L;

    private static final String HOSTNAME = "localhost";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER = Driver.class.getName();
    private static final String URL_TEMPLATE = "jdbc:mysql://%s/%s?autoReconnect=true&useSSL=false";

    public MultiTenantConnectionProvider() {

        setTenantDatabaseUsername(getBaseTenantIdentifier(), USERNAME);
        setTenantDatabasePassword(getBaseTenantIdentifier(), PASSWORD);
        setTenantDatabaseDriver(getBaseTenantIdentifier(), DRIVER);
        setTenantDatabaseUrl(getBaseTenantIdentifier(), String.format(URL_TEMPLATE, HOSTNAME, "dbbase"));

        for (int tenant = 1; tenant < 4; tenant++) {
            String tenantId = "tenant-" + tenant;
            setTenantDatabaseUsername(tenantId, USERNAME);
            setTenantDatabasePassword(tenantId, PASSWORD);
            setTenantDatabaseDriver(tenantId, DRIVER);
            setTenantDatabaseUrl(tenantId, String.format(URL_TEMPLATE, HOSTNAME, "db" + tenant));
        }
    }

    @Override
    protected ConnectionProvider getConnectionProvider() {
        return new C3P0ConnectionProvider();
    }

    @Override
    protected DatabaseInitializer getDatabaseInitializer() {
        return new TenantDatabaseInitializer();
    }
}

package com.twimba.multitenantconnection.sample;

import org.apache.commons.lang3.RandomUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;



public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    public static int currentTenant = RandomUtils.nextInt(1, 3);

    @Override
    public String resolveCurrentTenantIdentifier() {
        return "tenant-" + currentTenant;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}

package com.twimba.multitenantconnection.sample;

import java.util.HashMap;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.lang3.RandomUtils;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;

import com.twimba.multitenantconnection.sample.domains.Person;



public class Main {

    private EntityManagerFactory emFactory;

    private void configureHibernate() {

        HashMap<String, Object> params = new HashMap<>();
        params.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE.name());
        params.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, MultiTenantConnectionProvider.class);
        params.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, TenantIdentifierResolver.class);
        emFactory = Persistence.createEntityManagerFactory("pu", params);

    }

    private void addData() {
        Person person = new Person();
        String uuid = UUID.randomUUID().toString();
        int tenantId = TenantIdentifierResolver.currentTenant;
        person.setName("A Person in tenant " + tenantId + "(" + uuid + ")");
        person.setEmail(uuid + "@tenant" + tenantId + ".example.com");

        EntityManager em = null;
        try {
            em = emFactory.createEntityManager();
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        }
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void doQuery() {
        EntityManager em = null;
        try {
            em = emFactory.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createQuery("FROM Person");
            for (Object obj : query.getResultList()) {
                System.out.println(obj);
            }
            em.getTransaction().commit();
        }
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.configureHibernate();
        for (int i = 1; i < RandomUtils.nextInt(10, 100); i++) {
            for (int t = 1; t < 4; t++) {
                TenantIdentifierResolver.currentTenant = t;
                app.addData();
                app.doQuery();
            }
        }
    }
}

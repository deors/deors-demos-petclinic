package org.springframework.samples.petclinic.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.AbstractClinicTests;

public abstract class AbstractJpaClinicTests extends AbstractClinicTests {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager sharedEntityManager;

    @Test(expected = IllegalArgumentException.class)
    public void testBogusJpql() {
        this.sharedEntityManager.createQuery("SELECT RUBBISH FROM RUBBISH HEAP").executeUpdate();
    }

    @Test
    public void testApplicationManaged() {
        EntityManager appManaged = this.entityManagerFactory.createEntityManager();
        appManaged.joinTransaction();
        appManaged.close();
    }

}

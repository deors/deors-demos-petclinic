
package org.springframework.samples.petclinic.jpa;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p>
 * Tests for the DAO variant based on the shared EntityManager approach, using
 * Apache OpenJPA for testing instead of the reference implementation.
 * </p>
 * <p>
 * Specifically tests usage of an <code>orm.xml</code> file, loaded by the
 * persistence provider through the Spring-provided persistence unit root URL.
 * </p>
 *
 * @author Juergen Hoeller
 */
// Ignoring these tests due to conflicts in bytecode instrumentation, when using
// both OpenJPA and JaCoCo, that do not pass the bytecode validation in Java 8
@Ignore
@ContextConfiguration(locations = {
        "applicationContext-jpaCommon.xml",
        "applicationContext-openJpaAdapter.xml",
        "applicationContext-entityManager.xml"
}, inheritLocations = false)
public class OpenJpaEntityManagerClinicTests extends EntityManagerClinicTests {

}

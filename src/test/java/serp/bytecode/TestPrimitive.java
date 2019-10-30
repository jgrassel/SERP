package serp.bytecode;

import junit.framework.*;
import junit.textui.*;

/**
 * Tests the handling of primitive {@link BCClass}es.
 *
 * @author Abe White
 */
public class TestPrimitive extends AbstractStateTest {
    public TestPrimitive(String test) {
        super(test);
    }

    public void setUp() {
        _bc = _project.loadClass(int.class);
    }

    public void testType() {
        assertEquals("int", _bc.getName());
        assertNull(_bc.getPackageName());
        assertEquals("int", _bc.getClassName());
        assertEquals(int.class, _bc.getType());

        try {
            _bc.setName("long");
            fail("Allowed set name");
        } catch (UnsupportedOperationException uoe) {
        }

        assertTrue(_bc.isPrimitive());
        assertTrue(!_bc.isArray());
    }

    public void testSuperclass() {
        assertNull(_bc.getSuperclassName());
        try {
            _bc.setSuperclass("long");
            fail("Allowed set superclass");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testComponent() {
        assertNull(_bc.getComponentName());
    }

    public static Test suite() {
        return new TestSuite(TestPrimitive.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}

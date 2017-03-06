import api.core.impl.DataManager;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Created by dequan on 3/6/17.
 */
public class TestCodeCoverage {
    @Test
    public void test_initiateDatamanger() {
        //Initiate for coverage
        DataManager testDataManager =  new DataManager();
        assertTrue("DataManager is used as static class, should not be able to initialize",
                testDataManager == null);
    }
}

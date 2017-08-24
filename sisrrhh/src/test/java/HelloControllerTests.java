/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;
import py.gov.mca.sisrrhh.managedbeans.SisrrhhMB;


/**
 *
 * @author vinsfran
 */
public class HelloControllerTests {

    
    @Test
    public void testHandleRequestView() throws Exception{
        SisrrhhMB srrhh = new SisrrhhMB();
        String nowValue = "";
        nowValue = srrhh.test();
        assertNotNull(nowValue);
    }



}

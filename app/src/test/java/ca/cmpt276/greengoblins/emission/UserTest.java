package ca.cmpt276.greengoblins.emission;

import org.junit.Test;

import ca.cmpt276.greengoblins.foodsurveydata.User;

import static org.junit.Assert.*;

public class UserTest {
    private final double EPSILON = 0.001;

    @Test
    public void createUserTest1(){
        User testUser = new User();
        testUser.setCity("Bur7546-=naby");
        testUser.setEmailAddress("test85432@mgail.com");
        testUser.setFirstName("Greg");
        testUser.setLastName("Tom");
        testUser.setPledgeAmount(844.546);
        testUser.setShowNamePublic(false);
        assertEquals("Bur7546-=naby", testUser.getCity());
    }
    @Test
    public void createUserTest2(){
        User testUser = new User();
        testUser.setCity("Bu@atestsdfr7546-=naby");
        testUser.setEmailAddress("test=@5432@mgail.com");
        testUser.setFirstName("Bob");
        testUser.setLastName("Jerome");
        testUser.setPledgeAmount(9123.10);
        testUser.setShowNamePublic(true);
    }

    @Test
    public void getFirstName() {
        User testUser = new User();
        testUser.setFirstName("93210423klAD#@-a=dfa");
        assertEquals("93210423klAD#@-a=dfa", testUser.getFirstName());
    }

    @Test
    public void setFirstName() {
        User testUser = new User();
        testUser.setFirstName("alkdjsf943534lk=-xc_");
        assertEquals("alkdjsf943534lk=-xc_", testUser.getFirstName());
    }

    @Test
    public void getLastName() {
        User testUser = new User();
        testUser.setLastName("asdlkjfti_+_ASDF");
        assertEquals("asdlkjfti_+_ASDF", testUser.getLastName());
    }

    @Test
    public void setLastName() {
        User testUser = new User();
        testUser.setLastName("34960lkasjfdlkasj" + "asdlkjfti_");
        assertEquals("34960lkasjfdlkasjasdlkjfti_", testUser.getLastName());
    }

    @Test
    public void getPledgeAmount() {
        User testUser = new User();
        testUser.setPledgeAmount(0912130.102301);
        assertEquals(0912130.102301, testUser.getPledgeAmount(), EPSILON);
    }

    @Test
    public void setPledgeAmount() {
        User testUser = new User();
        testUser.setPledgeAmount(98124/2);
        assertEquals(98124/2, testUser.getPledgeAmount(), EPSILON);
    }

    @Test
    public void getEmailAddress() {
        User testUser = new User();
        testUser.setEmailAddress("alkdjlkfaj@" + "gmail.com");
        assertEquals("alkdjlkfaj@gmail.com", testUser.getEmailAddress());
    }

    @Test
    public void setEmailAddress() {
        User testUser = new User();
        testUser.setEmailAddress("alkdjl32432_4kfaj@gmail.com");
        assertEquals("alkdjl32432_4kfaj@gmail.com", testUser.getEmailAddress());
    }

    @Test
    public void getCity() {
        User testUser = new User();
        testUser.setCity("van2342_couve23423r");
        assertEquals("van2342_couve23423r", testUser.getCity());
    }

    @Test
    public void setCity() {
        User testUser = new User();
        testUser.setCity("234s342urre12421y");
        assertEquals("234s342urre12421y", testUser.getCity());
    }

    @Test
    public boolean isShowNamePublic() {
        User testUser = new User();
        testUser.setShowNamePublic(false);
        return testUser.isShowNamePublic();
    }

    @Test
    public void setShowNamePublic() {
        User testUser = new User();
        testUser.setShowNamePublic(true);
        assertTrue(testUser.isShowNamePublic());
    }
}
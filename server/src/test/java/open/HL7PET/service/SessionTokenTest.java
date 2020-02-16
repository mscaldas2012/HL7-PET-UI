package open.HL7PET.service;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SessionTokenTest {

    @Test
    public void testExtractingSessionTokenByString() {
        HashMap<SessionToken, Integer> map = new HashMap();

        SessionToken s1 = new SessionToken();
        map.put(s1, 1);

        SessionToken s2 = new SessionToken();
        map.put(s2, 2);

        SessionToken s3 = new SessionToken();
        map.put(s3, 3);

        SessionToken s4 = new SessionToken();
        map.put(s4, 4);

        SessionToken s5 = new SessionToken();
        map.put(s5, 5);

        //Still need to create a SessionToken to properly retrieve the right object.
        //Would like to simply pass the token value and still find the right SessionToken.
        Integer i3 = map.get(new SessionToken(s3.getToken()));
        assertTrue(i3 == 3);

        System.out.println("map.size() = " + map.size());
        s5.setLastUpdated(new Date());
        map.put(s5, 6);

        System.out.println("map.size() = " + map.size());

    }
    
    @Test
    public void testEquality() {
        SessionToken s = new SessionToken();

        assertEquals(s, s);
        assertEquals(s, s.getToken());
    }

}
package al.aldi.utils.classes;

import static org.junit.Assert.*;

import org.junit.Test;

import al.aldi.utils.classes.AldiStringUtils;

public class AldiStringUtilsTest {

    String[] strArray                          = new String[] { "a", "b", "c" };
    String   strArrayJoined                    = "abc";
    String   strArrayJoinedWithSpaces          = "a b c";
    String   strArrayJoinedWithSpacesAndDollar = "$a $b $c";
    String   separator                         = " ";
    String   prefix                            = "$";

    /* *************************************************************************** */

    String   linkWithHttp                      = "http://asdfa.com";
    String   linkWithHttp2                     = "http://";
    String   linkWithoutHttp                   = "ahttp://";
    String   linkWithoutHttp2                  = "http:/";

    String   linkWithHttps                     = "https://asdfa.com";
    String   linkWithHttps2                    = "https://";
    String   linkWithoutHttps                  = "ahttp://";
    String   linkWithoutHttps2                 = "http:/";

    @Test
    public void testArrayToStringStringArray() {
        assertEquals(AldiStringUtils.arrayToString(strArray), strArrayJoined);
    }

    @Test
    public void testArrayToStringStringArrayString() {
        assertEquals(AldiStringUtils.arrayToString(strArray, separator), strArrayJoinedWithSpaces);
    }

    @Test
    public void testArrayToStringStringArrayStringString() {
        assertEquals(AldiStringUtils.arrayToString(strArray, prefix, separator), strArrayJoinedWithSpacesAndDollar);
    }

    @Test
    public void testStartsWithHttpS() {
        assertTrue(AldiStringUtils.startsWithHttpS(linkWithHttp));
        assertTrue(AldiStringUtils.startsWithHttpS(linkWithHttp2));
        assertFalse(AldiStringUtils.startsWithHttpS(linkWithoutHttp));
        assertFalse(AldiStringUtils.startsWithHttpS(linkWithoutHttp2));

        assertTrue(AldiStringUtils.startsWithHttpS(linkWithHttps));
        assertTrue(AldiStringUtils.startsWithHttpS(linkWithHttps2));
        assertFalse(AldiStringUtils.startsWithHttpS(linkWithoutHttps));
        assertFalse(AldiStringUtils.startsWithHttpS(linkWithoutHttps2));
    }

}

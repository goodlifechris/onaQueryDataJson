package com.siafuinc.ona;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void getJson() throws Exception {
        int a =0;
        HashMap<Integer, String> hm = new HashMap<Integer, String>();
        hm.put(100, "sachins");
        hm.put(101, "sehwag");
        hm.put(102, "gambir");

        Set set = hm.entrySet();
        Iterator it = set.iterator();

        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            System.out.println(m.getKey() + ":" + m.getValue());

        }



    }

}
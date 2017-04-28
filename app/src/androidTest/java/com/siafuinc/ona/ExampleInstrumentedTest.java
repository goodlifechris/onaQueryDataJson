package com.siafuinc.ona;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.siafuinc.ona", appContext.getPackageName());
    }
    @Test
    public void getJson() throws Exception {
        JSONObject json = new JSONObject(IOUtils.toString(new URL("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json"), Charset.forName("UTF-8")));
        System.out.print(json);
    }
}

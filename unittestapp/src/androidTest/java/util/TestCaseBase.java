package util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import jp.hkawasaki.util.File.FileRW;
import jp.hkawasaki.util.net.HttpGetClient;

/**
 * Created by hirokuni on 15/09/12.
 */
@RunWith(AndroidJUnit4.class)
public class TestCaseBase extends AndroidTestCase {
    /* Set S3 full access AWS key */
    public static String sMusicLifeTestUserId = "key";
    public static String sMusicLifeTestUserPwd = "secret";

    private static final String TAG = TestCaseBase.class.getSimpleName();

    protected static File sAppRootDir = InstrumentationRegistry.getTargetContext().getDir("files", Context.MODE_PRIVATE);
    protected static final String testUrl = "http://localhost:8082/test.txt";
    protected static final String testString = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";

    public final static String sTestSndDFileName = "PresetDrum.raw";
    public static File sTestDRscFile = new File(sAppRootDir.getPath(), sTestSndDFileName);
    public final static String sTestSndDRscUrl = "https://s3-us-west-2.amazonaws.com/bm-kawa-test/unit_test_resource/preset/PresetDrum.raw";

    public final static String sTestSndGFileName = "PresetGuitar.raw";
    public static File sTestGRscFile = new File(sAppRootDir.getPath(), sTestSndGFileName);
    public final static String sTestSndGRscUrl = "https://s3-us-west-2.amazonaws.com/bm-kawa-test/unit_test_resource/preset/PresetGuitar.raw";

    @BeforeClass
    public static void resourceSetUp() throws IOException {
        //Download test resource from a storage server on internet (S3)
        if (!sTestDRscFile.exists() || sTestDRscFile.length() == 0 || !sTestGRscFile.exists() || sTestGRscFile.length() == 0) {
            Log.v(TAG, "Resource. Try to download. Network connection must be required!");

            HttpGetClient httpGet = new HttpGetClient(new URL(sTestSndDRscUrl));
            new FileRW(sTestDRscFile.getAbsolutePath()).write(httpGet.execute());

            HttpGetClient httpGet2 = new HttpGetClient(new URL(sTestSndGRscUrl));
            new FileRW(sTestGRscFile.getAbsolutePath()).write(httpGet2.execute());

            Log.v(TAG, "Finish downloading resource");
        } else {
            Log.v(TAG, "No download. Resource already exists");
        }
    }
}


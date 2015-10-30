package jp.hkawasaki.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import jp.hkawasaki.util.GeneratePresignedUrlAndUploadObject;
import jp.hkawasaki.util.mock.TestMockWebServer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jp.hkawasaki.util.File.FileRW;
import jp.hkawasaki.util.net.HttpGetClient;
import jp.hkawasaki.util.net.HttpPutClient;

/**
 * Created by hirokuni on 15/09/08.
 */
@RunWith(AndroidJUnit4.class)
public class HttpClientTest extends AndroidTestCase {
    final static String TAG = HttpClientTest.class.getSimpleName();
    static TestMockWebServer sTestMockWevServe;

    static final String testUrl = "http://localhost:8082/test.txt";
    static final String testString = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    static File mAppRootDir;

    @BeforeClass
    public static void doBeforeClass() throws Exception {
        sTestMockWevServe = new TestMockWebServer("localhost", 8082);
        sTestMockWevServe.start();



        mAppRootDir = new File("/sdcard/test");

    }


    @Test
    public void DownloadTest() throws IOException, JSONException {
        HttpGetClient getClient = new HttpGetClient(new URL(testUrl));

        InputStream is = getClient.execute();

        FileRW frw = new FileRW("/sdcard/test.txt");
        long wsize = frw.write(is);

        byte[] data = new byte[1024];
        int rsize = frw.read(data);

        assertEquals(testString.length(), wsize);
        assertEquals(testString.length(), rsize);
        assertEquals(testString, new String(data, 0, rsize));

        is.close();
        getClient.release();

        frw.deleteFile();
    }


    @Test
    public void UploadTest() throws IOException, JSONException {
        GeneratePresignedUrlAndUploadObject gpuao = new GeneratePresignedUrlAndUploadObject();
        URL url = gpuao.getSignedUrl("PUT");

        HttpPutClient putClient = new HttpPutClient(url);
        gpuao.UploadObject(url);

    }

    @Test
    public void UploadOneTextFile() throws IOException, JSONException {
        String rtnXml = "testtext";
        String uploadTargetFilePath = "/sdcard/test.txt";
        {
            InputStream bais = new ByteArrayInputStream(rtnXml.getBytes("utf-8"));

            new FileRW(uploadTargetFilePath).write(bais);
        }

        {
            GeneratePresignedUrlAndUploadObject ao = new GeneratePresignedUrlAndUploadObject();
            URL url = ao.getSignedUrl("PUT");

            HttpPutClient putClient = new HttpPutClient(url);
            Log.i(TAG, putClient.execute(new File(uploadTargetFilePath)));
        }

        {
            GeneratePresignedUrlAndUploadObject ao = new GeneratePresignedUrlAndUploadObject();
            URL url = ao.getSignedUrl("GET");

            HttpGetClient httpGet = null;

            httpGet = new HttpGetClient(url);
            new FileRW("/sdcard/test2.txt").write(httpGet.execute());
            httpGet.release();

            byte[] data = new byte[100];
            new FileRW("/sdcard/test2.txt").read(data);

            String d = new String(data);
            Log.i(TAG, "d: " + d);
            assertEquals(rtnXml, d);

            new File("/sdcard/test2.txt").delete();
        }
    }

    @AfterClass
    public static void doAfterClass() throws Exception {
        sTestMockWevServe.stop();

    }


}

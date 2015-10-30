package util;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jp.hkawasaki.util.File.FileRW;
import jp.hkawasaki.util.net.HttpGetClient;
import jp.hkawasaki.util.net.HttpPutClient;
import util.mock.TestMockWebServer;

/**
 * Created by hirokuni on 15/09/08.
 */
@RunWith(AndroidJUnit4.class)
public class HttpClientTest extends TestCaseBase {
    final static String TAG = HttpClientTest.class.getSimpleName();
    static TestMockWebServer sTestMockWevServe;

    @BeforeClass
    public static void doBeforeClass() throws Exception {
        sTestMockWevServe = new TestMockWebServer("localhost", 8082);
        sTestMockWevServe.start();
        resourceSetUp();
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
            Log.i(TAG, "url: " + url);
            HttpPutClient putClient = new HttpPutClient(url);
            String res = putClient.execute(new File(uploadTargetFilePath), "text/plain");
            Log.i(TAG, res);
        }

        String downloadPath = "/sdcard/test2.txt";
        {
            GeneratePresignedUrlAndUploadObject ao = new GeneratePresignedUrlAndUploadObject();
            URL url = ao.getSignedUrl("GET");

            HttpGetClient httpGet = new HttpGetClient(url);
            new FileRW(downloadPath).write(httpGet.execute());
            httpGet.release();

            byte[] data = new byte[100];
            int size = new FileRW(downloadPath).read(data);

            String d = new String(data, 0, size);
            Log.i(TAG, "d: " + d);

            assertEquals(HashCheck.getHashValue(rtnXml), HashCheck.getHashValue(d));

            new File(downloadPath).delete();
        }
    }


    @Test
    public void UploadOneBinaryFile() throws IOException, JSONException {

        String uploadTargetFilePath = sTestGRscFile.getAbsolutePath();
        {
            GeneratePresignedUrlAndUploadObject ao = new GeneratePresignedUrlAndUploadObject();
            URL url = ao.getSignedUrl("PUT");
            Log.i(TAG, "url: " + url);
            HttpPutClient putClient = new HttpPutClient(url);
            String res = putClient.execute(new File(uploadTargetFilePath), "text/plain");
            Log.i(TAG, res);
        }

        String downloadPath = "/sdcard/test.raw";
        {
            GeneratePresignedUrlAndUploadObject ao = new GeneratePresignedUrlAndUploadObject();
            URL url = ao.getSignedUrl("GET");

            HttpGetClient httpGet = new HttpGetClient(url);
            new FileRW(downloadPath).write(httpGet.execute());
            httpGet.release();

            FileInputStream fis1 = new FileInputStream(new File(uploadTargetFilePath));
            FileInputStream fis2 = new FileInputStream(new File(downloadPath));

            assertEquals(HashCheck.getHashValue(fis1),
                    HashCheck.getHashValue(fis2));

            fis1.close();
            fis2.close();

            new File(downloadPath).delete();
        }
    }


    @AfterClass
    public static void doAfterClass() throws Exception {
        sTestMockWevServe.stop();
    }
}

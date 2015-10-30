package jp.hkawasaki.util.mock;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by hirokuni on 15/09/12.
 */
public class TestMockWebServer extends NanoHTTPD {
    private static final String TAG = TestMockWebServer.class.getSimpleName();
    static final String testString = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";

    public TestMockWebServer(int port) {
        super(port);
    }

    public TestMockWebServer(String hostname, int port) throws IOException {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        Map<String, String> parms = session.getParms();
        String uri = session.getUri();
        String answer = "";

        Log.i(TAG, "serve--!!!");

        if (session.getMethod() == Method.PUT) {
            Log.i(TAG, "PUT");

        }

        if (session.getMethod() == Method.GET) {
            Log.i(TAG, "GET");

            if (uri.trim().equalsIgnoreCase("/test.txt")) {
                Log.i(TAG, "/test.txt");
                answer = testString;
            } else {
                answer = "--------";
            }

        }

        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, answer);
    }
}

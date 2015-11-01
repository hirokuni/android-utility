
package jp.hkawasaki.util.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 22110115 on 2015/09/10.
 */
abstract class HttpBaseClient {
    private static final int SECOND_IN_MILLIS = 1000;
    private static final int DEFAULT_TIMEOUT = (int) (20 * SECOND_IN_MILLIS);
    protected HttpURLConnection con;
    protected URL mUrl;

    public HttpBaseClient(URL url) throws IOException {
        mUrl = url;
        con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(DEFAULT_TIMEOUT);
        con.setReadTimeout(DEFAULT_TIMEOUT);
        con.setRequestMethod(getRequestMethod());
        con.setDoOutput(getOutputFlag());
    }

    abstract protected String getRequestMethod();

    abstract protected boolean getOutputFlag();

    public int getResponseCode() throws IOException {
        return con.getResponseCode();
    }

    public void release() {
        con.disconnect();
    }

    public String getResponseMsg() {
        try {
            return con.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
package jp.hkawasaki.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Override;import java.lang.String;import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 22110115 on 2015/09/09.
 */
public class HttpGetClient extends HttpBaseClient {

    public HttpGetClient(URL url) throws IOException {
        super(url);
    }

    @Override
    protected String getRequestMethod() {
        return "GET";
    }

    @Override
    protected boolean getOutputFlag() {
        return false;
    }


    public InputStream execute() throws MalformedURLException {

        InputStream res = null;
        //con.setInstanceFollowRedirects(false);
        //con.setRequestProperty("Accept-Language", "jp");
        try {
            con.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            res = con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return res;
    }

}

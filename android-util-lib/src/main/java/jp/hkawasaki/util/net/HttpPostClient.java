package jp.hkawasaki.util.net;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 22110115 on 2015/09/10.
 */
public class HttpPostClient extends HttpBaseClient {
    private static final String TAG = HttpPostClient.class.getSimpleName();
    private final static int BUF_SIZE = 4096;
    private String resBody;
    public static final String BOUNDARY = "---*#asdkfjaewaefa#";

    public HttpPostClient(URL url) throws IOException {
        super(url);
        //con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected boolean getOutputFlag() {
        return true;
    }

    /*
     * Put json data with string.
     * curl -v -H "Accept: aplication/json" H "Content-type: application/json" -X POST -d '{"test":"testVal"}' http://kawasakitwitterbot.mybluemix.net/testtext
     */
    public String execute(JSONObject json) throws IOException {
        resBody = null;
        //con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        con.setRequestProperty("Content-type", "application/json");
        if (json != null) {
            OutputStream os = con.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            Log.i(TAG,"json: " + json.toString());
            bos.write(json.toString().getBytes());
            bos.flush();
            bos.close();
        } else {
            Log.i(TAG,"json is null");
            con.connect();
        }

        //wait here until receiving the result.
        return getStringResBody();
    }

    public void setHeader(String key, String value){
        con.setRequestProperty(key, value);
    }

    public String execute(File file) throws IOException {
        resBody = null;
        con.setChunkedStreamingMode(-1);

        if (file != null) {
            OutputStream os = con.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);

            FileInputStream fis = new FileInputStream(file);

            final byte[] buffer = new byte[8192];

            for (int read = -1; (read = fis.read(buffer)) != -1; ) {
                bos.write(buffer, 0, read); // just a plain stream copy
                bos.flush();
            }
            bos.close();
        } else {
            Log.i(TAG,"String msg is null ");
            con.connect();
        }

        //wait here until receiving the result.
        return getStringResBody();
    }

    public String execute(String msg) throws IOException {
        resBody = null;

        if (msg != null) {
            OutputStream os = con.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            Log.i(TAG,"String msg: " + msg);
            bos.write(msg.getBytes());
            bos.flush();
            bos.close();
        } else {
            Log.i(TAG,"String msg is null ");
            con.connect();
        }

        //wait here until receiving the result.
        return getStringResBody();
    }

    public String getStringResBody() {
        if (resBody != null)
            return resBody;

        byte[] data = new byte[BUF_SIZE];
        String res = "";
        InputStream is = null;
        int rsize = 0;
        StringBuffer sb = new StringBuffer();

        try {
            if (getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)
                is = con.getInputStream();
            else
                is = con.getErrorStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        while (true) {
            try {
                rsize = is.read(data);
                if (rsize < 0)
                    break;
                sb.append(new String(data, 0, rsize));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        return resBody = sb.toString();
    }
}
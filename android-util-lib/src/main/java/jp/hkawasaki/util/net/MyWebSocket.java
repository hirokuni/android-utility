package jp.hkawasaki.util.net;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.String;import java.nio.ByteBuffer;

/**
 * Created by hirokuni on 15/09/30.
 * wrapper based on http://darutk-oboegaki.blogspot.jp/2015/04/websocket-java-se-15-android.html
 */
public class MyWebSocket {
    private static final String TAG = "MyWebSocket";
    private WebSocket ws;

    //uri : e.g. ws://localhost/endpoint
    public MyWebSocket(String address) throws IOException {
        WebSocketFactory factory = new WebSocketFactory();
        factory.setConnectionTimeout(5000);
        ws = factory.createSocket(address);
    }

    public void addListener(WebSocketAdapter adapter) {
        ws.addListener(adapter);
    }

    public void connect(boolean isAsync) throws WebSocketException {

        if (isAsync)
            ws.connectAsynchronously();
        else
            ws.connect();
    }

    public void sendText(String message) {

        ws.sendText(message);
    }

    public void sendBinary(File file) throws IOException {
        Log.i(TAG, "file exist: " + file.exists());
        Log.i(TAG, "file length: " + file.length());
        Log.i(TAG, "file path: " + file.getAbsolutePath());


        FileInputStream fis = new FileInputStream(file);

        //final byte[] buffer = new byte[8192];
        final byte[] buffer = new byte[8192];

        WebSocket _ws = ws;
        boolean isFirst = true;
        for (int read = -1; (read = fis.read(buffer)) != -1; ) {
            Log.i(TAG, "read: " + read);
            if (isFirst) {
                ByteBuffer bb = ByteBuffer.allocate(read);
                bb.put(buffer, 0, read);

                _ws = _ws.sendBinary(bb.array(), false);

                isFirst = false;
            } else {
                ByteBuffer bb = ByteBuffer.allocate(read);
                bb.put(buffer, 0, read);

                _ws = _ws.sendContinuation(bb.array());
            }
        }
        _ws.sendContinuation(true);
    }

    //after disconnect, connect does not work. Make new instance.
    public void disconnect() {
        ws.disconnect();
    }

}

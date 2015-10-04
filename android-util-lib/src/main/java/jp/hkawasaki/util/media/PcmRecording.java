package jp.hkawasaki.util.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

/**
 * Created by hirokuni on 15/09/29.
 *
 * Reference : http://blog.livedoor.jp/sce_info3-craft/archives/8280133.html
 */
public class PcmRecording {

    private String fpath;
    final static int SAMPLING_RATE = 44100;
    private int bufSize;
    private AudioRecord myAR;
    private WaveFile wav1 = new WaveFile();
    private short[] shortData;

    public PcmRecording() {

        //保存先
        fpath = Environment.getExternalStorageDirectory() + "/recaudio.wav";

        // AudioRecordオブジェクトを作成
        bufSize = android.media.AudioRecord.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        initAudioRecord();

        // ファイルを作成
        wav1.createFile(fpath);

    }

    public String getFilePath() {
        return fpath;
    }

    public void start() {
        //録音準備＆録音開始


        myAR.startRecording();
        myAR.read(shortData, 0, bufSize / 2);


    }

    public void stop() {
        myAR.stop();
    }

    // AudioRecordの初期化
    private void initAudioRecord() {

        // AudioRecordオブジェクトを作成
        bufSize = android.media.AudioRecord.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        myAR = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize);

        shortData = new short[bufSize / 2];

        // コールバックを指定
        myAR.setRecordPositionUpdateListener(new AudioRecord.OnRecordPositionUpdateListener() {

            // フレームごとの処理
            @Override
            public void onPeriodicNotification(AudioRecord recorder) {
                // TODO Auto-generated method stub
                myAR.read(shortData, 0, bufSize / 2); // 読み込む
                wav1.addBigEndianData(shortData); // ファイルに書き出す
            }

            @Override
            public void onMarkerReached(AudioRecord recorder) {
                // TODO Auto-generated method stub

            }
        });

        // コールバックが呼ばれる間隔を指定
        myAR.setPositionNotificationPeriod(bufSize / 2);

    }

    public void close() {
        wav1.close();
    }

    public void release() {
        myAR.release();
    }

}

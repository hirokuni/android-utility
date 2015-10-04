package jp.hkawasaki.util.media;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

/**
 * Created by hirokuni on 15/09/29.
 */
public class PFDefaultRecording {
    MediaRecorder recorder;
    String fpath;

    public PFDefaultRecording(){
        recorder = new MediaRecorder();
        //保存先
        fpath = Environment.getExternalStorageDirectory() + "/recaudio.wav";

    }

    public String getFilePath(){
        return fpath;
    }

    public void start(){
        //録音準備＆録音開始
        File file = new File(fpath);
        if (file.exists())
            file.delete();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(fpath);

        try {
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder.start();   //録音開始
    }

    public void stop(){
        recorder.stop();
    }

}

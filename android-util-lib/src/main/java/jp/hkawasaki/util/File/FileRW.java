package jp.hkawasaki.util.File;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 22110115 on 2015/09/10.
 * <p/>
 * Sample : http://syunpon.com/programing/java/sample/fileinout.shtml#sample8
 */
public class FileRW {
    private final static String TAG = FileRW.class.getSimpleName();
    private File mFile;
    private final static int BUF_SIZE = 4096;
    private BufferedInputStream mBis;
    //private FileOutputStream mFos;
    //private BufferedOutputStream mBos;
    //private FileInputStream mFis;

    public FileRW(String filePath) throws IOException {
        mFile = new File(filePath);
        Log.i(TAG, "filePath: " + filePath);

        if (!mFile.exists()) {
            String folderPath = mFile.getParent();
            Log.i(TAG, "folderPath: " + folderPath);
            if (folderPath != null) {
                new File(folderPath).mkdirs();
                Log.i(TAG, "mkdirs");
            }

            mFile.createNewFile();
            Log.v("HttpTest", "file creation");
        }

        //mFos = new FileOutputStream(mFile);
        //mBos = new BufferedOutputStream(mFos);
        //mFis = new FileInputStream(mFile);
        //mBis = new BufferedInputStream(mFis);
    }

    /*
    * Write data to the specified file.
    * */
    public long write(InputStream is) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(mFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] data = new byte[BUF_SIZE];
        int rsize = 0;
        int wsize = 0;

        while (true) {
            try {
                rsize = is.read(data);
                if (rsize < 0)
                    break;
                bos.write(data, 0, rsize);
                wsize += rsize;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }

        try {
            bos.flush();
            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return wsize;
    }


    public int read(byte[] data) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(mFile);

        BufferedInputStream bis = new BufferedInputStream(fis);
        int rsize = -1;
        try {
            rsize = bis.read(data);
            Log.i("HttpTest", "rsize :" + rsize);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        try {
            bis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rsize;
    }

    public void deleteFile() {
        mFile.delete();
    }
}

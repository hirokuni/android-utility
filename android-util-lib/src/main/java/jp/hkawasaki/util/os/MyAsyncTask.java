package jp.hkawasaki.util.os;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;import java.lang.Integer;import java.lang.Long;import java.lang.Override;import java.lang.String;

/**
 * Created by hirokuni on 15/09/30.
 */
public class MyAsyncTask extends AsyncTask<String, Integer, Long> implements DialogInterface.OnCancelListener {
    final String TAG = "MyAsyncTask";
    ProgressDialog dialog;
    Context context;
    background mListener;

    public interface background {
        void onPreExecute();

        Long doInBackground(String... params);

        void onProgressUpdate(Integer... params);

        void onPostExecute();
    }

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    public void addListener(background listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading data...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(this);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.show();
        if (mListener != null)
            mListener.onPreExecute();
    }

    @Override
    protected Long doInBackground(String... params) {
        Log.d(TAG, "doInBackground - ");
      /*
        try {
            for (int i = 0; i < 10; i++) {
                if (isCancelled()) {
                    Log.d(TAG, "Cancelled!");
                    break;
                }
                Thread.sleep(1000);
                publishProgress((i + 1) * 10);
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException in doInBackground");
        }
        */
        if (mListener != null)
            return mListener.doInBackground(params);

        return 0L;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "onProgressUpdate - " + values[0]);
        dialog.setProgress(values[0]);
        if (mListener != null)
            mListener.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "onCancelled");
        dialog.dismiss();
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.d(TAG, "onPostExecute - " + result);
        mListener.onPostExecute();
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d(TAG, "Dialog onCancell... calling cancel(true)");
        this.cancel(true);
    }
}
package jp.hkawasaki.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.amazonaws.auth.profile.ProfileCredentialsProvider;

/**
 * Created by hirokuni on 15/10/30.
 */
public class GeneratePresignedUrlAndUploadObject {
    private static String bucketName = "bm-kawa-test";
    private static String objectKey  = "unit_test_resource/uploadTest/test.txt";
    //private static String objectKey  = "uploadTest/test.txt";
    //private static String objectKey  = "test.txt";
    private BasicAWSCredentials mS3Credentials;

    public URL getSignedUrl(String operation) throws IOException {
        mS3Credentials = new BasicAWSCredentials("xxx", "xxx");

        AmazonS3 s3client = new AmazonS3Client(mS3Credentials);
        s3client.setRegion(Region.getRegion(Regions.US_WEST_2));
        try {
            System.out.println("Generating pre-signed URL.");
            java.util.Date expiration = new java.util.Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += 1000 * 60 * 60; // Add 1 hour.
            expiration.setTime(milliSeconds);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey);

            if (operation.equalsIgnoreCase("PUT"))
                generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
            else if (operation.equalsIgnoreCase("GET"))
                generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setContentType("text/plain");

            generatePresignedUrlRequest.setExpiration(expiration);

            URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);


            //UploadObject(url);

            System.out.println("Pre-Signed URL = " + url.toString());

            return url;
        } catch (AmazonServiceException exception) {
            System.out.println("Caught an AmazonServiceException, " +
                    "which means your request made it " +
                    "to Amazon S3, but was rejected with an error response " +
                    "for some reason.");
            System.out.println("Error Message: " + exception.getMessage());
            System.out.println("HTTP  Code: "    + exception.getStatusCode());
            System.out.println("AWS Error Code:" + exception.getErrorCode());
            System.out.println("Error Type:    " + exception.getErrorType());
            System.out.println("Request ID:    " + exception.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, " +
                    "which means the client encountered " +
                    "an internal error while trying to communicate" +
                    " with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return null;
    }

    public static void UploadObject(URL url) throws IOException
    {
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
       // connection.setRequestProperty("Content-Type", "application/octet-stream"); // Very important ! It won't work without adding this!
        connection.setRequestProperty("Content-Type", "text/plain"); // Very important ! It won't work without adding this!
        OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream());
        out.write("This text uploaded as object.");




        out.close();
        int responseCode = connection.getResponseCode();
        System.out.println("Service returned response code " + responseCode);
        String msg = connection.getResponseMessage();
        System.out.println("Service returned msg: " + msg);

    }
}

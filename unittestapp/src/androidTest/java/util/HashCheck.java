package util;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hirokuni on 15/11/01.
 */
public class HashCheck {

    public static String getHashValue(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }

        md.update(str.getBytes());

        byte[] dg = md.digest();
        StringBuffer sb = new StringBuffer();

        for (int b : dg) {
            sb.append(Character.forDigit(b >> 4 & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }

        return new String(sb.toString());
    }

    public static String getHashValue(InputStream is) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        DigestInputStream dis = new DigestInputStream(is, md);
        {
            /* Read decorated stream (dis) to EOF as normal... */
        }
        byte[] digest = md.digest();

        byte[] dg = md.digest();
        StringBuffer sb = new StringBuffer();

        for (int b : dg) {
            sb.append(Character.forDigit(b >> 4 & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }

        return new String(sb.toString());
    }

}

package kiolk.com.github.pen.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static kiolk.com.github.pen.utils.PenConstantsUtil.EMPTY_STRING;

public final class MD5Util {

    private static final String HASH_FORMAT_MD_5 = "MD5";

    public static String getHashString(final String pString) {
        try {
            final MessageDigest md = MessageDigest.getInstance(HASH_FORMAT_MD_5);
            md.update(pString.getBytes());
            final byte[] byteArray = md.digest();
            final StringBuilder buffer = new StringBuilder();

            for (final byte aByteArray : byteArray) {
                buffer.append(Integer.toHexString(0xFF & aByteArray));
            }

            return buffer.toString();
        } catch (final NoSuchAlgorithmException ignored) {

        }

        return EMPTY_STRING;
    }
}

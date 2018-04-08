package kiolk.com.github.pen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;

import static kiolk.com.github.pen.utils.PenConstantsUtil.INNER_FILE_CACHE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.KILOBYTE_SIZE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.LOG;
import static kiolk.com.github.pen.utils.PenConstantsUtil.MEMORY_CACHE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.SAVE_SCALING_IMAGE_STRATEGY;

final class ImageFactory {

    private static final Object mLock = new Object();

    static ImageResult creteBitmapFromUrl(ImageResult pResult) {
        final String url = pResult.getRequest().getUrl();

        switch (Pen.getInstance().getTypeOfMemoryCache()) {
            case MEMORY_CACHE:
                synchronized (mLock) {

                    if (Pen.getInstance().getBitmapFromLruCache(url) != null) {
                        pResult.setBitmap(Pen.getInstance().getBitmapFromLruCache(url));
                        LogUtil.msg("Set bitmap from LruCache");

                        return pResult;
                    }
                }
                break;
            case INNER_FILE_CACHE:
                synchronized (mLock) {
                    final String name = getName(pResult);
                    final Bitmap bitmap = DiskCache.getInstance().loadBitmapFromDiskCache(name);

                    if (bitmap != null) {
                        pResult.setBitmap(bitmap);
                        LogUtil.msg("Set bitmap from DiskCache");

                        return pResult;
                    }
                }
                break;
            default:
                break;
        }

        pResult = creteBitmap(pResult);

        switch (Pen.getInstance().getTypeOfMemoryCache()) {
            case MEMORY_CACHE:

                synchronized (mLock) {
                    if (pResult.getException() == null) { //Checking  for no correct saving bmp
                        Pen.getInstance().addBitmapForLruCache(pResult.getRequest().getUrl(), pResult.getBitmap());
                    }
                }

                break;
            case INNER_FILE_CACHE:

                synchronized (mLock) {
                    final boolean resultOfSave;

                    final String name = getName(pResult);
                    final Bitmap bitmap = pResult.getBitmap();
                    resultOfSave = DiskCache.getInstance().saveBitmapInDiskCache(bitmap, name);
                    if (resultOfSave) {
                        LogUtil.msg("Save " + name + " to DiskCache");
                    }
                }

                break;
            default:
                break;
        }

        return pResult;
    }

    private static ImageResult creteBitmap(final ImageResult pResult) {
        final String url = pResult.getRequest().getUrl();
        final int reqHeight = pResult.getRequest().getHeight();
        final int reqWidth = pResult.getRequest().getWidth();

        try {
            int bytesRead;
            final InputStream stream = new URL(url).openStream();
            final ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream(stream.available());
            final byte[] arrayByte = new byte[KILOBYTE_SIZE];

            while ((bytesRead = stream.read(arrayByte)) > 0) {
                byteArrayInputStream.write(arrayByte, 0, bytesRead);
            }

            final byte[] bytes = byteArrayInputStream.toByteArray();

            if (Pen.getInstance().getStrategySaveImage() == SAVE_SCALING_IMAGE_STRATEGY) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                options.inJustDecodeBounds = false;
                options.inSampleSize = ImageFactory.calculateInSimpleSize(options, reqHeight, reqWidth);
                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                final int size = bmp.getByteCount();
                pResult.setBitmap(bmp);
            } else {
                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                LogUtil.msg("Size non scaled file = " + bmp.getByteCount());
                pResult.setBitmap(bmp);
            }

            return pResult;
        } catch (final IOException e) {
            pResult.setException(e);

            return pResult;
        }
    }

    private static int calculateInSimpleSize(final BitmapFactory.Options pOptions, final int pHeight, final int pWidth) {
        int outHeight = pOptions.outHeight;
        int outWidth = pOptions.outWidth;
        int inSimpleSize = 1;

        if (outHeight > pHeight || outWidth > pWidth) {
            outWidth = outWidth / 2;
            outHeight = outWidth / 2;

            while ((outWidth / inSimpleSize) >= pWidth && (outWidth / inSimpleSize) >= pHeight) {
                outHeight = outWidth / 2;
                outWidth = outWidth / 2;
                inSimpleSize *= 2;
            }
            Log.d(LOG, "inSimpleSize: " + inSimpleSize);
        }

        return inSimpleSize;
    }

    private static String getName(final ImageResult pResult) {
        return MD5Util.getHashString(pResult.getRequest().getUrl());
    }
}

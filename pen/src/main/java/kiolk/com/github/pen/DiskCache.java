package kiolk.com.github.pen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kiolk.com.github.pen.utils.LogUtil;

import static kiolk.com.github.pen.utils.PenConstantsUtil.DEFAULT_FILE_CACHE_SIZE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.IMAGE_CACHE_DESTINATION;
import static kiolk.com.github.pen.utils.PenConstantsUtil.KILOBYTE_SIZE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.STORAGE_FILE_FORMAT;

final class DiskCache {

    private static DiskCache sDiskCache;

    private long mAvailableCacheSize;
    private File mCacheDir;
    private long mCurrentSizeCache;

    private DiskCache() {
        mAvailableCacheSize = DEFAULT_FILE_CACHE_SIZE;
    }

    static DiskCache getInstance() {

        if (sDiskCache == null) {
            sDiskCache = new DiskCache();
            LogUtil.msg("Create object of DiskCache");
        }

        return sDiskCache;
    }

    void setUserCacheSize(final long userCacheSize) {
        this.mAvailableCacheSize = userCacheSize * KILOBYTE_SIZE * KILOBYTE_SIZE;
        LogUtil.msg("User size of cache = " + this.mAvailableCacheSize + " Mb");
    }

    boolean saveBitmapInDiskCache(final Bitmap pBitmap, final String pName) {
        FileOutputStream fileOutputStream = null;

        if (mCacheDir == null) {
            getCacheDir();
        }

        final File myPath = new File(mCacheDir, pName + STORAGE_FILE_FORMAT);
        myPath.setLastModified(System.currentTimeMillis());
        boolean isSaved = false;

        try {
            fileOutputStream = new FileOutputStream(myPath);
            pBitmap.compress(Bitmap.CompressFormat.PNG, Pen.getInstance().getQualityOfCompressionBmp(), fileOutputStream);
            isSaved = true;
        } catch (final Exception ignored) {

        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    keepSizeCacheFolder();
                }
            } catch (final IOException ignored) {
            }
        }

        return isSaved;
    }

    Bitmap loadBitmapFromDiskCache(final String pName) {
        final File myPath = new File(mCacheDir, pName + STORAGE_FILE_FORMAT);
        myPath.setLastModified(System.currentTimeMillis());
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(myPath));
        } catch (final FileNotFoundException ignored) {
        }

        return bitmap;
    }

    private void sizeCacheFolder() {
        final File[] listFiles = mCacheDir.listFiles();
        long sizeCache = 0;

        for (final File listFile : listFiles) {
            sizeCache += listFile.length();
            LogUtil.msg("Size of file " + listFile.getName() + " equal: " + listFile.length());
        }

        LogUtil.msg("Size of cache folder" + sizeCache);
        mCurrentSizeCache = sizeCache;
    }

    private void keepSizeCacheFolder() {
        sizeCacheFolder();
        if (mCurrentSizeCache > mAvailableCacheSize) {
            final File[] listFiles = mCacheDir.listFiles();
            final List<File> arrayFiles = new ArrayList<>();
            arrayFiles.addAll(Arrays.asList(listFiles));

            final Comparator<File> comparator = new Comparator<File>() {

                @Override
                public int compare(final File o1, final File o2) {
                    final String lastModificationFile1 = String.valueOf(o1.lastModified());
                    final String lastModificationFile2 = String.valueOf(o2.lastModified());

                    return lastModificationFile1.compareTo(lastModificationFile2);
                }
            };

            Collections.sort(arrayFiles, comparator);
            int i = 0;

            do {
                LogUtil.msg("File remove: " + arrayFiles.get(i).getName());
                final boolean deleteResult = arrayFiles.get(i).delete();

                if (deleteResult) {
                    arrayFiles.remove(i);
                    ++i;
                    sizeCacheFolder();
                } else {
                    break;
                }

            } while (mCurrentSizeCache > mAvailableCacheSize);
        }
    }

    private void getCacheDir() {
        final File cachePath = Pen.getInstance().getCacheDir();
        final File imageFolder = new File(cachePath, IMAGE_CACHE_DESTINATION);

        if (!imageFolder.exists()) {
            final boolean isDirCreated = imageFolder.mkdir();

            if (isDirCreated) {
                mCacheDir = imageFolder;
                LogUtil.msg("Folder ImageCache created");
            } else {
                mCacheDir = cachePath;
                LogUtil.msg("Continue write in direct cache directory;");
            }

        } else {
            mCacheDir = imageFolder;
        }
    }
}
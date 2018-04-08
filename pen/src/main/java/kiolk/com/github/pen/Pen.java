package kiolk.com.github.pen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import kiolk.com.github.pen.utils.PenConstantsUtil;
import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;
import kiolk.com.github.pen.utils.PlaceHolderUtil;

import static kiolk.com.github.pen.utils.PenConstantsUtil.DEFAULT_COMPRESSION;
import static kiolk.com.github.pen.utils.PenConstantsUtil.INNER_FILE_CACHE;
import static kiolk.com.github.pen.utils.PenConstantsUtil.MAX_COMPRESSION;
import static kiolk.com.github.pen.utils.PenConstantsUtil.MIN_COMPRESSION;
import static kiolk.com.github.pen.utils.PenConstantsUtil.SAVE_FULL_IMAGE_STRATEGY;
import static kiolk.com.github.pen.utils.PenConstantsUtil.SAVE_SCALING_IMAGE_STRATEGY;
import static kiolk.com.github.pen.utils.PenConstantsUtil.WITHOUT_CACHE;

public final class Pen {

    private File mCacheDir;

    private int mQualityOfCompressionBmp;
    private int mTypeOfMemoryCache;

    private static Pen sInstance;

    private final BlockingDeque<ImageRequest> mQueue;
    private final ExecutorService mExecutor;
    private LruCache<String, Bitmap> mBitmapLruCache;

    private int mStrategySaveImage;
    private final Builder mBuilder;

    private Pen() {
        mQueue = new LinkedBlockingDeque<>();
        mExecutor = Executors.newFixedThreadPool(3);
        mBuilder = new Builder();
        mTypeOfMemoryCache = WITHOUT_CACHE;
        mStrategySaveImage = SAVE_SCALING_IMAGE_STRATEGY;

        initialisationLruCache();
        DiskCache.getInstance();
        LogUtil.msg("Create object of Pen");
    }

    private void initialisationLruCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / PenConstantsUtil.KILOBYTE_SIZE);

        final int cacheSize = maxMemory / PenConstantsUtil.PART_OF_MEMORY_CACHE;

        mBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(final String key, final Bitmap value) {
                return value.getByteCount() / PenConstantsUtil.KILOBYTE_SIZE;
            }
        };
    }

    public static Pen getInstance() {
        if (sInstance == null) {
            sInstance = new Pen();
        }

        return sInstance;
    }

    int getTypeOfMemoryCache() {
        return mTypeOfMemoryCache;
    }

    File getCacheDir() {
        return mCacheDir;
    }

    int getQualityOfCompressionBmp() {
        return mQualityOfCompressionBmp;
    }

    int getStrategySaveImage() {
        return mStrategySaveImage;
    }

    public Builder getImageFromUrl(final String url) {
        return mBuilder.getBitmapFromUrl(url);
    }

    private void enqueue(final ImageRequest imageRequest) {

        final ImageView imageView = imageRequest.getTarget().get();

        if (PlaceHolderUtil.getInstance().getDefaultDrawable() != null) {
            imageView.setImageDrawable(PlaceHolderUtil.getInstance().getDefaultDrawable());
        }

        if (imageRequest.getUrl() == null) {
            return;
        }

        if (imageView == null) {
            LogUtil.msg("Target image view not exist");

            return;
        }

        if (imageHasSize(imageRequest)) {
            final String tag = MD5Util.getHashString(imageRequest.getUrl());
            imageView.setTag(tag);
            LogUtil.msg(" get image " + tag + " " + imageRequest.getUrl());
            mQueue.addFirst(imageRequest);
            LogUtil.msg("Image view" + imageRequest.getTarget().get() + " start setup");
            try {
                //TODO to use ExecutorService / executeOnExecutor
//                new ImageLoadingAsyncTask().execute(mQueue.takeFirst());
                new ImageLoadingAsyncTask().executeOnExecutor(mExecutor, mQueue.takeFirst());
            } catch (final InterruptedException ignored) {

            }
        } else {
            waiterImageViewShow(imageRequest);
        }
    }

    public Builder setLoaderSettings() {
        return mBuilder;
    }

    private boolean imageHasSize(final ImageRequest request) {

        if (request.getHeight() > 0 && request.getWidth() > 0) {
            return true;
        }

        final ImageView view = request.getTarget().get();

        if (view != null && view.getHeight() > 0 && view.getWidth() > 0) {
            final int viewHeight = view.getHeight();
            final int viewWidth = view.getWidth();

            request.setHeight(viewHeight);
            request.setWidth(viewWidth);

            return true;
        }

        return false;
    }

    private void waiterImageViewShow(final ImageRequest pRequest) {
        LogUtil.msg("Image view" + pRequest.getTarget().get() + " wait for draw");

        final ImageView viewWaitDraw = pRequest.getTarget().get();

        viewWaitDraw.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                final ImageView v = pRequest.getTarget().get();

                if (v == null) {
                    return true;
                }

                if (v.getWidth() > 0 && v.getHeight() > 0) {
                    LogUtil.msg("Image view" + pRequest.getTarget().get() + " start draw");

                    pRequest.setWidth(v.getWidth());
                    pRequest.setHeight(v.getHeight());
                    enqueue(pRequest);
                    v.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                v.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    //add bitmap for LruCache
    void addBitmapForLruCache(final String key, final Bitmap bitmap) {
        if (getBitmapFromLruCache(key) == null) {
            mBitmapLruCache.put(key, bitmap);
            LogUtil.msg("Add bitmap by key: " + key);
        }
    }

    //get bitmap from LruCache
    Bitmap getBitmapFromLruCache(final String key) {
        LogUtil.msg("Try bitmap by key " + key);

        return mBitmapLruCache.get(key);
    }

    public final class Builder {

        private Builder() {
        }

        private String mUrl;

        private void setUrl(final String mUrl) {
            this.mUrl = mUrl;
        }

        private Builder getBitmapFromUrl(final String url) {
            setUrl(url);

            return mBuilder;
        }

        public Builder setTypeOfCache(final int pTypeOfCache) {
            if (pTypeOfCache >= WITHOUT_CACHE && pTypeOfCache <= INNER_FILE_CACHE) {
                mTypeOfMemoryCache = pTypeOfCache;
            }

            return mBuilder;
        }

        public Builder setSavingStrategy(final int pTypeStrategy) {
            if (pTypeStrategy >= SAVE_SCALING_IMAGE_STRATEGY && pTypeStrategy <= SAVE_FULL_IMAGE_STRATEGY) {
                mStrategySaveImage = pTypeStrategy;
            }

            return mBuilder;
        }

        public void inputTo(final ImageView pView) {
            final WeakReference<ImageView> weakReference = new WeakReference<>(pView);
            final ImageRequest imageRequest = new ImageRequest(mBuilder.mUrl, weakReference);
            enqueue(imageRequest);
        }

        public Builder setSizeInnerFileCache(final Long pSizeMB) {
            DiskCache.getInstance().setUserCacheSize(pSizeMB);

            return mBuilder;
        }

        public Builder setContext(final Context pContext) {
            mCacheDir = pContext.getCacheDir();

            return mBuilder;
        }

        public Builder setDefaultDrawable(final Drawable pDefaultDrawable) {
            PlaceHolderUtil.getInstance().setDefaultDrawable(pDefaultDrawable);

            return mBuilder;
        }

        public Builder setErrorDrawable(final Drawable pErorDrawable) {

            PlaceHolderUtil.getInstance().setErrorDrawable(pErorDrawable);
            return mBuilder;
        }

        public Builder setQualityImageCompression(final int pCompression) {
            if (pCompression > MIN_COMPRESSION && pCompression <= MAX_COMPRESSION) {
                mQualityOfCompressionBmp = pCompression;
            }

            return mBuilder;
        }

        public void getBitmapDirect(final GetBitmapCallback pCallBack) {

            final ImageRequest request = new ImageRequest(mBuilder.mUrl, pCallBack);
            new ImageLoadingAsyncTask().executeOnExecutor(mExecutor, request);
        }

        public void setUp() {
            if (mQualityOfCompressionBmp == MIN_COMPRESSION) {
                mQualityOfCompressionBmp = DEFAULT_COMPRESSION;
            }
        }
    }
}
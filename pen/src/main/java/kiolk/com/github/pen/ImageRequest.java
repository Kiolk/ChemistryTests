package kiolk.com.github.pen;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

class ImageRequest {

    private String mUrl;
    private WeakReference<ImageView> mTarget;
    private int mWidth;
    private int mHeight;
    private GetBitmapCallback mBitmapCallback;

    ImageRequest(final String mUrl, final WeakReference<ImageView> mTarget) {
        this.mUrl = mUrl;
        this.mTarget = mTarget;
    }

    ImageRequest(final String mUrl, final GetBitmapCallback mBitmapCallback) {
        this.mUrl = mUrl;
        this.mBitmapCallback = mBitmapCallback;
    }

    String getUrl() {
        return mUrl;
    }

    void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    WeakReference<ImageView> getTarget() {
        return mTarget;
    }

    void setTarget(final WeakReference<ImageView> mTarget) {
        this.mTarget = mTarget;
    }

    int getWidth() {
        return mWidth;
    }

    void setWidth(final int mWidth) {
        this.mWidth = mWidth;
    }

    int getHeight() {
        return mHeight;
    }

    void setHeight(final int mHeight) {
        this.mHeight = mHeight;
    }

    GetBitmapCallback getBitmapCallback() {
        return mBitmapCallback;
    }

    public void setBitmapCallback(final GetBitmapCallback mBitmapCallback) {
        this.mBitmapCallback = mBitmapCallback;
    }
}
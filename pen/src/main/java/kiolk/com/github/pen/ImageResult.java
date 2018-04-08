package kiolk.com.github.pen;

import android.graphics.Bitmap;

class ImageResult {

    private ImageRequest mRequest;

    private Bitmap mBitmap;

    private Exception mException;

    ImageResult(final ImageRequest request) {
        this.mRequest = request;
    }

    ImageRequest getRequest() {
        return mRequest;
    }

    public void setRequest(final ImageRequest mRequest) {
        this.mRequest = mRequest;
    }

    Bitmap getBitmap() {
        return mBitmap;
    }

    void setBitmap(final Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    Exception getException() {
        return mException;
    }

    void setException(final Exception mException) {
        this.mException = mException;
    }
}

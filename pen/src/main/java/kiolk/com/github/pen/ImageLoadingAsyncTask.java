package kiolk.com.github.pen;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;
import kiolk.com.github.pen.utils.PlaceHolderUtil;

class ImageLoadingAsyncTask extends AsyncTask<ImageRequest, Void, ImageResult> {

    @Override
    protected ImageResult doInBackground(final ImageRequest... pImageRequests) {
        LogUtil.msg("Started thread :" + Thread.currentThread().getName());

        final ImageRequest request = pImageRequests[0];
        final ImageResult result = new ImageResult(request);

        return ImageFactory.creteBitmapFromUrl(result);
    }

    @Override
    protected void onPostExecute(final ImageResult pImageResult) {
        super.onPostExecute(pImageResult);

        final Bitmap bitmap = pImageResult.getBitmap();
        final GetBitmapCallback bitmapCallback = pImageResult.getRequest().getBitmapCallback();
        final WeakReference<ImageView> target = pImageResult.getRequest().getTarget();

        if (bitmap != null && bitmapCallback == null && target != null) {
            final ImageView imageView = target.get();
            final String tag = MD5Util.getHashString(pImageResult.getRequest().getUrl());

            if (imageView != null && imageView.getTag().equals(tag)) {
                imageView.setImageBitmap(bitmap);
                //TODO change size of targetView
//                imageView.getLayoutParams().height = bitmap.getHeight();
            }

        } else if (bitmap != null && bitmapCallback != null) {
            bitmapCallback.getBitmap(bitmap);
//            LogUtil.msg(target.get().getTag().toString());

        } else if (bitmap == null && target != null && target.get() != null) {
            final Drawable errorDrawable = PlaceHolderUtil.getInstance().getErrorDrawable();

            if (errorDrawable != null) {
                final ImageView imageView = target.get();
                imageView.setImageDrawable(errorDrawable);
                imageView.setContentDescription(pImageResult.getException().getMessage());
            }
        }
    }
}
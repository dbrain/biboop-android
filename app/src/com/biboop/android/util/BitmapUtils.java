package com.biboop.android.util;

import android.graphics.*;

public final class BitmapUtils {

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        paint.setShader(mBitmapShader);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        return output;
    }

}

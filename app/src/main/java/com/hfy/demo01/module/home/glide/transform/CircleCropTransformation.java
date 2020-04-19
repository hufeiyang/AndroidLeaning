package com.hfy.demo01.module.home.glide.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 图片 圆形化
 * @author hufeiyang
 */
public class CircleCropTransformation extends BitmapTransformation {
    public CircleCropTransformation(Context context) {
        super(context);
    }

    public CircleCropTransformation(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        //先算出原图宽度和高度中较小的值，因为对图片进行圆形化变换肯定要以较小的那个值作为直径来进行裁剪
        int diameter = Math.min(toTransform.getWidth(), toTransform.getHeight());

        //从Bitmap缓存池中尝试获取一个Bitmap对象来进行重用，如果没有可重用的Bitmap对象的话就创建一个(固定套路)
        final Bitmap toReuse = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        final Bitmap result;
        if (toReuse != null) {
            result = toReuse;
        } else {
            result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        }

        //圆形化变换:这里算出了画布的偏移值，并且根据刚才得到的直径算出半径来进行画圆
        int dx = (toTransform.getWidth() - diameter) / 2;
        int dy = (toTransform.getHeight() - diameter) / 2;
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP);
        if (dx != 0 || dy != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-dx, -dy);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float radius = diameter / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        //尝试将复用的Bitmap对象重新放回到缓存池当中，并将圆形化变换后的Bitmap对象进行返回(固定套路)
        if (toReuse != null && !pool.put(toReuse)) {
            toReuse.recycle();
        }
        return result;
    }

    @Override
    public String getId() {
        return "com.hfy.demo01.module.home.glide.transform.CircleCropTransformation";
    }
}

package net.nat123.wpt.zxlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * Created by wupeitao on 16/4/5.
 */
public class DrawableUtils {

    private static final int IMAGE_HALFWIDTH = 50;// 宽度值，影响中间图片大小

    /**
     * 生成二维码
     *
     * @param context 上下问对象
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * @return Bitmap 位图
     * @throws WriterException
     */
    public static Bitmap createCode(Context context, String string, Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);// 设置缩放信息
        // 将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hst = new Hashtable();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 设置字符编码
        BitMatrix matrix = writer.encode(string, format, dip2px(context, 300), dip2px(context, 300), hst);// 生成二维码矩阵信息
        int width = matrix.getWidth();// 矩阵高度
        int height = matrix.getHeight();// 矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];// 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {// 从行开始迭代矩阵
            for (int x = 0; x < width; x++) {// 迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {// 该位置用于存放图片信息
                    // 记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {// 如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;// 记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 生成二维码
     *
     * @param context 上下问对象
     * @param string  二维码中包含的文本信息
     * @throws WriterException
     */
    private static Bitmap createCode(Context context, String string) throws WriterException {
        int QR_WIDTH = dip2px(context, 300);
        int QR_HEIGHT = dip2px(context, 300);

        // 需要引入core包
        QRCodeWriter writer = new QRCodeWriter();
        if (string == null || "".equals(string) || string.length() < 1) {
            throw new NullPointerException();
        }
        // 把输入的文本转为二维码
        BitMatrix martix = writer.encode(string, BarcodeFormat.QR_CODE,
                QR_WIDTH, QR_HEIGHT);

        System.out.println("w:" + martix.getWidth() + "h:"
                + martix.getHeight());

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(string,
                BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
        for (int y = 0; y < QR_HEIGHT; y++) {
            for (int x = 0; x < QR_WIDTH; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * QR_WIDTH + x] = 0xff000000;
                } else {
                    pixels[y * QR_WIDTH + x] = 0xfff5f5f5;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        return bitmap;
    }

    public static String recode(Context context,String path) {
        Result result = scanningImage(path);
        // String result = decode(photo_path);
        if (result == null) {

//            Toast.makeText(context, "图片格式有误", 0)
//                    .show();
            return "";
        } else {
            if(TextUtils.isEmpty(result.toString()))
                return null;
            String formart = "";
            try {
                boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                        .canEncode(result.toString());
                if (ISO) {
                    formart = new String(result.toString().getBytes("ISO-8859-1"), "GB2312");
                } else {
                    formart = result.toString();
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                return null;
            }
            return formart;
        }
    }


    public static Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小

        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;


        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result decode = reader.decode(bitmap1, hints);
            return decode;
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}

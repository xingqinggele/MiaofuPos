package com.example.miaofupos.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者: qgl
 * 创建日期：2022/8/26
 * 描述:图片视频转换
 */
public class BitMapUtil {

    /*
     * bitmap转base64
     * */
//    public static String bitmapToBase64(Bitmap bitmap) {
//        String result = null;
//        ByteArrayOutputStream baos = null;
//        try {
//            if (bitmap != null) {
//                baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//                baos.flush();
//                baos.close();
//
//                byte[] bitmapBytes = baos.toByteArray();
//                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (baos != null) {
//                    baos.flush();
//                    baos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }


    /**
     * bitmap 转为 base64
     *
     * 说明：如果发现转换后的图片存在黑边，可以将图片格式改为 png
     *
     * @param bitmap Bitmap?            图片资源
     * @param compress Int              压缩比例，范围：0-100
     * @param format CompressFormat     图片格式：JPEG、PNG、WEBP、WEBP_LOSSY、WEBP_LOSSLESS
     * @return String?                  base64 字符串
     */
    public static String bitmap2Base64(Bitmap bitmap, int compress, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(format != null ? format : Bitmap.CompressFormat.JPEG, compress, baos);

            baos.flush();
            baos.close();

            byte[] bitmapBytes = baos.toByteArray();
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 图片文件转Base64字符串
     * @param path 文件所在的绝对路径加文件名　
     * @return
     */
    public static String fileBase64String(String path){
        try {
            FileInputStream fis = new FileInputStream(path);//转换成输入流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = fis.read(buffer)) >= 0){
                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中
            }
            fis.close();//关闭文件输入流
            String uploadBuffer = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));  //进行Base64编码
            return uploadBuffer;
        } catch (Exception e) {
            return null;
        }

    }


    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * base64字符串转视屏
     * videoFilePath  输出视频文件路径带文件名
     */
    public static void base64ToVideo(String base64) {
        try {
            //base解密
            byte[] videoByte = Base64.decode(base64.getBytes(),Base64.DEFAULT);
            File videoFile = new File(Environment.getExternalStorageDirectory()
                    + "/Convert.mp4");
            if (videoFile.exists()){
                videoFile.delete();
            }
            try {
                //创建文件
                videoFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("creatXMLFileException",e.getMessage());
            }

            //输入视频文件
            FileOutputStream fos = new FileOutputStream(videoFile);
            fos.write(videoByte, 0, videoByte.length);
            fos.flush();
            fos.close();
            Log.d("11111111111","视屏保存的地址--" + videoFile);
        } catch (IOException e) {
            Log.e("222222222222222","base64转换为视频异常",e);
        }
    }

    /**
     * 获取文件大小
     * @param path
     * @return
     */
    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }
} 
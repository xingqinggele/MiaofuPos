package com.example.miaofupos.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hw.videoprocessor.VideoProcessor;

import java.io.File;

/**
 * 作者: qgl
 * 创建日期：2022/8/24
 * 描述:视频压缩工具类
 */
public class VideoUtil {
    private static long start;
    private static long end;
    public static void compressVideo2(Context context, String path, Handler.Callback callback) {

        String compressPath = Constant.video_path + File.separator + "VID_" + System.currentTimeMillis() + ".mp4";
        new Thread(() -> {
            boolean success = true;
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                int originWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int originHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));

                int outWidth = originWidth / 2;
                int outHeight = originHeight / 2;
                start = System.currentTimeMillis();
                VideoProcessor.processor(context)
                        .input(path)
                        .output(compressPath)
                        .bitrate(bitrate / 2)//这个属性不加,有些小视频反而越压缩越大
                        //以下参数全部为可选
                        .outWidth(outWidth)
                        .outHeight(outHeight)
                        .bitrate(838860)

//                            .startTimeMs(startTimeMs)//用于剪辑视频
//                            .endTimeMs(endTimeMs)    //用于剪辑视频
//                            .speed(speed)            //改变视频速率，用于快慢放
//                            .changeAudioSpeed(changeAudioSpeed) //改变视频速率时，音频是否同步变化
//                            .bitrate(bitrate)       //输出视频比特率
//                            .frameRate(frameRate)   //帧率
//                            .iFrameInterval(iFrameInterval)  //关键帧距，为0时可输出全关键帧视频（部分机器上需为-1）
//                            .progressListener(listener)      //可输出视频处理进度
                        .process();
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }
            if (success) {
                end = System.currentTimeMillis();
                Log.e("aaa", "压缩耗时：" + (end - start) / 1000 + "秒");
                Log.e("aaa", "视频压缩后大小：" + new File(compressPath).length() / 1024 / 1024 + "MB");
                Log.e("aaa", "视频大小：" + new File(path).length() / 1024 / 1024 + "MB");
                Message message = Message.obtain();
                message.obj = compressPath;
                callback.handleMessage(message);
            } else {
            }
        }).start();


    }

} 
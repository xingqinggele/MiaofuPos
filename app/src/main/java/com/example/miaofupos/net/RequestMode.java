package com.example.miaofupos.net;


import com.example.miaofupos.bean.OrderBean;

import java.io.File;
import java.util.List;

/**
 * 创建： zb
 * 描述：请求模式
 */

public class RequestMode {

  /**
   * GET请求
   * @param url URL请求地址
   * @param params 入参
   * @param callback 回调接口
   * @param clazz 需要解析的实体类
   */
  public static void getRequest(String url, RequestParams params,
                                ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
        new ResposeDataHandle(callback, clazz));
  }

  public static void getRequest1(String url, RequestParams params,  String token,ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.get(CommonRequest.createGetRequest1(url,token, params),
            new ResposeDataHandle(callback, clazz));
  }

  public static void getRequest2(String url, RequestParams params,  String token,ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.get(CommonRequest.createGetRequest2(url,token, params),
            new ResposeDataHandle(callback, clazz));
  }

  /**
   * POST请求 转换的
   * @param url URL请求地址
   * @param params 入参
   * @param callback 回调接口
   * @param clazz 需要解析的实体类
   */
  public static void postRequest(String url, RequestParams params, String token,ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.post(CommonRequest.createPostRequest(url,token, params),
        new ResposeDataHandle(callback, clazz));
  }

  /**
   * POST请求 转换的 传输List
   * @param url URL请求地址
   * @param params 入参
   * @param callback 回调接口
   * @param clazz 需要解析的实体类
   */
  public static void postRequest2(String url, RequestParams params, String token, int[] data, ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.post(CommonRequest.createPostRequest2(url,token, params,data),
            new ResposeDataHandle(callback, clazz));
  }

  /**
   * POST请求 转换的 传输List
   * @param url URL请求地址
   * @param params 入参
   * @param callback 回调接口
   * @param clazz 需要解析的实体类
   */
  public static void postRequest3(String url, RequestParams params, String token, List<OrderBean>data, ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.post(CommonRequest.createPostRequest3(url,token, params,data),
            new ResposeDataHandle(callback, clazz));
  }
  /**
   * 下载图片 Get方式
   */
  public static void getLoadImg(String url, RequestParams params, String imgPath, ResponseByteCallback callback){
    CommonOkHttpClient.downLadImg(CommonRequest.createGetRequest(url, params),imgPath,callback);
  }

  /**
   * 下载图片 Post方式
   */
  public static void postLoadImg( String token,String url, RequestParams params, String imgPath, ResponseByteCallback callback){
    CommonOkHttpClient.downLadImg(CommonRequest.createPostRequest(url,token, params),imgPath,callback);
  }

  /**
   * 表单和媒体 图文混合qgl
   */
  public static void postMultipart(String url, RequestParams params,
                                   List<File> files, ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.post(CommonRequest.createMultipartRequest(url, params, files),
        new ResposeDataHandle(callback, clazz));
  }
/**
   * 表单和媒体 图文混合
   */
  public static void postMultipart_qgl(String url, RequestParams params,
                                       List<File> files, ResponseCallback callback, Class<?> clazz) {
    CommonOkHttpClient.post(CommonRequest.createMultipartRequest_qgl(url, params, files),
        new ResposeDataHandle(callback, clazz));
  }


}

package com.example.cameratest;

import android.app.Application;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class UploadFile extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志, 开启debug会影响性能.

    }

    public void upload(String url, String path){
        //String path="/sdcard/11.jpg";
        System.out.println(path);
        RequestParams params = new RequestParams(url);
        System.out.println(params);
        params.setMultipart(true);
        params.addBodyParameter("file",new File(path));
        System.out.println(params);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("aaaaaaaaa");
                System.out.println(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("0000");
                System.out.println(isOnCallback);
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("1111");
                System.out.println(cex.getMessage());
            }

            @Override
            public void onFinished() {
                System.out.println("2222");
            }
        });
    }
}

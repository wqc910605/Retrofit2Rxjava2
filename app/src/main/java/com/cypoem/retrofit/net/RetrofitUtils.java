package com.cypoem.retrofit.net;

import android.text.TextUtils;

import com.cypoem.retrofit.utils.LogUtils;
import com.cypoem.retrofit.utils.NetworkUtils;
import com.cypoem.retrofit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhpan on 2017/4/1.
 */

public class RetrofitUtils {
    private Retrofit retrofit;
    private ApiService service;
    private static String mBaseUrl;//基础地址

    private RetrofitUtils(String baseUrl) {
       /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    LogUtils.e("OKHttp-----", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    LogUtils.e("OKHttp-----", message);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(Utils.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiService.API_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if (TextUtils.isEmpty(baseUrl)) {
            builder.baseUrl(ApiService.API_SERVER_URL);
        } else if (baseUrl.startsWith("http://") || baseUrl.startsWith("http")) {
//            throw new RuntimeException("baseUrl不能为空");
            try {
                builder.baseUrl(baseUrl);
            } catch (Exception e) {
                throw new RuntimeException("baseUrl: "+baseUrl+" ,无效， 请填写一个有效的地址， 例如https://www.baidu.com");
            }
        } else {
            builder.baseUrl(ApiService.API_SERVER_URL);
        }
        retrofit = builder.build();
        service = retrofit.create(ApiService.class);
    }

    //  创建单例
    private static class SingletonHolder {
        private static final RetrofitUtils INSTANCE = new RetrofitUtils(mBaseUrl);
    }
    public static ApiService getApiService(String baseUrl) {
        mBaseUrl = baseUrl;
        return SingletonHolder.INSTANCE.service;
    }
    public static ApiService getApiService() {
        return SingletonHolder.INSTANCE.service;
    }

    static class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {  //没网强制从缓存读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("Okhttp", "no network");
            }


            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}

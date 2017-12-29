package com.cypoem.retrofit.net;

import com.cypoem.retrofit.bean.BaseBean;
import com.cypoem.retrofit.bean.LoginBean;
import com.cypoem.retrofit.bean.MeiZi;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wwf on 2017/4/1.
 */

public interface ApiService {
    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 20000;

    String HOST = "http://gank.io/";
    String API_SERVER_URL = HOST + "api/data/";


    @GET("福利/10/1")
    Observable<BaseBean<List<MeiZi>>> getMezi();

    /**
     * @param page
     * @param number
     * @return
     */
    @Headers("Cache-Control: public, max-age=100")//设置缓存 缓存时间为100s
    @GET("everySay/selectAll.do")
    Observable<BaseBean<String>> lookBack(@Query("page") int page, @Query("rows") int number);


   /* @POST("upload/uploadFile.do")
    Observable<BaseBean<String>> uploadFiles(@Part("filename") String description,
                                          @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
                                          @Part("pic\"; filename=\"image2.png") RequestBody imgs2);

    @POST("upload/uploadFile.do")
    Observable<BaseBean<String>> uploadFiles(@Part("filename") String description, @PartMap() Map<String, RequestBody> maps);*/



    /**
     * @param url 全路径, 和baseURL没有关系, baseUrl随便设置, 但是要有意义, 是一个真实的网址
     * @return
     */
    @GET
    Observable<BaseBean<String>> get(@Url String url);

    /**
     * 例如: https://snail-stg1.zysnail.com/snail/queryMyHomePage.do?sid=1&userId=123456&homepageId=SY00000001&os=IOS&versionNumber=100
     *
     * @param path1 代表snail
     * @param path2 代表selectLiveStatus.do
     * @return
     */
    @GET("{path1}/{path2}")
    Observable<BaseBean<String>> get(@Path("path1") String path1, @Path("path2") String path2);

    /**
     * 例如: https://snail-stg1.zysnail.com/snail?sid=1&userId=123456&homepageId=SY00000001&os=IOS&versionNumber=100
     *
     * @param path1  代表snail
     * @param parmas ?后的请求参数 是一个map集合
     * @return
     */
    @GET("{path1}")
    Observable<BaseBean<String>> get(@Path("path1") String path1, @QueryMap Map<String, String> parmas);

    /**
     * 例如: https://snail-stg1.zysnail.com/snail/queryMyHomePage.do?sid=1&userId=123456&homepageId=SY00000001&os=IOS&versionNumber=100
     *
     * @param path1  代表snail
     * @param path2  代表selectLiveStatus.do
     * @param parmas ?后的请求参数 是一个map集合
     * @return
     */
    @GET("{path1}/{path2}")
    Observable<BaseBean<String>> get(@Path("path1") String path1, @Path("path2") String path2, @QueryMap Map<String, String> parmas);

    //post请求

    /**
     * 参数含义详情, 请看对应的get请求
     *
     * @param path1
     * @param path2
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("{path1}/{path2}")
    Observable<BaseBean<String>> post(@Path("path1") String path1, @Path("path2") String path2, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{path1}")
    Observable<BaseBean<String>> post(@Path("path1") String path1, @FieldMap Map<String, String> params);
    ///snail/autoLogin.do?sid=1&userId=123456

    /**
     * 参数含义详情, 请看对应的get请求
     *
     * @param path1
     * @param path2
     * @param loginBean 用来封装请求参数的bean
     * @return
     */
    @POST("{path1}/{path2}")
    Observable<BaseBean<String>> post(@Path("path1") String path1, @Path("path2") String path2, @Body LoginBean loginBean);

    //    单文件上传方法(1) 适合参数少的
    @Multipart
    @POST("user/register.do")
    Observable<BaseBean<String>> upFile(@Part("phone") RequestBody phone, @Part("password") RequestBody password, @Part MultipartBody.Part image);

    /*
          单文件上传方法(1)， 使用方式
          File file = new File(picPath);
         //  图片参数
         RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
         MultipartBody.Part body = MultipartBody.Part.createFormData("uploadFile", file.getName(), requestFile);
         //  手机号参数
         RequestBody phoneBody = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
         //  密码参数
         RequestBody pswBody = RequestBody.create(MediaType.parse("multipart/form-data"), password);
         IdeaApi.getApiService()
         .register(phoneBody,pswBody,body)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(new DefaultObserver<BasicResponse<RegisterBean>>(this, true) {
         @Override
         public void onSuccess(BasicResponse<RegisterBean> response) {
         EventBus.getDefault().post(new RegisterSuccess("register success"));
         showToast("注册成功，请登陆");
         finish();
         }
         });
     */
    //    单文件上传方法(2) 适合参数多的
    @Multipart
    @POST("user/register.do")
    Observable<BaseBean<String>> register(@Part List<MultipartBody.Part> partList);
    /*
        单文件上传方法(2) 适合参数多的， 使用方式：
        File file = new File(picPath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("password", password)
                        .addFormDataPart("uploadFile", file.getName(), imageBody);
        List<MultipartBody.Part> parts = builder.build().parts();

        IdeaApi.getApiService()
               .register(parts)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new DefaultObserver<BasicResponse<RegisterBean>>(this, true) {
                   @Override
                   public void onSuccess(BasicResponse<RegisterBean> response) {
                       EventBus.getDefault().post(new RegisterSuccess("register success"));
                       showToast("注册成功，请登陆");
                       finish();
                   }
    */
    //    多文件传方法(1)
    @POST("upload/")
    Observable<BaseBean<String>> uploadFiles(@Part("filename") String description,
                                             @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
                                             @Part("pic\"; filename=\"image2.png") RequestBody imgs2);
    /*      多文件传方法(1)， 使用方式
            File file = new File(picPath);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part
                   .createFormData("uploadFile", file.getName(), requestFile);

            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part
                   .createFormData("uploadFile", file.getName(), requestFile);

            IdeaApi.getApiService()
                   .uploadFiles("pictures",requestFile1,requestFile2 )
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new DefaultObserver<BasicResponse<RegisterBean>>(this, true) {
                       @Override
                       public void onSuccess(BasicResponse<RegisterBean> response) {
                           EventBus.getDefault().post(new RegisterSuccess("register success"));
                           showToast("注册成功，请登陆");
                           finish();
                       }
     */
    //    多文件传方法(2), 采用map集合来存放多个图片RequestBody参数。
    @POST()
    Observable<BaseBean<String>> uploadFiles(@Part("filename") String description,
                                             @PartMap() Map<String, RequestBody> maps);
    /*
            多文件传方法(2), 使用方式
            File file = new File(picPath);
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part
                    .createFormData("uploadFile", file.getName(), requestFile);

            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part
                    .createFormData("uploadFile", file.getName(), requestFile);
            Map<String,RequestBody> map=new HashMap<>();
            map.put("文件1",requestFile1 );
            map.put("文件2",requestFile2 );

            IdeaApi.getApiService()
                   .uploadFiles("pictures",map)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new DefaultObserver<BasicResponse<RegisterBean>>(this, true) {
                       @Override
                       public void onSuccess(BasicResponse<RegisterBean> response) {
                           EventBus.getDefault().post(new RegisterSuccess("register success"));
                           showToast("注册成功，请登陆");
                           finish();
                       }
     */
}

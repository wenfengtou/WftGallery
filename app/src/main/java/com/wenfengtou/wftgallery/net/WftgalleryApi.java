package com.wenfengtou.wftgallery.net;

import com.wenfengtou.wftgallery.bean.Data;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Xiamin on 2017/2/12.
 */


public class WftgalleryApi {
    public static final String BASE_URL= "http://gank.io";

    /**
     * 每次加载条目
     */
    public static final int LOAD_LIMIT = 20;
    /**
     * 加载起始页面
     */

    public static WftgalleryApi instance;

    private final GankCloudService mWebService;

    public static WftgalleryApi getInstance() {
        if (null == instance) {
            synchronized (WftgalleryApi.class) {
                if (null == instance) {
                    instance = new WftgalleryApi();
                }
            }
        }
        return instance;
    }

    public WftgalleryApi() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mWebService = restAdapter.create(GankCloudService.class);
    }

    public interface GankCloudService {
        @GET("/api/data/福利/{limit}/{page}")
        Observable<Data> getBenefitsGoods(
                @Path("limit") int limit,
                @Path("page") int page
        );
    }

    public Observable<Data> getCommonGoods(String type, int limit, int page) {
            return mWebService.getBenefitsGoods(limit, page);

    }

    public GankCloudService getWebService(){
        return mWebService;
    }

}
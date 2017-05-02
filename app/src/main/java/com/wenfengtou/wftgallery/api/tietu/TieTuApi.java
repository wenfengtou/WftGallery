package com.wenfengtou.wftgallery.api.tietu;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import android.os.Handler;
import android.os.Message;


/**
 * 51贴图
 * M站版
 */
public class TieTuApi {

    private final static String TAG = "TieTuApi";
    private static  TieTuApi instance;
    private Callback mCallback;
    List<TieTuListBean> result;

    public static TieTuApi getInstance(){
        if(instance == null){
            synchronized (TieTuApi.class){
                if(instance == null){
                    instance = new TieTuApi();
                }
            }
        }
        return instance;
    }

    public  interface  Callback{
        void onCallback(List<TieTuListBean> result,boolean success);
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void syncGetTietuListByType(int page) {
        final String url = TieTuApi.TIETU_TYPE_DONGMAN + page;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).timeout(10000).get();
                    if (doc != null) {
                        TietuUtil util = new TietuUtil();
                        result = util.getTietuListByType(doc);
                    }
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCallback.onCallback(result,result!=null);
        }
    };

    // 首页
    public static final String TIETU_MAIN = "http://m.51tietu.net";

    // 图片广场
    public static final String TIETU_TYPE_P = "http://m.51tietu.net/p/0";

    // 小清新
    public static final String TIETU_TYPE_XIAOQINGXIN = "http://m.51tietu.net/xiaoqingxin/0";

    // 唯美
    public static final String TIETU_TYPE_TP = "http://m.51tietu.net/tp/";

    // lomo图片
    public static final String TIETU_TYPE_LOMO = "http://m.51tietu.net/lomo/0";

    // 非主流
    public static final String TIETU_TYPE_FZL = "http://m.51tietu.net/fzl/0";

    // 女生图片
    public static final String TIETU_TYPE_NVDR = "http://m.51tietu.net/nvdr/0";

    // 文字图片
    public static final String TIETU_TYPE_QMT = "http://m.51tietu.net/qmt/0";

    // QQ皮肤
    public static final String TIETU_TYPE_mk = "http://m.51tietu.net/mk/0";

    // 伤感图片
    public static final String TIETU_TYPE_SHANGAN = "http://m.51tietu.net/shangan/0";

    // 爱情图片
    public static final String TIETU_TYPE_AIQING = "http://m.51tietu.net/aiqing/0";

    // 搞笑图片
    public static final String TIETU_TYPE_GAOXIAO = "http://m.51tietu.net/gaoxiao/0";

    // 卡通图片
    public static final String TIETU_TYPE_KTTP = "http://m.51tietu.net/kttp/0";

    // 动漫图片
    public static final String TIETU_TYPE_DONGMAN = "http://m.51tietu.net/dongman/0";

    // 二次元
    public static final String TIETU_TYPE_CIYUAN = "http://m.51tietu.net/2ciyuan/0";

    // 素材图片
    public static final String TIETU_TYPE_YOUXIBIZHI = "http://m.51tietu.net/youxibizhi/0";

    // 美文
    public static final String TIETU_TYPE_WEITU = "http://m.51tietu.net/weitu/0";


    // 详情
//    public static final String TIETU_DETAIL1 = "http://m.51tietu.net/tp/42208.html";
    public static final String TIETU_DETAIL2 = "http://m.51tietu.net/weitu/42126.html";

}

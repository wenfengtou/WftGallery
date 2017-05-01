package com.wenfengtou.wftgallery.api.tietu;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TietuUtil {

    private static final String TAG = "TietuUtil";

    public List<TieTuListBean> getTietuListByType(Document doc) {
        List<TieTuListBean> tieTuListBeens = new ArrayList<>();
        Elements imgtcEles = doc.getElementsByClass("imgtc");
        Element imgtcEle = imgtcEles.get(0);

        if (imgtcEle != null) {

            Elements bannerLists = imgtcEle.select("li");

            for (int i = 0; i < bannerLists.size(); i++) {

                TieTuListBean tieTuListBean = new TieTuListBean();

                Element banners = bannerLists.get(i);

                Elements b_hrefs = banners.select("a");
                if (b_hrefs != null && b_hrefs.size() > 0) {
                    String href = "http://m.51tietu.net" + b_hrefs.get(0).attr("href");
                    String title = b_hrefs.get(0).attr("title");

                    tieTuListBean.title = title;
                    tieTuListBean.detailUrl = href;
                }

                Elements b_imgs = banners.select("img");
                if (b_imgs != null && b_imgs.size() > 0) {
                    String src = b_imgs.get(0).attr("src");

                    tieTuListBean.imgUrl = src;
                }

                tieTuListBeens.add(tieTuListBean);
            }


            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tieTuListBeens.size(); i++) {
                String data = tieTuListBeens.get(i).toString();
                sb.append(data).append("\n");
            }
        }

        return  tieTuListBeens;
    }

}

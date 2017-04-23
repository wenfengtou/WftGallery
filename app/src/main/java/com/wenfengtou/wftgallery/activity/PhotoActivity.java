package com.wenfengtou.wftgallery.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wenfengtou.wftgallery.R;
import com.wenfengtou.wftgallery.base.AppSwipeBackActivity;
import com.wenfengtou.wftgallery.utils.ImageSave;
import com.wenfengtou.wftgallery.view.PinchImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by Xiamin on 2017/3/2.
 */

public class PhotoActivity extends AppSwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "PhotoActivity";
    private static final String URL = "URL";

    @Bind(R.id.meizi_image)
    PinchImageView pinchImageView;
    @Bind(R.id.btn_back)
    ImageView mBtnBack;
    @Bind(R.id.btn_save)
    ImageView mBtnSave;

    private String mUrl;
    private Bitmap mBitmap;

    public static void startActivity(AppCompatActivity activity, String url, View transitionView) {
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra(URL, url);

        // 这里指定了共享的视图元素
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitionView, "image");

        ActivityCompat.startActivity(activity, intent, options.toBundle());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mBtnBack.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);
        Log.d(TAG, "url: " + mUrl);
        Glide.with(this)
                .load(mUrl)
                .asBitmap()
                .error(R.drawable.yingfei)
                .placeholder(R.drawable.yingfei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        pinchImageView.setImageBitmap(resource);
                        mBitmap = resource;
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                Log.d(TAG,"点击back,结束Activity");
                finish();
                overridePendingTransition(R.anim.out_to_bottom, 0);
                break;
            case R.id.btn_save:
                Log.d(TAG,"点击保存,保存图片");
                Toast.makeText(this, "保存图片", Toast.LENGTH_SHORT).show();
                ImageSave.with(getApplicationContext())
                        .save(mBitmap)
                        .setImageSaveListener(new ImageSave.ImageSaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(PhotoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(PhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }

    }
}

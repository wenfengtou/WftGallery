package com.wenfengtou.wftgallery.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wenfengtou.wftgallery.R;
import com.wenfengtou.wftgallery.activity.PhotoActivity;
import com.wenfengtou.wftgallery.api.tietu.TieTuListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wenfengtou on 17/5/1.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>{
    private static final String TAG = "ComicAdapter";
    private List<TieTuListBean> mDatas;
    private Context mContext;
    private List<Integer> heightList;//装产出的随机数
    public  ComicAdapter(Context context){
        mContext = context;
        mDatas = new ArrayList<>();
        //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
        heightList = new ArrayList<>();
    }

    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_fragment_item, parent, false);
        Log.i(TAG,"onCreateViewHolder");
        return new ComicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComicAdapter.ViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder");
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = heightList.get(position);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        holder.imageView.setLayoutParams(layoutParams);
        final TieTuListBean data = mDatas.get(position);
        if(data.imgUrl != null) {
            Log.i(TAG,"imgUrl is "+data.imgUrl);
            Glide.with(mContext)
                    .load(data.imgUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.yingfei);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.startActivity((AppCompatActivity) mContext,data.imgUrl,holder.imageView);
            }
        });
        //holder.textView.setText(data.getDesc());
    }

    @Override
    public int getItemCount() {
        Log.i(TAG,"getItemCount");
        return mDatas.size();
    }

    public void addData(List<TieTuListBean> datas) {
        this.mDatas.addAll(datas);
        Log.i(TAG,"addData size is"+mDatas.size());
        for (int i = 0; i <mDatas.size(); i++) {
            int height = new Random().nextInt(200) + 250;//[100,300)的随机数
            heightList.add(height);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            textView = (TextView) itemView.findViewById(R.id.item_title);
            textView.setVisibility(View.GONE);
        }
    }

}

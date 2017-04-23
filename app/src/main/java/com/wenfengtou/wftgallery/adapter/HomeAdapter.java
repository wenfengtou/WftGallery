package com.wenfengtou.wftgallery.adapter;

import android.content.ClipData;
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
import com.wenfengtou.wftgallery.bean.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 80119510 on 2017-04-17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Result> mDatas;
    private Context mContext;
    private List<Integer> heightList;//装产出的随机数
    public  HomeAdapter(Context context){
        mContext = context;
        mDatas = new ArrayList<>();
        //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
        heightList = new ArrayList<>();
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meizi_fragment_item, parent, false);
        Log.i("wenfeng","onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i("wenfeng","onBindViewHolder");
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = heightList.get(position);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        holder.imageView.setLayoutParams(layoutParams);
        final Result data = mDatas.get(position);
        if(data.getUrl() != null) {
            Glide.with(mContext)
                    .load(data.getUrl())
                   // .error(R.drawable.jay)
                    .centerCrop()
                   // .placeholder(R.drawable.bg_cyan)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.yingfei);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.startActivity((AppCompatActivity) mContext,data.getUrl(),holder.imageView);
            }
        });
        holder.textView.setText(data.getDesc());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(List<Result> datas) {
        Log.i("wenfeng","addData");
        this.mDatas.addAll(datas);
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
        }
    }

}

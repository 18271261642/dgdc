package com.jkcq.homebike.ride.freeride.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkcq.homebike.R;
import com.jkcq.util.AppUtil;
import com.jkcq.util.LoadImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
    private List<Integer> bgs = new ArrayList<>();
    Context mContext;


    public ViewPagerAdapter(Context context) {
        this.mContext = context;
        if (AppUtil.INSTANCE.isCN()) {
            bgs.add(R.mipmap.bg_guide_free_ch_one);
            bgs.add(R.mipmap.bg_guide_free_ch_two);
            bgs.add(R.mipmap.bg_guide_free_ch_three);
        } else {
            bgs.add(R.mipmap.bg_guide_free_en_one);
            bgs.add(R.mipmap.bg_guide_free_en_two);
            bgs.add(R.mipmap.bg_guide_free_en_three);
        }

    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_guide, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {

        LoadImageUtil.getInstance().load(mContext, bgs.get(position), holder.ivBg);
    }

    @Override
    public int getItemCount() {
        return bgs.size();
    }

    class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBg;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
        }
    }
}



package com.mi.layoutinspector;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/
class ViewAttributesAdapter extends RecyclerView.Adapter<ViewAttributesAdapter.ViewHolder> {
    private final String TAG = "FileListAdapter";
    private List<ViewAttribute> mDatas = null;

    public ViewAttributesAdapter() {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewInfo;
        View cliclableView;

        public ViewHolder(View view) {
            super(view);
            viewInfo = view.findViewById(R.id.item);
            cliclableView = view.findViewById(R.id.clickable);
        }
    }

    void setDatas(List<ViewAttribute> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutinspector_adapter_detail_view_item, parent, false);//加载view布局文件
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == holder) {
            return;
        }
        final ViewAttribute viewAttribute = mDatas.get(position);
        String name = viewAttribute.getName();
        String value = viewAttribute.getValue();
        holder.viewInfo.setText(name + ": " + value);

        if (viewAttribute.getOnClickListener() != null) {
            holder.itemView.setOnClickListener((v) -> {
                viewAttribute.getOnClickListener().onClick(v);
            });
            holder.cliclableView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setOnClickListener(null);
            holder.cliclableView.setVisibility(View.GONE);
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

}

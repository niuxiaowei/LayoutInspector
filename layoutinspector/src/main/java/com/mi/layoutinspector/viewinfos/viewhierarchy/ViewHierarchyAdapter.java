package com.mi.layoutinspector.viewinfos.viewhierarchy;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.layoutinspector.R;
import com.mi.layoutinspector.inspect.InspectItemView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/
public class ViewHierarchyAdapter extends RecyclerView.Adapter<ViewHierarchyAdapter.ViewHolder> {
    private final String TAG = "FileListAdapter";
    private List<HierarchyItem> mDatas = null;
    private InspectItemView inspectItemView;

    public ViewHierarchyAdapter() {
    }

    public void setInspectItemView(InspectItemView inspectItemView) {
        this.inspectItemView = inspectItemView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewDesc;
        TextView cliclableView;
        View layout;

        public ViewHolder(View view) {
            super(view);
            viewDesc = view.findViewById(R.id.view_desc);
            cliclableView = view.findViewById(R.id.label);
            layout = view.findViewById(R.id.layout);
        }
    }

    public void setDatas(List<HierarchyItem> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutinspector_adapter_hierarchy_view_item, parent, false);//加载view布局文件
        return new ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == holder) {
            return;
        }
        final HierarchyItem hierarchyItem = mDatas.get(position);
        //blank
        StringBuilder blankStr = new StringBuilder();
        for (int i = 0; i < hierarchyItem.getBlankCount(); i++) {
            blankStr.append(" ");
        }
        holder.viewDesc.setText(blankStr + "-" + hierarchyItem.getViewDesc());
        if (hierarchyItem.getInspectItemView() != null && !hierarchyItem.isSelected()) {
            holder.itemView.setOnClickListener(v -> {
                inspectItemView.hideViewInfosPopupWindown();
                hierarchyItem.getInspectItemView().showViewInfosPopupWindow();
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
        if (hierarchyItem.isSelected()) {
            holder.viewDesc.setTextColor( holder.layout.getResources().getColor(R.color.li_gift_number_second));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.cliclableView.setText(R.string.li_cur_view_text);
        }else{
            holder.viewDesc.setTextColor( holder.layout.getResources().getColor(R.color.li_black));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.cliclableView.setText(R.string.li_clickable_text);
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

}

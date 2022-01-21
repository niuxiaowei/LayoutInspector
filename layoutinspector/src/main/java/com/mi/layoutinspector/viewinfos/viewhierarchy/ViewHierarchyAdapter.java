package com.mi.layoutinspector.viewinfos.viewhierarchy;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
        TextView notClickableReason;

        public ViewHolder(View view) {
            super(view);
            viewDesc = view.findViewById(R.id.view_desc);
            cliclableView = view.findViewById(R.id.label);
            layout = view.findViewById(R.id.layout);
            notClickableReason = view.findViewById(R.id.not_clickable_reason);
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

        //收集不可点击的原因
        String notClickableReason = "";
        if (hierarchyItem.getInspectItemView().getSizeIsZero()) {
            notClickableReason = "(宽/高是0)";
        }
        if (hierarchyItem.getInspectItemView().isOutOfScreen()) {
            notClickableReason += " (超出屏幕)";
        }

        if (hierarchyItem.isSelected()) {
            holder.viewDesc.setTextColor(holder.layout.getResources().getColor(R.color.li_gift_number_second));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.cliclableView.setText(R.string.li_cur_view_text);
            holder.notClickableReason.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(notClickableReason)) {
            holder.notClickableReason.setText(notClickableReason);
            holder.notClickableReason.setVisibility(View.VISIBLE);
            holder.cliclableView.setVisibility(View.INVISIBLE);
            holder.viewDesc.setTextColor(holder.itemView.getResources().getColor(R.color.li_color_5a5a5a_60));
        } else {
            holder.viewDesc.setTextColor(holder.layout.getResources().getColor(R.color.li_black));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.cliclableView.setText(R.string.li_clickable_text);
            holder.notClickableReason.setVisibility(View.GONE);
        }

        if (hierarchyItem.getInspectItemView() != null && !hierarchyItem.isSelected() && TextUtils.isEmpty(notClickableReason)) {
            holder.itemView.setOnClickListener(v -> {
                inspectItemView.hideViewInfosPopupWindown();
                hierarchyItem.getInspectItemView().showViewInfosPopupWindow();
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

}

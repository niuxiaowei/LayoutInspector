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
import com.mi.layoutinspector.inspector.ViewInspector;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by niuxiaowei
 * date : 2021/7/30
 **/
public class ViewHierarchyAdapter extends RecyclerView.Adapter<ViewHierarchyAdapter.ViewHolder> {
    private final String TAG = "FileListAdapter";
    private List<HierarchyItem> mDatas = null;
    private ViewInspector viewInspector;

    public ViewHierarchyAdapter() {
    }

    public void setInspectItemView(ViewInspector viewInspector) {
        this.viewInspector = viewInspector;
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

        //收集不可点击的原因
        String notClickableReason = "";
        if (hierarchyItem.getViewInspector().getSizeIsZero()) {
            notClickableReason = " (宽/高是0)";
        }
        if (hierarchyItem.getViewInspector().isOutOfScreen()) {
            notClickableReason += " (超出屏幕)";
        }

        holder.viewDesc.setText(blankStr + "-" + hierarchyItem.getViewDesc() + notClickableReason);

        if (hierarchyItem.isSelected()) {
            holder.viewDesc.setTextColor(holder.layout.getResources().getColor(R.color.li_gift_number_second));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.cliclableView.setText(R.string.li_cur_view_text);
        } else if (!TextUtils.isEmpty(notClickableReason)) {
            holder.cliclableView.setVisibility(View.INVISIBLE);
            holder.viewDesc.setTextColor(holder.itemView.getResources().getColor(R.color.li_color_5a5a5a_60));
        } else {
            holder.viewDesc.setTextColor(holder.layout.getResources().getColor(R.color.li_black));
            holder.viewDesc.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.cliclableView.setText(R.string.li_clickable_text);
        }

        if (hierarchyItem.getViewInspector() != null && !hierarchyItem.isSelected() && TextUtils.isEmpty(notClickableReason)) {
            holder.itemView.setOnClickListener(v -> {
                viewInspector.hideViewInfosPopupWindown();
                hierarchyItem.getViewInspector().showViewInfosPopupWindow();
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

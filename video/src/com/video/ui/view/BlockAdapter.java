package com.video.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 1/13/15.
 */
public class BlockAdapter  extends RecyclerView.Adapter<BlockAdapter.ViewHolder> {
    Context mContext;
    private int mBackground;

    Block<DisplayItem> mBlockRoot;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;
        public View mItemView;

        public ViewHolder(View v) {
            super(v);
            mItemView = v;
        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }

    public BlockAdapter(Context context, Block<DisplayItem> rootblock) {
        mContext = context;
        TypedValue val = new TypedValue();
        if (context.getTheme() != null) {
            context.getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackground, val, true);
        }
        mBackground = val.resourceId;
        mBlockRoot = rootblock;
    }


    @Override
    public BlockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = null;
        if(mBlockRoot.items != null){
            view = ViewCreateFactory.CreateSingleView(mContext,mBlockRoot.items.get(position));
        } else if(mBlockRoot.blocks != null){
            view = ViewCreateFactory.CreateBlockView(mContext, mBlockRoot.blocks.get(position), 0);  //??? tag
        }

        final ViewHolder h = new ViewHolder(view);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        h.mItemView.setLayoutParams(lp);
        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    private int getBackgroundColor(int position) {
        switch (position % 4) {
            case 0: return Color.BLACK;
            case 1: return Color.RED;
            case 2: return Color.DKGRAY;
            case 3: return Color.BLUE;
        }
        return Color.TRANSPARENT;
    }

    @Override
    public int getItemCount() {
        if(mBlockRoot.items != null){
            return mBlockRoot.items.size();
        }
        else if(mBlockRoot.blocks != null){
            return mBlockRoot.blocks.size();
        }
        return 0;
    }

    public int getItemViewType(int position) {
        return position;
    }
}

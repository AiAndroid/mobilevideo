package com.video.ui.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.EpisodePlayAdapter;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.detail.*;
import com.video.ui.view.block.SelectItemsBlockView;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DetailFragment extends LoadingFragment {

    private static final String TAG = DetailFragment.class.getName();

    private VideoItem           mItem;
    private DetailPosterView    mPosterView;     //background
    private DetailInfoView      mInfoView;       //header viewer
    private DetailIntroduceView mIntroduceView;  //introduce
    private DetailCommentView   mCommentView;    //comments
    private RecommendBlockView mRecommendView;  //recommends videos
    private EpisodeContainerView mEpisodeView;    //for episode
    private BlockContainerView   relative_region;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView =" + this);
        View root = inflater.inflate(R.layout.detail_view, null);

        mEpisodeView   = (EpisodeContainerView) root.findViewById(R.id.detail_episode_view);
        mInfoView      = (DetailInfoView) root.findViewById(R.id.detail_info_view);
        mIntroduceView = (DetailIntroduceView) root.findViewById(R.id.detail_introduce_view);
        mCommentView   = (DetailCommentView) root.findViewById(R.id.detail_comment_view);
        mRecommendView = (RecommendBlockView) root.findViewById(R.id.detail_recommend_view);
        relative_region = (BlockContainerView) root.findViewById(R.id.relative_region);

        mPosterView    = (DetailPosterView) root.findViewById(R.id.detail_poster_view);

        ObserverScrollView mScrollView = (ObserverScrollView) root.findViewById(R.id.detail_scroll_view);
        if(mScrollView != null) {
            mScrollView.setOnScrollChangedListener(mOnScrollChangedListener);
        }
        return root;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        mItem = (VideoItem) getArguments().getSerializable("item");

        updateVideo(mItem);
    }

    public void updateVideo(VideoItem videoItem) {
        mItem = videoItem;
        if(mItem != null){
            if(mItem.media.items.size() <= 1){
                mEpisodeView.setVisibility(View.GONE);
            }else {
                mEpisodeView.setVideo(mItem);
            }

            mRecommendView.setVisibility(View.GONE);

            if(mItem.blocks == null || mItem.blocks == null || mItem.blocks.size() == 0){
                relative_region.setVisibility(View.GONE);
            }else{
                if(iDataORM.getBooleanValue(getActivity(), iDataORM.debug_mode, true)){
                    boolean hasAppRecommend = false;
                    for(Block<DisplayItem> block:mItem.blocks){
                        if(block.ui_type.id == LayoutConstant.app_grid || block.ui_type.id == LayoutConstant.app_list){
                            hasAppRecommend = true;
                            break;
                        }
                    }

                    if(hasAppRecommend == false) {
                        addRecommendApps(mItem);
                    }
                }
                relative_region.setVideo(mItem);
            }

            mInfoView.setVideo(mItem);

            if(TextUtils.isEmpty(mItem.media.description)){
                mIntroduceView.setVisibility(View.GONE);
            }else {
                mIntroduceView.setIntroduce(mItem.media.description);
            }

            mPosterView.setImageUrlInfo(mItem.media.poster);

            mCommentView.setVideoContent(mItem, getActivity());

            SelectItemsBlockView fv = (SelectItemsBlockView) EpisodePlayAdapter.findFilterBlockView(mEpisodeView);
            if(fv != null) {
                fv.setOnPlayClickListener(episodeClickListener, null);
            }
        }
    }

    private void addRecommendApps(VideoItem block){
        {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<DisplayItem>>();
            //add title
            item.blocks.add(createTitleBlock(getActivity(), "推荐应用"));


            //add content
            item.blocks.add(createAppsBlock());

            //add more button
            item.blocks.add(createLineBlock(getActivity(), "更多应用"));

            block.blocks.add(0, item);
        }
        {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<DisplayItem>>();
            //add title
            item.blocks.add(createTitleBlock(getActivity(), "小米彩票"));

            //add content
            item.blocks.add(createCaiPiaoBlock());
            //add more button
            item.blocks.add(createLineBlock(getActivity(), "进入小米彩票"));
            block.blocks.add(0, item);
        }

        {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<DisplayItem>>();
            //add title
            item.blocks.add(createTitleBlock(getActivity(), "影院电影"));


            //add single poster
            item.blocks.add(createMovieSinglePosterBlock());

            //add content
            item.blocks.add(createMovieBlock());

            //add more button
            item.blocks.add(createLineBlock(getActivity(), "进入小米生活——电影频道"));

            block.blocks.add(0, item);
        }
    }

    private ObserverScrollView.OnScrollChangedListener mOnScrollChangedListener = new ObserverScrollView.OnScrollChangedListener() {

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            int posterViewTranslateSpeed = 2;
            int scrollViewTopPadding = getActivity().getResources().getDimensionPixelSize(R.dimen.detail_scroll_top_padding);
            float scrollY = t;
            if(scrollY < 0) {
                scrollY = 0;
            } else if(scrollY > scrollViewTopPadding) {
                scrollY = scrollViewTopPadding;
            }
            float alpha = 1 - scrollY / scrollViewTopPadding;
            mPosterView.setTranslationY(scrollViewTopPadding / posterViewTranslateSpeed * (alpha - 1));
        }
    };


    View.OnClickListener episodeClickListener;
    public void setEpisodeClick(View.OnClickListener episodeClick) {
        episodeClickListener = episodeClick;
    }


    public void insertComment(DetailCommentView.VideoComments.VideoComment comment) {
        mCommentView.addNewComment(comment);
    }
}

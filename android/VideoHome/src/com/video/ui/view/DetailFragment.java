package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.view.detail.*;

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
    private DetailRecommendView mRecommendView;  //recommends videos
    private DetailEpisodeView   mEpisodeView;    //for episode

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView =" + this);
        View root = inflater.inflate(R.layout.detail_view, null);

        mEpisodeView   = (DetailEpisodeView) root.findViewById(R.id.detail_episode_view);
        mInfoView      = (DetailInfoView) root.findViewById(R.id.detail_info_view);
        mIntroduceView = (DetailIntroduceView) root.findViewById(R.id.detail_introduce_view);
        mCommentView   = (DetailCommentView) root.findViewById(R.id.detail_comment_view);
        mRecommendView = (DetailRecommendView) root.findViewById(R.id.detail_recommend_view);

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
        if(mItem != null){
            if(mItem.media.items.size() <= 1){
                mEpisodeView.setVisibility(View.GONE);
            }else {
                mEpisodeView.setVideo(mItem);
            }

            mInfoView.setVideo(mItem);
            mIntroduceView.setIntroduce(mItem.media.description);
            mPosterView.setImageUrlInfo(mItem.media.poster);
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
}

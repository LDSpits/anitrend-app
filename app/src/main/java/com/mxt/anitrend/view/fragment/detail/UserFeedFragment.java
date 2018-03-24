package com.mxt.anitrend.view.fragment.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mxt.anitrend.base.interfaces.event.PublisherListener;
import com.mxt.anitrend.model.entity.base.UserBase;
import com.mxt.anitrend.model.entity.container.request.QueryContainerBuilder;
import com.mxt.anitrend.util.KeyUtils;
import com.mxt.anitrend.view.fragment.index.FeedFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by max on 2017/11/26.
 * user profile targeted feeds
 */

public class UserFeedFragment extends FeedFragment implements PublisherListener<UserBase> {

    public static UserFeedFragment newInstance(Bundle params, QueryContainerBuilder queryContainer) {
        Bundle args = new Bundle(params);
        args.putParcelable(KeyUtils.arg_graph_params, queryContainer);
        UserFeedFragment fragment = new UserFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Override and set presenter, mColumnSize, and fetch argument/s
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            queryContainer.putVariable(KeyUtils.arg_userId, getArguments().getLong(KeyUtils.arg_id))
                    .putVariable(KeyUtils.arg_userName, getArguments().getLong(KeyUtils.arg_userName));
        }
        isMenuDisabled = true; isFeed = false;
    }

    @Override
    public void makeRequest() {
        if (queryContainer.containsVariable(KeyUtils.arg_userId) || queryContainer.containsVariable(KeyUtils.arg_userName))
            super.makeRequest();
    }

    /**
     * Responds to published events, be sure to add subscribe annotation
     *
     * @param userBase passed event
     * @see Subscribe
     */
    @Override @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEventPublished(UserBase userBase) {
        queryContainer.putVariable(KeyUtils.arg_userId, userBase.getId());
        if (model == null)
            onRefresh();
        else if (stateLayout.isLoading())
            updateUI();
    }
}
package com.hasawi.sears_outlet.ui.main.listeners;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerOnScrollAtEndListener extends RecyclerView.OnScrollListener {
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = false; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public RecyclerOnScrollAtEndListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy < 0) {
            return;
        }

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

        synchronized (this) {
            try {
                if (lastVisibleItem >= (totalItemCount - 1)) {
                    current_page++;
                    onLoadMore(current_page, totalItemCount, lastVisibleItem);
                    //loading = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public abstract void onLoadMore(int current_page, int totalItemCount, int lastVisibleItem);

}
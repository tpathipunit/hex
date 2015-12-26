package uk.co.longdivision.hex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import uk.co.longdivision.hex.adapter.FrontPageListAdapter;
import uk.co.longdivision.hex.asynctask.GetFrontPageItems;
import uk.co.longdivision.hex.asynctask.FrontPageItemsHandler;
import uk.co.longdivision.hex.decoration.DividerItemDecoration;
import uk.co.longdivision.hex.listener.ClickListener;
import uk.co.longdivision.hex.model.Item;
import uk.co.longdivision.hex.viewmodel.ItemListItemViewModel;
import uk.co.longdivision.hex.viewmodel.factory.ItemListItemFactory;


public class FrontPageActivity extends Activity implements FrontPageItemsHandler, ClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private List<? extends Item> mItems;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static String STORY_ID_INTENT_EXTRA_NAME = "storyId";
    private boolean mRefreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.front_page_layout);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        setupRefreshLayout();
        setupRecyclerView();
        fetchFrontPageItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onFrontPageItemsReady(List<? extends Item> items) {
        setRefreshing(false);
        mItems = items;
        List<ItemListItemViewModel> itemListItems = ItemListItemFactory.createItemListItems(items);
        RecyclerView.Adapter mAdapter = new FrontPageListAdapter(itemListItems, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        Intent storyIntent = new Intent(this, StoryActivity.class);
        storyIntent.putExtra(STORY_ID_INTENT_EXTRA_NAME, mItems.get(position).getId());
        startActivity(storyIntent);
    }

    @Override
    public void onRefresh() {
        setRefreshing(true);
        fetchFrontPageItems();
    }

    private void setupRecyclerView() {
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setHasFixedSize(true);
    }

    private void setupRefreshLayout() {
        mRefreshing = true;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(mRefreshing);
            }
        }, 500);
    }

    private void setRefreshing(boolean refreshing) {
        mRefreshing = refreshing;
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    private void fetchFrontPageItems() {
        GetFrontPageItems getFrontPageItems = new GetFrontPageItems(this, (HexApplication)
                this.getApplication());
        getFrontPageItems.execute();
    }
}

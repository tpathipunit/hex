package com.hexforhn.hex.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.hexforhn.hex.fragment.article.ArticleFragment;
import com.hexforhn.hex.fragment.comments.CommentsFragment;

import java.util.ArrayList;

public class StorySlidePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments;

    public StorySlidePagerAdapter(FragmentManager fm, String storyId) {
        super(fm);

        Bundle args = new Bundle();
        args.putString("STORY_ID", storyId);

        ArticleFragment article = new ArticleFragment();
        article.setArguments(args);
        CommentsFragment comments = new CommentsFragment();
        comments.setArguments(args);

        mFragments = new ArrayList<>();
        mFragments.add(article);
        mFragments.add(comments);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}


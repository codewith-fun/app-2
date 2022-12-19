package com.mcode.app2.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.mcode.app2.R;
import com.mcode.app2.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

public class TestFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static String KEY = "key";
    private final static String REFRESH_SUPPORT = "refresh_support";


    public static TestFragment newInstance(String desc, boolean refreshSupport) {
        Bundle args = new Bundle();
        args.putString(KEY, desc);
        args.putBoolean(REFRESH_SUPPORT, refreshSupport);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TestFragment newInstance(String desc) {
        return newInstance(desc, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_test, container, false);
        initView(root);
        initData();
        return root;
    }


    private void initView(ViewGroup root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.test_recycler);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setEnabled(getArguments().getBoolean(REFRESH_SUPPORT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    public void setRefreshEnable(boolean refreshEnable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(refreshEnable);
        }
    }

    private void initData() {
        String key = getArguments().getString(KEY, "default");
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            res.add(key + ":Fragment item :" + i);
        }
        mRecyclerView.setAdapter(new RecyclerViewAdapter(res).setOnItemClickListener(this));
    }

    @Override
    public void onItemClick(View view, int position) {
        Snackbar.make(mRecyclerView, position + ":click", Snackbar.LENGTH_SHORT).show();
    }
}
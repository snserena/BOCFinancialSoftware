package com.sf.bocfinancialsoftware.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.bocfinancialsoftware.R;

/**
 * 财务助手
 * Created by sn on 2017/6/7.
 */

public class FinancialAssistantFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_financial_assistant,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
    }

    private void initData() {
    }
}

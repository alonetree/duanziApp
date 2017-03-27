package cn.xdysite.duanzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xdysite.duanzi.R;

/**
 * Created by Administrator on 2017/3/19.
 */

public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onCreateView");
        return inflater.inflate(R.layout.test_layout, container, false);
    }
}

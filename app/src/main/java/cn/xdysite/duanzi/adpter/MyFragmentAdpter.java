package cn.xdysite.duanzi.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class MyFragmentAdpter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public  MyFragmentAdpter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentAdpter(FragmentManager fm, List<Fragment> _list) {
        super(fm);
        list = _list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}

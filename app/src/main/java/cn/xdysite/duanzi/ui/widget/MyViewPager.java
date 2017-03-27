package cn.xdysite.duanzi.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import cn.xdysite.duanzi.ui.view.NavBarIcon;

/**
 * Created by Administrator on 2017/3/25.
 */

public class MyViewPager extends ViewPager {
    NavBarIcon[] navBarIcons;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindNavBar(NavBarIcon[] _navBarIcons) {
        navBarIcons = _navBarIcons;
    }

    /**
     * 该方法在滑动屏幕时调用且会被调用多次,需要与导航栏同步
     * @param position 假如当前位置为0,但向右滑动时position值不变,当右边页面完全出来时position直接变为1
     *                 假如当前位置为1,向左滑动时position会马上变为0,这是左滑和右滑的区别
     * @param offset  取值范围0~1,从左往右滑动时慢慢变大到1,从右往左滑时慢慢变小到0
     * @param offsetPixels  偏移的像素点
     */
    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        //设置导航栏状态
        navBarIcons[position].setmAlphaRatio(1-offset);
        //可能会有数组越界,需要做判断
        if (position != 4)
            navBarIcons[position+1].setmAlphaRatio(offset);

    }


}

package cn.xdysite.duanzi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xdysite.duanzi.adpter.MyFragmentAdpter;
import cn.xdysite.duanzi.ui.fragment.BaseFragment;
import cn.xdysite.duanzi.ui.view.NavBarIcon;
import cn.xdysite.duanzi.ui.widget.MyViewPager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.nav_one) NavBarIcon one;
    @BindView(R.id.nav_two) NavBarIcon two;
    @BindView(R.id.nav_three) NavBarIcon three;
    @BindView(R.id.nav_four) NavBarIcon four;
    @BindView(R.id.nav_five) NavBarIcon five;
    NavBarIcon[] navBars;
    private MyViewPager vp;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Fragment> list =  new ArrayList<>();
        list.add(new BaseFragment());
        list.add(new BaseFragment());
        list.add(new BaseFragment());
        list.add(new BaseFragment());
        list.add(new BaseFragment());
        vp = (MyViewPager) findViewById(R.id.up_vp);
        vp.setAdapter(new MyFragmentAdpter(getSupportFragmentManager(), list));
        one.setmAlphaRatio(1.0f);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //为自定义View绑定监听器
        navBars = new NavBarIcon[]{one, two, three, four, five};
        int i = 0;
        for (NavBarIcon navBarIcon : navBars) {
            navBarIcon.setMid(i++);  //为每个View设置一个id
            navBarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavBarIcon nb = (NavBarIcon)v;
                    if (currentItem == (nb.getMid()))
                        return;
                    currentItem = nb.getMid();
                    clearViewState();
                    vp.setCurrentItem(nb.getMid(), false);
                }
            });
        }
        //为navBars与viewPager中的item建立映射关系
        vp.bindNavBar(navBars);
    }

    private void clearViewState() {
        for (NavBarIcon narBarIcon : navBars)
            narBarIcon.setmAlphaRatio(0.0f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package kr.fugle.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.fugle.Item.Content;
import kr.fugle.Item.User;
import kr.fugle.R;
import kr.fugle.rating.RatingActivity;

/**
 * Created by 김은진 on 2016-07-26.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public int[] tabIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_trending_up_white_24dp,
            R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_person_white_24dp
    };

    ArrayList<Content> contentArrayList;
    int pageNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent data = getIntent();
        JSONObject jsonObject;

        try {
            Log.d("--->","user json " + data.getStringExtra("user"));
            jsonObject = new JSONObject(data.getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // 탭 클릭시 indicator color
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        // 탭 클릭시 indicator height
        //tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setSelectedTabIndicatorHeight(10);
        setupTabIcons();

        // 추천 뷰용으로 arraylist 생성
        contentArrayList = new ArrayList<>();
        pageNo = 1;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TabStatusListener tabStatusListener = new TabStatusListener() {
            @Override
            public void setContentList(ArrayList<Content> list) {
                contentArrayList = list;
            }

            @Override
            public ArrayList<Content> getContentList() {
                return contentArrayList;
            }

            @Override
            public void setPageNo(int pageNum) {
                pageNo = pageNum;
            }

            @Override
            public int getPageNo() {
                return pageNo;
            }
        };

        TabFragment1 tabFragment1 = new TabFragment1();
        tabFragment1.setTabStatusListener(tabStatusListener);

        TabFragment2 tabFragment2 = new TabFragment2();
        tabFragment2.setTabStatusListener(tabStatusListener);

        TabFragment3 tabFragment3 = new TabFragment3();
        tabFragment3.setTabStatusListener(tabStatusListener);

        // 홈, 순위, 추천, 마이페이지
        adapter.addFragment(tabFragment1, "");
        adapter.addFragment(tabFragment2, "");
        adapter.addFragment(tabFragment3, "");
        adapter.addFragment(new TabFragment4(), "");
        viewPager.setAdapter(adapter);
    }

    public void onFragmentChanged(int index) {
        if(index == 0)  {
            Intent intent = new Intent(MainActivity.this, RatingActivity.class);
            //intent.putExtra("userNo", user.getNo());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }
}

package kr.fugle.main.tab2.webtoon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import kr.fugle.Item.Content;
import kr.fugle.Item.User;
import kr.fugle.R;
import kr.fugle.commonlist.CommonRecyclerAdapter;
import kr.fugle.main.ViewPagerAdapter;
import kr.fugle.webconnection.GetContentList;

/**
 * Created by 김은진 on 2016-08-12.
 */
public class WebtoonRankActivity extends AppCompatActivity {

    // 툴바, 탭레이아웃
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CountOfWebtoonRankFragment countOfWebtoonRankFragment;
    ScoreOfWebtoonRankFragment scoreOfWebtoonRankFragment;

    // 리스트뷰
    private ArrayList<Content> contentArrayList;
    private RecyclerView recyclerView;

    private CommonRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        contentArrayList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon_rank);

        // 툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("웹툰 랭킹!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // 탭 클릭시 indicator color
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        // 탭 클릭시 indicator height
        //tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setSelectedTabIndicatorHeight(10);
    }

    // 뒤로가기 버튼 눌렀을때 (backbutton)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 작가명, 작품명 입력받았을때 서버로 보냄
    // 파라미터에 맞는 리스트 받아옴
    public void performSearch() {
        contentArrayList.clear();
        new GetContentList(getApplicationContext(),
                contentArrayList,
                adapter,
                4,
                User.getInstance().getNo())
                .execute("mainPage/", User.getInstance().getNo() + "");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        countOfWebtoonRankFragment = new CountOfWebtoonRankFragment();
        scoreOfWebtoonRankFragment = new ScoreOfWebtoonRankFragment();

        // 평가갯수순, 평점순 랭킹
        adapter.addFragment(countOfWebtoonRankFragment, "평가갯수");
        adapter.addFragment(scoreOfWebtoonRankFragment, "평균평점");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }
}

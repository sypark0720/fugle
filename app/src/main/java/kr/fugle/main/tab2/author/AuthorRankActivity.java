package kr.fugle.main.tab2.author;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import kr.fugle.Item.Author;
import kr.fugle.R;
import kr.fugle.main.ViewPagerAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by 김은진 on 2016-08-16.
 */
public class AuthorRankActivity extends AppCompatActivity {

    // 툴바, 탭레이아웃
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CountOfAuthorRankFragment countOfAuthorRankFragment;
    ScoreOfAuthorRankFragment scoreOfAuthorRankFragment;

    // 리스트뷰
    private ArrayList<Author> authorArrayList1, authorArrayList2;

    // 서버 통신
    public final MediaType HTML = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    OkHttpClient client;
    String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_rank);

        // 툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("작가 랭킹!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // 탭 클릭시 indicator color
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        // 탭 클릭시 indicator height
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        countOfAuthorRankFragment = new CountOfAuthorRankFragment();
        scoreOfAuthorRankFragment = new ScoreOfAuthorRankFragment();

        // 평가갯수순, 평점순 랭킹
        adapter.addFragment(countOfAuthorRankFragment, "평가갯수");
        adapter.addFragment(scoreOfAuthorRankFragment, "평균평점");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

//    // 작가명, 작품명 입력받았을때 서버로 보냄
//    // 파라미터에 맞는 리스트 받아옴
//    public void performSearch() {
//        if (authorArrayList.isEmpty()) {
//            new GetAuthorList().execute("authorRank/", User.getInstance().getNo() + "");
//        }
//    }
//
//    private class GetAuthorList extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            Log.d("uwangg's activity", "GetAuthorList.doInBackground");
//
//            String data = "userId=" + params[1];
//            Log.d("uwangg's activity", "GetAuthorList data " + data);
//
//            RequestBody body = RequestBody.create(HTML, data);
//
//            Request request = new Request.Builder()
//                    .url(serverUrl + params[0])
//                    .post(body)
//                    .build();
//
//            // json 데이터가 담길 변수
//            String result = "";
//
//            try {
//                // 서버 통신 실행
//                Response response = client.newCall(request).execute();
//
//                // json 형태로의 변환을 위해 { "" :  } 추가
//                result = "{\"\":" + response.body().string() + "}";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.d("uwangg's activity", "GetAuthorList " + s);
//
//            if (s != null && s != "") {
//                try {
//                    // 통째로 받아들여서 하나씩 자르기 위한 json object
//                    JSONObject reader = new JSONObject(s);
//
//                    // 하나씩 잘라서 adapter에 저장해야 한다
//                    JSONArray dataList = reader.getJSONArray("");
//
//                    for (int i = 0; i < dataList.length(); i++) {
//                        JSONObject obj = dataList.getJSONObject(i);
//
//                        Author author = new Author();
//                        if (!obj.isNull("author")) {
//                            author.setName(obj.getString("author"));
//                        }
//                        if (!obj.isNull("average")) {
//                            author.setAvgStar((float) obj.getInt("average") / 1000);
//                        }
//                        if (!obj.isNull("count")) {
//                            author.setCountStar(obj.getInt("count"));
//                        }
//                        if()
//                        authorArrayList.add(author);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (authorArrayList.size() == 0)
//                return;
//
//            adapter.notifyDataSetChanged();
//        }
//    }
}

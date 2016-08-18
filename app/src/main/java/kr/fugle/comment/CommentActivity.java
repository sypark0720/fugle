package kr.fugle.comment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import kr.fugle.Item.User;
import kr.fugle.R;
import kr.fugle.webconnection.PostSingleData;

public class CommentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RatingBar ratingBar;
    private EditText commentInput;

    private int contentNo;
    private String title;
    private Float star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // 이전 액티비티에서 작품 번호, 작품 이름, 내 별점 받아옴
        Intent data = getIntent();
        contentNo = data.getIntExtra("contentNo", 0);
        title = data.getStringExtra("title");
        star = data.getFloatExtra("star", 0.0f);

        // 툴바 생성
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        commentInput = (EditText) findViewById(R.id.comment_input);

        ratingBar.setRating(star);
        ratingBar.setOnRatingBarChangeListener(onRatingBarChangeListener);

        commentInput.setHint(title + "에 대한 생각을 자유롭게 표현해주세요.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comment, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            int end = spanString.length();
            spanString.setSpan(new AbsoluteSizeSpan(30, true), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spanString.setSpan(new RelativeSizeSpan(1.5f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(spanString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.doneBtn:  // 완료버튼
                done();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private RatingBar.OnRatingBarChangeListener onRatingBarChangeListener =
            new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(!fromUser)
                        return;

                    // 별점 준 갯수 증가
                    if(rating == 0){
                        User.getInstance().setStars(User.getInstance().getStars() - 1);
                    }else if(star == 0){
                        User.getInstance().setStars(User.getInstance().getStars() + 1);
                    }

                    Integer Rating = (int)(rating * 10);

                    star = rating;

                    Toast.makeText(getApplicationContext(), "작품 번호 : " + contentNo + ", 별점 : " + Rating.toString(), Toast.LENGTH_SHORT).show();

                    new PostSingleData(CommentActivity.this)
                            .execute("insert/",
                                    User.getInstance().getNo() + "",
                                    contentNo + "",
                                    Rating.toString());
                }
            };

    // 코멘트 완료버튼
    private void done(){

        String comment = commentInput.getText().toString();

        if(star == 0){
            Toast.makeText(CommentActivity.this, "별점을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(comment.equals("")){
            Toast.makeText(CommentActivity.this, "코멘트를 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        new PostSingleData(CommentActivity.this)
                .execute("comment/",
                        User.getInstance().getNo() + "",
                        contentNo + "",
                        comment);

        Toast.makeText(CommentActivity.this, "입력되었습니다", Toast.LENGTH_SHORT).show();
        finish();
    }
}

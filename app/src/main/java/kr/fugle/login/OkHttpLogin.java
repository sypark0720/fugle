package kr.fugle.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.fugle.Item.ActivityStartListener;
import kr.fugle.Item.User;
import kr.fugle.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 김은진 on 2016-08-01.
 */
public class OkHttpLogin extends AsyncTask<String, Void, String> {

    ActivityStartListener activityStartListener;

    String serverUrl;
    public final MediaType HTML = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    Context context;

    public OkHttpLogin(Context context) {
        this.context = context;
        serverUrl = context.getResources().getString(R.string.server_url);
    }

    public void setActivityStartListener(ActivityStartListener activityStartListener) {
        this.activityStartListener = activityStartListener;
    }

    @Override
    protected String doInBackground(String... params) {
        // 서버로 보낼 사용자 데이터
        // server address, primary, name, password, message, profile
        String data = "primary=" + params[1] + "&name=" + params[2]
                + "&password=" + params[3] + "&message=" + params[4]
                + "&profile=" + params[5];
        Log.i("OkHttpLogin.data", data);

        RequestBody body = RequestBody.create(HTML, data);

        Request request = new Request.Builder()
                .url(serverUrl + params[0])
                .post(body)
                .build();

        Log.i("OkHttpLogin.request", request.toString());

        try {
            // 서버로 전송
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // 서버에서 로그인 성공여부 받음
        // 성공시 startActivity. 실패시 토스트 메세지
        Log.i("ho's activity", "LoginActivity.OkHttpLogin.onPostExecute " + s);

        if(s.equals("result:1")) {  // 로그인 실패시
            Toast.makeText(context, "존재하지 않는 이메일이거나 틀린비밀번호 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.equals("result:2")) {  // 회원가입 실패시
            Toast.makeText(context, "존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.equals("result:3")) {
            Toast.makeText(context, "Thank You!", Toast.LENGTH_SHORT).show();
            activityStartListener.activityStart();
            return;
        }

        JSONObject mypage, userInfo;

        User user = User.getInstance();

        try {
            JSONArray array = new JSONArray(s);
            mypage = array.getJSONObject(0);
            userInfo = array.getJSONObject(1);

            user.setAttributes(
                    userInfo.getInt("id"),
                    userInfo.getString("name"),
                    userInfo.getString("primary"),
                    userInfo.getString("profile"),
                    userInfo.getString("message")
            );

            if(!mypage.isNull("likecount"))
                user.setLikes(mypage.getInt("likecount"));
            if(!mypage.isNull("webtooncount"))
                user.setWebtoonStars(mypage.getInt("webtooncount"));
            if(!mypage.isNull("cartooncount"))
                user.setCartoonStars(mypage.getInt("cartooncount"));
            if(!mypage.isNull("dontseecount"))
                user.setHates(mypage.getInt("dontseecount"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "로그인에 실패하였습니다(서버 페이지 에러)", Toast.LENGTH_SHORT).show();

            if(activityStartListener != null)
                activityStartListener.activityFinish();

            return;
        } catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(context, "로그인에 실패하였습니다(서버 커넥션 에러)", Toast.LENGTH_SHORT).show();

            if(activityStartListener != null)
                activityStartListener.activityFinish();

            return;
        }

        if(activityStartListener != null)
            activityStartListener.activityStart();
    }
}

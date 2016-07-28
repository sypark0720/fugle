package kr.fugle.Item;

/**
 * Created by hokyung on 16. 7. 14..
 * 유저 정보를 갖고있기 위한 클래스
 */
public class User {

    private static User user;

    private int no;
    private String email;
    private String passwd;
    private String name;
    private String gender;
    private String primaryKey;
    private String profileImg;
    private String message;

    private User(){
        no = 0;
        email = passwd = name = gender = primaryKey = profileImg = message = "";
    }

    public static User getInstance(){

        // 앱 실행시
        if(user != null)
            return user;

        // 세션으로 불렸을 시
        user = new User();
        return user;
    }

    public void setAttributes(int no,
                              String name,
                              String primaryKey,
                              String profileImg,
                              String message){
        this.no = no;
        this.name = name;
        this.primaryKey = primaryKey;
        this.profileImg = profileImg;
        this.message = message;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

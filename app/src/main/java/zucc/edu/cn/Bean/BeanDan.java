package zucc.edu.cn.Bean;

/**
 * Created by Administrator on 2016/3/13.
 */
public class BeanDan {

    private String pic_url;
    private String name;
    private String time;
    private String sex;
    private String school;
    private String type;
    private String content;
    private int state;
    private int money;

    public BeanDan(){

    }
    public BeanDan (String pic_url, String name, String time, String sex, String school, String type, String content, int state, int money){
        this.pic_url = pic_url;
        this.name    = name;
        this.time    = time;
        this.sex     = sex;
        this.school  = school;
        this.type    = type;
        this.content = content;
        this.state   = state;
        this.money   = money;
    }
    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


}

package zucc.edu.cn.Bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by falling on 2016/4/16.
 */
public class AnswerBean {
    private int id;
    private int question_id;
    private Date time;
    private String student_number;
    private String content;
    private String sex;
    private String school;
    private String student_name;


    public AnswerBean(){}

    public AnswerBean(int question_id, String studentNumber, String content) {
        this.question_id = question_id;
        this.student_number = studentNumber;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public  String getUrl_getAnswers(){
        String res = "answer?" +
                "question_id="+question_id;
        return res;
    }

    public String getUrl_putAnswer(){

        String res = null;
        try {
            res = "answer?" +
                    "question_id=" +question_id+
                    "&studentNumber=" +student_number+
                    "&content="+ URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}

package zucc.edu.cn.Bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by falling on 2016/3/11.
 */
public class OrderBean implements Serializable {
    private int order_id;          //单号id
    private String release_student_number;   //发布人学号
    private Date time;           //发布时间
    private String startLocation;  //任务起始地点
    private String endLocation;       //交任务地点

    private String content;        //任务内容
    private String lable;          //任务标签（跑腿，代买。。。）
    private double tip;            // 小费
    private String acceptance_student_number;//接单人学号
    private double score;             //评分
    private String state;          //状态
    private String complaint_content; //举报内容
    private Date complaint_time;       //举报时间
    private String complaint_student_number;  //举报人
    private int MAX_SCORE = 5;


    private String student_name;   //名字
    private String sex;
    private String cell_phone;
    private String school;



    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }




    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getRelease_student_number() {
        return release_student_number;
    }

    public void setRelease_student_number(String release_student_number) {
        this.release_student_number = release_student_number;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String location) {
        this.endLocation = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public String getAcceptance_student_number() {
        return acceptance_student_number;
    }

    public void setAcceptance_student_number(String acceptance_student_number) {
        this.acceptance_student_number = acceptance_student_number;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComplaint_content() {
        return complaint_content;
    }

    public void setComplaint_content(String complaint_content) {
        this.complaint_content = complaint_content;
    }

    public Date getComplaint_time() {
        return complaint_time;
    }

    public void setComplaint_time(Date complaint_time) {
        this.complaint_time = complaint_time;
    }

    public String getComplaint_student_number() {
        return complaint_student_number;
    }

    public void setComplaint_student_number(String complaint_student_number) {
        this.complaint_student_number = complaint_student_number;
    }
    /**
     * 
     * @author Administrator
     * @time 2016/3/17 18:52
     * 生成 rleaseOrder 时的Url
     */
    public String getUrl_releaseOrder(){
        String res = "";
        try {
            res +="order?release_student_number="+ URLEncoder.encode(release_student_number, "UTF-8")
                    +"&startLocation="+URLEncoder.encode(startLocation, "UTF-8")
                    +"&endLocation="+URLEncoder.encode(endLocation, "UTF-8")
                    +"&content="+URLEncoder.encode(content, "UTF-8")
                    +"&lable="+URLEncoder.encode(lable, "UTF-8")
                    +"&tip="+tip;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     *  按时间加载 所有任务
     * @author Administrator
     * @time 2016/3/18 19:14
     *
     */
    public String getUrl_getOrderAll(){
        String res = "order";
        return res;
    }

    /**
     * 接受任务
     * @author Administrator
     * @time 2016/3/20 10:14
     *
     */
//    params
//            acceptance_student_number     //接任务的学生学号
//    order_id                      //任务ID
    public String getUrl_AcceptOrder(){
        String res = "";
        try {
            res += "order?acceptance_student_number="+ URLEncoder.encode(acceptance_student_number, "UTF-8")
                    +"&order_id="+order_id;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * 加载我的抢单
     * @author Administrator
     * @time 2016/3/20 12:34
     *
     */

    public String getUrl_MyOrder_grab(){
        String res = "";
        try {
            res += "order?acceptance_student_number="+ URLEncoder.encode(acceptance_student_number, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;

    }
    /**
     * 加载自己发的单
     * @author Administrator
     * @time 2016/3/21 17:17
     *
     */

    public String getUrl_MyOrder_releases(){
        String res = "";
            res += "order?release_student_number="+release_student_number;
        return res;

    }
    
    /**
     * 自己删除自己未被强的单
     * @author Administrator
     * @time 2016/3/21 17:16
     *
     */
    public String getUrl_deleteTask(){
        String res = "";
        res += "order?order_id="+order_id+"&release_student_number="+release_student_number;
        return res;
    }

    /**
     * 确认别人完成单
     * @author Administrator
     * @time 2016/3/21 17:18
     *
     */
    public String getUrl_finishTask(){
        String res = "";
        res +=  "order?order_id="+order_id+"&release_student_number="+release_student_number;
        return res;
    }

    /**
     * evaluat
     * @author Administrator
     * @time 2016/3/21 17:33
     *
     */
    public String getUrl_updateScore(){
        String res = "";
        res +=  "order?order_id="+order_id+"&release_student_number="+release_student_number + "&score="+score;
        return res  ;
    }

    /**
     * 更具标签获取单的信息
     * @author Administrator
     * @time 2016/3/28 15:22
     *
     */
    public String getUrl_getOrderByLable(){
        String res = "";
        try {
            res +=  "order?lable="+URLEncoder.encode(lable, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res  ;
    }

    /**
     * @return
     */
    public String getUrl_getOrderByLocation(){
        String res = "";
        try {
            res += "order?startLocation=" + URLEncoder.encode(startLocation, "UTF-8")+
                    "&endLocation=" + URLEncoder.encode(endLocation, "UTF-8")+
                    "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * @return 返回 complaint 的url
     */
    public String getUrl_Complaint(){
        String res = "";
        try {
            res += "order?order_id="+order_id+
                    "&complaint_content=" +URLEncoder.encode(complaint_content, "UTF-8")+
                    "&complaint_student_number=" + URLEncoder.encode(complaint_student_number,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}

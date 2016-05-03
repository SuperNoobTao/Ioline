package zucc.edu.cn.Bean;
/**
 * Created by falling on 2016/4/16.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@DatabaseTable(tableName = "tb_LocationBean")
public class LocationBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "school")
    private String school;
    @DatabaseField(columnName = "location")
    private String location;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 得到获取学校地点的url
     * @return
     */
    public String getUrl_SchoolLocation(){
        String result = "";
        try {
            result += "location?school="+URLEncoder.encode(school, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}

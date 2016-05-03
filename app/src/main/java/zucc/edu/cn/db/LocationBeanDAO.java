package zucc.edu.cn.db;

import android.content.Context;
import android.database.SQLException;
import android.location.Location;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import zucc.edu.cn.Bean.LocationBean;

/**
 * Created by Administrator on 2016/4/20.
 */
public class LocationBeanDAO {
    private Context context;

    private Dao<LocationBean, Integer> LocationDaoOpe;
    private DatabaseHelper helper;


    public LocationBeanDAO(Context context) {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            LocationDaoOpe = helper.getDao(LocationBean.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 增加一个
     *
     * @param locationBean
     * @throws SQLException
     */

    public  void add(LocationBean locationBean)
    {
        try
        {
            LocationDaoOpe.create(locationBean);
        } catch (SQLException e)
        {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param list
     */
    public  void addlist(List<LocationBean> list)
    {
        try {
            for (int i = 0;i < list.size(); i++) {
                LocationDaoOpe.create(list.get(i));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     *  删除所有的数据
     */
    public void deletAll(){
        try {
            LocationDaoOpe.delete( LocationDaoOpe.queryForAll());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 返回 所有的 location
     */
    public List<LocationBean> getAll(){
        try {
            return   LocationDaoOpe.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<LocationBean, Integer> getLocationDaoOpe() {
        return LocationDaoOpe;
    }


    public String[] getLocationToStringArg(List<LocationBean> tmp){
        List<String> stringList =  new ArrayList<String>();
        try {
            for (int i = 0; i<tmp.size(); i++){
                stringList.add(tmp.get(i).getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] strings = new String[stringList.size()];
        return stringList.toArray(strings);
    }




}

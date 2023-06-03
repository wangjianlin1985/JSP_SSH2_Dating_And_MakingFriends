package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.ActivityType;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.ActivityInfo;

@Service @Transactional
public class ActivityInfoDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private int rows = 5;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加ActivityInfo信息*/
    public void AddActivityInfo(ActivityInfo activityInfo) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(activityInfo);
    }

    /*查询ActivityInfo信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityInfo> QueryActivityInfoInfo(ActivityType typeObj,String title,String activityTime,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ActivityInfo activityInfo where 1=1";
    	if(null != typeObj && typeObj.getTypeId()!=0) hql += " and activityInfo.typeObj.typeId=" + typeObj.getTypeId();
    	if(!title.equals("")) hql = hql + " and activityInfo.title like '%" + title + "%'";
    	if(!activityTime.equals("")) hql = hql + " and activityInfo.activityTime like '%" + activityTime + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and activityInfo.userObj.user_name='" + userObj.getUser_name() + "'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List activityInfoList = q.list();
    	return (ArrayList<ActivityInfo>) activityInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityInfo> QueryActivityInfoInfo(ActivityType typeObj,String title,String activityTime,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ActivityInfo activityInfo where 1=1";
    	if(null != typeObj && typeObj.getTypeId()!=0) hql += " and activityInfo.typeObj.typeId=" + typeObj.getTypeId();
    	if(!title.equals("")) hql = hql + " and activityInfo.title like '%" + title + "%'";
    	if(!activityTime.equals("")) hql = hql + " and activityInfo.activityTime like '%" + activityTime + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and activityInfo.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List activityInfoList = q.list();
    	return (ArrayList<ActivityInfo>) activityInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityInfo> QueryAllActivityInfoInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From ActivityInfo";
        Query q = s.createQuery(hql);
        List activityInfoList = q.list();
        return (ArrayList<ActivityInfo>) activityInfoList;
    }
    
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityInfo> userQueryAllActivityInfoInfo(String user_name) {
        Session s = factory.getCurrentSession();
        String hql = "From ActivityInfo at where at.userObj.user_name='" + user_name + "'";
        Query q = s.createQuery(hql);
        List activityInfoList = q.list();
        return (ArrayList<ActivityInfo>) activityInfoList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(ActivityType typeObj,String title,String activityTime,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From ActivityInfo activityInfo where 1=1";
        if(null != typeObj && typeObj.getTypeId()!=0) hql += " and activityInfo.typeObj.typeId=" + typeObj.getTypeId();
        if(!title.equals("")) hql = hql + " and activityInfo.title like '%" + title + "%'";
        if(!activityTime.equals("")) hql = hql + " and activityInfo.activityTime like '%" + activityTime + "%'";
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and activityInfo.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List activityInfoList = q.list();
        recordNumber = activityInfoList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ActivityInfo GetActivityInfoByActivityId(int activityId) {
        Session s = factory.getCurrentSession();
        ActivityInfo activityInfo = (ActivityInfo)s.get(ActivityInfo.class, activityId);
        return activityInfo;
    }

    /*更新ActivityInfo信息*/
    public void UpdateActivityInfo(ActivityInfo activityInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(activityInfo);
    }

    /*删除ActivityInfo信息*/
    public void DeleteActivityInfo (int activityId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object activityInfo = s.load(ActivityInfo.class, activityId);
        s.delete(activityInfo);
    }

}

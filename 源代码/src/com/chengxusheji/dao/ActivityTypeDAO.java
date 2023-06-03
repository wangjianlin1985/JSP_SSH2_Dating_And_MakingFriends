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

@Service @Transactional
public class ActivityTypeDAO {

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

    /*添加ActivityType信息*/
    public void AddActivityType(ActivityType activityType) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(activityType);
    }

    /*查询ActivityType信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityType> QueryActivityTypeInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ActivityType activityType where 1=1";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List activityTypeList = q.list();
    	return (ArrayList<ActivityType>) activityTypeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityType> QueryActivityTypeInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ActivityType activityType where 1=1";
    	Query q = s.createQuery(hql);
    	List activityTypeList = q.list();
    	return (ArrayList<ActivityType>) activityTypeList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ActivityType> QueryAllActivityTypeInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From ActivityType";
        Query q = s.createQuery(hql);
        List activityTypeList = q.list();
        return (ArrayList<ActivityType>) activityTypeList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From ActivityType activityType where 1=1";
        Query q = s.createQuery(hql);
        List activityTypeList = q.list();
        recordNumber = activityTypeList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ActivityType GetActivityTypeByTypeId(int typeId) {
        Session s = factory.getCurrentSession();
        ActivityType activityType = (ActivityType)s.get(ActivityType.class, typeId);
        return activityType;
    }

    /*更新ActivityType信息*/
    public void UpdateActivityType(ActivityType activityType) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(activityType);
    }

    /*删除ActivityType信息*/
    public void DeleteActivityType (int typeId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object activityType = s.load(ActivityType.class, typeId);
        s.delete(activityType);
    }

}

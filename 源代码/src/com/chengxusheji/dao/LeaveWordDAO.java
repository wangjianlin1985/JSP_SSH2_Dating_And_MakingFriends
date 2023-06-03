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
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.LeaveWord;

@Service @Transactional
public class LeaveWordDAO {

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

    /*添加LeaveWord信息*/
    public void AddLeaveWord(LeaveWord leaveWord) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(leaveWord);
    }

    /*查询LeaveWord信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<LeaveWord> QueryLeaveWordInfo(String title,String addTime,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From LeaveWord leaveWord where 1=1";
    	if(!title.equals("")) hql = hql + " and leaveWord.title like '%" + title + "%'";
    	if(!addTime.equals("")) hql = hql + " and leaveWord.addTime like '%" + addTime + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and leaveWord.userObj.user_name='" + userObj.getUser_name() + "'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List leaveWordList = q.list();
    	return (ArrayList<LeaveWord>) leaveWordList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<LeaveWord> QueryLeaveWordInfo(String title,String addTime,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From LeaveWord leaveWord where 1=1";
    	if(!title.equals("")) hql = hql + " and leaveWord.title like '%" + title + "%'";
    	if(!addTime.equals("")) hql = hql + " and leaveWord.addTime like '%" + addTime + "%'";
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and leaveWord.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List leaveWordList = q.list();
    	return (ArrayList<LeaveWord>) leaveWordList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<LeaveWord> QueryAllLeaveWordInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From LeaveWord";
        Query q = s.createQuery(hql);
        List leaveWordList = q.list();
        return (ArrayList<LeaveWord>) leaveWordList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String title,String addTime,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From LeaveWord leaveWord where 1=1";
        if(!title.equals("")) hql = hql + " and leaveWord.title like '%" + title + "%'";
        if(!addTime.equals("")) hql = hql + " and leaveWord.addTime like '%" + addTime + "%'";
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and leaveWord.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List leaveWordList = q.list();
        recordNumber = leaveWordList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public LeaveWord GetLeaveWordByLeaveWordId(int leaveWordId) {
        Session s = factory.getCurrentSession();
        LeaveWord leaveWord = (LeaveWord)s.get(LeaveWord.class, leaveWordId);
        return leaveWord;
    }

    /*更新LeaveWord信息*/
    public void UpdateLeaveWord(LeaveWord leaveWord) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(leaveWord);
    }

    /*删除LeaveWord信息*/
    public void DeleteLeaveWord (int leaveWordId) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object leaveWord = s.load(LeaveWord.class, leaveWordId);
        s.delete(leaveWord);
    }

}

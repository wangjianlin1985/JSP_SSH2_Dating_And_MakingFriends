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
import com.chengxusheji.domain.ActivityInfo;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.SignUp;

@Service @Transactional
public class SignUpDAO {

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

    /*添加SignUp信息*/
    public void AddSignUp(SignUp signUp) throws Exception {
    	Session s = factory.getCurrentSession();
    	ActivityInfo at = signUp.getActivityObj();
    	at.setPersonNum(at.getPersonNum() + 1);
    	s.merge(at);
    	s.merge(signUp);
    }

    /*查询SignUp信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SignUp> QuerySignUpInfo(ActivityInfo activityObj,UserInfo userObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SignUp signUp where 1=1";
    	if(null != activityObj && activityObj.getActivityId()!=0) hql += " and signUp.activityObj.activityId=" + activityObj.getActivityId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and signUp.userObj.user_name='" + userObj.getUser_name() + "'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List signUpList = q.list();
    	return (ArrayList<SignUp>) signUpList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SignUp> QuerySignUpInfo(ActivityInfo activityObj,UserInfo userObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From SignUp signUp where 1=1";
    	if(null != activityObj && activityObj.getActivityId()!=0) hql += " and signUp.activityObj.activityId=" + activityObj.getActivityId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and signUp.userObj.user_name='" + userObj.getUser_name() + "'";
    	Query q = s.createQuery(hql);
    	List signUpList = q.list();
    	return (ArrayList<SignUp>) signUpList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<SignUp> QueryAllSignUpInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From SignUp";
        Query q = s.createQuery(hql);
        List signUpList = q.list();
        return (ArrayList<SignUp>) signUpList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(ActivityInfo activityObj,UserInfo userObj) {
        Session s = factory.getCurrentSession();
        String hql = "From SignUp signUp where 1=1";
        if(null != activityObj && activityObj.getActivityId()!=0) hql += " and signUp.activityObj.activityId=" + activityObj.getActivityId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and signUp.userObj.user_name='" + userObj.getUser_name() + "'";
        Query q = s.createQuery(hql);
        List signUpList = q.list();
        recordNumber = signUpList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public SignUp GetSignUpBySignId(int signId) {
        Session s = factory.getCurrentSession();
        SignUp signUp = (SignUp)s.get(SignUp.class, signId);
        return signUp;
    }

    /*更新SignUp信息*/
    public void UpdateSignUp(SignUp signUp) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(signUp);
    }

    /*删除SignUp信息*/
    public void DeleteSignUp (int signId) throws Exception {
        Session s = factory.getCurrentSession(); 
        SignUp signUp = (SignUp)s.get(SignUp.class, signId);
        ActivityInfo at = signUp.getActivityObj();
        at.setPersonNum(at.getPersonNum() - 1);
        s.merge(at);
        s.delete(signUp);
    }

}

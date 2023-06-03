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

import com.chengxusheji.domain.Admin;
import com.chengxusheji.domain.UserInfo;

@Service @Transactional
public class UserInfoDAO {

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

    /*添加UserInfo信息*/
    public void AddUserInfo(UserInfo userInfo) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(userInfo);
    }

    /*查询UserInfo信息*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryUserInfoInfo(String user_name,String name,String birthday,String telephone,String registerTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From UserInfo userInfo where 1=1";
    	if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
    	if(!name.equals("")) hql = hql + " and userInfo.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) hql = hql + " and userInfo.telephone like '%" + telephone + "%'";
    	if(!registerTime.equals("")) hql = hql + " and userInfo.registerTime like '%" + registerTime + "%'";
    	 Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.rows;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.rows);
    	List userInfoList = q.list();
    	return (ArrayList<UserInfo>) userInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryUserInfoInfo(String user_name,String name,String birthday,String telephone,String registerTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From UserInfo userInfo where 1=1";
    	if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
    	if(!name.equals("")) hql = hql + " and userInfo.name like '%" + name + "%'";
    	if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) hql = hql + " and userInfo.telephone like '%" + telephone + "%'";
    	if(!registerTime.equals("")) hql = hql + " and userInfo.registerTime like '%" + registerTime + "%'";
    	Query q = s.createQuery(hql);
    	List userInfoList = q.list();
    	return (ArrayList<UserInfo>) userInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<UserInfo> QueryAllUserInfoInfo() {
        Session s = factory.getCurrentSession();
        String hql = "From UserInfo";
        Query q = s.createQuery(hql);
        List userInfoList = q.list();
        return (ArrayList<UserInfo>) userInfoList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String user_name,String name,String birthday,String telephone,String registerTime) {
        Session s = factory.getCurrentSession();
        String hql = "From UserInfo userInfo where 1=1";
        if(!user_name.equals("")) hql = hql + " and userInfo.user_name like '%" + user_name + "%'";
        if(!name.equals("")) hql = hql + " and userInfo.name like '%" + name + "%'";
        if(!birthday.equals("")) hql = hql + " and userInfo.birthday like '%" + birthday + "%'";
        if(!telephone.equals("")) hql = hql + " and userInfo.telephone like '%" + telephone + "%'";
        if(!registerTime.equals("")) hql = hql + " and userInfo.registerTime like '%" + registerTime + "%'";
        Query q = s.createQuery(hql);
        List userInfoList = q.list();
        recordNumber = userInfoList.size();
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public UserInfo GetUserInfoByUser_name(String user_name) {
        Session s = factory.getCurrentSession();
        UserInfo userInfo = (UserInfo)s.get(UserInfo.class, user_name);
        return userInfo;
    }

    /*更新UserInfo信息*/
    public void UpdateUserInfo(UserInfo userInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(userInfo);
    }

    /*删除UserInfo信息*/
    public void DeleteUserInfo (String user_name) throws Exception {
        Session s = factory.getCurrentSession(); 
        Object userInfo = s.load(UserInfo.class, user_name);
        s.delete(userInfo);
    } 
	
	/*保存业务逻辑错误信息字段*/
	private String errMessage;
	public String getErrMessage() { return this.errMessage; }
	
	
	
	/*验证相亲用户登录*/
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean CheckLogin(String userName, String password){ 
		Session s = factory.getCurrentSession();  
		UserInfo db_user = (UserInfo)s.get(UserInfo.class, userName);
		if(db_user == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_user.getPassword().equals(password)) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		} else if(!db_user.getUserType().equals("相亲用户")) {
			this.errMessage = " 你不是相亲用户! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	
	/*验证用户登录*/
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean CheckLogin(Admin admin) { 
		Session s = factory.getCurrentSession(); 

		UserInfo db_user = (UserInfo)s.get(UserInfo.class, admin.getUsername());
		if(db_user == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_user.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_user.getUserType().equals("相亲组织者")) {
			this.errMessage = " 你不是相亲组织者! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
	
	
}

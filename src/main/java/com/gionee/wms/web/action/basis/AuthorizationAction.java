package com.gionee.wms.web.action.basis;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.entity.Authorization;
import com.gionee.wms.entity.User;
import com.gionee.wms.web.action.CrudActionSupport;

@Controller("AuthorizationAction")
@Scope("prototype")
public class AuthorizationAction extends CrudActionSupport<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8697580045674095911L;
	private List<Authorization> authorizationList;

	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareInput() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareUpdate() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepareAdd() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public List<Authorization> getAuthorizationList() {
		return authorizationList;
	}

	public void setAuthorizationList(List<Authorization> authorizationList) {
		this.authorizationList = authorizationList;
	}

}

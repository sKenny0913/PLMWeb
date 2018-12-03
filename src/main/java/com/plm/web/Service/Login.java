package com.plm.web.Service;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IUser;
import com.qsi.agile.ConnectAgileUtil;

@Service
public class Login  {
	
	public IAgileSession agileLogin(String sUID, String sPW, HttpSession session) {
		// TODO Auto-generated method stub
		IAgileSession agileSession = null;
		try {
			//check if user has privilege
			agileSession = ConnectAgileUtil.connect23(sUID, sPW);
			IUser user = (IUser)agileSession.getObject(IUser.OBJECT_TYPE, sUID);
			session.setAttribute("agileSession", agileSession); 
			session.setAttribute("user", user); 
		} catch (APIException e) {
			// TODO Auto-generated catch block
		}
		return agileSession; 
		
		
	}

}

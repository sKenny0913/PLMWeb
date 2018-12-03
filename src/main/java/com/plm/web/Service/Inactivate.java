package com.plm.web.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IUser;
import com.agile.api.UserConstants;
import com.plm.common.ConnectionUtil;

@Service
public class Inactivate {

	public String inactivate(IAgileSession agileSession, String sUsername, String sDesc)
			throws SQLException, APIException {
		// TODO Auto-generated method stub
		String sResult = "";
		System.out.println("Connecting 24 DB...");
		Connection conn = ConnectionUtil.getConnectionUtil().getProductConnection();
		System.out.println("Connecting 24 DB done");
		String strStatement = null;
		StringBuffer sb = new StringBuffer();

		strStatement = "select loginid,email from agileuser where lower(email) like '%" + sUsername.toLowerCase()
				+ "%'";
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		ResultSet rsUser;
		rsUser = conn.prepareStatement(strStatement).executeQuery();
		if (!rsUser.next()) {
			sResult = "user not exist";
			return sResult;
		}

//				while (rsUser.next()) {
		IUser user = (IUser) agileSession.getObject(IUser.OBJECT_TYPE, rsUser.getString("LOGINID"));
		if (user.getValue(UserConstants.ATT_GENERAL_INFO_STATUS).toString().equals("Active")) {
			user.setValue(UserConstants.ATT_GENERAL_INFO_STATUS, "Inactive");
			sResult = "user " + user.getName() + " has been inactivated";
//						System.out.println(user.getName() + " has been inactivated.");
			if (user.getValue(UserConstants.ATT_PAGE_TWO_MULTITEXT10).toString().equals(null)) {
				user.setValue(UserConstants.ATT_PAGE_TWO_MULTITEXT10, sDesc);
			} else {
				sb.append(user.getValue(UserConstants.ATT_PAGE_TWO_MULTITEXT10));
//							System.out.println(sb);
				sb.append("\n" + sDesc);
				user.setValue(UserConstants.ATT_PAGE_TWO_MULTITEXT10, sb.toString());
//							System.out.println(sb);
			}
//						System.out.println(user.getName() + " is now Inactive.");
		} else {
			sResult = user.getName() + " had been inactive";
		}
//				}
		return sResult;
	}

}

package com.plm.web.Service;

import org.springframework.stereotype.Service;

import com.agile.api.IAdmin;
import com.agile.api.IAdminList;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.IListLibrary;

@Service
public class ListMaintain {

	public String listMaintain(IAgileSession agileSession, String sListName, String sListValue) throws Exception {
		
		String sResult = null;
//		if (agileSession == null) {
//			sResult = "please login first";
//			return sResult;
//		}
//		// Get the Admin instance
//		IAdmin admin = agileSession.getAdminInstance();
//		// Get the List Library
//		IListLibrary listLib = admin.getListLibrary();
//		// Get the Product Lines list
//		IAdminList iAdminList = listLib.getAdminList(sListName);
//		// Add values to the list
//		IAgileList iAgileList = iAdminList.getValues();
//		Object[] children = iAgileList.getChildren();
//		for (int j = 0; j < children.length; j++) { // check if value exists
//			IAgileList listChildren = (IAgileList) children[j];
//			if (listChildren.getValue().equals(sListValue)) {
//				sResult= sListValue + " has already exist.";
//				return sResult;
//			}
//		}
//
//			iAgileList.addChild(sListValue);
//			iAdminList.setValues(iAgileList);
//			sResult = sListValue + " has been added.";
//
		return sResult;
//
	}
}

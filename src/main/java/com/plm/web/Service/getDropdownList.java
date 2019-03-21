package com.plm.web.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.agile.api.APIException;
import com.agile.api.IAdmin;
import com.agile.api.IAdminList;
import com.agile.api.IAgileSession;
import com.agile.api.IListLibrary;

@Service
public class getDropdownList {

	public String getDropdownList(IAgileSession agileSession) throws APIException, JSONException {
		String sResult = null;
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		
		IAdmin admin = agileSession.getAdminInstance();// Get the Admin instance
		IListLibrary listLib = admin.getListLibrary();	// Get the List Library
		IAdminList[] iAdminList = listLib.getAdminLists();	// Get the Product Lines list
		JSONArray ja = new JSONArray();
		for (IAdminList list : iAdminList) {
			JSONObject jo = new JSONObject();
			jo.put("listName", list.getName());
			ja.put(jo);
		}
		JSONObject joR = new JSONObject();
		joR.put("joR", ja);
		return joR.toString();

	}
}

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
		// Get the Admin instance
		IAdmin admin = agileSession.getAdminInstance();
		// Get the List Library
		IListLibrary listLib = admin.getListLibrary();
		// Get the Product Lines list
		IAdminList[] iAdminList = listLib.getAdminLists();
		JSONArray ja = new JSONArray();
		for (IAdminList list : iAdminList) {
//			System.out.println(list.getName());
			JSONObject jo = new JSONObject();
			jo.put("listName", list.getName());
			ja.put(jo);
//			System.out.println(ja);
		}
		JSONObject joR = new JSONObject();
		joR.put("joR", ja);

		return joR.toString();

	}
}

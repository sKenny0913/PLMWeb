package com.plm.web.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.agile.api.APIException;
import com.agile.api.IAdmin;
import com.agile.api.IAdminList;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.IListLibrary;

@Service
public class ListValueMaintain {

	public String getListValue(IAgileSession agileSession, String listName) throws APIException, JSONException {

		String sResult = null;
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		
		IAdmin admin = agileSession.getAdminInstance();// Get the Admin instance
		IListLibrary listLib = admin.getListLibrary();	// Get the List Library
		IAdminList iAdminList = listLib.getAdminList(listName);	// Get the Product Lines list
		IAgileList iAgileList = iAdminList.getValues();	// Add values to the list
		Object[] children = iAgileList.getChildren();//get all list values
		JSONArray ja = new JSONArray();
		for (Object obj : children) {//put in JSON

			IAgileList listChildren = (IAgileList) obj;
			JSONObject jo = new JSONObject();
			jo.put("Name", listChildren.getValue());
			if (listChildren.isObsolete() == false) {
				jo.put("Active", "Active");
			} else {
				jo.put("Active", "Inactive");
			}
			ja.put(jo);
		}
		JSONObject joR = new JSONObject();
		joR.put("joR", ja);

		return joR.toString();

	}

	public String addListValue(IAgileSession agileSession, String listName, String listValue)
			throws APIException, JSONException {

		String sResult = null;
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		IAdmin admin = agileSession.getAdminInstance();
		IListLibrary listLib = admin.getListLibrary();
		IAdminList iAdminList = listLib.getAdminList(listName);
		IAgileList iAgileList = iAdminList.getValues();
		Object[] children = iAgileList.getChildren();

		for (Object obj : children) {//check if value already exist
			IAgileList listChildren = (IAgileList) obj;
			if (listChildren.getValue().equals(listValue)) {
				sResult = listValue + " already exist";
				return sResult;
			}
		}

		iAgileList.addChild(listValue);//add list value
		iAdminList.setValues(iAgileList);
		sResult = listValue + " added";

		return sResult;

	}

	public String updateListValue(IAgileSession agileSession, String listName, String oListValue, String nListValue)
			throws APIException, JSONException {

		String sResult = null;
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		IAdmin admin = agileSession.getAdminInstance();
		IListLibrary listLib = admin.getListLibrary();
		IAdminList iAdminList = listLib.getAdminList(listName);
		IAgileList iAgileList = iAdminList.getValues();
		IAgileList children = (IAgileList) iAgileList.getChild(oListValue);
		children.setValue(nListValue);//update list value
		iAdminList.setValues(iAgileList);
		sResult = nListValue + " updated";

		return sResult;

	}

	public String deleteListValue(IAgileSession agileSession, String listName, String listValue)
			throws APIException, JSONException {

		String sResult = null;
		if (agileSession == null) {
			sResult = "please login first";
			return sResult;
		}
		IAdmin admin = agileSession.getAdminInstance();
		IListLibrary listLib = admin.getListLibrary();
		IAdminList iAdminList = listLib.getAdminList(listName);
		IAgileList iAgileList = iAdminList.getValues();
		iAgileList.removeChild(listValue);// delete list value
		iAdminList.setValues(iAgileList);
		sResult = listValue + " deleted";

		return sResult;

	}
}

package com.plm.web.Controller;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IUser;
import com.plm.web.Model.LoginSession;
import com.plm.web.Service.CompareService;
import com.plm.web.Service.ProductLine;
import com.qsi.agile.CheckSDKAuthority;
import com.plm.web.Service.Login;
import com.plm.web.Service.Inactivate;
import com.plm.web.Service.ListMaintain;
import com.plm.web.Service.getDropdownList;
import com.plm.web.Service.getListValue;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/PLMWeb" })
public class WebRestController {

	IAgileSession agileSession = null;

	@Autowired
	CompareService CompareService;
	@Autowired
	ProductLine ProductLine;
	@Autowired
	Login Login;
	@Autowired
	Inactivate Inactivate;
	@Autowired
	ListMaintain ListMaintain;
	@Autowired
	getDropdownList getDropdownList;
	@Autowired
	getListValue getListValue;

	@CrossOrigin
	@RequestMapping(value = { "/compareCKDID/{PN}&{CPN}" }, method = { RequestMethod.GET }, produces = {
			"application/json" })
	public String CompareCKDID(@ModelAttribute("message") String msg, @PathVariable String PN,
			@PathVariable String CPN) {
		JSONObject sCompareResultCKDID = null;
		try {
			sCompareResultCKDID = this.CompareService.compareCKDID(PN, CPN);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sCompareResultCKDID);
		return sCompareResultCKDID.toString();
	}

	@CrossOrigin
	@RequestMapping(value = { "/productLine" }, method = { RequestMethod.GET })
	public String ProductLine(@ModelAttribute("message") String msg, @RequestParam String productCode) {
		String sResult = this.ProductLine.add(productCode);
		System.out.println(productCode + " added");
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/login" }, method = { RequestMethod.POST })
	public String Login(@ModelAttribute("LoginSession") LoginSession LS, HttpSession session) throws APIException {
		agileSession = this.Login.agileLogin(LS.getUsername(), LS.getPassword(), session);
		CheckSDKAuthority sdkTool = new CheckSDKAuthority(
				(IUser) agileSession.getObject(IUser.OBJECT_TYPE, LS.getUsername()));
		if (sdkTool.CheckAuthorityInUserRole("Administrator")) {
//			System.out.println("insufficient privilege");
//			throw new Exception(sbResult.toString());
			return "Administrator";
		} else if (sdkTool.CheckAuthorityInUserRole("A01. CCB Admin")) {
			return "A01. CCB Admin";
		}
		return agileSession.getCurrentUser().getName().toString();
	}

	@CrossOrigin
	@RequestMapping(value = { "/inactivate/{username}&{desc}" }, method = { RequestMethod.GET })
	public String Inactivate(@ModelAttribute("message") String msg, @PathVariable String username,
			@PathVariable String desc) {
		String sResult;
		try {
			sResult = this.Inactivate.inactivate(agileSession, username, desc);
		} catch (SQLException e) {
			return e.getMessage();
		} catch (APIException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/listMaintain/{listName}&{listValue}" }, method = { RequestMethod.GET })
	public String ListMaintain(@ModelAttribute("message") String msg, @PathVariable String listName,
			@PathVariable String listValue) throws APIException {
		String sResult;
		try {
			sResult = this.ListMaintain.listMaintain(agileSession, listName, listValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		return sResult;
	}
	@CrossOrigin
	@RequestMapping(value = { "/getDropdownList" }, method = { RequestMethod.GET }, produces = {
	"application/json" })
	public String getDropdownList(@ModelAttribute("message") String msg) throws APIException {
		String sResult;
		try {
			sResult = this.getDropdownList.getDropdownList(agileSession);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		return sResult;
	}
	@CrossOrigin
	@RequestMapping(value = { "/getListValue/{listName}" }, method = { RequestMethod.GET }, produces = {
	"application/json" })
	public String getListValue(@ModelAttribute("message") String msg, @PathVariable String listName) throws APIException {
		String sResult = null;
		try {
			sResult = this.getListValue.getListValue(agileSession, listName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		return sResult;
	}
}

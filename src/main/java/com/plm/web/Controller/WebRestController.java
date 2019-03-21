package com.plm.web.Controller;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IUser;
import com.plm.web.Model.LoginSession;
import com.plm.web.Service.CompareService;
import com.plm.web.Service.ProductLine;
import com.qsi.agile.CheckSDKAuthority;
import com.qsi.agile.ConnectAgileUtil;
import com.plm.web.Service.Login;
import com.plm.web.Service.Inactivate;
import com.plm.web.Service.getDropdownList;
import com.plm.web.Service.ListValueMaintain;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
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
	IAgileSession agileSession1 = null;

	
	
	@Autowired
	CompareService CompareService;
	@Autowired
	ProductLine ProductLine;
	@Autowired
	Login Login;
	@Autowired
	Inactivate Inactivate;
	@Autowired
	getDropdownList getDropdownList;
	@Autowired
	ListValueMaintain ListValueMaintain;


	@PostConstruct
	public void Initial() {//build connection with PLM when deployment for EF-PLM add product line use
		try {
			agileSession1 = ConnectAgileUtil.connect23("it_plmweb", "Plmweb123");
		} catch (APIException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
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
		String sResult = this.ProductLine.add(productCode, agileSession1);
		System.out.println(productCode + " added");
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/login" }, method = { RequestMethod.POST })
	public String Login(@ModelAttribute("LoginSession") LoginSession LS, HttpSession session) {
		String sResult;
		agileSession = this.Login.agileLogin(LS.getUsername(), LS.getPassword(), session);
		CheckSDKAuthority sdkTool;
		try {
			sResult = agileSession.getCurrentUser().getName().toString();
			sdkTool = new CheckSDKAuthority((IUser) agileSession.getObject(IUser.OBJECT_TYPE, LS.getUsername()));

			if (sdkTool.CheckAuthorityInUserRole("Administrator")) {
//			System.out.println("insufficient privilege");
//			throw new Exception(sbResult.toString());
				return "Administrator";
			} else if (sdkTool.CheckAuthorityInUserRole("A01. CCB Admin")) {
				return "A01. CCB Admin";
			}
		} catch (APIException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/inactivate/{username}&{desc}" }, method = { RequestMethod.GET })
	public String Inactivate(@ModelAttribute("message") String msg, @PathVariable String username,
			@PathVariable String desc) {
		String sResult;
		try {
			sResult = this.Inactivate.inactivate(agileSession, username, desc);
		} catch (SQLException | APIException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/getDropdownList" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public String getDropdownList() {
		String sResult;
		try {
			sResult = this.getDropdownList.getDropdownList(agileSession);
		} catch (APIException | JSONException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/getListValue/{listName}" }, method = { RequestMethod.GET }, produces = {
			"application/json" })
	public String getListValue(@PathVariable String listName) {
		String sResult = null;
		try {
			sResult = this.ListValueMaintain.getListValue(agileSession, listName);
		} catch (APIException | JSONException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/getListValue/{listName}&{listValue}" }, method = { RequestMethod.POST })
	public String postListValue(@PathVariable String listName,
			@PathVariable String listValue) {
		String sResult = null;
		try {
			sResult = this.ListValueMaintain.addListValue(agileSession, listName, listValue);
		} catch (APIException | JSONException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/getListValue/{listName}&{oListValue}&{nListValue}" }, method = { RequestMethod.PUT })
	public String putListValue(@PathVariable String listName,
			@PathVariable String oListValue, @PathVariable String nListValue) {
		String sResult = null;
		try {
			sResult = this.ListValueMaintain.updateListValue(agileSession, listName, oListValue, nListValue);
		} catch (APIException | JSONException e) {
			return e.getMessage();
		}
		return sResult;
	}

	@CrossOrigin
	@RequestMapping(value = { "/getListValue/{listName}&{listValue}" }, method = { RequestMethod.DELETE })
	public String deleteListValue(@ModelAttribute("message") String msg, @PathVariable String listName,
			@PathVariable String listValue) {
		String sResult = null;
		try {
			sResult = this.ListValueMaintain.deleteListValue(agileSession, listName, listValue);
		} catch (APIException | JSONException e) {
			return e.getMessage();
		}
		System.out.println("111 " + sResult);
		return sResult;
	}
}

package com.plm.web.Service;

import org.springframework.stereotype.Service;

import com.agile.api.APIException;
import com.agile.api.IAdmin;
import com.agile.api.IAdminList;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.IListLibrary;
import com.qsi.agile.ConnectAgileUtil;

@Service
public class ProductLine {

	public String add(String sPCode) {
		String sResult = "";
		try {
			IAgileSession agileSession = ConnectAgileUtil.connect23("it_plmweb", "Plmweb123");

			// Get the Admin instance
			IAdmin admin = agileSession.getAdminInstance();
			// Get the List Library
			IListLibrary listLib = admin.getListLibrary();
			// Get the Product Lines list
			IAdminList listProdLineforChange = listLib.getAdminList("Product Line");
			IAdminList listProdLineforItem = listLib.getAdminList("Product_Line");
			// Add values to the list
			IAgileList listValuesforChange = listProdLineforChange.getValues();
			Object[] children1 = listValuesforChange.getChildren();
			IAgileList listValuesforItem = listProdLineforItem.getValues();
			Object[] children2 = listValuesforItem.getChildren();
			
			for (int j = 0; j < children1.length; j++) { // check if value exists
				IAgileList listChildren = (IAgileList) children1[j];
				if (listChildren.getValue().equals(sPCode)) {
					sResult = "N";
					throw new Exception(sPCode + " has already exist.");
				}
			}
			for (int j = 0; j < children2.length; j++) { // check if value exists
				IAgileList listChildren = (IAgileList) children2[j];
				if (listChildren.getValue().equals(sPCode)) {
					sResult = "N";
					throw new Exception(sPCode + " has already exist.");
				}
			}
			
			listValuesforItem.addChild(sPCode);
			listProdLineforItem.setValues(listValuesforItem);
			listValuesforChange.addChild(sPCode);
			listProdLineforChange.setValues(listValuesforChange);
			sResult = "Y";

		} catch (APIException e) {
			// TODO Auto-generated catch block
			sResult = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			sResult = e.getMessage();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sResult;
	}

}

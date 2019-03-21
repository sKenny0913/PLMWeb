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

	public String add(String sPCode,IAgileSession agileSession1) {
		String sResult = "";
		try {
			IAdmin admin = agileSession1.getAdminInstance();
			IListLibrary listLib = admin.getListLibrary();
			IAdminList listProdLineforChange = listLib.getAdminList("Product Line");
			IAdminList listProdLineforItem = listLib.getAdminList("Product_Line");
			IAgileList listValuesforChange = listProdLineforChange.getValues();
			Object[] children1 = listValuesforChange.getChildren();
			IAgileList listValuesforItem = listProdLineforItem.getValues();
			Object[] children2 = listValuesforItem.getChildren();
			
			for (int j = 0; j < children1.length; j++) { // check if productline value exists
				IAgileList listChildren = (IAgileList) children1[j];
				if (listChildren.getValue().equals(sPCode)) {
					sResult = "N";
					throw new Exception(sPCode + " has already exist.");
				}
			}
			for (int j = 0; j < children2.length; j++) { // check if productline value exists
				IAgileList listChildren = (IAgileList) children2[j];
				if (listChildren.getValue().equals(sPCode)) {
					sResult = "N";
					throw new Exception(sPCode + " has already exist.");
				}
			}
			
			listValuesforItem.addChild(sPCode);//add productline in list
			listProdLineforItem.setValues(listValuesforItem);//save list
			listValuesforChange.addChild(sPCode);//add productline in another list (there are two product line list for item and changes)
			listProdLineforChange.setValues(listValuesforChange);//save list
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

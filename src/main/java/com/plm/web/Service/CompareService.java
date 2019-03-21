package com.plm.web.Service;

import com.agile.api.ItemConstants;
import com.agile.ws.schema.business.v1.axis.AgileDataTableRequestType;
import com.agile.ws.schema.business.v1.axis.AgileGetObjectRequest;
import com.agile.ws.schema.business.v1.axis.AgileGetObjectResponse;
import com.agile.ws.schema.business.v1.axis.GetObjectRequestType;
import com.agile.ws.schema.business.v1.axis.GetObjectResponseType;
import com.agile.ws.schema.common.v1.axis.AgileExceptionListType;
import com.agile.ws.schema.common.v1.axis.AgileExceptionType;
import com.agile.ws.schema.common.v1.axis.AgileListEntryType;
import com.agile.ws.schema.common.v1.axis.AgileObjectType;
import com.agile.ws.schema.common.v1.axis.AgileRowType;
import com.agile.ws.schema.common.v1.axis.AgileTableType;
import com.agile.ws.schema.common.v1.axis.ResponseStatusCode;
import com.agile.ws.schema.common.v1.axis.SelectionType;
import com.agile.ws.service.business.v1.axis.BusinessObjectServiceLocator;
import com.agile.ws.service.business.v1.axis.BusinessObject_BindingStub;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis.AxisFault;
import org.apache.axis.message.MessageElement;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CompareService {
	public static String clsName;
	public static String SERVER_INF = "http://plm01.qsitw.com/CoreService/services";
	public static String USERNAME = "it_plmweb";
	public static String PASSWORD = "Plmweb123";
	public static String serviceName = "BusinessObject";
	public static String SERVER_URL = SERVER_INF + "/" + serviceName;
	public static BusinessObject_BindingStub agileStub;
	public static Map<String, String> m1 = new HashMap<String, String>();
	public static Map<String, String> m2 = new HashMap<String, String>();
	public static Map<String, String> m1q = new HashMap<String, String>();
	public static Map<String, String> m2q = new HashMap<String, String>();
	public static Map<String, String> md = new HashMap<String, String>();

	public void setupServerLogin() throws Exception {// get agileStub(PLM WebService) from PLM AP, same as agileSession
		BusinessObjectServiceLocator locator = new BusinessObjectServiceLocator();

		agileStub = (BusinessObject_BindingStub) locator.getBusinessObject(new URL(SERVER_URL));

		agileStub.setUsername(USERNAME);
		agileStub.setPassword(PASSWORD);
	}

	public String getMessageElementValue(MessageElement element) {//get PLM text or list value
		if (element.getFirstChild() != null) {
			if (element.getType().getLocalPart().equals("AgileListEntryType")) {
				AgileListEntryType list = (AgileListEntryType) element.getObjectValue();
				SelectionType[] selection = list.getSelection();
				SelectionType[] arrayOfSelectionType1 = selection;
				int i = arrayOfSelectionType1.length;
				int j = 0;
				if (j < i) {
					SelectionType sel = arrayOfSelectionType1[j];
					return sel.getValue();
				}
			} else {
				return element.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	public void getBOM(String sPartNumber, Boolean bComparePN) {
		try {
			GetObjectRequestType getObjectRequestType = new GetObjectRequestType();
			AgileGetObjectRequest[] agileGetObjectRequest = new AgileGetObjectRequest[1];
			agileGetObjectRequest[0] = new AgileGetObjectRequest();

			agileGetObjectRequest[0].setClassIdentifier("Items");
			agileGetObjectRequest[0].setObjectNumber(sPartNumber);

			AgileDataTableRequestType[] tableRequests = new AgileDataTableRequestType[1];
			tableRequests[0] = new AgileDataTableRequestType();
			tableRequests[0].setTableIdentifier(ItemConstants.TABLE_BOM.toString());
			tableRequests[0].setLoadCellMetaData(Boolean.valueOf(false));
			agileGetObjectRequest[0].setTableRequests(tableRequests);

			getObjectRequestType.setRequests(agileGetObjectRequest);
			GetObjectResponseType getObjectResponseType = agileStub.getObject(getObjectRequestType);
			if (!getObjectResponseType.getStatusCode().toString().equals(ResponseStatusCode.SUCCESS.getValue())) {
				AgileExceptionListType[] agileExceptionListType = getObjectResponseType.getExceptions();
				for (int i = 0; i < agileExceptionListType.length; i++) {
					AgileExceptionType[] exceptions = agileExceptionListType[i].getException();
					for (int j = 0; j < exceptions.length; j++) {
						System.out.println(exceptions[j].getMessage());
					}
				}
			} else {
				AgileGetObjectResponse[] responses = getObjectResponseType.getResponses();
				if (responses != null) {
					for (int i = 0; i < responses.length; i++) {
						AgileObjectType agileObject = responses[i].getAgileObject();
						MessageElement[] messages = agileObject.get_any();

						AgileTableType[] tables = agileObject.getTable();
						if (tables != null) {
							for (int j = 0; j < tables.length; j++) {
								AgileRowType[] rows = tables[j].getRow();

								String sItemNumber = null;
								String sRefDes = null;
								String sQty = null;
								String sitemDescription = null;
								if (rows != null) {
									for (int k = 0; k < rows.length; k++) {
										messages = rows[k].get_any();
										if (messages != null) {
											for (int jj = 0; jj < messages.length; jj++) {
												if (messages[jj].getTagName().equals(":itemNumber")) {
													sItemNumber = getMessageElementValue(messages[jj]);//get item number
												}
												if (messages[jj].getTagName().equals(":refDes")) {
													sRefDes = getMessageElementValue(messages[jj]);//get CKD-ID
													if (bComparePN.booleanValue()) {//if boolean = false, it is first Item
														m1.put(sItemNumber, sRefDes);
													} else {
														m2.put(sItemNumber, sRefDes);
													}
												}
												if (messages[jj].getTagName().equals(":qty")) {//get Qty
													sQty = getMessageElementValue(messages[jj]);
													if (bComparePN.booleanValue()) {//if boolean = true, it is second Item
														m1q.put(sItemNumber, sQty);
													} else {
														m2q.put(sItemNumber, sQty);
													}
												}
												if (messages[jj].getTagName().equals(":itemDescription")) {//get item description
													sitemDescription = getMessageElementValue(messages[jj]);
													md.put(sItemNumber, sitemDescription);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (AxisFault e) {
			System.out
					.println("---------------------------------------------------------------------------------------");
			System.out.println("An Axis fault was encountered.\nEnsure that your server URL is legitimate ");
			System.out.println("and also check your username / password credentials. ");
			System.out.println("Your server URL: " + SERVER_URL + "\nmay be inaccurate or the server may be down.");
			System.out
					.println("---------------------------------------------------------------------------------------");
		} catch (Exception e) {
			System.out.println("Exceptions: ");
			e.printStackTrace();
		}
	}

	public JSONObject compareBOM(String partNumber, String cPartNumber) throws Exception {
		System.out.println("\n------------------------------------------------------------------------");
		System.out.println("Executing webservice sample: ");

		setupServerLogin();// get agile session

		System.out.println("Top Level PartNumber: " + partNumber);
		getBOM(partNumber, Boolean.valueOf(false));
		System.out.println("Top Level Compare PartNumber: " + cPartNumber);
		getBOM(cPartNumber, Boolean.valueOf(true));
		System.out.println("------------------------------------------------------------");

		JSONArray jArray = new JSONArray();
		for (Map.Entry<String, String> entry1 : m1.entrySet()) {
			if ((m2.get(entry1.getKey()) == null) || (m2.get(entry1.getKey()) == "")) {// if BOM1 item not exist in BOM2
				JSONObject jObjNew = new JSONObject();
				jObjNew.put("PN", "");
				jObjNew.put("CPN", entry1.getKey());
				jArray.put(jObjNew);
			}
		}
		for (Map.Entry<String, String> entry1 : m2.entrySet()) {
			if ((m1.get(entry1.getKey()) == null) || (m1.get(entry1.getKey()) == "")) {// if BOM2 item not exist in BOM1
				JSONObject jObjNew = new JSONObject();
				jObjNew.put("PN", entry1.getKey());
				jObjNew.put("CPN", "");
				jArray.put(jObjNew);
			}
		}
		JSONObject jObjDevice = new JSONObject();
		jObjDevice.put("jsonName", jArray);

		m1.clear();
		m2.clear();
		return jObjDevice;
	}

	public JSONObject compareCKDID(String partNumber, String cPartNumber) throws Exception {
		System.out.println("\n------------------------------------------------------------------------");
		System.out.println("Executing webservice sample: ");

		setupServerLogin();

		System.out.println("Top Level PartNumber: " + partNumber);
		getBOM(partNumber, Boolean.valueOf(false));
		System.out.println("Top Level Compare PartNumber: " + cPartNumber);
		getBOM(cPartNumber, Boolean.valueOf(true));
		System.out.println("------------------------------------------------------------");

		JSONArray jArray1 = new JSONArray();
		for (Map.Entry<String, String> entry1 : m1.entrySet()) {
			if (!m2.containsKey(entry1.getKey())) { //get CKD-ID for BOM2 part not exist in BOM1, if BOM 2 does not contain part number in BOM 1
				JSONObject jObjNew = new JSONObject();
				jObjNew.put("PN", "");
				jObjNew.put("PN_QTY", "");
				jObjNew.put("PN_CKDID", "");
				jObjNew.put("CPN", entry1.getKey());
				jObjNew.put("CPN_QTY", m1q.get(entry1.getKey()));
				if (m1.get(entry1.getKey()) != null) { // if BOM 1 part has CKDID
					ArrayList<String> m1_CKDID_BOM = new ArrayList<String>();
					if (((String) m1.get(entry1.getKey())).contains(",")) {

						String[] v1 = ((String) m1.get(entry1.getKey())).split(",");//split CKDID to Array by comma,

						for (int i = 0; i < v1.length; i++) {//depart CKDID to a sequence, e.g. A01-A03 = A01 A02 A03
							if (v1[i].contains("-")) {
								String[] v1c = v1[i].split("-");
								String prefix_CKDID = v1c[0].replaceAll("[^A-Za-z]+", "");
								String sFirst_number_CKDID = v1c[0].replaceAll("[^\\d.]", "");
								String sLast_number_CKDID = v1c[1].replaceAll("[^\\d.]", "");
								int iFirst_number_CKDID = Integer.parseInt(sFirst_number_CKDID);
								int iLast_number_CKDID = Integer.parseInt(sLast_number_CKDID);
								m1_CKDID_BOM.add(prefix_CKDID + sFirst_number_CKDID);
								while (iFirst_number_CKDID != iLast_number_CKDID) {
									iFirst_number_CKDID++;
									m1_CKDID_BOM.add(prefix_CKDID + iFirst_number_CKDID);
								}
							} else {
								m1_CKDID_BOM.add(v1[i]);
							}
						}
						jObjNew.put("CPN_CKDID", m1_CKDID_BOM);
					} else {
						jObjNew.put("CPN_CKDID", m1.get(entry1.getKey()));
					}
				} else {
					jObjNew.put("CPN_CKDID", "");
				}
				jObjNew.put("CPN_Desc", md.get(entry1.getKey()));
				jArray1.put(jObjNew);
			}
		}
		for (Map.Entry<String, String> entry1 : m2.entrySet()) {//get CKD-ID for BOM1 part not exist in BOM1, if BOM 1 does not contain part number in BOM 2
			if (!m1.containsKey(entry1.getKey())) {
				JSONObject jObjNew = new JSONObject();
				jObjNew.put("PN", entry1.getKey());
				jObjNew.put("PN_QTY", m2q.get(entry1.getKey()));
				if (m2.get(entry1.getKey()) != null) {
					ArrayList<String> m2_CKDID_BOM = new ArrayList<String>();

					if (((String) m2.get(entry1.getKey())).contains(",")) {

						String[] v1 = ((String) m2.get(entry1.getKey())).split(",");

						for (int i = 0; i < v1.length; i++) {
							if (v1[i].contains("-")) {
								String[] v1c = v1[i].split("-");
								String prefix_CKDID = v1c[0].replaceAll("[^A-Za-z]+", "");
								String sFirst_number_CKDID = v1c[0].replaceAll("[^\\d.]", "");
								String sLast_number_CKDID = v1c[1].replaceAll("[^\\d.]", "");
								int iFirst_number_CKDID = Integer.parseInt(sFirst_number_CKDID);
								int iLast_number_CKDID = Integer.parseInt(sLast_number_CKDID);
								m2_CKDID_BOM.add(prefix_CKDID + sFirst_number_CKDID);
								while (iFirst_number_CKDID != iLast_number_CKDID) {
									iFirst_number_CKDID++;
									m2_CKDID_BOM.add(prefix_CKDID + iFirst_number_CKDID);
								}
							} else {
								m2_CKDID_BOM.add(v1[i]);
							}
						}
						jObjNew.put("PN_CKDID", m2_CKDID_BOM);

					} else {
						jObjNew.put("PN_CKDID", m2.get(entry1.getKey()));
					}
				} else {
					jObjNew.put("PN_CKDID", "");
				}
				jObjNew.put("CPN", "");
				jObjNew.put("CPN_QTY", "");
				jObjNew.put("CPN_CKDID", "");
				jObjNew.put("CPN_Desc", md.get(entry1.getKey()));
				jArray1.put(jObjNew);
			}
		}
		for (Map.Entry<String, String> entry1 : m1.entrySet()) {// compare CKD-ID of same part, which both BOM1 and BOM2 has same part
			String m1value = entry1.getValue() == null ? "" : (String) entry1.getValue();
			String m2value = m2.get(entry1.getKey()) == null ? "" : (String) m2.get(entry1.getKey());
			if ((!m1value.equals("")) && (!m2value.equals("")) && (!m1value.equals(m2value))) {
				String[] v1 = m1value.split(",");
				String[] v2 = m2value.split(",");

				List<String> m1_list = Arrays.asList(m1value.split(","));
				ArrayList<String> m1_CKDID = new ArrayList<String>();
				m1_CKDID.addAll(m1_list);
				for (int i = 0; i < v1.length; i++) {
					if (v1[i].contains("-")) {
						String[] v1c = v1[i].split("-");
						m1_CKDID.remove(v1[i]);
						String prefix_CKDID = v1c[0].replaceAll("[^A-Za-z]+", "");
						String sFirst_number_CKDID = v1c[0].replaceAll("[^\\d.]", "");
						String sLast_number_CKDID = v1c[1].replaceAll("[^\\d.]", "");
						int iFirst_number_CKDID = Integer.parseInt(sFirst_number_CKDID);
						int iLast_number_CKDID = Integer.parseInt(sLast_number_CKDID);
						m1_CKDID.add(prefix_CKDID + sFirst_number_CKDID);
						while (iFirst_number_CKDID != iLast_number_CKDID) {
							iFirst_number_CKDID++;
							m1_CKDID.add(prefix_CKDID + iFirst_number_CKDID);
						}
					}
				}
				List<String> m2_list = Arrays.asList(m2value.split(","));
				ArrayList<String> m2_CKDID = new ArrayList<String>();
				m2_CKDID.addAll(m2_list);
				for (int i = 0; i < v2.length; i++) {
					if (v2[i].contains("-")) {
						String[] v2c = v2[i].split("-");
						m2_CKDID.remove(v2[i]);
						String prefix_CKDID = v2c[0].replaceAll("[^A-Za-z]+", "");
						String sFirst_number_CKDID = v2c[0].replaceAll("[^\\d.]", "");
						String sLast_number_CKDID = v2c[1].replaceAll("[^\\d.]", "");
						int iFirst_number_CKDID = Integer.parseInt(sFirst_number_CKDID);
						int iLast_number_CKDID = Integer.parseInt(sLast_number_CKDID);
						m2_CKDID.add(prefix_CKDID + sFirst_number_CKDID);
						while (iFirst_number_CKDID != iLast_number_CKDID) {
							iFirst_number_CKDID++;
							m2_CKDID.add(prefix_CKDID + iFirst_number_CKDID);
						}
					}
				}
				ArrayList<String> m1_result = new ArrayList<String>(m1_CKDID);
				m1_result.removeAll(m2_CKDID);
				ArrayList<String> m2_result = new ArrayList<String>(m2_CKDID);
				m2_result.removeAll(m1_CKDID);
				if ((!m1_result.isEmpty()) || (!m2_result.isEmpty())) {
					JSONObject jObjNew = new JSONObject();
					jObjNew.put("PN", entry1.getKey());
					jObjNew.put("PN_QTY", m2q.get(entry1.getKey()));
					jObjNew.put("PN_CKDID", m2_result);
					jObjNew.put("CPN", entry1.getKey());
					jObjNew.put("CPN_QTY", m1q.get(entry1.getKey()));
					jObjNew.put("CPN_CKDID", m1_result);
					jObjNew.put("CPN_Desc", md.get(entry1.getKey()));
					jArray1.put(jObjNew);
				}
			}
		}
		JSONObject jObjDevice1 = new JSONObject();
		jObjDevice1.put("jsonName", jArray1);
		System.out.println(jObjDevice1);
		m1.clear();
		m2.clear();
		m1q.clear();
		m2q.clear();
		md.clear();
		return jObjDevice1;
	}
}

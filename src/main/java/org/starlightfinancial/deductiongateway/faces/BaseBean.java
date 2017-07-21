package org.starlightfinancial.deductiongateway.faces;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.mongodb.gridfs.GridFSDBFile;
import forward.chuwa.microcredit.model.*;
import forward.chuwa.microcredit.service.CreditApplyService;
import forward.chuwa.microcredit.service.CustomerService;
import forward.chuwa.microcredit.service.SystemService;
import forward.chuwa.microcredit.utility.DictionaryType;
import forward.chuwa.microcredit.utility.HashType;
import forward.chuwa.microcredit.utility.Options;
import forward.chuwa.microcredit.utility.Utility;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseBean {

	private static final int wdFormatPDF = 17;

	public static final String SUCCESS = "success";

	public static final String EMPTY = "empty";

	public static final String BACK = "back";

	public static final int pageSize = 18;

	public static final int pageLongSize = 15;

	public static final int pageMinSize = 10;

	public static final int maxFileSize = 10000000;

	private StreamedContent downloadFile;

	public int getPageLongSize() {
		return pageLongSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageMinSize() {
		return pageMinSize;
	}

	public int getMaxFileSize() {
		return maxFileSize;
	}

	@Autowired
	protected SessionBean sessionBean;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	protected SystemService systemService;

	@Autowired
	private CreditApplyService creditApplyService;

	@Autowired
	private CustomerService customerService;

	public String[] getParameters(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterValuesMap().get(key);
	}

	public int[] getIntParameters(String key) {
		String[] parameter = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterValuesMap().get(key);

		if (parameter == null) {
			return null;
		}

		int[] intParameter = new int[parameter.length];

		for (int i = 0; i < intParameter.length; ++i) {
			try {
				intParameter[i] = Integer.parseInt(parameter[i]);
			} catch (Exception e) {
				// e.printStackTrace();
				intParameter[i] = -1;
			}
		}

		return intParameter;
	}

	public String getParameter(String key) {
		String param = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key);
		if (param == null)
			return "";
		return param;
	}

	public int getIntParameter(String key) {
		Map map=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String param = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key);
		try {
			return Integer.parseInt(param);
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}

	public long getLongParameter(String key) {
		String param = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key);
		try {
			return Long.parseLong(param);
		} catch (Exception e) {
			// e.printStackTrace();
			return -1;
		}
	}

	// HashType中的key是小类，value是大类
	public boolean checkHasFiles(int creApId, List<HashType> types) {
		if (types.size() <= 0)
			return false;
		for (HashType type : types) {
			if (checkHasFile(creApId, type) == false)
				return false;
		}
		return true;
	}

	public boolean checkHasFile(int creApId, HashType type) {
		if (type == null || StringUtils.isEmpty(type.getKey())
				|| StringUtils.isEmpty(type.getValue()))
			return false;

		List<CreMaterialInfo> list = creditApplyService.listMaterialbyType(
				creApId, type.getKey(), type.getValue());
		if (list == null || list.size() <= 0) {
			return false;
		}
		return true;
	}

	// 根据全局变量设置的下级数据是否上级获取的OrgID组合
	public String getLimitedOrgID(String type) {
		String str = "";
		str = sessionBean.getOrgId() + "";

		if (sessionBean.getOrgNo().equals("001")) {
			List<SysGlobal> list = systemService.querySysGlobals(" and type='"
					+ type + "'");
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					SysGlobal global = list.get(i);
					if (global.getValue().equals("1")
							&& (global.getOrgId() != sessionBean.getOrgId())) {
						str = str + "," + global.getOrgId();
					}
				}
			}
		}

		return str;
	}

	// type为类型，如AUTH_CONTRACT = "合同列表"; operType为操作类型，如：AUTH_TYPE_UPDATE = 2;修改
	public boolean hasAuthority(int authId, String type, int operType) {
		return systemService.hasAuthority(authId, type, operType,
				sessionBean.getId(), sessionBean.getStaffId());
	}

	/*
	 * public void checkLogin() { if (sessionBean != null &&
	 * sessionBean.getOrgId() > 0 && sessionBean.getStaffId() > 0 &&
	 * sessionBean.getId() > 0) { return; } else { NavigationHandler nh =
	 * FacesContext.getCurrentInstance()
	 * .getApplication().getNavigationHandler();
	 * nh.handleNavigation(FacesContext.getCurrentInstance(), null, "logout");
	 * FacesContext.getCurrentInstance().renderResponse(); } }
	 */
	public void showMessage(int type, String message) {

		if (message != null && !message.equals("")) {
			if (type == 0) {// global
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage("global", new FacesMessage("", message));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("", message));
			}

		}
	}

	public boolean getCheckAutoNum() {
		boolean result = false;
		if (systemService.querySysGlobals(
				" and type='" + Options.getGlobalType().get(0).getKey()
						+ "' and orgId = " + sessionBean.getOrgId()).size() > 0) {
			result = true;
		}
		return result;
	}

	public List<HashType> queryStaff(String query) {
		List<HashType> list = new ArrayList<HashType>();
		List<SysUserStaffinfo> staffs = systemService
				.querySysUserStaffinfos(" and t.orgId="
						+ sessionBean.getOrgId() + " and (t.staffNo like '%"
						+ query + "%' or t.name like '%" + query
						+ "%') and t.isresign = '"
						+ DictionaryType.STAFF_IS_RESIGN_01 + "'");
		staffs = staffs.subList(0, staffs.size() > 5 ? 5 : staffs.size());

		for (SysUserStaffinfo staff : staffs) {
			HashType temp = new HashType(staff.getId() + "", staff.getName()
					+ "-" + staff.getStaffNo());
			list.add(temp);
		}

		return list;
	}

	public List<HashType> querySalesStaff(String query) {
		List<HashType> list = new ArrayList<HashType>();
		List<SysUserStaffinfo> staffs = systemService
				.querySysUserStaffinfos(" and t.orgId="
						+ sessionBean.getOrgId() + " and (t.staffNo like '%"
						+ query + "%' or t.name like '%" + query
						+ "%') and t.isresign = '"
						+ DictionaryType.STAFF_IS_RESIGN_01
						+ "' and t.isSalesman='"
						+ DictionaryType.STAFF_IS_SALESMAN_01 + "'");
		staffs = staffs.subList(0, staffs.size() > 5 ? 5 : staffs.size());

		for (SysUserStaffinfo staff : staffs) {
			HashType temp = new HashType(staff.getId() + "", staff.getName()
					+ "-" + staff.getStaffNo());
			list.add(temp);
		}

		return list;
	}

	public List<HashType> querySalesStaff2() {
		List<HashType> list = new ArrayList<HashType>();
		List<SysUserStaffinfo> staffs = systemService
				.querySysUserStaffinfos(" and t.orgId="
						+ sessionBean.getOrgId() + " and t.isresign = '"
						+ DictionaryType.STAFF_IS_RESIGN_01
						+ "' and t.isSalesman='"
						+ DictionaryType.STAFF_IS_SALESMAN_01 + "'");
		for (SysUserStaffinfo staff : staffs) {
			HashType temp = new HashType(staff.getId() + "", staff.getName()
					+ "-" + staff.getStaffNo());
			list.add(temp);
		}

		return list;
	}

	public List<HashType> queryCustomer(String query, String type) {
		List<HashType> list = new ArrayList<HashType>();
		String condition = "";
		if (type != null && !type.equals("")) {
			condition = " and t.type=" + type;// DictionaryType.CUSTOMER_TYPE_01
		}

		if (!query.equals("")) {
			condition = condition + " and (t.name like '%" + query + "%'";
			condition = condition + " or t.certificateNo like '%" + query
					+ "%'";
			condition = condition + " or t.connectPhone like '%" + query + "%'";
			condition = condition + " or t.customerUniqueNo like '%" + query
					+ "%')";

		}

		List<CusMain> listCustomer = customerService.queryCustomers(Utility
				.getCusLimitSQL(condition, sessionBean.getId(),
						sessionBean.getStaffId()));

		for (CusMain customer : listCustomer) {
			HashType temp = new HashType(customer.getId() + "",
					customer.getName() + "-" + customer.getCertificateNo());
			list.add(temp);
		}
		return list;
	}

	public List<HashType> queryPerformanceStaff(String query) {
		List<HashType> list = new ArrayList<HashType>();
		List<SysUserStaffinfo> staffs = systemService
				.querySysUserStaffinfos(" and t.orgId="
						+ sessionBean.getOrgId() + " and (t.staffNo like '%"
						+ query + "%' or t.name like '%" + query
						+ "%') and t.isresign = '"
						+ DictionaryType.STAFF_IS_RESIGN_01 + "'");
		for (SysUserStaffinfo staff : staffs) {
			HashType temp = new HashType(staff.getId() + "", staff.getName()
					+ "-" + staff.getStaffNo());
			list.add(temp);
		}

		return list;
	}

	/*
	 * 文件下载
	 */
	public void downloadFile(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		String mID = context.getExternalContext().getRequestParameterMap()
				.get("fileMongoId");

		if (!StringUtils.isEmpty(mID)) {
			downloadFile = getStreamFileByMongoID(mID);
		} else {
			downloadFile = null;
		}
		if (downloadFile == null) {
			showMessage(0, "无此文件，不能提供下载！");
		}
	}

	/*
	 * 物理文件删除
	 */
	public boolean deleteFile(String mongoId) {
		try {
			Query query = new Query(Criteria.where("id").is(
					new ObjectId(mongoId)));
			GridFSDBFile gridFSFile = gridFsTemplate.findOne(query);
			if (gridFSFile != null) {
				gridFsTemplate.delete(query);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public StreamedContent getStreamFileByMongoID(String mongoId) {
		StreamedContent file = null;
		try {
			if (!mongoId.equals("")) {
				Query query = new Query(Criteria.where("id").is(
						new ObjectId(mongoId)));
				GridFSDBFile gridFSFile = gridFsTemplate.findOne(query);
				if (gridFSFile != null) {
					InputStream in = gridFSFile.getInputStream();
					file = new DefaultStreamedContent(
							in,
							"",
							URLEncoder.encode(gridFSFile.getFilename(), "utf-8"));
					in.close();
				}
			}
		} catch (Exception e) {
			return null;
		}
		return file;
	}

	public StreamedContent getTemplateFileByType(int templateId, String type) {
		CreConTemplate temp = null;
		if (StringUtils.equals(type, DictionaryType.TEMPLATE_TYPE_01)) {
			temp = systemService.loadTemplate(templateId);
		} else {
			List<CreConTemplate> list = systemService
					.listTemplate(" and  t.parentId=" + templateId
							+ " and t.type='" + type + "'");
			if (list.size() > 0) {
				temp = list.get(0);
			}
		}
		StreamedContent streamContent = null;
		if (temp != null && temp.getFilePath() != null
				&& !temp.getFilePath().equals("")) {
			Query query = new Query(Criteria.where("id").is(
					new ObjectId(temp.getFilePath())));
			GridFSDBFile gridFSFile = gridFsTemplate.findOne(query);
			if (gridFSFile != null) {
				InputStream in = gridFSFile.getInputStream();
				streamContent = new DefaultStreamedContent(in, "",
						gridFSFile.getFilename());
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (streamContent != null) {
				return streamContent;
			}
		}

		return null;
	}

	public File genDocumentFromTemplate(InputStream templateStream,
			String templateFileName, String destFileName, String extendName,
			Map<String, Object> values) {

		Configuration configuration = null;
		File outFile = null;
		if (templateStream == null || values == null)
			return null;

		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		try {

			File template = getFileFromInputStream(templateStream,
					templateFileName);
			String filepath = template.getAbsolutePath();
			String temp1 = filepath.substring(0, filepath.lastIndexOf('\\'));
			String temp2 = filepath.substring(filepath.lastIndexOf('\\') + 1,
					filepath.length());

			configuration.setDirectoryForTemplateLoading(new File(temp1));

			Template t = configuration.getTemplate(temp2, "utf-8");

			outFile = getTempFileByName(destFileName, extendName);
			FileOutputStream fos = new FileOutputStream(outFile);
			OutputStreamWriter ow = new OutputStreamWriter(fos, "utf-8");
			Writer out2 = new BufferedWriter(ow);
			try {
				t.process(values, out2);
			} catch (TemplateException e) {
				return null;
			}

			out2.flush();
			out2.close();
			ow.close();
			fos.flush();
			fos.close();
			template.delete();
		} catch (IOException e) {
			return null;
		}
		return outFile;
	}

	public File genPDFFromTemplate(InputStream templateStream,
			String templateFileName, String destFileName, String extendName,
			Map<String, Object> values) {

		Configuration configuration = null;
		File outFile = null;
		if (templateStream == null || values == null)
			return null;

		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		try {

			File template = getFileFromInputStream(templateStream,
					templateFileName);
			String filepath = template.getAbsolutePath();
			String temp1 = filepath.substring(0, filepath.lastIndexOf('\\'));
			String temp2 = filepath.substring(filepath.lastIndexOf('\\') + 1,
					filepath.length());

			configuration.setDirectoryForTemplateLoading(new File(temp1));

			Template t = configuration.getTemplate(temp2, "utf-8");

			outFile = getTempFileByName(destFileName, extendName);
			FileOutputStream fos = new FileOutputStream(outFile);
			OutputStreamWriter ow = new OutputStreamWriter(fos, "utf-8");
			Writer out2 = new BufferedWriter(ow);
			try {
				t.process(values, out2);
			} catch (TemplateException e) {
				return null;
			}

			out2.flush();
			out2.close();
			ow.close();
			fos.flush();
			fos.close();
			template.delete();

		} catch (IOException e) {
			return null;
		}
		return word2PDF(destFileName, outFile);
	}

	public File word2PDF(String destFileName, File inputFile) {
		File out = null;
		try {
			out = getTempFileByName(destFileName, ".pdf");

			ActiveXComponent app = new ActiveXComponent("Word.Application");
			if (app != null) {
				app.setProperty("Visible", false);
				Dispatch docs = app.getProperty("Documents").toDispatch();
				Dispatch doc = Dispatch.call(docs, "Open",
						inputFile.getAbsolutePath(), false, true).toDispatch();
				Dispatch.call(doc, "ExportAsFixedFormat",
						out.getAbsolutePath(), wdFormatPDF // word保存为pdf格式宏，值为17
				);
				Dispatch.call(doc, "Close", false);
				app.invoke("Quit", 0);
				if (inputFile != null) {
					inputFile.delete();
				}
			}
		} catch (Exception e) {
			if (inputFile != null) {
				inputFile.delete();
			}
			if (out != null) {
				out.delete();
			}			
			return null;
		} catch (UnsatisfiedLinkError e2) {
			if (inputFile != null) {
				inputFile.delete();
			}
			if (out != null) {
				out.delete();
			}
			return null;
		}
		return out;
	}

	public File getTempFileByName(String fileName, String extendName) {
		File tempFile = null;
		try {
			String prefix = "";
			String suffix = "";

			if (fileName.lastIndexOf('.') > 0) {
				prefix = fileName.substring(0, fileName.lastIndexOf('.'));
				suffix = fileName.substring(fileName.lastIndexOf('.'),
						fileName.length());
			}

			if (!prefix.equals("") && !suffix.equals("")) {
				if (!extendName.equals("")) {
					tempFile = File.createTempFile(prefix, extendName);
				} else {
					tempFile = File.createTempFile(prefix, suffix);
				}
			} else {
				tempFile = File.createTempFile("tempFile", ".tfl");
			}

			tempFile.deleteOnExit();

		} catch (Exception e) {
			tempFile = null;
		}
		return tempFile;
	}

	public File getFileFromInputStream(InputStream in, String fileName) {

		File tempFile = null;
		FileOutputStream fout = null;
		try {
			String prefix = "";
			String suffix = "";
			// 根据src分割字符串
			if (fileName.lastIndexOf('.') > 0) {
				prefix = fileName.substring(0, fileName.lastIndexOf('.'));
				suffix = fileName.substring(fileName.lastIndexOf('.'),
						fileName.length());
			}

			if (!prefix.equals("") && !suffix.equals("")) {
				tempFile = File.createTempFile(prefix, suffix);
			} else {
				tempFile = File.createTempFile("tempFile", ".tfl");
			}

			tempFile.deleteOnExit();
			fout = new FileOutputStream(tempFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				fout.write(buf, 0, len);
			}
		} catch (Exception e) {
			tempFile = null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e) {
				;
			}
		}
		return tempFile;
	}

	// 小类
	public boolean checkCreApMainMaterialLock(int creApID, String dic_type) {
		boolean isLock = false;
		SysDict loadDict = systemService.loadSysDict(
				DictionaryType.MATERIAL_SMALL_CLASS, dic_type);
		if (loadDict != null && loadDict.getParentid() > 0) {

			SysDict parentDict = systemService.loadSysDict(loadDict
					.getParentid());
			if (parentDict != null && parentDict.getId() > 0) {
				String parentDictDes = parentDict.getDicValue();
				if (parentDictDes != null && !parentDictDes.equals("")) {
					if (parentDictDes
							.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_01)
							|| parentDictDes
									.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_02)) {
						isLock = creditApplyService.checkCreApMainMaterialLock(
								creApID, DictionaryType.MATERIAL_LOCK_01);
					}

					if (parentDictDes
							.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_03)
							|| parentDictDes
									.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_04)
							|| parentDictDes
									.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_06))

					{
						isLock = creditApplyService.checkCreApMainMaterialLock(
								creApID, DictionaryType.MATERIAL_LOCK_02);
					}

					if (parentDictDes
							.equals(DictionaryType.MATERIAL_LOCK_BIG_CLASS_05)) {
						isLock = creditApplyService.checkCreApMainMaterialLock(
								creApID, DictionaryType.MATERIAL_LOCK_03);
					}
				}
			}
		}
		return isLock;
	}

	public StreamedContent getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(StreamedContent downloadFile) {
		this.downloadFile = downloadFile;
	}

	public void loginout() {
		FacesContext fc = FacesContext.getCurrentInstance();
		sessionBean.clear();
		try {
			HttpSession session = (HttpSession) fc.getExternalContext()
					.getSession(false);
			if (session != null) {
				try {
					session.removeAttribute("onlineUserBindingListener");
				} catch (IllegalStateException e) {
					;
				}
			}
			// fc.getExternalContext().invalidateSession();
		} catch (Exception e) {
			;
		}
	}

	public boolean checkHasAuthority(List<String> endRoles) {
		List<String> temp2 = sessionBean.getRolesId();
		if (temp2 != null && temp2.size() > 0 && endRoles != null
				&& endRoles.size() > 0) {
			for (String temp : temp2) {
				if (endRoles.contains(temp)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean saveStaffOperLog(String operContent, String operRemark,
			String operType) {
		SysOperLog sysOperLog = new SysOperLog();
		sysOperLog.setOperatorId(sessionBean.getStaffId());
		sysOperLog.setOperator(sessionBean.getStaffName());
		sysOperLog.setOperatorDate(new Date());
		sysOperLog.setOperType(operType);
		sysOperLog.setOperRemark(operRemark);
		sysOperLog.setOperContent(operContent);
		sysOperLog = systemService.createOrUpdateSysOperLog(sysOperLog);
		if (sysOperLog != null && sysOperLog.getId() > 0) {
			return true;
		}
		return false;
	}
}

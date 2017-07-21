package org.starlightfinancial.deductiongateway.faces;

import forward.chuwa.microcredit.faces.BaseBean;
import forward.chuwa.microcredit.model.GoPayBean;
import forward.chuwa.microcredit.model.LoanRePlan;
import forward.chuwa.microcredit.model.MortgageDeduction;
import forward.chuwa.microcredit.model.SysAutoNum;
import forward.chuwa.microcredit.service.ContractService;
import forward.chuwa.microcredit.service.CreditApplyService;
import forward.chuwa.microcredit.service.LoanChargeService;
import forward.chuwa.microcredit.service.SystemService;
import forward.chuwa.microcredit.service.model.CreApMainServiceData;
import forward.chuwa.microcredit.service.service.CreApMainServiceDataService;
import forward.chuwa.microcredit.utility.*;
import org.apache.poi.hssf.usermodel.*;
import org.primefaces.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 应还款统计及批量扣款
 * @author DELL
 *
 */
@Service("mortgageDeductionFaces")
@Scope("view")
public class MortgageDeductionFaces extends BaseBean {

	@Autowired
	private ContractService contractService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private CreApMainServiceDataService  creApMainServiceDataService ;

	@Autowired
	private CreditApplyService creditApplyService;


	private List<HashType> listCustomer;
	private Date beginDate;
	private String status;
	private Date endDate;
	private String name=null;
	private 	 HttpClientUtil httpClientUtil = new HttpClientUtil();
	@Autowired
	private LoanChargeService loanChargeService;
	private boolean status1=false;
	private boolean status2=false;
	@PostConstruct
	public void init() {
		query();

	}

	static boolean dd = false;
	public void query(){
		listCustomer  =findDate();

	}


	private String createSql( ){
		StringBuffer sql = new StringBuffer();
		sql.append(" select  a.id as contractId, a.contract_no,a.apply_main_id , b.cre_unique_no ,c.name ,temp.planId,");
		sql.append("  temp.planNo,temp.scDatetime,temp.scPrincipal-temp.rePrincipal as prinprical, ");
		sql.append(" (temp.scInterest-temp.reInterest)+(temp.scForfeit-temp.reForfeit)  as interest, ");
		sql.append("  temp.status,temp.available,temp.debit_status ,");
		sql.append("  	 a.re_bank as param1,a.card_type as param2,a.re_bankNo as param3,a.card_name as param4,a.card_certype as param5,  a.card_cerno as param6");
		sql.append("  from (");
			sql.append("  select loanRePlan.id planId, loanRePlan.contract_id contractId,  loanRePlan.plan_no planNo, loanRePlan.debit_status,");
			sql.append("   loanRePlan.sc_date scDatetime,loanRePlan.sc_amount scPrincipal, isNull(loanRePlan.sc_interest, 0) scInterest,");
			sql.append("   isNull(lateCharge.sc_forfeit, 0) scForfeit, isNull(loanRePlan.sc_other,0) scOther, isNull(chargeMain.PRINCIPAL, 0) rePrincipal,");
			sql.append("  isNull(chargeMain.INTEREST, 0) reInterest, isNull(chargeMain.FORFEIT, 0) reForfeit, loanRePlan.STATUS status, loanRePlan.AVAILABLE available");
			sql.append(" from loan_re_plan loanRePlan ");
			sql.append("   outer apply (select top 1 isNull(a.late_amount, 0) - isNull(remit_amount, 0) sc_forfeit from LOAN_LATE_CHARGE a where a.contract_id = loanRePlan.contract_id and a.plan_no = loanRePlan.plan_no) lateCharge");
			sql.append("  outer apply (select sum(isNull(b.PRINCIPAL, 0)) PRINCIPAL, sum(isNull(b.INTEREST, 0)) INTEREST, sum(isNull(b.FORFEIT, 0)) FORFEIT from loan_charge_main b where b.REPLAN_ID = loanRePlan.id) chargeMain ");
		sql.append("  ) temp, CRE_CON_SIGN A , CRE_AP_MAIN B,CRE_AP_CUS_MAIN C ");
		sql.append("  where a.apply_main_id=b.id   and b.customer_id=c.id and temp.contractId=a.id  and B.status='1' and EXISTS( select u.id from CRE_CON_SIGN u where u.id = temp.contractId and ( u.deducttype is null or u.deducttype='0'))");
		sql.append(getCondition());
		//sql.append("   order by temp.scDatetime  desc");
		return  sql.toString();
	}

	private String createServiceSql( ){
		StringBuffer sql = new StringBuffer();
		sql.append("   select  temp.planId,temp.contractId,planNo, scDatetime , ");
		sql.append("    ( scPrincipal+scInterest)-(rePrincipal+reInterest)as serviceAmount, temp.status ,B.Org_manager_id ");
		sql.append("   from (  ");
			sql.append("  select loanRePlan.id planId, loanRePlan.contract_id contractId, loanRePlan.pln_no planNo,loanRePlan.service_data scDatetime,");
			sql.append("   loanRePlan.service_amount scPrincipal,  isNull(lateCharge.sc_forfeit, 0) scInterest, isNull(loanRePlan.sc_other,0) scOther,");
			sql.append("  isNull(chargeMain.PRINCIPAL, 0) rePrincipal, isNull(chargeMain.INTEREST, 0) reInterest,ltrim(rtrim(loanRePlan.status)) status ");
			sql.append("  from LOAN_RE_PLAN_SERVICE loanRePlan ");
			sql.append("  outer apply (select top 1 isNull(a.late_amount, 0) - isNull(a.remitAmount,0) sc_forfeit from LOAN_LATE_CHARGE_SERVICE a where a.contract_id = loanRePlan.contract_id and a.plan_no = loanRePlan.pln_no) lateCharge ");
			sql.append("  outer apply (select sum(isNull(b.RE_SERVICEAMOUNT, 0)) PRINCIPAL, sum(isNull(b.RE_DAMAGES, 0)) INTEREST  from LOAN_CHARGE_MAIN_SERVICE b where b.REPLAN_ID = loanRePlan.id) chargeMain  ");
		sql.append("   ) temp , CRE_AP_MAIN_SERVICE B ");
		sql.append(" where   temp.contractId=B.contractId  ");
		sql.append(getConditionServie());
		//sql.append("   order by planNo desc");
		return  sql.toString();
	}

	private  List<Map> dealList(List<Map> list,List<Map> listService){
		List<Map> result = new ArrayList<Map>();
		Map content=null;
		 for (int i = 0; i <list.size(); i++) {
			 content= list.get(i);
			 for(int j=0;j<listService.size();j++){
				 if(content.get("contractId").equals(listService.get(j).get("contractId"))&&
						 (content.get("planNo").equals(listService.get(j).get("planNo")))){
					 content.put("serviceAmount", listService.get(j).get("serviceAmount"));
					 content.put("Org_manager_id", listService.get(j).get("Org_manager_id"));
				 }
			 }
			 result.add(content);
		 }
		return result;
	}
	public List<HashType> findDate(){
		List<HashType> listCustomer  =new ArrayList<HashType>();
		 List<Map> listRePlans = contractService.executeSQL(createSql());
		 List<Map> listRePlanService = creApMainServiceDataService.executeSQL(createServiceSql());
		   if(listRePlanService!=null&&listRePlanService.size()>0){
			   listRePlans= dealList(listRePlans,listRePlanService);
		   }
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			 for (int i = 0; i <listRePlans.size(); i++) {
					Map temp10 = listRePlans.get(i);
					HashType ht1 = new HashType();
					if(temp10.get("apply_main_id")!=null)
					ht1.setKey(temp10.get("apply_main_id").toString());
					if(temp10.get("name")!=null)
					ht1.setValue(temp10.get("name").toString());
					if(temp10.get("Org_manager_id")!=null){
						ht1.setValue1(temp10.get("Org_manager_id").toString());
					}
					if(temp10.get("cre_unique_no")!=null)
					ht1.setValue2(temp10.get("cre_unique_no").toString());
					if(temp10.get("scDatetime")!=null)
					ht1.setValue3(temp10.get("scDatetime").toString());
					if(temp10.get("prinprical")!=null)
					ht1.setValue5(temp10.get("prinprical").toString());//本金
					if(temp10.get("interest")!=null)
					ht1.setValue6(temp10.get("interest").toString());//利息
					if(temp10.get("prinprical")!=null){
						ht1.setValue7(Utility.getDecimalStr2(new BigDecimal(temp10.get("prinprical").toString()).add(
								new BigDecimal(temp10.get("interest").toString()))));//本金＋利息
					}else{
						ht1.setValue7(Utility.getDecimalStr2(new BigDecimal(temp10.get("interest").toString())));//本金＋利息
					}
					if(temp10.get("serviceAmount")!=null)
					ht1.setValue8(temp10.get("serviceAmount").toString());//服务费+违约金
					if(temp10.get("status")!=null)
					ht1.setValue9(temp10.get("status").toString());
					if(temp10.get("contract_no")!=null)
					ht1.setValue10(temp10.get("contract_no").toString());
					if(temp10.get("contractId")!=null)
					ht1.setValue11(temp10.get("contractId").toString());
					if(temp10.get("planId")!=null)
					ht1.setValue12(temp10.get("planId").toString());
					if(temp10.get("debit_status")!=null)
					ht1.setValue14(temp10.get("debit_status").toString());
					if(temp10.get("planId")!=null)
					ht1.setValue15(temp10.get("planId").toString());

					if(temp10.get("param1")!=null)
					ht1.setParam1(temp10.get("param1").toString());
					if(temp10.get("param2")!=null)
						ht1.setParam2(temp10.get("param2").toString());
					if(temp10.get("param3")!=null)
						ht1.setParam3(temp10.get("param3").toString());
					if(temp10.get("param4")!=null)
						ht1.setParam4(temp10.get("param4").toString());
					if(temp10.get("param5")!=null)
						ht1.setParam5(temp10.get("param5").toString());
					if(temp10.get("param6")!=null)
						ht1.setParam6(temp10.get("param6").toString());

					listCustomer.add(ht1);
			 }
			 return listCustomer ;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void exportXLS() throws IOException, ParseException {

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("扣款统计");
			//表头

			String[] headers = new String[]
					{"客户名", "贷款编号", "应还款日期", "项目1(元)", "项目2（元）", "项目3（元）","项目4（元）", "状态"};
			sheet.setDefaultColumnWidth(15);
			HSSFRow rowHead1 = sheet.createRow(0);
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setFont(font);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = rowHead1.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(cellStyle);
			}

			HSSFRow row = null; HSSFCell cell = null;
			HSSFCellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
			HSSFCellStyle numericStyle = workbook.createCellStyle();
			numericStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			int i = 1;
			 List<HashType> list =new ArrayList<HashType>();
			 listCustomer= findDate();
			for (HashType hashType :  listCustomer) {
				row = sheet.createRow(i );

				cell = row.createCell(0);
				cell.setCellValue(hashType.getValue());
				cell = row.createCell(1);
				cell.setCellValue(hashType.getValue2());
				cell = row.createCell(2);
				cell.setCellValue(hashType.getValue3());
				cell = row.createCell(3);
				cell.setCellValue(hashType.getValue5());
				cell = row.createCell(4);
				cell.setCellValue(hashType.getValue6());
				cell = row.createCell(5);
				cell.setCellValue(hashType.getValue7());
				cell = row.createCell(6);
				cell.setCellValue(hashType.getValue8());
				String status=hashType.getValue14();
				if("21".equals(status)){
					status="未扣款";
				}else if("22".equals(status)){
					status="已扣款";
				}else if("23".equals(status)){
					status="未扣款成功";
				}
				cell = row.createCell(7);
				cell.setCellValue(status);
				i = i + 1;
			}

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.setResponseContentType("application/vnd.ms-excel");
	    	externalContext.setResponseHeader("Expires", "0");
	    	externalContext.setResponseHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
	    	externalContext.setResponseHeader("Pragma", "public");
	    	SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
	    	String fileName= "扣款统计报表"+format2.format(new Date());
	    	externalContext.setResponseHeader("Content-disposition", "attachment;filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
	    	externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());

	        OutputStream out = externalContext.getResponseOutputStream();
			workbook.write(out);
			externalContext.responseFlushBuffer();
			FacesContext.getCurrentInstance().responseComplete();

	}

	/**
	 * 批量扣款的方法
	 * @return
	 */
	public String saveMortgageDeductions() {

		String path=FacesContext.getCurrentInstance().getExternalContext().getRealPath("WEB-INF\\classes");
		List<HashType> list = new ArrayList<HashType>();

		int[] ids = getIntParameters("deleteIds");
		String reIds="";
		if (ids != null&&ids.length>0){
						for(int i=0;i<listCustomer.size();i++){
				String id=listCustomer.get(i).getValue12();
				for(int j=0;j<ids.length;j++){
					if(Integer.parseInt(id)==ids[j]){
						list.add(listCustomer.get(i));
						break;
					}
				}
			}
		}
			 List<GoPayBean> messages= new ArrayList<GoPayBean>();
			 String splitData="";
			 boolean flag=false;
			 HashType loanRePlan=null;
			 for( int i=0;i<list.size(); i++){
				 String 	contactNo="";
				 loanRePlan=list.get(i);
				   GoPayBean goPayBean = new GoPayBean();
				   goPayBean.setContractId( loanRePlan.getValue11());//设置合同编号

				   String OrdId=MerSeq.tickOrder();
				   goPayBean.setMerId(Utility.SEND_BANK_MERID);
				   goPayBean.setBusiId("");
				   goPayBean.setOrdId(OrdId);

				   //计算账户管理费
				   goPayBean.setCustomerNo( loanRePlan.getValue2());//设置客户编号
				   goPayBean.setCustomerName(loanRePlan.getValue());//设置客户名称

				   goPayBean.setContractNo(loanRePlan.getValue10());//设置合同编号
				   goPayBean.setOrgManagerId(contactNo);//设置服务费的管理公司
				   goPayBean.setRePlanId(loanRePlan.getValue12());//设置还款计划的id
				    String amount1= loanRePlan.getValue7();
				    String amount2= loanRePlan.getValue8();
				    int m1=0;
				    int m2=0;
				    if( amount1!=null&&!"".equals(amount1)){
				    	m1= new BigDecimal(amount1).movePointRight(2).intValue();
				    }
				    if( amount2!=null&&!"".equals(amount2)){
				    	m2= new BigDecimal(amount2).movePointRight(2).intValue();
				    }

				   goPayBean.setOrdAmt(m1+m2+"");
				 //  splitData="00145111^"+m1+";00145112^"+m2+";";
				   int orgId=-1;
				   CreApMainServiceData  creApMainServiceData=	creApMainServiceDataService.getCreApMainServiceData(Integer.parseInt(loanRePlan.getKey()));
					if(creApMainServiceData!=null){
						orgId=creApMainServiceData.getOrgManagerId();
					}
					//取商户号
					SysAutoNum sysAutoNum=systemService.loadSysAutoNum(orgId,"5");

					if(sysAutoNum!=null){
					 	contactNo=sysAutoNum.getNowNo();
					}
					//商户分账数据
					splitData="00145111^"+m1;
					 if(contactNo!=null){
						 splitData=splitData+";"+contactNo+"^"+m2+";";
					 }else{
						 contactNo="00145112";
						 splitData=splitData+";"+contactNo+"^"+m2+";";
					 }

				   goPayBean.setSplitData1(new BigDecimal(amount1));
				   if(!"".equals(amount2))
				   goPayBean.setSplitData2(new BigDecimal(amount2));
				   goPayBean.setOrgManagerId(contactNo);//设置服务费的管理公司
				   goPayBean.setMerId(Utility.SEND_BANK_MERID);//商户号
				   goPayBean.setCuryId(Utility.SEND_BANK_CURYID);//订单交易币种
				   goPayBean.setVersion(Utility.SEND_BANK_VERSION);//版本号
				   goPayBean.setBgRetUrl(Utility.SEND_BANK_BGRETURL);//后台交易接收URL地址
				   goPayBean.setPageRetUrl(Utility.SEND_BANK_PAGERETURL);//页面交易接收URL地址
				   goPayBean.setGateId(Utility.SEND_BANK_GATEID);//支付网关号
				   if(loanRePlan.getParam1()!=null&&!"".equals(loanRePlan.getParam1())&&
						   loanRePlan.getParam2()!=null&&!"".equals(loanRePlan.getParam2())&&
						   loanRePlan.getParam3()!=null&&!"".equals(loanRePlan.getParam3())&&
					     loanRePlan.getParam4()!=null&&!"".equals(loanRePlan.getParam4())
					     &&loanRePlan.getParam5()!=null&&!"".equals(loanRePlan.getParam5())
					     &&loanRePlan.getParam6()!=null&&!"".equals(loanRePlan.getParam6())){
					     goPayBean.setParam1(loanRePlan.getParam1());//开户行号
						 goPayBean.setParam2(loanRePlan.getParam2());//卡折标志
						 goPayBean.setParam3(loanRePlan.getParam3());//卡号/折号
						 goPayBean.setParam4(loanRePlan.getParam4());//持卡人姓名
						 goPayBean.setParam5(loanRePlan.getParam5());//证件类型
						 goPayBean.setParam6(loanRePlan.getParam6()); //证件号

				   }else{
					     flag= queryByContractId(Integer.parseInt(loanRePlan.getKey()),goPayBean);
						 if(!flag){
							 goPayBean.setParam1(goPayBean.getParam1());//开户行号
							 goPayBean.setParam2(goPayBean.getParam2());//卡折标志
							 goPayBean.setParam3(goPayBean.getParam3());//卡号/折号
							 goPayBean.setParam4(goPayBean.getParam4());//持卡人姓名
							 goPayBean.setParam5(goPayBean.getParam5());//证件类型
							 goPayBean.setParam6(goPayBean.getParam6()); //证件号
						 }
				   }
				 goPayBean.setParam7("");
				 goPayBean.setParam8("");
				 goPayBean.setParam9("");
				 goPayBean.setParam10("");
				 goPayBean.setOrdDesc("批量代扣款");
				 goPayBean.setShareType(Utility.SEND_BANK_TYPE);//分账类型
				 goPayBean.setShareData(splitData);
				 goPayBean.setPriv1("");
				 goPayBean.setCustomIp("");
				 messages.add(goPayBean);
			 }
			 if(httpClientUtil==null){
				 httpClientUtil = new HttpClientUtil();
			 }

			 try{
					List<Map> result= httpClientUtil.sendInformation(messages,path,contractService,Integer.parseInt(loanRePlan.getValue11()),
							loanRePlan.getValue2(), loanRePlan.getValue(), loanRePlan.getValue10() ,null,null,null,
							sessionBean.getStaffId(),null);

					if(result!=null&&result.size()>0&&messages!=null&&messages.size()>0){
						BigDecimal rsAmount1=new BigDecimal("0.00");
						BigDecimal rsAmount2=new BigDecimal("0.00");
						  Map map=null;
						  String status=null;
						  String id=null;
						  String contractId=null;
						  String rePlanId=null;

						 for(int i=0;i<list.size();i++){
							   map=result.get(i);
							    if(map.get("mresultid")!=null){
							    	id=map.get("mresultid").toString();
							    }
							    if(map.get("rePlanId")!=null){
							    	rePlanId=map.get("rePlanId").toString();
							    }
							    if(map.get("status")!=null){
							    	   status=map.get("status").toString();
							    }
							    if(map.get("contractId")!=null){
							    	contractId=map.get("contractId").toString();
							    }

								if(map.get("mount1")!=null){
									rsAmount1=new BigDecimal(map.get("mount1").toString());
								}
								if(map.get("mount2")!=null){
									rsAmount2=new BigDecimal(map.get("mount2").toString());
								}
								 LoanRePlan loanRePlan2=	 contractService.loanLoanRePlan(Integer.parseInt(rePlanId));
								if(status!=null&&"1".equals(status.trim())){
									//修改进件的还款状态


									 if(loanRePlan2!=null){
										 loanRePlan2.setDebitStatus(22+"");
										 contractService.saveOrUpdateLoanRePlan(loanRePlan2);
									 }
									 //调用自动核销去核销
									 try{
										 loanChargeService.chargeOff(Integer.parseInt(contractId), new Date(),rsAmount1);
										 creApMainServiceDataService.chargeOff_new(Integer.parseInt(contractId), null, new Date(), rsAmount2);
										 //改更扣款的状态
										 contractService.updateMortgageDeduction(Integer.parseInt(id), "1");
										 MortgageDeduction mortgageDeduction= contractService.loadMortgageDeduction(Integer.parseInt(id));
										 mortgageDeduction.setIsoffs("1");
										 contractService.updateMortgageDeduction(mortgageDeduction);


									 }catch(Exception e){
										e.printStackTrace();
									 }

								}else{
									loanRePlan2.setDebitStatus(23+"");
									 contractService.saveOrUpdateLoanRePlan(loanRePlan2);
								}
						 }
						 super.showMessage(0, "批理扣款完成，请查看扣款结果统计表");
						 return "";
					}

				}catch(Exception e){
						 e.printStackTrace();
						 super.showMessage(0, "代扣款失败！");

			 }

			return "";

		}

	private  boolean   queryByContractId(int id, GoPayBean goPayBean ){

		 List<MortgageDeduction> result =contractService.queryMortgageDeduction(" and t.type='1' and t.applyMainId ="+id);
		  if(result!=null&&result.size()>0){
			  MortgageDeduction mortgageDeduction=result.get(0);
			  goPayBean.setMerId(mortgageDeduction.getMerId());
				 goPayBean.setParam1(mortgageDeduction.getParam1());
				 goPayBean.setParam2(mortgageDeduction.getParam2());
				 goPayBean.setParam3(mortgageDeduction.getParam3());
				 goPayBean.setParam4(mortgageDeduction.getParam4());
				 goPayBean.setParam5(mortgageDeduction.getParam5());
				 goPayBean.setParam6(mortgageDeduction.getParam6());
				 return true;
		  }else{
			  return false;
		  }
	}

	private String getConditionServie(){
		String condition="";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1=null;
		Date date2=null;
		Calendar cal = Calendar.getInstance();
		try{
			if(beginDate!=null&&!"".equals(beginDate)&&endDate!=null&&!"".equals(endDate)){
				date1=formatter.parse(formatter.format(beginDate));
				date2=formatter.parse(formatter.format(endDate));
			}else{
				date2=formatter.parse(formatter.format(new Date()));
			}

			cal.setTime(date2);
			//cal.add(Calendar.DATE, 1);
		}catch(Exception e){

		}

		if(beginDate!=null&&!"".equals(beginDate)&&endDate!=null&&!"".equals(endDate)){

				condition = " and scDatetime>='"+formatter.format(beginDate)+"'";
				condition = condition+" and scDatetime<='"+formatter.format(cal.getTime())+"'";
		}else{
			condition = condition+" and scDatetime<='"+formatter.format(cal.getTime())+"'";
		}

		if(name!=null&&!"".equals(name.trim())){
			condition = condition+" and B.customerName like '%"+name+"%' ";

		}

//		if(status!=null&&status.trim().length()==0){
//			condition = 	condition+	" and status in ('"	+ DictionaryType.CRE_PLAN_STATUS_04		+ "' ,'"+DictionaryType.CRE_PLAN_STATUS_05
//					+"', '"+DictionaryType.CRE_PLAN_STATUS_02 +"' )";
//		}else  if(status!=null) {
//	    	condition = condition+	" and debit_status= '"	+ status +"'";
//	     }else{
//	    	 condition = 	condition+
//	    			 " and status in ('"	+ DictionaryType.CRE_PLAN_STATUS_04		+ "' ,'"+DictionaryType.CRE_PLAN_STATUS_05
//						+"', '"+DictionaryType.CRE_PLAN_STATUS_02 +"' )";
//	     }
		return condition;

	}

	private String getCondition(){
		String condition="";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date1=null;
		Date date2=null;
		Calendar cal = Calendar.getInstance();
		try{
			if(beginDate!=null&&!"".equals(beginDate)&&endDate!=null&&!"".equals(endDate)){
				date1=formatter.parse(formatter.format(beginDate));
				date2=formatter.parse(formatter.format(endDate));
			}else{
				date2=formatter.parse(formatter.format(new Date()));
			}

			cal.setTime(date2);
			//cal.add(Calendar.DATE, 1);
		}catch(Exception e){

		}

		if(beginDate!=null&&!"".equals(beginDate)&&endDate!=null&&!"".equals(endDate)){

				condition = " and scDatetime>='"+formatter.format(beginDate)+"'";
				condition = condition+" and scDatetime<='"+formatter.format(cal.getTime())+"'";
		}else{
			condition = condition+" and scDatetime<='"+formatter.format(cal.getTime())+"'";
		}

		if(name!=null&&!"".equals(name.trim())){
			condition = condition+" and name like '%"+name+"%' ";

		}

		if(status!=null&&status.trim().length()==0){
			condition = 	condition+	" and temp.status in ('"	+ DictionaryType.CRE_PLAN_STATUS_04		+ "' ,'"+DictionaryType.CRE_PLAN_STATUS_05
					+"', '"+DictionaryType.CRE_PLAN_STATUS_02 +"' )";
		}else  if(status!=null) {
	    	condition = condition+	" and temp.debit_status= '"	+ status +"'";
	     }else{
	    	 condition = 	condition+
	    			 " and temp.status in ('"	+ DictionaryType.CRE_PLAN_STATUS_04		+ "' ,'"+DictionaryType.CRE_PLAN_STATUS_05
						+"', '"+DictionaryType.CRE_PLAN_STATUS_02 +"' )";
	     }
		return condition;

	}


	public List<HashType> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(List<HashType> listCustomer) {
		this.listCustomer = listCustomer;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}




}

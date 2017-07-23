package org.starlightfinancial.deductiongateway.utility;

public class Utility {

    public static final String LOAN_ADJUST_TYPE_01 = "1";// "调整类型_月利率调整";
    public static final String LOAN_ADJUST_TYPE_02 = "2";// "调整类型_提前还款";
    public static final String LOAN_ADJUST_TYPE_03 = "3";// "调整类型_展期调整";

    public static final String SYS_MENU_01 = "2";// "待填详细资料";
    public static final String SYS_MENU_02 = "3";// "本笔借款情况";
    public static final String SYS_MENU_03 = "4";// "市场部意见";
    public static final String SYS_MENU_04 = "5";// "客户资料复核";
    public static final String SYS_MENU_05 = "6";// "风控初审";
    public static final String SYS_MENU_06 = "7";// "风控复审";
    public static final String SYS_MENU_07 = "8";// "领导审核";
    public static final String SYS_MENU_08 = "9";// "待处理合同";
    public static final String SYS_MENU_09 = "15";// "财务管理";
    public static final String SYS_MENU_10 = "54";// "放款管理";
    public static final String SYS_MENU_11 = "16";// "法务管理";

    public static final String SYS_MENU_01_01 = "待提交申请";
    public static final String SYS_MENU_01_02 = "多次贷款";
    public static final String SYS_MENU_01_03 = "市场部意见";
    public static final String SYS_MENU_01_04 = "客户资料复核";
    public static final String SYS_MENU_01_05 = "内审";
    public static final String SYS_MENU_01_06 = "外审";// 原 风控复审
    public static final String SYS_MENU_01_07 = "审批";
    public static final String SYS_MENU_01_08 = "待签约";
    public static final String SYS_MENU_01_09 = "财务管理";
    public static final String SYS_MENU_01_10 = "放款管理";
    public static final String SYS_MENU_01_11 = "法务管理";

    public static final String MESSAGE_SAVE_01 = "保存成功！";
    public static final String MESSAGE_SAVE_02 = "保存失败！";
    public static final String MESSAGE_DELETE_01 = "删除成功！";
    public static final String MESSAGE_DELETE_02 = "删除失败！";

    public static final String MESSAGE_SUBMIT_01 = "提交成功！";
    public static final String MESSAGE_SUBMIT_02 = "提交失败！";
    public static final String MESSAGE_SUBMIT_03 = "存在同名客户，请进行查重操作！";

    public static final String AUTH_CONTRACT = "合同列表";
    public static final String AUTH_CUSTOMER = "客户列表";
    public static final String AUTH_CREDIT = "申请列表";
    public static final String AUTH_TASK_NOTICE = "事项列表";
    public static final int AUTH_TYPE_VIEW = 0;// 查看
    public static final int AUTH_TYPE_UPDATE = 2;// 修改
    public static final int AUTH_TYPE_ADD = 1;// 增加
    public static final int AUTH_TYPE_DELETE = 3;// 删除

    public static final String TEMPLATE_FILE_01 = "风控调查表-模板.xls";// 风控调查表
    public static final String TEMPLATE_FILE_02 = "预约签约模板.xls";// 预约签约模板
    public static final String TEMPLATE_FILE_03 = "放款通知书模板.xls";// 放款通知书模板
    public static final String TEMPLATE_FILE_04 = "催款通知书模板.xls";// 放款通知书模板

    public static final String JOB_MESSAGE_00001 = "查询结果为待办任务中的数据   ";// 放款通知书模板
    public static final String JOB_MESSAGE_00002 = "查询结果为所有任务中的数据    ";// 放款通知书模板

    public static final String CON_DATA_PAGE_TYPE_01 = "1";// 总资产
    public static final String CON_DATA_PAGE_TYPE_02 = "2";// 总负债con_data_id
    public static final String CON_DATA_ID_01 = "1";//
    public static final String CON_DATA_ID_06 = "6";//
    public static final String CON_DATA_ID_08 = "8";//

    public static final String CON_DATA_NAME_MATTER_03 = "3";//
    public static final String CON_DATA_NAME_MATTER_07 = "7";//
    public static final String CON_DATA_NAME_MATTER_40 = "40";//
    public static final String CON_DATA_NAME_MATTER_41 = "41";//
    public static final String CON_DATA_NAME_MATTER_54 = "54";//
    public static final String CON_DATA_NAME_MATTER_49 = "49";//
    public static final String CON_DATA_NAME_MATTER_52 = "52";//

    //	public static final String SEND_BANK_URL= "http://bianmin-test.chinapay.com/cpeduinterface/OrderGet.do";//发送到银联的测试的地址
//    public static final String SEND_BANK_URL = "http://apps.chinapay.com/cpeduinterface/OrderGet.do";//生产地址
    public static final String SEND_BANK_URL = "http://newpayment-test.chinapay.com/CTITS/cpeduinterface/OrderGet.do ";//测试环境
//    public static final String SEND_BANK_BGRETURL = "http://192.168.0.122:8082/microcredit/mortgagededuction/BgRetUrl.jsp";//后台交易接收URL地址
    public static final String SEND_BANK_BGRETURL = "http://113.204.117.46:8081/updateDeduction";//后台交易接收URL地址
//    public static final String SEND_BANK_PAGERETURL = "http://192.168.0.122:8082/microcredit/mortgagededuction/PageRetUrl.html";//页面交易接收URL地址
    public static final String SEND_BANK_PAGERETURL = "http://113.204.117.46:8081/updateDeduction";//页面交易接收URL地址
//    public static final String SEND_BANK_KEY_FILE = "MerPrK_808080201302851_20131030113446.key";//生产环境私钥文件名
    public static final String SEND_BANK_KEY_FILE = "MerPrK808080290000001.key";//测试环境私钥文件名
    public static final String SEND_BANK_KEY_PUB_FILE = "PgPubk.key";//公钥文件名
//    public static final String SEND_BANK_MERID = "808080201302851";//生产环境
    public static final String SEND_BANK_MERID = "808080290000001";//测试环境
    public static final String SEND_BANK_VERSION = "20100401";//版本号curyId
    public static final String SEND_BANK_GATEID = "7008";//网关
    public static final String SEND_BANK_CURYID = "156";//交易币种
    public static final String SEND_BANK_TYPE = "0001";//分账类型
}

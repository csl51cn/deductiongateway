package org.starlightfinancial.deductiongateway.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DictionaryType {
    public static final String CREIDT_SUBJECT_001 = "1";// 借款主体-企业,生成合同根据此判断

    public static final String CUSTOMER_SALES_STATUS = "50";// 客户销售状态
    public static final String CUSTOMER_TYPE = "3";// 客户类型
    public static final String CREIDT_SUBJECT = "25";// 借款主体
    public static final String CUSTOMER_SOURCE = "1";// 客户来源
    public static final String CERTIFICATE_TYPE = "16";// 证件类型
    public static final String SALES_CHANNELS = "37";// 销售渠道
    public static final String BELONG_TO_STORE = "40";// 所属门店
    public static final String CORP_CUSTOMER_LEVEL = "9";// 企业客户等级
    public static final String CORP_OWN_PROPERTY = "13";// 企业登记类型
    public static final String MANAGEMENT_PLACE_PROPERTIES = "14";// 经营场所性质
    public static final String LEADERSHIP_MANAGEMENT_ABILITY = "51";// 领导管理能力
    public static final String FINANCIAL_STATEMENT_TYPE = "52";// 财务报表类型
    public static final String PERSONAL_CUSTOMER_LEVEL = "15";// 个人客户等级
    public static final String PERSONAL_FINANCIAL_ABILITY = "53";// 个人理财力
    public static final String CUSTOMER_DEGREE = "2";// 学历
    public static final String CUSTOMER_CAREER = "4";// 职业
    public static final String CUSTOMER_INDUSTRY = "5";// 行业
    public static final String COMPANY_TYPE = "6";// 单位性质
    public static final String CUSTOMER_POSITION = "7";// 职务
    public static final String SALARY_PAYMENT = "55";// 发薪支付方式
    public static final String INCOME_SOURCE = "8";// 收入来源
    public static final String ACCUMULATION_FUND_BANK = "56";// 公积金开户银行
    public static final String ASSET_CATEGORY = "57";// 资产大类
    public static final String ASSET_SMALL_CLASS = "58";// 资产小类
    public static final String PLAN_REIMBURSEMENT_CORP = "21";// 计划还款单位
    public static final String PRINCIPAL_PAYMENT = "22";// 还款方式（还本）
    public static final String INTREST_PAYMENT = "61";// 还款方式（付息）
    public static final String CUSTOMER_QUALITY = "20";// 客户品质
    public static final String PAWN_TYPE = "62";// 抵押物类型
    public static final String BUY_TYPE = "63";// 购买方式
    public static final String BUILDING_STRUCTURE = "64";// 房屋结构
    public static final String LAND_TYPE = "65";// 土地类型
    public static final String LAND_USE = "66";// 土地用途
    public static final String PLEDGE_TYPE = "31";// 质押类型
    public static final String RISK_TYPE = "67";// 保险公司名称
    public static final String VERIFY_TYPE = "32";// 核实方式
    public static final String CONTRACT_STATUS = "68";// 合同签约状态
    public static final String MATERIAL_TYPE = "69";// 材料类型大类
    public static final String MATERIAL_SMALL_CLASS = "70";// 材料类型小类
    public static final String CREDIT_USE = "23";// 贷款用途
    public static final String HAS_LEFT = "33";// 有无电梯
    public static final String DECORATE_LEVEL = "34";// 装修级别
    public static final String LOCATION_LEVEL = "35";// 地段等级
    public static final String CUSTOMER_SEX = "54";// 性别
    public static final String VISIT_TYPE = "72";// 访问类型
    public static final String DOCUMENT_STATUS = "73";// 单据状态
    public static final String CRE_PLAN_STATUS = "74";// 还款计划状态
    public static final String CONTRACT_PRE_MATERIAL = "75";// 预约签约材料类型
    public static final String CRE_CON_BRE_MAIN_TYPE = "76";// 合同违约类型
    public static final String CRE_CON_BRE_RESULT_TYPE = "77";// 合同违约处理类型
    public static final String LOAN_PRO_RECORD_METHOD = "78"; // 催款手段
    public static final String LOAN_PRO_RECORD_TARGET = "79"; // 催款对象
    public static final String LOAN_PRO_RECORD_CALL_RESULT = "80"; // 通话结果
    public static final String LOAN_PRO_RECORD_COM_RESULT = "81"; // 谈话结果
    public static final String CUS_MATERIAL_TYPE = "82"; // 谈话结果
    public static final String SCHEDULE_TYPE = "83"; // 回访类型
    public static final String CORP_REA_POSITION = "10"; // 企业联系人职务
    public static final String PER_MAIN_LEVEL = "11"; // 绩效考核等级
    public static final String STAFF_PER_POST_LEVEL = "84"; // 绩效考核等级
    public static final String CUSTOMER_STAY_STYLE = "85"; // 现住房情况
    public static final String RISK_AFTER_STATUS = "200"; // 贷后风险五级分类
    public static final String RISK_AFTER_STATUS_01 = "1"; // 贷后风险五级分类--正常贷款
    public static final String HO_STATUS = "201"; // 抵押物现状
    public static final String MEDIUM_STATUS = "202"; // 中介费收取状态
    public static final String FAMILY_ASSETS_NATURE = "87";// 家庭资产性质
    public static final String STATE_OF_MATTER = "88";// 家庭资产物质情况

    public static final String OVERDUE_DATE_NUMBER = "91";// 逾期天数
    public static final String OVERDUE_AMOUNT = "92";// 逾期金额

    public static final String SAFEGUARD_WAY = "93";// 保障方式

    public static final String OBJECT_TYPES = "94";// 贷款对象类型
    public static final String LOAN_INDUSTRY = "95";// 贷款行业
    public static final String CUSTOMER_SIZE = "96";// 客户规模
    public static final String CREDIT_LOAN_CLASSIFICATION = "97";// 信用贷款分类
    public static final String USED_FOR_CLASSIFICATION = "98";// 贷款用途分类

    public static final String SYS_DICT_300 = "300"; // 划款方式
    public static final String SYS_DICT_301 = "301"; // 贷款还款支付情况
    public static final String SYS_DICT_302 = "302"; // 提前收费方式

    public static final String CRE_PLAN_STATUS_01 = "1";// 已正常核销
    public static final String CRE_PLAN_STATUS_02 = "2";// 未全额还款
    public static final String CRE_PLAN_STATUS_03 = "3";// 已补交 -- 废弃
    public static final String CRE_PLAN_STATUS_04 = "4";// 未核销
    public static final String CRE_PLAN_STATUS_05 = "5";// 已逾期

    public static final String MEDIUM_STATUS_01 = "4"; // 未支付
    public static final String MEDIUM_STATUS_02 = "1"; // 已支付
    public static final String MEDIUM_STATUS_03 = "2"; // 未完全支付

    public static final String CREDIT_STATUS_01 = "0";// 申请 单据状态 0
    public static final String CREDIT_STATUS_02 = "1";// 放贷 单据状态 1
    public static final String CREDIT_STATUS_03 = "2";// 扣收 单据状态 2
    public static final String CREDIT_STATUS_04 = "3";// 已提前还款 单据状态3
    public static final String CREDIT_STATUS_05 = "4";// 已结清 单据状态 4
    public static final String CREDIT_STATUS_06 = "5";// 已转入内表 单据状态5
    public static final String CREDIT_STATUS_07 = "6";// 已转入外表 单据状态 6
    public static final String CREDIT_STATUS_08 = "7";// 表外核销结算 单据状态7
    public static final String CREDIT_STATUS_09 = "8";// 逾期 单据状态8
    public static final String CREDIT_STATUS_10 = "9";// 违约处理结束 单据状态9
    public static final String CREDIT_STATUS_11 = "10";// 待初审 单据状态 10
    public static final String CREDIT_STATUS_12 = "11";// 待复审 单据状态 11
    public static final String CREDIT_STATUS_13 = "12";// 待风控初审 单据状态 12
    public static final String CREDIT_STATUS_14 = "13";// 待风控复审 单据状态 13
    public static final String CREDIT_STATUS_15 = "14";// 待领导审批 单据状态 14
    public static final String CREDIT_STATUS_16 = "15";// 待合同签约 单据状态 15
    public static final String CREDIT_STATUS_17 = "16";// 已合同签约 单据状态 16
    public static final String CREDIT_STATUS_18 = "17";// 客户主动申请已撤销 单据状态 17
    public static final String CREDIT_STATUS_19 = "18";// 审批已撤销 单据状态 18
    public static final String CREDIT_STATUS_20 = "19";// 呆帐 单据状态 19
    public static final String CREDIT_STATUS_21 = "20";// 放款核实
    public static final String CREDIT_STATUS_22 = "21";// 财务审批
    public static final String CREDIT_STATUS_23 = "22";// 总经理审批

    public static final Map<String, String> CREDIT_STATUS_MAP;
    public static final Map<String, String> CREDIT_NAME_STATUS_MAP;

    public static final String CERTIFICATE_TYPE_04 = "4";// 营业执照
    public static final String CERTIFICATE_TYPE_05 = "5";// 组织机构代码

    // 合同表状态
    public static final String CONTRACT_STATUS_01 = "0";// 68 未预约
    public static final String CONTRACT_STATUS_02 = "1";// 68 预约
    public static final String CONTRACT_STATUS_03 = "2";// 68 已签约
    public static final String CONTRACT_STATUS_04 = "3";// 68 本公司放弃签约
    public static final String CONTRACT_STATUS_05 = "4";// 68 客户放弃签约

    // 合同取回状态
    public static final String CONTRACT_CHECK_STATUS_01 = "0";// 正常
    public static final String CONTRACT_CHECK_STATUS_02 = "1";// 取回
    public static final String CONTRACT_CHECK_STATUS_03 = "2";// 取消贷款

    // 模板类型
    // public static final String TEMPLATE_01 = "01";// 合同模板
    // public static final String TEMPLATE_02 = "02";// 尽职调查报告模板

    // 客户类型
    public static final String CUSTOMER_TYPE_01 = "0"; // 个人客户
    public static final String CUSTOMER_TYPE_02 = "1"; // 企业客户

    public static final String CUSTOMER_ISBACK_01 = "0";// 非黑名单
    public static final String CUSTOMER_ISBACK_02 = "1";// 黑名单
    public static final String CUSTOMER_LEVEL_04 = "4";// 黑名单等级，若客户等级为黑名单等级，在cus_main表中的黑名单字段03中设置为1
    public static final String CUSTOMER_LEVEL_02 = "2";// 客户等级2 正式客户

    // 自动编号类型
    public static final String SYS_AUTO_NUM_01 = "1"; // 客户编号
    public static final String SYS_AUTO_NUM_02 = "2"; // 贷款申请编号
    public static final String SYS_AUTO_NUM_03 = "3"; // 合同编号
    public static final String SYS_AUTO_NUM_04 = "4"; // 员工编号

    // 审批类型
    public static final String APPROVE_TYPE_01 = "1"; // 业务意见
    public static final String APPROVE_TYPE_02 = "2"; // 资料复审
    public static final String APPROVE_TYPE_03 = "3"; // 风控初审
    public static final String APPROVE_TYPE_04 = "4"; // 风控复审
    public static final String APPROVE_TYPE_05 = "5"; // 领导审批
    public static final String APPROVE_TYPE_06 = "6"; // 法律意见审批
    public static final String APPROVE_TYPE_07 = "7"; // 放款意见
    public static final String APPROVE_TYPE_08 = "8"; // 核实报告情况
    public static final String APPROVE_TYPE_09 = "9"; // 风控意见审批
    public static final String APPROVE_TYPE_10 = "0"; // 集团法务意见审批
    public static final String APPROVE_TYPE_11 = "11"; // 放款核实
    public static final String APPROVE_TYPE_12 = "12"; // 财务审批
    public static final String APPROVE_TYPE_13 = "13"; // 总经理审批

    public static final Map<String, String> APPROVE_TYPE_MAP;

    // 资产小类类型
    public static final String CUSTOMER_PERSON_ASSET_01 = "6"; // 房产类型
    public static final String CUSTOMER_PERSON_ASSET_02 = "1"; // 汽车类型
    public static final String CUSTOMER_PERSON_ASSET_03 = "4"; // 大类其它型

    // 员工是否在职状态
    public static final String STAFF_IS_RESIGN_01 = "0"; // 在职
    public static final String STAFF_IS_RESIGN_02 = "1"; // 离职

    public static final String STAFF_IS_ADMIN_01 = "0"; // 非管理人员
    public static final String STAFF_IS_ADMIN_02 = "1"; // 管理人员

    // 材料大类型
    public static final String CRE_MATERIAL_TYPE_01 = "0"; // 贷款材料
    public static final String CRE_MATERIAL_TYPE_02 = "1"; // 公司录入材料(风控调查表、资产评估表、法律意见书,风控意见书,放款通知书)
    public static final String CRE_MATERIAL_TYPE_03 = "2"; // 生成合同数据(借款合同、抵押合同、还本付息表,授权委托书)
    // 贷款公司材料
    public static final String CRE_MATERIAL_IN_01 = "1"; // 小贷业务尽职调查报告
    public static final String CRE_MATERIAL_IN_02 = "2"; // 法律意见书
    public static final String CRE_MATERIAL_IN_03 = "3"; // 风控意见，风控总结报告
    public static final String CRE_MATERIAL_IN_04 = "4"; // 合同书
    public static final String CRE_MATERIAL_IN_05 = "5"; // 还本付息本
    public static final String CRE_MATERIAL_IN_06 = "6"; // 待缴款授权书
    public static final String CRE_MATERIAL_IN_07 = "7"; // 贷款付款审批单
    public static final String CRE_MATERIAL_IN_08 = "8"; // 律师函
    public static final String CRE_MATERIAL_IN_09 = "9"; // 讼诉程序
    public static final String CRE_MATERIAL_IN_10 = "10"; // 其它
    public static final String CRE_MATERIAL_IN_11 = "11"; // 风控意见书
    public static final String CRE_MATERIAL_IN_12 = "12"; // 放款通知书
    public static final String CRE_MATERIAL_IN_13 = "13"; // 集团法律意见书
    public static final String CRE_MATERIAL_IN_14 = "41"; // 业务调查

    // 是否销售人员
    public static final String STAFF_IS_SALESMAN_01 = "1"; // 是否销售人员

    // 客户提交加锁
    public static final String CUSTOMER_LOCK_01 = "0"; // 创建客户
    public static final String CUSTOMER_LOCK_02 = "1"; // 待填详细资料
    public static final String CUSTOMER_LOCK_03 = "2"; // 可提交客户贷款申请
    public static final String CUSTOMER_LOCK_04 = "3"; // 不可提交客户申请

    // 抵押物类型
    public static final String MORT_TYPE_01 = "1"; // 房产
    public static final String MORT_TYPE_02 = "2"; // 其他

    // 质押物类型
    public static final String PLEDGE_TYPE_01 = "1"; // 汽车
    public static final String PLEDGE_TYPE_02 = "2"; // 机器设备
    public static final String PLEDGE_TYPE_03 = "3"; // 股权
    public static final String PLEDGE_TYPE_04 = "4"; // 有价证券
    public static final String PLEDGE_TYPE_05 = "5"; // 其他

    public static final String DELETE_FLAG_01 = "0"; // 可用
    public static final String DELETE_FLAG_02 = "1"; // 不可用
    public static final String DELETE_FLAG_03 = "2"; // 重复

    public static final String CUSTOMER_STATE_01 = "0"; // 非正式客户
    public static final String CUSTOMER_STATE_02 = "1"; // 正式客户

    // 合同模版类型
    public static final String TEMPLATE_TYPE_01 = "1"; // 个人借款合同模板
    public static final String TEMPLATE_TYPE_02 = "2"; // 抵押合同模板
    public static final String TEMPLATE_TYPE_03 = "3"; // 还本付息表模板
    public static final String TEMPLATE_TYPE_04 = "4"; // 保证合同-个人模板
    public static final String TEMPLATE_TYPE_05 = "5"; // 保证合同-企业模板
    public static final String TEMPLATE_TYPE_06 = "6"; // 企业借款合同模板

    public static final int TEMPLATE_PARENT_ID = 0; // 借款合同模板的parentID

    // 计划还款状态
    public static final String LOAN_RE_PLAN_04 = "4"; // 未核销
    public static final String LOAN_RE_PLAN_05 = "5"; // 已逾期

    public static final String AUDIT_ITEMS_STATUS_01 = "1"; // 新建
    public static final String AUDIT_ITEMS_STATUS_02 = "2"; // 发起
    public static final String AUDIT_ITEMS_STATUS_03 = "3"; // 完成

    public static final String NOTICE_STATUS_01 = "1"; // 为阅完
    public static final String NOTICE_STATUS_02 = "2"; // 已阅完

    public static final String NOTICE_TYPE_01 = "1"; // 告知类型1
    public static final String NOTICE_TYPE_02 = "2";// 告知类型2
    public static final String NOTICE_TYPE_10 = "10";// 流程告知
    public static final String NOTICE_TYPE_11 = "11";// 业务意见告知法务管理
    public static final String NOTICE_TYPE_12 = "12";// 风控复审告知财务管理
    public static final String NOTICE_TYPE_13 = "13";// 领导审核告知合同管理
    public static final String NOTICE_TYPE_14 = "14";// 生成合同告知放款管理
    public static final String NOTICE_TYPE_15 = "15";// 领导审核告知集团风控人员
    public static final String NOTICE_TYPE_20 = "20";// 审批通过告知
    public static final String NOTICE_TYPE_21 = "21";// 审批打回告知
    public static final String NOTICE_TYPE_22 = "22";// 审批撤销告知
    public static final String NOTICE_TYPE_23 = "23";// 申请撤销告知
    public static final String NOTICE_TYPE_24 = "24";// 放款审核通知
    public static final String NOTICE_TYPE_25 = "25";// 财务放款通知
    public static final String NOTICE_TYPE_26 = "26";// 请款打回告知

    public static final String NOTICE_AUDIT_TYPE_01 = "1"; // 人工
    public static final String NOTICE_AUDIT_TYPE_02 = "2";// 其他

    public static final String NOTICE_MENU_NAME_01 = "法务管理";
    public static final String NOTICE_MENU_NAME_02 = "财务管理";
    public static final String NOTICE_MENU_NAME_03 = "待处理合同";
    public static final String NOTICE_MENU_NAME_04 = "放款管理";

    public static final String NOTICE_TITLE_01 = "审批通过消息";
    public static final String NOTICE_TITLE_02 = "审批打回消息";
    public static final String NOTICE_TITLE_03 = "审批撤销消息";
    public static final String NOTICE_TITLE_04 = "申请撤销消息";

    public static final String NOTICE_CONTENT_01 = "已通过业务意见审批，请准备法律意见书信息!";
    public static final String NOTICE_CONTENT_02 = "已通过风控复审，请查看财务管理信息!";
    public static final String NOTICE_CONTENT_03 = "已通过领导审批，请准备合同信息!";
    public static final String NOTICE_CONTENT_04 = "已通生成合同，请查看放款管理信息!";

    // 材料锁
    public static final String MATERIAL_LOCK_01_01 = "0";// 申请材料锁 未锁
    public static final String MATERIAL_LOCK_01_02 = "1";// 申请材料锁 锁定
    public static final String MATERIAL_LOCK_02_01 = "0";// 放贷过程锁 未锁
    public static final String MATERIAL_LOCK_02_02 = "1";// 放贷过程锁 锁定
    public static final String MATERIAL_LOCK_03_01 = "0";// 贷后材料锁 未锁
    public static final String MATERIAL_LOCK_03_02 = "1";// 贷后材料锁 锁定

    public static final String MATERIAL_LOCK_01 = "1";// 申请材料锁
    public static final String MATERIAL_LOCK_02 = "2";// 放贷过程锁
    public static final String MATERIAL_LOCK_03 = "3";// 贷后材料锁

    public static final String MATERIAL_LOCK_BIG_CLASS_01 = "贷款对象材料";
    public static final String MATERIAL_LOCK_BIG_CLASS_02 = "贷款担保人材料";
    public static final String MATERIAL_LOCK_BIG_CLASS_03 = "抵押/质押材料";
    public static final String MATERIAL_LOCK_BIG_CLASS_04 = "贷款过程照片";
    public static final String MATERIAL_LOCK_BIG_CLASS_05 = "其他";
    public static final String MATERIAL_LOCK_BIG_CLASS_06 = "贷款过程文档";

    // 自动编号 是否日期
    public static final String IS_DATE_01 = "0"; // 否
    public static final String IS_DATE_02 = "1"; // 是

    public static final String RISK_ASSIGN_TYPE_01 = "1"; // 风控初审
    public static final String RISK_ASSIGN_TYPE_02 = "2"; // 风控复审

    public static final String RISK_ASSIGN_OPERATE_01 = "1"; // 手动
    public static final String RISK_ASSIGN_OPERATE_02 = "2"; // 抢占自由
    public static final String RISK_ASSIGN_OPERATE_03 = "3"; // 平均

    public static final Map<String, String> NOTICE_TYPE_MAP;
    public static final Map<String, String> NOTICE_TYPE_MAP2;

    public static final Map<String, String> NOTICE_AUDIT_TYPE_MAP;

    public static final Map<String, String> NOTICE_STATUS_MAP;

    public static final Map<String, String> RISK_ASSIGN_OPERATE_MAP;

    // 企业关系人类型
    public static final String CORP_CONTACT_00 = "0"; // 法人
    public static final String CORP_CONTACT_01 = "1"; // 实际控制人
    public static final String CORP_CONTACT_02 = "2"; // 股东
    public static final String CORP_CONTACT_03 = "3"; // 上游企业
    public static final String CORP_CONTACT_04 = "4"; // 下游企业

    // 个人联系人和配偶类型
    public static final String REALATION_TYPE_00 = "0"; // 配偶
    public static final String REALATION_TYPE_01 = "1"; // 父亲
    public static final String REALATION_TYPE_02 = "2"; // 母亲
    public static final String REALATION_TYPE_03 = "3"; // 兄弟
    public static final String REALATION_TYPE_04 = "4"; // 姐妹
    public static final String REALATION_TYPE_05 = "5"; // 表亲
    public static final String REALATION_TYPE_06 = "6"; // 同事
    public static final String REALATION_TYPE_07 = "7"; // 朋友
    public static final String REALATION_TYPE_08 = "8"; // 儿女
    public static final String REALATION_TYPE_09 = "9"; // 其它

    // 股东是否参与管理
    public static final String IS_MANAGEMENT_00 = "0"; // 是
    public static final String IS_MANAGEMENT_01 = "1"; // 否

    // 考核状态
    public static final String PER_MAIN_STATE_00 = "0"; // 未考核
    public static final String PER_MAIN_STATE_01 = "1"; // 正在考核
    public static final String PER_MAIN_STATE_02 = "2"; // 考核完成
    // 考核指标类型
    public static final String PER_RULE_TYPE_00 = "0"; // 内指标
    public static final String PER_RULE_TYPE_01 = "1"; // 外指标

    public static final String GLOBAL_TYPE_50 = "50"; // 自动回访天数
    public static final String GLOBAL_TYPE_51 = "51"; // 公司所在城市

    public static final String SCHEDULE_TYPE_00 = "1"; // 客户拜访
    public static final String SCHEDULE_TYPE_01 = "2"; // 客户关怀
    public static final String SCHEDULE_TYPE_02 = "3"; // 贷后管理
    public static final String SCHEDULE_TYPE_03 = "4"; // 日程管理

    // 还本方式
    public static final String REPAY_METHOD01_01 = "1"; // 等额本息
    public static final String REPAY_METHOD01_02 = "2"; // 等额本金递减
    public static final String REPAY_METHOD01_03 = "3"; // 到期一次还款
    public static final String REPAY_METHOD01_04 = "4"; // 人工填写本息

    // 还息方式
    public static final String REPAY_METHOD02_01 = "1"; // 等额本息还款
    public static final String REPAY_METHOD02_02 = "2"; // 等额本金递减
    public static final String REPAY_METHOD02_03 = "3"; // 到期一次还息和管理费
    public static final String REPAY_METHOD02_04 = "4"; // 第1期一次还息和管理费
    public static final String REPAY_METHOD02_05 = "5"; // 按月还利息和管理费
    public static final String REPAY_METHOD02_06 = "6"; // 人工填写本息

    public static final String CREDIT_TIME_UNIT_01 = "1"; // 月
    public static final String CREDIT_TIME_UNIT_02 = "2"; // 天
    public static final String CREDIT_TIME_UNIT_03 = "3"; // 年

    public static final String OPERATOR_TIME_TYPE_1 = "0"; // 风控初审
    public static final String OPERATOR_TIME_TYPE_2 = "1"; // 风控复审

    // 还款认领等状态
    public static final String FIN_REPAY_CHECK_01 = "1"; // 未提交
    public static final String FIN_REPAY_CHECK_02 = "2"; // 待确认
    public static final String FIN_REPAY_CHECK_03 = "3"; // 已确认

    public static final String MERID_SOURCE = "726";// 开户行
    public static final String CURYID_SOURCE = "724";// 交易币种
    public static final String SPLITTYPE_SOURCE = "725";// 分账类型

    // 领导审批
    public static Map<String, Object> Statuss = null;

    static {
        CREDIT_STATUS_MAP = new TreeMap<String, String>();
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_01, "录单");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_11, "业务意见");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_12, "资料复核");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_13, "内审");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_14, "外审");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_15, "审批");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_16, "签约");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_02, "放款");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_22, "财务审批");
        CREDIT_STATUS_MAP.put(CREDIT_STATUS_23, "总经理审批");

        CREDIT_NAME_STATUS_MAP = new TreeMap<String, String>();
        CREDIT_NAME_STATUS_MAP.put("录单", CREDIT_STATUS_01);
        CREDIT_NAME_STATUS_MAP.put("业务意见", CREDIT_STATUS_11);
        CREDIT_NAME_STATUS_MAP.put("资料复核", CREDIT_STATUS_12);
        CREDIT_NAME_STATUS_MAP.put("内审", CREDIT_STATUS_13);
        CREDIT_NAME_STATUS_MAP.put("外审", CREDIT_STATUS_14);
        CREDIT_NAME_STATUS_MAP.put("审批", CREDIT_STATUS_15);
        CREDIT_NAME_STATUS_MAP.put("签约", CREDIT_STATUS_16);
        CREDIT_NAME_STATUS_MAP.put("放款", CREDIT_STATUS_02);
        CREDIT_NAME_STATUS_MAP.put("财务审批", CREDIT_STATUS_22);
        CREDIT_NAME_STATUS_MAP.put("总经理审批", CREDIT_STATUS_23);

        APPROVE_TYPE_MAP = new TreeMap<String, String>();
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_01, "业务意见");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_02, "资料复核");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_03, "内审");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_04, "外审");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_05, "审批");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_06, "法律意见");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_07, "放款意见");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_08, "核实报告情况");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_09, "风控意见");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_10, "集团法务意见");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_11, "放款核实");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_12, "财务审批");
        APPROVE_TYPE_MAP.put(APPROVE_TYPE_13, "总经理审批");

        NOTICE_TYPE_MAP = new TreeMap<String, String>();
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_01, "通用通知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_02, "通用告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_10, "流程告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_11, "业务意见告知法务管理");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_12, "风控复审告知财务管理");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_13, "领导审核告知合同管理");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_14, "生成合同告知放款管理");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_15, "领导审核告知集团风控人员");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_20, "审批通过告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_21, "审批打回告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_22, "审批撤销告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_23, "申请撤销告知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_24, "放款审核通知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_25, "财务放款通知");
        NOTICE_TYPE_MAP.put(NOTICE_TYPE_26, "请款打回告知");

        NOTICE_TYPE_MAP2 = new TreeMap<String, String>();
        NOTICE_TYPE_MAP2.put(NOTICE_TYPE_01, "通用通知");
        NOTICE_TYPE_MAP2.put(NOTICE_TYPE_02, "通用告知");

        NOTICE_AUDIT_TYPE_MAP = new TreeMap<String, String>();
        NOTICE_AUDIT_TYPE_MAP.put(NOTICE_AUDIT_TYPE_01, "人工");
        NOTICE_AUDIT_TYPE_MAP.put(NOTICE_AUDIT_TYPE_02, "其他");

        NOTICE_STATUS_MAP = new TreeMap<String, String>();
        NOTICE_STATUS_MAP.put(NOTICE_STATUS_01, "未阅");
        NOTICE_STATUS_MAP.put(NOTICE_STATUS_02, "已阅");

        RISK_ASSIGN_OPERATE_MAP = new TreeMap<String, String>();
        RISK_ASSIGN_OPERATE_MAP.put(RISK_ASSIGN_OPERATE_01, "手动");
        RISK_ASSIGN_OPERATE_MAP.put(RISK_ASSIGN_OPERATE_02, "抢占自由");
        RISK_ASSIGN_OPERATE_MAP.put(RISK_ASSIGN_OPERATE_03, "平均");

        Statuss = new HashMap<String, Object>();
        Statuss.put(CREDIT_STATUS_13, "13");
        Statuss.put(CREDIT_STATUS_14, "15");
        Statuss.put(CREDIT_STATUS_15, "33");
        Statuss.put(CREDIT_STATUS_15, "34");
        // Statuss.put(CREDIT_STATUS_14,"15");
        // Statuss.put(CREDIT_STATUS_13,"13");
        // Statuss.put(CREDIT_STATUS_14,"15");
        // Statuss.put(CREDIT_STATUS_13,"13");
        // Statuss.put(CREDIT_STATUS_14,"15");
        // Statuss.put(CREDIT_STATUS_13,"13");
        // Statuss.put(CREDIT_STATUS_14,"15");
        // Statuss.put(CREDIT_STATUS_13,"13");
        // Statuss.put(CREDIT_STATUS_14,"15");

    }
}

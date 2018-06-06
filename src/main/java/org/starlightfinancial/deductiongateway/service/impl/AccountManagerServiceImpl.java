package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.dao.AccountDao;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.enums.ChinaPayCertTypeEnum;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 卡号管理Service实现类
 *
 * @author senlin.deng
 */
@Service
public class AccountManagerServiceImpl implements AccountManagerService {

    private static final Logger logger = LoggerFactory.getLogger(AccountManagerServiceImpl.class);

    @Autowired
    private AccountManagerRepository accountManagerRepository;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ChinaPayConfig chinaPayConfig;
    @Autowired
    private BaofuConfig baofuConfig;

    /**
     * 查询卡号
     *
     * @param contractNo  合同编号
     * @param bizNo       业务编号
     * @param accountName 账户名
     * @param pageBean    分页对象
     * @return 返回分页对象
     */
    @Override
    public PageBean queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean) {

        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //不为空则加条件
                if (StringUtils.isNotEmpty(contractNo)) {
                    predicates.add(cb.equal(root.get("contractNo"), contractNo));
                }
                if (StringUtils.isNotEmpty(bizNo)) {
                    predicates.add(cb.equal(root.get("bizNo"), bizNo));
                }
                if (StringUtils.isNotEmpty(accountName)) {
                    predicates.add(cb.equal(root.get("accountName"), accountName));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        long count = accountManagerRepository.count(specification);
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);
        Page<AccountManager> accountManagers = accountManagerRepository.findAll(specification, pageRequest);
        if (accountManagers.hasContent()) {
            pageBean.setTotal(accountManagers.getTotalElements());
            pageBean.setRows(accountManagers.getContent());
            return pageBean;
        }
        return null;
    }


    /**
     * 更新操作
     *
     * @param accountManager
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAccount(AccountManager accountManager) {
        accountManager.setChangeTime(new Date());
        accountManagerRepository.saveAndFlush(accountManager);
    }

    /**
     * 查询最后一条记录
     *
     * @return
     */
    @Override
    public List findLastAccount() {
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(1);
        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<AccountManager> accountManagers = accountManagerRepository.findAll(specification, pageRequest);
        return accountManagers.getContent();
    }

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message addAccount(String bizNo) {
        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(bizNo)) {
                    predicates.add(cb.equal(root.get("bizNo"), bizNo));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        //根据业务编号查询已经保存了的卡号信息
        List<AccountManager> existedAccountManagerList = accountManagerRepository.findAll(specification);
        //查询date_id
        Integer dateId = accountDao.findDateIdByBizNo(bizNo);
        //根据date_id查询业务系统代扣卡信息
        List<AccountManager> accountManagerList = accountDao.findAccountByDateId(dateId);
        //根据sort升序排序
        accountManagerList.sort(Comparator.comparingInt(AccountManager::getSort));
        if (accountManagerList.size() == 0) {
            return Message.fail("添加失败,未查询到代扣卡");
        }

        Iterator<AccountManager> iterator = accountManagerList.iterator();


        while (iterator.hasNext()) {
            AccountManager accountManager = iterator.next();
            if (existedAccountManagerList.size() > 0) {
                //去重,避免重复入库
                for (AccountManager existedAccountManager : existedAccountManagerList) {
                    //通过date_id,账户名,银行卡号,身份证,手机号对比,如果这五个字段相同,那么就认为两个对象相同
                    boolean isRepeated = existedAccountManager.getDateId().equals(accountManager.getDateId()) && StringUtils.equals(existedAccountManager.getAccountName(), accountManager.getAccountName()) &&
                            StringUtils.equals(existedAccountManager.getAccount(), accountManager.getAccount()) && StringUtils.equals(existedAccountManager.getCertificateNo(), accountManager.getCertificateNo()) &&
                            StringUtils.equals(existedAccountManager.getMobile(), accountManager.getMobile());
                    //如果数据库中已经有当前记录,从待保存的List中删除,避免重复入库
                    if (isRepeated) {
                        iterator.remove();
                        break;
                    }
                }

            }
        }

        accountManagerRepository.save(accountManagerList);
        return Message.success("添加代扣卡成功");

    }

    /**
     * 银联-查询是否签约
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message unionPayIsSigned(Integer id) {
        AccountManager accountManager = accountManagerRepository.findById(id);
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", accountManager.getAccount()));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(accountManager.getCertificateType())));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", accountManager.getCertificateNo()));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountManager.getAccountName()));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", accountManager.getMobile()));
        Message message;
        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealtimeSignStatusUrl(), basicNameValuePairs);
        } catch (Exception e) {
            logger.error("银联签约状态查询异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("银联签约状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }
        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);

                //获取签约状态:00-未签约,01-已签约,02-签约失败,03-已撤销
                String signState = result.getString("SignState");
                if (StringUtils.equals("01", signState)) {
                    //已签约,将返回的协议号提取出来,保存到数据库中
                    JSONObject cardTranData = result.getJSONObject("CardTranData");
                    accountManager.setUnionpayProtocolNo(cardTranData.getString("ProtocolNo"));
                    accountManager.setUnionpayIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManager);
                    message = Message.success("当前卡号已完成签约");
                } else {
                    //除去已签约的其他状态
                    message = Message.fail("当前卡号需签约");
                }
            } else {
                message = Message.fail("银联签约状态查询失败", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } else {
            message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
        }
        return message;
    }


    /**
     * 银联-发送签约短信
     *
     * @param id              记录的id
     * @param account         银行卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message unionPaySendSignSmsCode(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile) {

        Message message = null;

        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", account));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(certificateType)));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", certificateNo));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountName));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", mobile));

        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeSignSmsCodeUrl(), basicNameValuePairs);
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    //发送短信成功
                    JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                    //将商户订单号返回到前端页面
                    message = Message.success();
                    message.setData(result.getString("MerOrderNo"));
                } else {
                    message = Message.fail("银联签约短信发送失败");
                }
            } else {
                message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } catch (Exception e) {
            logger.error("发送银联签约短信异常,记录id:" + id + ",账户名:" + accountName + ",账户:" + account, e);
            message = Message.fail("银联签约短信发送异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }


        return message;
    }

    /**
     * 银联--签约
     *
     * @param id              记录id
     * @param account         账户卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @param smsCode         短信验证码
     * @param merOrderNo      短信验证码对应的订单号
     * @return
     */
    @Override
    public Message unionPaySign(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile, String smsCode, String merOrderNo) {
        Message message = null;
        AccountManager accountManager = accountManagerRepository.findById(id);
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", account));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(certificateType)));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", certificateNo));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountName));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", mobile));
        basicNameValuePairs.add(new BasicNameValuePair("MerOrderNo", merOrderNo));
        basicNameValuePairs.add(new BasicNameValuePair("MobileAuthCode", smsCode));
        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeSignUrl(), basicNameValuePairs);
        } catch (Exception e) {
            logger.error("银联签约异常,记录id:" + id + ",账户名:" + accountName + ",账户:" + account, e);
            message = Message.fail("银联签约异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }

        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                //获取签约状态:00-未签约,01-已签约,02-签约失败,03-已撤销
                String signState = result.getString("SignState");
                if (StringUtils.equals("01", signState)) {
                    //签约成功,将返回的协议号提取出来,保存到数据库中
                    JSONObject cardTranData = result.getJSONObject("CardTranData");
                    accountManager.setUnionpayProtocolNo(cardTranData.getString("ProtocolNo"));
                    accountManager.setUnionpayIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManager);
                    message = Message.success("银联签约成功");
                } else {
                    //除去签约成功的其他状态
                    message = Message.fail("银联签约失败");
                }
            } else {
                message = Message.fail("银联签约失败");
            }
        } else {
            message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
        }


        return message;
    }

    /**
     * 宝付-查询是否签约
     *
     * @param id 记录的id
     * @return
     */
    @Override
    public Message baoFuIsSigned(Integer id) {
        AccountManager accountManager = accountManagerRepository.findById(id);
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("accNo", accountManager.getAccount()));

        Message message;
        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignStatusUrl(), basicNameValuePairs);
        } catch (Exception e) {
            logger.error("宝付签约状态查询异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("宝付签约状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }
        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //绑定成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                String protocols = result.getString("protocols");
                //签约协议号|用户ID|银行卡号|银行编码|银行名称
                String[] accInfo = protocols.split("\\|");
                accountManager.setBaofuProtocolNo(accInfo[0]);
                accountManager.setBaofuIsSigned(1);
                accountManagerRepository.saveAndFlush(accountManager);
//                message = Message.success("当前卡号已完成签约");
                // TODO: 2018/5/25 删除下一行代码,打开上一行注释
                message = Message.fail("当前卡号需签约");
            } else {
                if (StringUtils.equals(jsonObject.getString("error_code"), BFErrorCodeEnum.BF00134.getCode())) {
                    message = Message.fail("当前卡号需签约");
                } else {
                    message = Message.fail("宝付签约状态查询失败", ConstantsEnum.NO_DATA_RESPONSE.getCode());
                }
            }
        } else {
            message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
        }
        return message;

    }

    /**
     * 宝付--发送签约短信
     *
     * @param id              记录的id
     * @param account         银行卡号
     * @param certificateType 证件类型
     * @param certificateNo   证件号码
     * @param accountName     账户名
     * @param mobile          手机号
     * @return
     */
    @Override
    public Message baoFuSendSignSmsCode(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile) {
        Message message = null;
        //账户信息:银行卡号|持卡人姓名|证件号|手机号|银行卡安全码|银行卡有效期   银行卡安全码和银行卡有效期贷记卡才有,借记卡没有,留空
        StringBuilder accInfo = new StringBuilder();
        accInfo.append(account).append("|").append(accountName).append("|").append(certificateNo).append("|").append(mobile).append("||");
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        //卡类型:101	借记卡，102 信用卡
        basicNameValuePairs.add(new BasicNameValuePair("cardType", "101"));
        //证件类型:01 身份证
        basicNameValuePairs.add(new BasicNameValuePair("idCardType", ChinaPayCertTypeEnum.getCodeByDesc(certificateType)));
        basicNameValuePairs.add(new BasicNameValuePair("accInfo", accInfo.toString()));

        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignSmsCodeUrl(), basicNameValuePairs);
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    //发送短信成功
                    JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                    String bizRespCode = result.getString("biz_resp_code");
                    if (StringUtils.equals(bizRespCode, "0000")) {
                        //将商户订单号返回到前端页面
                        message = Message.success();
                        message.setData(result.getString("unique_code"));
                    } else {
                        message = Message.fail("宝付签约短信发送失败,失败原因:" + BFErrorCodeEnum.getValueByCode(bizRespCode));
                    }

                } else {
                    message = Message.fail("宝付签约短信发送失败,宝付返回原因:" + jsonObject.getString("reason"));
                }
            } else {
                message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } catch (Exception e) {
            logger.error("发送宝付签约短信异常,记录id:" + id + ",账户名:" + accountName + ",账户:" + account, e);
            message = Message.fail("宝付签约短信发送异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }


        return message;
    }

    /**
     * 宝付-签约
     *
     * @param id         记录id
     * @param smsCode    短信验证码
     * @param uniqueCode 预签约唯一码
     * @return
     */
    @Override
    public Message baoFuSign(Integer id, String smsCode, String uniqueCode) {
        Message message = null;
        AccountManager accountManager = accountManagerRepository.findById(id);
        //格式：预签约唯一码|短信验证码
        uniqueCode = uniqueCode + "|" + smsCode;
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("uniqueCode", uniqueCode));
        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignUrl(), basicNameValuePairs);
        } catch (Exception e) {
            logger.error("宝付签约异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("宝付签约异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }

        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                String bizRespCode = result.getString("biz_resp_code");
                if (StringUtils.equals(bizRespCode, BFErrorCodeEnum.BF00000.getCode())) {
                    //签约成功
                    accountManager.setBaofuProtocolNo(result.getString("protocol_no"));
                    accountManager.setBaofuIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManager);
                    message = Message.success("宝付签约成功");
                } else {
                    //签约失败
                    message = Message.fail("宝付签约失败");
                }
            } else {
                message = Message.fail("宝付签约失败");
            }
        } else {
            message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
        }

        return message;
    }

}

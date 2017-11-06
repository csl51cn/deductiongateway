package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017-7-22.
 */
public class PayGetPgController {
    private static final Logger log = LoggerFactory.getLogger(PayGetPgController.class);

    @RequestMapping("/PayGetPgAsyn")
    public void UpdateDeduction(HttpServletRequest req) {
        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
        }

        GoPayBean bean = this.getGoPayBean(req);

        // 打印出接口发送过来的信息
        log.info("=========== 打印商户收到的前台响应信息  start=================");
        log.info(bean.getMerId());
        log.info(bean.getBusiId());
        log.info(bean.getOrdId());
        log.info(bean.getOrdAmt());
        log.info(bean.getCuryId());
        log.info(bean.getVersion());
        log.info(bean.getGateId());
        log.info(bean.getParam1());
        log.info(bean.getParam2());
        log.info(bean.getParam3());
        log.info(bean.getParam4());
        log.info(bean.getParam5());
        log.info(bean.getParam6());
        log.info(bean.getParam7());
        log.info(bean.getParam8());
        log.info(bean.getParam9());
        log.info(bean.getParam10());
        log.info(bean.getOrdDesc());
        log.info(bean.getShareType());
        log.info(bean.getShareData());
        log.info(bean.getPriv1());
        log.info(bean.getCustomIp());
        log.info(bean.getPayStat());
        log.info(bean.getPayTime());
        log.info(bean.getChkValue());
        log.info("=========== 打印商户收到的前台响应信息  end =================");
    }

    public GoPayBean getGoPayBean(HttpServletRequest req) {
        GoPayBean goPayBean = new GoPayBean();
        goPayBean.setMerId(req.getParameter("MerId"));
        goPayBean.setBusiId(req.getParameter("BusiId"));
        goPayBean.setOrdId(req.getParameter("OrdId"));
        goPayBean.setOrdAmt(req.getParameter("OrdAmt"));
        goPayBean.setCuryId(req.getParameter("CuryId"));
        goPayBean.setVersion(req.getParameter("Version"));
        goPayBean.setGateId(req.getParameter("GateId"));
        goPayBean.setParam1(req.getParameter("Param1"));
        goPayBean.setParam2(req.getParameter("Param2"));
        goPayBean.setParam3(req.getParameter("Param3"));
        goPayBean.setParam4(req.getParameter("Param4"));
        goPayBean.setParam5(req.getParameter("Param5"));
        goPayBean.setParam6(req.getParameter("Param6"));
        goPayBean.setParam7(req.getParameter("Param7"));
        goPayBean.setParam8(req.getParameter("Param8"));
        goPayBean.setParam9(req.getParameter("Param9"));
        goPayBean.setParam10(req.getParameter("Param10"));
        goPayBean.setOrdDesc(req.getParameter("OrdDesc"));
        goPayBean.setShareType(req.getParameter("ShareType"));
        goPayBean.setShareData(req.getParameter("ShareData"));
        goPayBean.setPriv1(req.getParameter("Priv1"));
        goPayBean.setCustomIp(req.getParameter("CustomIp"));
        goPayBean.setPayStat(req.getParameter("PayStat"));
        goPayBean.setPayTime(req.getParameter("PayTime"));
        goPayBean.setChkValue(req.getParameter("ChkValue"));
        return goPayBean;
    }
}

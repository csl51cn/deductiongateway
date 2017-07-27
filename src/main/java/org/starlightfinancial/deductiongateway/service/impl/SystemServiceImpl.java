package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.dao.SysAutoNumDao;
import org.starlightfinancial.deductiongateway.dao.SysUserDao;
import org.starlightfinancial.deductiongateway.model.SysAutoNum;
import org.starlightfinancial.deductiongateway.model.SysLoginHistory;
import org.starlightfinancial.deductiongateway.model.SysUser;
import org.starlightfinancial.deductiongateway.service.SystemService;
import org.starlightfinancial.deductiongateway.utility.EncryptHelper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SysAutoNumDao sysAutoNumDao;
    @Autowired
    private SysUserDao sysUserDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SysAutoNum loadSysAutoNum(int orgId, String type) {
        List<SysAutoNum> list = sysAutoNumDao.query(" and t.sysOrg=" + orgId
                + " and t.type = '" + type + "'");

        if (list != null && list.size() > 0)// 修改
        {
            return list.get(0);
        }
        return null;
    }

    public List<SysUser> querySysUsers(String condition) {
        return sysUserDao.query("and deleteFlag = '0' " + condition);
    }


    public SysUser findSysUser(String loginName, String password) {
        String de_password = EncryptHelper.Instance.getEncString(password);
        if (StringUtils.isEmpty(de_password))
            return null;
        de_password = de_password.trim();
        List<SysUser> listSysUser = this.querySysUsers(" and loginName='"
                + loginName + "' and loginPassword ='" + de_password + "'");
        if (listSysUser.size() > 0)
            return listSysUser.get(0);
//        else {
//            SysUserStaffinfo userstaff = this.loadSysUserStaffinfoByStaffNo(
//                    loginName, DictionaryType.STAFF_IS_RESIGN_01);
//            if (userstaff != null && userstaff.getId() > 0) {
//                List<SysUser> users = this
//                        .querySysUsers(" and t.sysUserStaffinfo.id="
//                                + userstaff.getId() + " and loginPassword ='"
//                                + de_password + "'");
//                if (users.size() > 0) {
//                    return users.get(0);
//                }
//            }
//        }
        return null;
    }

    public SysLoginHistory createSysLoginHistory(int user_id, String loginIP, String remark) {
//        if (user_id <= 0 || StringUtils.isEmpty(remark))
//            return null;
//        SysUser su = this.loadSysUser(user_id);
//        if (su == null || su.getId() <= 0)
//            return null;
//        String ip = loginIP;
//        if (StringUtils.isEmpty(loginIP)) {
//            ip = su.getSysUserStaffinfo().getHostIp();
//        }
//
//        SysLoginHistory slh = new SysLoginHistory();
//        slh.setSysUser(su);
//        if (StringUtils.equals("退出", remark)) {
//            Date d1 = new Date();
//            slh.setLoginDate(new Date(d1.getTime() - 1000));
//        } else {
//            slh.setLoginDate(new Date());
//        }
//        slh.setLoginIP(ip);
//        slh.setRemark(remark);
//
//        return sysLoginHistoryDao.save(slh);
        return  null;
    }

    public List<Map> executeSQL(String condition) {
        return null;
    }

    public <T> T queryForObject(String condition, Class<T> requiredType) {
        return null;
    }

    public <T3> List<T3> queryForList(String sql, Class<T3> requiredType) {
        return null;
    }

    public Map queryForMap(String sql) {
        return null;
    }

    public int queryForInt(String sql) {
        return 0;
    }

    public BigDecimal queryForBigDecimal(String sql) {
        return null;
    }
}

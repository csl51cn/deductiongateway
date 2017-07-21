package org.starlightfinancial.deductiongateway.service;

import forward.chuwa.microcredit.model.*;
import forward.chuwa.microcredit.utility.HashType;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface SystemService extends BaseService {
	// 组织
	public SysOrg createSysOrg(String orgNo, String orgName,
                               String orgShortName, String contactAddress, String contactPhone,
                               BigDecimal prepayRate, BigDecimal overduePRate,
                               BigDecimal overdueRate, BigDecimal divertRate, String remark);

	public SysOrg updateSysOrg(int id, String orgNo, String orgName,
                               String orgShortName, String contactAddress, String contactPhone,
                               BigDecimal prepayRate, BigDecimal overduePRate,
                               BigDecimal overdueRate, BigDecimal divertRate, String remark);

	public SysOrg saveOrUpdateSysOrg(SysOrg sysOrg);

	public void deleteSysOrg(int id);

	public SysOrg loadSysOrg(int id);

	public List<SysOrg> querySysOrgs(String condition);

	public List<SysOrg> listSysOrg(String condition, int start, int length);

	public int countSysOrg(String condition);

	// 部门
	public SysDepartment createSysDepartment(int parentId, int parentOrgId,
                                             SysOrg SysOrg, String deptNo, String deptName, String remark);

	public SysDepartment updateSysDepartment(int id, int parentId,
                                             int parentOrgId, SysOrg SysOrg, String deptNo, String deptName,
                                             String remark);

	public void deleteSysDepartment(int id);

	public SysDepartment loadSysDepartment(int id);

	public List<SysDepartment> querySysDepartments(String condition);

	public List<SysDepartment> listSysDepartment(String condition, int start,
                                                 int length);

	public int countSysDepartment(String condition);

	public String getSysDepartments(int deptId);

	// 团队
	public SysUnit createSysUnit(SysOrg SysOrg, String unitNo, String unitName,
                                 String remark);

	public SysUnit updateSysUnit(int id, SysOrg SysOrg, String unitNo,
                                 String unitName, String remark);

	public void deleteSysUnit(int id);

	public SysUnit loadSysUnit(int id);

	public List<SysUnit> querySysUnits(String condition);

	public List<SysUnit> listSysUnit(String condition, int start, int length);

	public int countSysUnit(String condition);

	// 员工
	public boolean checkSysUserStaffinfoByStaffNo(String staffNo);

	public SysUserStaffinfo createSysUserStaffinfo(SysDepartment SysDepartment,
                                                   String staff_no, String name, String job, String per_post,
                                                   String sex, BigDecimal salaryBasic, String mobilePhone,
                                                   String contactPhone, String email, Date employedDate,
                                                   String isfirstJobman, String hostIp, String postLevel,
                                                   String isAdmin, String isSalesman, String area, String remark,
                                                   int orgId);

	SysUserStaffinfo updateSysUserStaffinfo(int id,
                                            SysDepartment SysDepartment, String staff_no, String name,
                                            String job, String per_post, String sex, BigDecimal salaryBasic,
                                            String mobilePhone, String contactPhone, String email,
                                            Date employedDate, String isResign, String isfirstJobman,
                                            String hostIp, String postLevel, String isAdmin, String isSalesman,
                                            String area, String remark, int orgId);

	public void deleteSysUserStaffinfo(int id);

	public SysUserStaffinfo loadSysUserStaffinfo(int id);

	public SysUserStaffinfo loadSysUserStaffinfo(String staffName,
                                                 String isResign);

	public SysUserStaffinfo loadSysUserStaffinfoByStaffNo(String staffNo,
                                                          String isResign);

	public SysUserStaffinfo loadSysUserStaffinfo(int staffId, String isResign);

	public List<SysUserStaffinfo> querySysUserStaffinfos(String condition);

	public List<SysUserStaffinfo> findRelatedStaffsByApplyMain(int applyMainId);

	public List<SysUserStaffinfo> findStaffsByIds(Collection<Integer> ids);

	public List<SysUserStaffinfo> findSysUserStaffinfoByParentDepartment(
            int departmentId, String condition);

	public SysUserStaffinfo cusToSales(int id, List<String> cusMains);

	public List<SysUserStaffinfo> listSysUserStaffinfo(String condition,
                                                       int start, int length);

	public int countSysUserStaffinfo(String condition);

	// 用户
	public SysUser createSysUser(SysUserStaffinfo SysUserStaffinfo,
                                 String loginName, String loginPassword, String userNameLow,
                                 String certNo, int photoId);

	public SysUser updateSysUser(int id, SysUserStaffinfo SysUserStaffinfo,
                                 String loginName, String loginPassword, String userNameLow,
                                 String certNo, int photoId);

	public void deleteSysUser(int id);

	public SysUser loadSysUser(int id);

	public List<SysUser> querySysUsers(String condition);

	public SysUser findSysUser(String loginName, String password);

	public SysUser userToRole(int id, List<String> roles);

	public List<SysUser> listSysUser(String condition, int start, int length);

	public int countSysUser(String condition);

	// 角色
	public SysRole createSysRole(String roleNo, String roleName, String remark);

	public SysRole updateSysRole(int id, String roleNo, String roleName,
                                 String remark);

	public void deleteSysRole(int id);

	public SysRole loadSysRole(int id);

	public List<SysRole> querySysRoles(String condition);

	public List<SysRole> listSysRole(String condition, int start, int length);

	public int countSysRole(String condition);

	// 菜单
	public SysRole roleToMenu(int id, List<String> menus);

	public SysMenu loadSysMenuById(int id);

	public List<SysMenu> loadSysMenu(int userId);

	public List<SysMenu> listSysMenus();

	public List<SysMenu> querySysMenus(String condition);

	public String countSysMenu(int T);

	// 区域
	public SysDictArea loadSysDictArea(String dic_key);

	public List<SysDictArea> listSysDictArea(String dic_type, int parient_id);

	// 字典表
	public List<SysDict> listSysDict(String dic_type, int parient_id);

	public SysDict loadSysDict(String dic_type, String dic_key);

	public List<SysDict> findSysDicts(String condition);

	public List<HashType> findBigClassDict();

	public List<SysDict> listSysDicts(String condition, int start, int length);

	public int countSysDict(String condition);

	public String getSysDictValue(String dicType, String dicKey);

	// 产品
	public SysDictProduct loadSysDictProduct(String dicKey, int isproduct);

	public SysDictProduct loadSysDictProduct(int id);

	public void deleteSysDictProduct(int id);

	public List<SysDictProduct> querySysDictProduct(int parientID, int isproduct);

	public List<SysDictProduct> findSysDictProducts(String condition);

	public SysDictProduct createSysDictProduct(int parentId, String dictKey,
                                               String dictValue, String dictDes, BigDecimal creCommission,
                                               BigDecimal creOverhead, BigDecimal mIRate, int isproduct);

	public SysDictProduct updateSysDictProduct(int id, int parentId,
                                               String dictKey, String dictValue, String dictDes,
                                               BigDecimal creCommission, BigDecimal creOverhead,
                                               BigDecimal mIRate, int isproduct);

	public SysDict createSysDict(int parentId, String dicKey, String dicType,
                                 String dicValue, String dicDesc, int dicOrder, int isedit);

	public SysDict updateSysDict(int id, int parentId, String dicKey,
                                 String dicType, String dicValue, String dicDesc, int dicOrder,
                                 int isedit);

	public SysDict loadSysDict(int id);

	public void deleteSysdict(int id);

	public List<SysDictProduct> listSysDictProduct(String condition, int start,
                                                   int length);

	public int countSysDictProduct(String condition);

	// 用户权限
	public List<SysRoleData> findRoleToDatas(String condition);

	public SysRoleData updateSysRoleData(int id, SysRole sysRole, String type,
                                         String conQuery, String conCreate, String conUpdate,
                                         String conDelete);

	public SysRoleData createSysRoleData(SysRole sysRole, String type,
                                         String conQuery, String conCreate, String conUpdate,
                                         String conDelete);

	public List<SysRoleData> listRoleToDatas(String condition, int start,
                                             int length);

	public int countSysRoleData(String condition);

	// 合同模板
	public List<CreConTemplate> listTemplate(String condition, int start,
                                             int length);

	public int countTemplate(String condition);

	public List<CreConTemplate> listTemplate(String condition);

	public CreConTemplate loadTemplate(int id);

	public CreConTemplate createTemplate(String name, String filePath,
                                         String type, int orgId, int parentId);

	public CreConTemplate updateTemplate(int id, String name, String filePath,
                                         String type, int orgId);

	public void deleteTemplate(int id);

	// 自动编号
	public SysAutoNum saveSysAutoNum(int orgId, String type, String isDate,
                                     String corpPre, String typePre, String initNo, String nowNo,
                                     int stepNo);

	public SysAutoNum loadSysAutoNum(int orgId, String type);

	public String genSysAutoNum(int orgId, String type);

	// 全局
	public SysGlobal createSysGlobal(String value, String type, String remark,
                                     int orgId);

	public List<SysGlobal> querySysGlobals(String condition);

	public void deleteSysGlobal(int id);

	// 系统公告
	public SysAnnounce createSysAnnounce(String announceContent, String isAll,
                                         String deleteFlag, int createId, Date createDate, int orgId);

	// 只修改公告内容
	public SysAnnounce updateSysAnnounce(int id, String announceContent,
                                         String isAll);

	public void deleteSysAnnounce(int id);

	public List<SysAnnounce> listAnnounces(String condition);

	public List<SysAnnounce> queryAnnounces(String condition, int start,
                                            int length);

	public int countAnnounces(String condition);

	public SysAnnounce loadSysAnnounce(int id);

	public boolean hasAuthority(int authId, String type, int operType,
                                int userId, int staffId);

	public boolean hasNoticeAuthority(int authId, int type, int operType,
                                      int userId, int staffId);

	// 登录历史记录
	public SysLoginHistory createSysLoginHistory(int user_id, String loginIP,
                                                 String remark);

	public List<SysLoginHistory> listSysLoginHistory(String condition,
                                                     int start, int length);

	public int countSysLoginHistory(String condition);

	// 信用评分

	public FicoFormula createFicoFormula(String deleteFlag, String name,
                                         String remark, String ruleName, String type);

	public FicoFormula updateFicoFormula(int id, String name, String remark,
                                         String ruleName, String type);

	public FicoFormula loadFicoFormula(int id);

	public List<FicoFormula> queryFicoFormulas(String condition);

	public void deleteFicoFormula(int id);

	// 评分指标

	public FicoItem createFicoItem(String deleteFlag, String factName,
                                   String itemSql, String name, int ficoFormulaId);

	public FicoItem updateFicoItem(int id, String factName, String itemSql,
                                   String name);

	public FicoItem loadFicoItem(int id);

	public List<FicoItem> queryFicoItems(String condition);

	public void deleteFicoItem(int id);

	public boolean changePassword(int userId, String oldPassword,
                                  String newPassword);

	// 公司银行
	public SysBank createSysBank(String bankName, String bankNo, String corpName);

	public SysBank updateSysBank(int id, String bankName, String bankNo,
                                 String corpName);

	public boolean deleteSysBank(int id);

	public SysBank loadSysBank(int id);

	public List<SysBank> querySysBank(String condition);

	public List<SysBank> listSysBank(String condition, int start, int length);

	public int countSysBank(String condition);

	// 金融办理上报[FINANCE_GETINFO]
	public List<HashType> listFinanceInfo();

	public SysRole getSysRole(int id);
	public List<SysUserStaffinfo> listStaffAssignByRoleIds(
            List<Integer> roleIds, String condition);
	
	// 系统操作日志
	public SysOperLog createOrUpdateSysOperLog(SysOperLog sysOperLog);

	public SysOperLog updateSysOperLog(SysOperLog sysOperLog);

	public boolean deleteSysOperLog(int id);

	public SysOperLog loadSysOperLog(int id);

	public List<SysOperLog> querySysOperLog(String condition);

	public List<SysOperLog> listSysOperLog(String condition, int start, int length);

	public int countSysOperLog(String condition);
	
	List<SysHoliday> findSysHoliday(String condition);
	
	void createSysHoliday(SysHoliday sysHoliday);
	
	public void deleteSysHoliday(int id);
	public void importCusInsideBlack(InputStream is, String createName, int orgId) throws Exception;
//	public void importCusInsideBlackXss(InputStream is, String createName, int orgId) throws Exception;
}

package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by senlin.deng on 2017-08-01.
 */
public interface SysUserRepository extends JpaRepository<SysUser, Integer> {
    List<SysUser> findByLoginNameAndLoginPasswordAndDeleteFlag(String loginName, String loginPassword, String deleteFlag);
}

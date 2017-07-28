package org.starlightfinancial.deductiongateway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sili.chen on 2017/7/26
 */
public interface SysDictRepository extends JpaRepository<SysDict, Integer> {
    List<SysDict> findByDicType(String dicType);
}

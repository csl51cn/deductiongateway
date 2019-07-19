package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 默认渠道
 * @date: Created in 2019/7/10 10:32
 * @Modified By:
 */
public interface DefaultChannelRepository extends JpaRepository<DefaultChannel, Integer>, JpaSpecificationExecutor<DefaultChannel> {
    /**
     * 通过银行编码查询记录
     *
     * @param bankCode 银行编码
     * @return
     */
    DefaultChannel getByBankCode(String bankCode);
}

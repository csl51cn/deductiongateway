package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by sili.chen on 2017/7/26
 */
public interface MortgageDeductionRepository extends JpaRepository<MortgageDeduction, Integer>, JpaSpecificationExecutor<MortgageDeduction> {

    /**
     * 根据订单号查询记录
     *
     * @param ordId 订单号
     * @return
     */
    MortgageDeduction findByOrdId(String ordId);

    /**
     * 根据是否发送交易和创建人查询记录
     *
     * @param type    是否发送交易  最初建表时使用的规则:type为0表示已发起过代扣，type为1时未发起过代扣
     * @param creatId 创建人
     * @return
     */
    List<MortgageDeduction> findByTypeAndCreatId(String type, int creatId);

    /**
     * 根据订单号更新银联对账状态
     *
     * @param checkState 银联对账状态
     * @param ordId      订单号
     * @return
     */
    @Modifying
    @Query("update BU_MORTGAGEDEUCTION t set t.checkState = ?1 where OrdId = ?2")
    int setCheckStateFor(String checkState, String ordId);


    /**
     * 根据订单号更新宝付分账状态
     *
     * @param ledgerState 宝付分账状态
     * @param ordId       订单号
     * @return
     */
    @Modifying
    @Query("update BU_MORTGAGEDEUCTION t set t.ledgerState = ?1 where OrdId = ?2")
    int setLedgerStateFor(String ledgerState, String ordId);


    /**
     * 根据自动入账文件发送状态,是否代扣成功,创建时间查询代扣记录
     *
     * @param isUploaded 发送状态:0-未发送,1-已发送
     * @param isSuccess  代扣状态:0-失败,1-成功,其他-暂无结果
     * @param startDate  查询startDate之后创建的记录
     * @return 返回查询到代扣记录
     */
    List<MortgageDeduction> findByIsUploadedAndIssuccessAndCreateDateAfterOrderByPayTimeDesc(String isUploaded, String isSuccess, Date startDate);


    /**
     * 根据自动入账文件发送状态,是否代扣成功,创建时间查询代扣记录
     *
     * @param isSuccess 代扣状态:0-失败,1-成功,其他-暂无结果
     * @param startDate 查询date之后扣款的记录
     * @param endDate   查询date之后创建的记录
     * @return 返回查询到代扣记录
     */
    List<MortgageDeduction> findByIssuccessAndPayTimeAfterAndPayTimeBefore(String isSuccess, Date startDate, Date endDate);


}

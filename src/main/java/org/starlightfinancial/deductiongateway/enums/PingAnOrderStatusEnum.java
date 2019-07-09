package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 平安订单状态枚举
 * @date: Created in 2019/7/8 11:55
 * @Modified By:
 */
public enum PingAnOrderStatusEnum {
    /**
     * 支付成功
     */
    STATUS00("00"),
    /**
     * 待支付
     */
    STATUS01("01"),
    /**
     * 待付款
     */
    STATUS02("02"),
    /**
     * 支付失败
     */
    STATUS03("03"),
    /**
     * 已退款
     */
    STATUS04("04"),
    /**
     * 退款失败
     */
    STATUS05("05"),
    /**
     * 交易进行中
     */
    STATUS06("06"),
    /**
     * 已关闭
     */
    STATUS08("08"),
    /**
     * 落单失败
     */
    STATUS09("09"),
    ;
    private String status;

    PingAnOrderStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }}

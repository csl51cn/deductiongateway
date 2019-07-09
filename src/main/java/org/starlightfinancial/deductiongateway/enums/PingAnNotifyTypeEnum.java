package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 平安通知类型
 * @date: Created in 2019/7/8 14:18
 * @Modified By:
 */
public enum PingAnNotifyTypeEnum {

    /**
     * 00	支付成功
     */
    TYPE00("00"),
    /**
     * 01	订单创建成功
     */
    TYPE01("01"),
    /**
     * 02	订单关闭
     */
    TYPE02("02"),
    /**
     * 03	订单创建失败
     */
    TYPE03("03"),
    /**
     * 04	支付完成
     */
    TYPE04("04"),
    /**
     * 05	代付成功
     */
    TYPE05("05"),
    /**
     * 06	代付失败退款成功
     */
    TYPE06("06"),
    /**
     * 08	代付失败退款失败
     */
    TYPE08("08"),
    /**
     * 09	支付失败
     */
    TYPE09("09"),
    /**
     * 10	签约成功
     */
    TYPE10("10"),
    /**
     * 11	签约失败
     */
    TYPE11("11");
    private String type;

    PingAnNotifyTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }}

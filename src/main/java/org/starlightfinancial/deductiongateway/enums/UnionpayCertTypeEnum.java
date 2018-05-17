package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: senlin.deng
 * @Description: 银联证件类型
 * @date: Created in 2018/5/9 15:24
 * @Modified By:
 */
public enum UnionpayCertTypeEnum {

    /**
     * 身份证
     */
    CERT_TYPE_01("01", "身份证"),

    /**
     * 军官证
     */
    CERT_TYPE_02("02", "军官证"),

    /**
     * 护照
     */
    CERT_TYPE_03("03", "护照"),

    /**
     * 回乡证
     */
    CERT_TYPE_04("04", "回乡证"),

    /**
     * 台胞证
     */
    CERT_TYPE_05("05", "台胞证"),

    /**
     * 警官证
     */
    CERT_TYPE_06("06", "警官证"),

    /**
     * 士兵证
     */
    CERT_TYPE_07("07", "士兵证"),

    /**
     * 其他证件
     */
    CERT_TYPE_99("99", "其他证件");

    private String code;
    private String certTyeDesc;


    UnionpayCertTypeEnum(String code, String certTyeDesc) {
        this.code = code;
        this.certTyeDesc = certTyeDesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCertTyeDesc() {
        return certTyeDesc;
    }

    public void setCertTyeDesc(String certTyeDesc) {
        this.certTyeDesc = certTyeDesc;
    }


    public static String getCodeByDesc(String certTyeDesc) {
        for (UnionpayCertTypeEnum unionpayCertTypeEnum : UnionpayCertTypeEnum.values()) {
            if (certTyeDesc.equals(unionpayCertTypeEnum.getCertTyeDesc())) {
                return unionpayCertTypeEnum.getCode();
            }
        }
        return null;
    }

}

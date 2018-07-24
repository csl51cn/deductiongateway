package org.starlightfinancial.deductiongateway.domain.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 业务类
 * @date: Created in 2018/7/20 14:39
 * @Modified By:
 */
public class BusinessTransaction {

    /**
     * 业务编号
     */
    private Long dateId;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 主借人
     */
    private String subject;

    /**
     * 个人共借人和担保人
     */
    private List<String> personalCoBorrowerAndGuarantor = new ArrayList<>();


    /**
     * 企业共借人和担保人
     */
    private List<String> companyCoBorrowerAndGuarantor = new ArrayList<>();


    /**
     * 服务费入账公司
     */
    private String serviceFeeChargeCompany;

    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getPersonalCoBorrowerAndGuarantor() {
        return personalCoBorrowerAndGuarantor;
    }

    public void setPersonalCoBorrowerAndGuarantor(List<String> personalCoBorrowerAndGuarantor) {
        this.personalCoBorrowerAndGuarantor = personalCoBorrowerAndGuarantor;
    }

    public List<String> getCompanyCoBorrowerAndGuarantor() {
        return companyCoBorrowerAndGuarantor;
    }

    public void setCompanyCoBorrowerAndGuarantor(List<String> companyCoBorrowerAndGuarantor) {
        this.companyCoBorrowerAndGuarantor = companyCoBorrowerAndGuarantor;
    }


    public String getServiceFeeChargeCompany() {
        return serviceFeeChargeCompany;
    }

    public void setServiceFeeChargeCompany(String serviceFeeChargeCompany) {
        this.serviceFeeChargeCompany = serviceFeeChargeCompany;
    }

    @Override
    public String toString() {
        return "BusinessTransaction{" +
                "dateId=" + dateId +
                ", contractNo='" + contractNo + '\'' +
                ", subject='" + subject + '\'' +
                ", personalCoBorrowerAndGuarantor=" + personalCoBorrowerAndGuarantor +
                ", companyCoBorrowerAndGuarantor=" + companyCoBorrowerAndGuarantor +
                ", serviceFeeChargeCompany='" + serviceFeeChargeCompany + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessTransaction that = (BusinessTransaction) o;
        return Objects.equals(dateId, that.dateId) &&
                Objects.equals(contractNo, that.contractNo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dateId, contractNo);
    }
}

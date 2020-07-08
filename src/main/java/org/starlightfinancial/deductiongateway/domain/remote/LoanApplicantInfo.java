package org.starlightfinancial.deductiongateway.domain.remote;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author: Senlin.Deng
 * @Description: 贷款申请人信息
 * @date: Created in 2020/7/6 15:27
 * @Modified By:
 */
@Entity(name = "Data_申请人_个人")
@Data
public class LoanApplicantInfo {


    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "maindate_id")
    private  Long  mainDateId;

    @Column(name = "申请人")
    private String applicant;

    @Column(name = "证件号码")
    private String identityNo;


}

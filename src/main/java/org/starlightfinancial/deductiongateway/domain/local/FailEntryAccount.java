package org.starlightfinancial.deductiongateway.domain.local;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/24 11:49
 * @Modified By:
 */
@Data
@Entity(name = "fail_entry_account_deduction")
public class FailEntryAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer  id;


    @Column(name = "bu_mortgagedeuction_id")
    private Integer  mortgageDeuctionId;

    @Column(name = "contract_no")
    private String  contractNo;

    @Column(name = "gmt_create")
    private Date gmtCreate;
}

package org.starlightfinancial.deductiongateway.domain.local;

import javax.persistence.*;

/**
 * MD5值
 */
@Entity(name = "BU_MD5_TEST")
public class MD5Value {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "md5")
    private String md5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}

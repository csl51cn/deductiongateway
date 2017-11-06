package org.starlightfinancial.deductiongateway.domain.remote;

import javax.persistence.*;

/**
 * 节假日实体类
 */
@Entity(name = "tbioanonoverduetime")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nonovertime")
    private String nonOverTime;

    @Column(name = "adddate")
    private String addDate;

    @Column(name = "loginid")
    private Integer loginId;

    @Column(name = "messages")
    private Integer messages;

    public Holiday() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNonOverTime() {
        return nonOverTime;
    }

    public void setNonOverTime(String nonOverTime) {
        this.nonOverTime = nonOverTime;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public Integer getMessages() {
        return messages;
    }

    public void setMessages(Integer messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", nonOverTime='" + nonOverTime + '\'' +
                ", addDate='" + addDate + '\'' +
                ", loginId=" + loginId +
                ", messages=" + messages +
                '}';
    }
}

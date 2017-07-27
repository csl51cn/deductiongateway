package org.starlightfinancial.deductiongateway.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the SYS_LOGIN_HISTORY database table.
 * 
 */
@Entity
@Table(name="SYS_LOGIN_HISTORY")
public class SysLoginHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="login_date")
	private Date loginDate;

	@Column(name="login_IP")
	private String loginIP;

	private String remark;

	//bi-directional many-to-one association to SysUser
    @ManyToOne
	@JoinColumn(name="user_id")
	private SysUser sysUser;

    public SysLoginHistory() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
}
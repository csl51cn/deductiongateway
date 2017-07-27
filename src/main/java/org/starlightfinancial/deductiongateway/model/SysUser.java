package org.starlightfinancial.deductiongateway.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the SYS_USER database table.
 * 
 */
@Entity
@Table(name="SYS_USER")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="cert_no")
	private String certNo;

	@Column(name="last_login_date")
	private Date lastLoginDate;

	@Column(name="login_name")
	private String loginName;

	@Column(name="login_password")
	private String loginPassword;

	@Column(name="photo_id")
	private int photoId;

	@Column(name="user_name_low")
	private String userNameLow;

	@Column(name="delete_flag")
	private String deleteFlag;


	//bi-directional many-to-one association to SysLoginHistory
	@OneToMany(mappedBy="sysUser")
	private Set<SysLoginHistory> sysLoginHistories;

    public SysUser() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCertNo() {
		return this.certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return this.loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public int getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public String getUserNameLow() {
		return this.userNameLow;
	}

	public void setUserNameLow(String userNameLow) {
		this.userNameLow = userNameLow;
	}

	public Set<SysLoginHistory> getSysLoginHistories() {
		return this.sysLoginHistories;
	}

	public void setSysLoginHistories(Set<SysLoginHistory> sysLoginHistories) {
		this.sysLoginHistories = sysLoginHistories;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
package org.starlightfinancial.deductiongateway.baofu.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: senlin.deng
 * @Description: 交易头信息
 * @date: Created in 2018/9/10 15:15
 * @Modified By:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransHead {

	/**
	 *响应码元素返回交易处理状态码
	 */
	@JsonProperty(value = "return_code")
	private String returnCode;

	/**
	 * 响应信息元素交易处理状态中文信息
	 */
	@JsonProperty(value = "return_msg")
	private String returnMsg;


	/**
	 * 交易总笔数
	 */
	@JsonProperty(value = "trans_count")
	private String transCount;

	/**
	 * 交易总金额单位：元
	 * 实际数字是（15，2）小数点占一位，整数部分占13位
	 */
	@JsonProperty(value = "trans_totalMoney")
	private String transTotalMoney;


	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getTransCount() {
		return transCount;
	}

	public void setTransCount(String transCount) {
		this.transCount = transCount;
	}

	public String getTransTotalMoney() {
		return transTotalMoney;
	}

	public void setTransTotalMoney(String transTotalMoney) {
		this.transTotalMoney = transTotalMoney;
	}

	@Override
	public String toString() {
		return "TransHead{" +
				"returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", transCount='" + transCount + '\'' +
				", transTotalMoney='" + transTotalMoney + '\'' +
				'}';
	}
}

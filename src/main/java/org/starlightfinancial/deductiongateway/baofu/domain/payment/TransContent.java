package org.starlightfinancial.deductiongateway.baofu.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * @author: senlin.deng
 * @Description: 交易信息
 * @date: Created in 2018/9/10 15:17
 * @Modified By:
 */
@JsonRootName(value = "trans_content")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransContent<T> {

	/**
	 * 交易头信息
	 */
	@JsonProperty(value ="trans_head" )
	private TransHead transHead;
	/**
	 * 交易请求数据
	 */
	@JsonProperty(value ="trans_reqDatas")
	private List<TransReqDataWrapper<T>> transReqDatas;



	public TransHead getTransHead() {
		return transHead;
	}

	public void setTransHead(TransHead transHead) {
		this.transHead = transHead;
	}

	public List<TransReqDataWrapper<T>> getTransReqDatas() {
		return transReqDatas;
	}

	public void setTransReqDatas(List<TransReqDataWrapper<T>> transReqDatas) {
		this.transReqDatas = transReqDatas;
	}

	@Override
	public String toString() {
		return "TransContent{" +
				"transHead=" + transHead +
				", transReqDatas=" + transReqDatas +
				'}';
	}
}
package com.gionee.wms.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("interface")
public class KpInterface {

	private GlobalInfo globalInfo;
	private ReturnStateInfo returnStateInfo;
	private Data Data;
	
	public GlobalInfo getGlobalInfo() {
		return globalInfo;
	}
	public void setGlobalInfo(GlobalInfo globalInfo) {
		this.globalInfo = globalInfo;
	}
	public ReturnStateInfo getReturnStateInfo() {
		return returnStateInfo;
	}
	public void setReturnStateInfo(ReturnStateInfo returnStateInfo) {
		this.returnStateInfo = returnStateInfo;
	}
	public Data getData() {
		return Data;
	}
	public void setData(Data data) {
		this.Data = data;
	}
	
	
}

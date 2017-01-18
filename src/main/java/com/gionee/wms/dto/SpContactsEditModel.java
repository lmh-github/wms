package com.gionee.wms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpContactsEditModel implements Serializable {
	private static final long serialVersionUID = -5160900989640545249L;
	private List<SpMContactsPhoneValue> phoneList = new ArrayList<SpMContactsPhoneValue>();

	public List<SpMContactsPhoneValue> getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(List<SpMContactsPhoneValue> phoneList) {
		this.phoneList = phoneList;
	}
}
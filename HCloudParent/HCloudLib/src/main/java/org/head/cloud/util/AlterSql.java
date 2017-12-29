package org.head.cloud.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlterSql implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> lists = new ArrayList<>();
	String destConnName;

	public List<String> getLists() {
		return lists;
	}

	public void addLists(List<String> lists) {
		this.lists.addAll(lists);
	}

	public String getDestConnName() {
		return destConnName;
	}

	public void setDestConnName(String destConnName) {
		this.destConnName = destConnName;
	}

}

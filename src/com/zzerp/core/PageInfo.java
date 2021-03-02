package com.zzerp.core;

import java.util.Map;

public class PageInfo<T> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8631690178921209486L;
	private int[] indices;
	private int total;
	private int pageSize;
	private int page;
	private T filter;
	private int count;
	private int groupSize = 25;
	private int currGroup;
	private int totalGroup;
	private Map<String, Object> conditionMap;

	public PageInfo(T f, int rowCount, int pageSize, int currPage, Map<String, Object> conditionMap) {
		this.count = rowCount;
		this.pageSize = pageSize;
		this.total = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

		this.page = currPage;
		this.currGroup = page % groupSize == 0 ? page / groupSize - 1 : page / groupSize;
		this.totalGroup = total % groupSize == 0 ? total / groupSize : total / groupSize + 1;

		int mod = total % groupSize;
		int len = groupSize;
		if (currGroup + 1 == totalGroup && mod > 0) {
			len = groupSize > mod ? mod : groupSize;
		}
		if (count == 0) {
			len = 0;
		}
		this.indices = new int[len];

		for (int i = 0; i < len; i++) {
			indices[i] = currGroup * groupSize + i + 1;
		}
		this.filter = f;
		this.conditionMap = conditionMap;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getTotal() {
		return total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPage() {
		return page;
	}

	public int getPre() {
		int pre = page - 1;
		if (pre < 1) {
			pre = 1;
		}
		return pre;
	}

	public int getPreGroup() {
		int preG = (currGroup - 1) * groupSize + 1;
		if (preG < 1) {
			preG = 1;
		}
		return preG;
	}

	public int getNext() {
		int next = page + 1;
		if (next > total) {
			next = total;
		}
		return next;
	}

	public int getNextGroup() {
		int next = (currGroup + 1) * groupSize + 1;
		if (next > total) {
			next = total;
		}
		return next;
	}

	public int getCount() {
		return count;
	}

	public T getFilter() {
		return filter;
	}

	public Map<String, Object> getConditionMap() {
		return conditionMap;
	}

	public int getTotalGroup() {
		return totalGroup;
	}

	public void setTotalGroup(int totalGroup) {
		this.totalGroup = totalGroup;
	}

	public void setConditionMap(Map<String, Object> conditionMap) {
		this.conditionMap = conditionMap;
	}

}

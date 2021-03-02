package com.zzerp.good;

import java.util.List;
import java.util.Map;

public interface GoodService {

	List<Good> listGood(Good g, int pageSize, int page, Map<String, Object> conditionMap);

	List<Category> listCategory(int parentCateId);

	int addGood(Good good);

	int count(Good good, Map<String, Object> conditionMap);

	List<String[]> list(int pageSize, int page, Map<String, Object> conditionMap);

	int deleteGood(int id);

	List<String[]> list(int category_id);

	Good detailGood(int id);

	int updateGood(Good g);

	int warehouse(Warehousing w);

	List<Warehousing> listWarehousing(Warehousing w, int pageSize, int page, Map<String, Object> conditionMap);

	int countWarehousing(Warehousing filter, Map<String, Object> conditionMap);

	Warehousing detailWarehousing(int id);

	List<ReserveRecord> listReserveRecord(ReserveRecord filter, int pageSize, int page, Map<String, Object> conditionMap);

	int countReserveRecord(ReserveRecord filter, Map<String, Object> conditionMap);

}

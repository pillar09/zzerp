package com.zzerp.good;

import java.util.List;
import java.util.Map;

public interface WarehousingDao {

	List<Warehousing> listWarehousing(Warehousing w, int pageSize, int page, Map<String, Object> conditionMap);

	int countWarehousing(Warehousing filter, Map<String, Object> conditionMap);

	int warehouse(Warehousing w);

	Warehousing detailWarehousing(int id);

	List<Good> listGood(int id);
}

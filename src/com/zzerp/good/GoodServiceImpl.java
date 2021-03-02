package com.zzerp.good;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodServiceImpl implements GoodService {
	
	@Resource
	private GoodDao goodDao;

	@Resource
	private WarehousingDao warehousingDao;

	@Override
	public List<Good> listGood(Good good, int pageSize, int page, Map<String, Object> conditionMap) {
		return goodDao.listGood(good, pageSize, page, conditionMap);
	}

	@Override
	public List<Category> listCategory(int parentCateId) {
		return goodDao.listCategory(parentCateId);
	}

	@Override
	public int addGood(Good good) {
		return goodDao.addGood(good);
	}

	@Override
	public int count(Good good, Map<String, Object> conditionMap) {
		return goodDao.count(good, conditionMap);
	}

	@Override
	public List<String[]> list(int pageSize, int page, Map<String, Object> conditionMap) {
		return goodDao.list(pageSize, page, conditionMap);
	}

	@Override
	public int deleteGood(int id) {
		return goodDao.deleteGood(id);
	}

	@Override
	public List<String[]> list(int category_id) {
		return goodDao.list(category_id);
	}

	@Override
	public Good detailGood(int id) {
		return goodDao.detailGood(id);
	}

	@Override
	public int updateGood(Good g) {
		return goodDao.updateGood(g);
	}

	@Transactional
	@Override
	public int warehouse(Warehousing w) {
		List<Warehousing_Good> wgs = new ArrayList<Warehousing_Good>();
		Set<Integer> goodIdSet = new  HashSet<Integer>();
		for (Warehousing_Good wg : w.getWgList()) {
			if (wg != null && wg.getGood().getId() > 0) {
				if(goodIdSet.contains(wg.getGood().getId()))
				{
					return -2;//表示商品冲突，一个列表不能放入两个相同的商品
				}
				goodIdSet.add(wg.getGood().getId());
				Good g = goodDao.detailGood(wg.getGood().getId());
				wg.setGood(g);
				wgs.add(wg);
			}
		}
		w.setWgList(wgs);		
		return warehousingDao.warehouse(w);
		
	}

	@Override
	public List<Warehousing> listWarehousing(Warehousing wsearch, int pageSize, int page, Map<String, Object> conditionMap) {
		List<Warehousing> l = warehousingDao.listWarehousing( wsearch, pageSize, page, conditionMap);
		for(Warehousing  w :l){
			List<Good> gList = warehousingDao.listGood(w.getId());
			StringBuffer buff = new StringBuffer("");
			if(gList.isEmpty()){
				continue;
			}
			buff.append(gList.get(0).getCategory().getTitle());
			if (gList.size() >= 2) {
				buff.append(",");
				buff.append(gList.get(1).getCategory().getTitle());
				int i = gList.size() - 2;
				while (i > 0) {
					buff.append(".");
					i--;
				}
			}
			w.setTitle(buff.toString());
		}
		return  l;
	}

	@Override
	public int countWarehousing(Warehousing filter, Map<String, Object> conditionMap) {
		return warehousingDao.countWarehousing(filter, conditionMap);
	}
	
	@Override
	public Warehousing detailWarehousing(int id) {
		return warehousingDao.detailWarehousing(id);
	}

	@Override
	public List<ReserveRecord> listReserveRecord(ReserveRecord filter, int pageSize, int page, Map<String, Object> conditionMap) {
		return goodDao.listReserveRecord(filter,pageSize,page,conditionMap);
	}

	@Override
	public int countReserveRecord(ReserveRecord filter, Map<String, Object> conditionMap) {
		return goodDao.countReserveRecord(filter,conditionMap);
	}
	
	
}

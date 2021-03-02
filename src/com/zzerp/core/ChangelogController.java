package com.zzerp.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/changelog")
public class ChangelogController {

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		List<Changelog> logs = new ArrayList<Changelog>();
		logs.add(new Changelog("版本未定", "待开发功能", new String[] {
				"条码入库",
				"同一个订单可以新增订单编号；或者：1周内有同样的收货人，请确认是否重复发货" }));
		
		
		logs.add(new Changelog("1.0.4", "2017年12月29日", new String[] {
				"速卖通excel新格式调整",
				"物流excel导入发货信息",
		}));
		
		
		logs.add(new Changelog("1.0.3", "2017年07月04日", new String[] {
				"手机上传订单相关图片。"
		}));
		
		logs.add(new Changelog("1.0.2", "2016年03月20日", new String[] {
				"导入备注订单金额。"
		}));
		
		logs.add(new Changelog("1.0.1", "2016年03月12日", new String[] {
				"导入增加三个字段：店铺号、销售（用户的登录名，区分大小写）、付款方式。"
		}));
		
		logs.add(new Changelog("1.0", "2016年03月05日", new String[] {
				"经过一年多稳定运行，转为正式版",
				"优化数据库查询，解决订单列表显示慢的问题"
		}));
		
		logs.add(new Changelog("1.0 α34", "2014年11月15日", new String[] {
				"发货，及申报的焦点问题，用tab和回车可以提交",
				"买家ID，电话，邮件搜索功能",
				"这里序号不会变，永远是1",
				"清空一下，他就要刷新下，稍微点快点，就要出错",
				"订单号支持模糊搜索"
		}));
		
		logs.add(new Changelog("1.0 α33", "2014年10月25日", new String[] {
				"导入，编辑，补货，任何涉及地址的地方，地址里面有分号，都会丢掉分号后面的内容"
		}));
		
		logs.add(new Changelog("1.0 α32", "2014年08月22日", new String[] {
				"申报内容：申报信息导出,增加新字段,英文品名,重量,币种"
		}));
		
		logs.add(new Changelog("1.0 α31", "2014年08月02日", new String[] {
				"申报内容：申报信息用列表形式列出，支持数量，单价，小计，总计金额放在申报金额一栏"
		}));

		logs.add(new Changelog("1.0 α30", "2014年06月30日", new String[] {
				"已删除订单：搜索功能，彻底删除功能"
		}));
		logs.add(new Changelog("1.0 α29", "2014年06月25日", new String[] {
				"订单管理：批量导入订单"
		}));
		logs.add(new Changelog("1.0 α28", "2014年04月20日", new String[] {
				"订单管理：先把发货的时候，把库存信息做一个快照保存下来",
				"商品管理：进销存记录显示（旧记录不能显示，仅仅支持系统更新后的日期）"
				
		}));
		logs.add(new Changelog("1.0 α27", "2014年03月16日", new String[] {
				"订单管理：选择当前页面订单并导出"
		}));
		logs.add(new Changelog("1.0 α26", "2013年11月25日", new String[] {
				"入库记录显示商品类目和经手人",
				"入库明细显示当时入库后的库存状态"
		}));
		logs.add(new Changelog("1.0 α25", "2013年11月05日", new String[] {
				"纠纷列表的翻页会回到“所有订单”tab",
				"ID粘贴快捷方式那里，如果显示客户在线，on line, 复制不了。。这是bug",
				"任意填写过的订单都可以存草稿，不校验哪些字段必填之后才可以存草稿 （新增的时候）" }));
		logs.add(new Changelog("1.0 α24", "2013年08月24日", new String[] {
				"买家ID快捷粘帖",
				"买家信息填写重新排序，以方便tap键" }));
		logs.add(new Changelog("1.0 α23", "2013年08月17日", new String[] {
				"对于已经填写了单号，又被修改过的订单在主界面提示（商品类目红色）",
				"最后入库的放在最上面显示",
				"入库时，同一个商品选择两次，弹出提示窗",
				"商品列表中总销量错误修正",
				"入库时，可以入负库存，用以修正库存",
				"入库时，显示实时库存信息"
		}));
		logs.add(new Changelog("1.0 α22", "2013年08月11日", new String[] {
				"最后提交的纠纷要排在最上面",
				"备货和待发货按照最后一次修改订单的时间来排序",
				"新建订单时，库存【可用】显示成了金额",
				"发货信息只有一条时，不显示“,”",
				"在商品管理页面显示销售量",
				"禁止在入库时，同一个商品选择两次"
		}));
		logs.add(new Changelog("1.0 α21", "2013年07月27日", new String[] {
				"商品管理显示当前库存和待发货库存",
				"订单详情，新增修改等页面显示当前库存和待发货库存"
		}));
		logs.add(new Changelog("1.0 α20", "2013年07月20日", new String[] {
				"新增纠纷字段"
		}));
		logs.add(new Changelog("1.0 α19", "2013年07月13日", new String[] {
				"订单管理界面，增加数字显示"
		}));
		model.put("logs", logs);
		return "changelog";
	}

}

$(function(){
	$.tools.datetimeinput.localize("cn", {
		months: 		 '1月,2月,3月,4月,5月,6月,7月,8月,9月,10月,11月,12月', 
		shortMonths: '1月,2月,3月,4月,5月,6月,7月,8月,9月,10月,11月,12月',   
		days: 		 '日,一,二,三,四,五,六', 
		shortDays: 	 '日,一,二,三,四,五,六'	  
	});
	
	$("input[type='datetime']").datetimeinput({
	  format: 'yyyy-mm-dd HH:MM',
	  speed: 'fast',
	  lang:'cn'});
	
});
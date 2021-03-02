package com.zzerp.core;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * 正则表达式工具集
 * @author 梁柱
 * @ClassName: RegX 
 * @date 2016-3-14 上午10:29:07 
 *
 */
public class RegX {
	public static final Pattern PATTERN_EMAIL = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	//注意：正则表达式在java里面反斜杠“\”要写成双反斜杠
	public static final Pattern PATTERN_PHONE =   Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
//	private static final String PATTERN_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";//更为严格
	public static final Pattern PATTERN_USERNAME = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9_\\u4e00-\\u9fa5]{1,15}$");
	public static final Pattern PATTERN_NICKNAME = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5_][a-zA-Z0-9_\\u4e00-\\u9fa5-]{1,15}$");
	public static final Pattern PATTERN_COMPANY = Pattern.compile("^[a-zA-Z<《（“\".\\u4e00-\\u9fa5][a-zA-Z0-9_>《》（）%-“”\".· ;\\u4e00-\\u9fa5]{1,31}$");
	public static final Pattern PATTERN_IDENTITY = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5][a-zA-Z0-9_\\/\\u4e00-\\u9fa5]{1,15}$");
	public static final Pattern PATTERN_PASSWORD = Pattern.compile("^[\\w]{6,18}$");
	public static final Pattern PATTERN_URL = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
	public static final Pattern PATTERN_TIME = Pattern.compile("((2[0-3])|([01]\\d)):[0-5]\\d(:[0-5]\\d)?");
	public static final Pattern PATTERN_DATETIME = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} ((2[0-3])|([01]\\d)):[0-5]\\d(:[0-5]\\d)?");
	public static final Pattern PATTERN_FEE = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
	public static final Pattern PATTERN_FLOAT = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
	public static final Pattern PATTERN_NUM = Pattern.compile("[0-9]{1,15}");
	public static final Pattern PATTERN_IDS = Pattern.compile("[0-9,]*");
	public static final Pattern PATTERN_FIELD = Pattern.compile("^[a-zA-Z0-9<《（\"“”(.γ\\u4e00-\\u9fa5][a-zA-Z0-9_>《》&\"“”（）().•、:γ%-–+/ ;\\u4e00-\\u9fa5]{1,10000}$");
	public static final Pattern PATTERN_EMOJI = Pattern.compile("([\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]){5,}", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	public static final Pattern PATTERN_FIELD_KEYWORD = Pattern.compile("^[a-zA-Z0-9<《（(.γ\\u4e00-\\u9fa5][a-zA-Z0-9_>《》&“”（）().•、:γ%-–+/ ;\\u4e00-\\u9fa5]{0,10000}$");
	public static final Pattern PATTERN_IOS_UA = Pattern.compile("\\(i[^;]+;( U;)? CPU.+Mac OS X");
	public static final Pattern PATTERN_MOBILE_UA = Pattern.compile("AppleWebKit.*Mobile.*");
	
	
	public static void main(String[]args) throws UnsupportedEncodingException{
//		String time = "08:02:59";
//		log.debug(PATTERN_TIME.matcher(time).matches());
		
		String nickname = "上海青年志愿者“大数据”平台建设";
		
		System.out.println(nickname+":"+PATTERN_FIELD.matcher(new String(nickname)).matches());
		String[] ids = new String[]{"1,34,39","lkjll,lj","","1,3,","1.45"};
//		for(String s:ids){
//			System.out.println(s+":"+PATTERN_IDS.matcher(s).matches());
//		}
		
		String fee = "";
//		String latitude = "134.0090989";
//		String latitude2 = "-122.406";
//		String undefinded = "undefided";
//		String nullString = "null";
		String identity = "记者/专家";
		System.out.println(fee+":"+PATTERN_NUM.matcher(fee).matches());
//		System.out.println(latitude+":"+PATTERN_FLOAT.matcher(latitude).matches());
//		System.out.println(latitude2+":"+PATTERN_FLOAT.matcher(latitude2).matches());
//		System.out.println(undefinded+":"+PATTERN_FLOAT.matcher(undefinded).matches());
//		System.out.println(nullString+":"+PATTERN_FLOAT.matcher(nullString).matches());
//		System.out.println(identity+":"+PATTERN_IDENTITY.matcher(identity).matches());
	}
}

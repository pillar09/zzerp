package com.zzerp.core;

public class JSPUtils {
	public static boolean equals( String l1, String l2 ) {
	    return l1.equals(l2);
	  }
	
	public static boolean startWith(Integer theNum,Integer tee){
		if((""+theNum).startsWith(""+tee)){
			return true;
		}
		return false;
	}
}

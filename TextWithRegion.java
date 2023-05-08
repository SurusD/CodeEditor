package org.surus.lang.tools;

public class TextWithRegion {
	public String text = "";
	public int start = 0;
	public int end = 0;
	
	public TextWithRegion(String arg){
		text = arg;
		start = 0;
		end = arg.length();
	}
	
	public TextWithRegion(String arg,int arg0,int arg1){
		text = arg;
		start = arg0;
		end = arg1;
	}
}
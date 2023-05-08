package org.surus.lang.tools;

import android.graphics.Color;
import java.util.regex.Pattern;

public class ColoredPattern {
	public int color;
	public Pattern pattern;
	
	public ColoredPattern(int arg,Pattern arg0){
		color = arg;
		pattern = arg0;
	}
	
	public ColoredPattern(String stringColor,String stringPattern){
		color = Color.parseColor(stringColor);
		pattern = Pattern.compile(stringPattern);
	}
	
	public ColoredPattern(int colorInt,String stringPattern){
		color = colorInt;
		pattern = Pattern.compile(stringPattern);
	}
}
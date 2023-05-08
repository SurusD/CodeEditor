package org.surus.lang.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternGroup {
	private List<ColoredPattern> patterns = new ArrayList<>();

	public PatternGroup() {

	}

	public PatternGroup(List<ColoredPattern> arg) {
		patterns = arg;
	}

	public void add(ColoredPattern coloredPattern) {
		patterns.add(coloredPattern);
	}

	public ColoredPattern get(int index) {
		return patterns.get(index);
	}

	public void remove(int index) {
		patterns.remove(index);
	}

	public List<ColoredPattern> patterns() {
		return patterns;
	}

	public static PatternGroup parse(ColoredPattern... patterns) {
		if (patterns == null) {
			return new PatternGroup();
		} else {
			PatternGroup pg = new PatternGroup();
			for (ColoredPattern p : patterns) {
				pg.add(p);
			}
			return pg;
		}
	}
}
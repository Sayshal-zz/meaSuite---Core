package com.turt2live.mea.Math;

public class StringUtil {

	public static String removeCharacters(String x, char... cs) {
		for (char c : cs)
			x = x.replaceAll("[" + String.valueOf(c) + "]", "");
		return x;
	}
}
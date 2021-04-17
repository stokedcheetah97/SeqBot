package com.seq;

import java.util.Arrays;
import java.util.List;

public class Color {

	static final String BLUE = "Blue";
	static final String RED = "Red";
	static final String GREEN = "Green";
	
	public static List<String> getColors() {
		return Arrays.asList( "", BLUE, GREEN, RED );
	}
}

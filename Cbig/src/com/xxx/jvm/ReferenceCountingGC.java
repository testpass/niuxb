package com.xxx.jvm;

import java.lang.reflect.Field;

public class ReferenceCountingGC {
	private static final int  _1MB = 1024*1024;
	public Object instance = null;
	private byte[]bigSize = new byte[2 * _1MB];		
	public static void testGC() {
		ReferenceCountingGC objA=new ReferenceCountingGC();
		ReferenceCountingGC objB=new ReferenceCountingGC();
		objA.instance=objB;
		objB.instance=objA;
		objA=null;
		objB = null;
				//假设在这行发生G
		System.gc();
	}
	public static void main(String[] args) {
		testGC();
	}
}

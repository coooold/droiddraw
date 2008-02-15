package org.droiddraw.gui;

import java.util.Hashtable;

public class WidgetRegistry {
	static Hashtable<Class<?>, WidgetPainter> painters 
		= new Hashtable<Class<?>, WidgetPainter>();
	
	public static void registerPainter(Class<?> c, WidgetPainter wp) {
		painters.put(c, wp);
	}
	
	public static WidgetPainter getPainter(Class<?> c) {
		Class<?> clazz = c;
		while (clazz != null && !clazz.equals(Object.class)) {
			if (painters.get(clazz) != null) {
				return painters.get(clazz);
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

}

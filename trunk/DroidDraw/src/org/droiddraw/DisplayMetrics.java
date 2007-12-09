package org.droiddraw;

public class DisplayMetrics {
	public static float density=1.0f;
	public static float scaledDensity=1.0f;
	public static float xdpi=160;
	public static float ydpi=160;

	public static final float MM_TO_IN = 0.0393700787f;
	public static final float PT_TO_IN = 1/72.0f;

	public static int readSize(StringProperty prop) 
	{
		return readSize(prop.getStringValue());
	}

	public static int readSize(String sz)
	{
		try {
			float size = Float.parseFloat(sz.substring(0, sz.length()-2));
			if (sz.endsWith("px")) {
				return (int)size;
			}
			else if (sz.endsWith("in")) {
				return (int)(size*xdpi);
			}
			else if (sz.endsWith("mm")) {
				return (int)(size*MM_TO_IN*xdpi);
			}
			else if (sz.endsWith("pt")) {
				return (int)(size*PT_TO_IN*xdpi);
			}
			else if (sz.endsWith("dp")) {
				return (int)(size*density);
			}
			else if (sz.endsWith("sp")) {
				return (int)(size*scaledDensity);
			}
			else {
				return -1;
			}
		} catch (NumberFormatException ex) {
			return -1;
		}
	}
}

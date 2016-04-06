package net.nat123.wpt.zxlibrary;

import android.content.Context;

public class ResourcesUtil {
	public static int getLayout(Context context,String name){
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
	
	public static int getId(Context context,String name){
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}
	
	public static int getRaw(Context context,String name){
		return context.getResources().getIdentifier(name, "raw", context.getPackageName());
	}
	
	public static int getStyle(Context context,String name){
		return context.getResources().getIdentifier(name, "style", context.getPackageName());
	}
	public static int getString(Context context,String name){
		return context.getResources().getIdentifier(name, "string", context.getPackageName());
	}
	public static int getColor(Context context,String name){
		return context.getResources().getIdentifier(name, "color", context.getPackageName());
	}
	public static int getDrawable(Context context,String name){
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
}

package jna.office.excel;

import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;

@ComInterface(iid = "{0002096B-0000-0000-C000-000000000046}")
public interface Workbook {
	
	@ComMethod
	void Save();

	@ComProperty
	String getPath();
}

package org.kylin.modules.excel;

import java.util.List;

public abstract interface ExcelInfo {
	public abstract List<String> getHeaders();

	public abstract String getContent(Object paramObject, String paramString)
			throws Exception;
}

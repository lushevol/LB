package org.kylin.modules.excel;

import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract interface ExcelService {
	public abstract InputStream getExcelInputStream(String paramString1,
			List<?> paramList, String paramString2, List<String> paramList1)
			throws Exception;

	public abstract HSSFWorkbook createExcel(String paramString1,
			List<?> paramList, String paramString2, List<String> paramList1)
			throws Exception;
}

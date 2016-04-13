package org.kylin.modules.excel;

import java.util.List;

public abstract interface CreateExcel {
	public abstract void createSheet(String paramString);

	public abstract void createRow();

	public abstract void createCell(int paramInt);

	public abstract void setCell(String paramString, int paramInt);

	public abstract CreateExcel setTitle(String paramString);

	public abstract CreateExcel setHead();

	public abstract CreateExcel setCellData(List<?> paramList) throws Exception;
}

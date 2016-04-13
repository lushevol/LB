package org.kylin.klb.service.myexcel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CreateExcel {
	public HSSFWorkbook createExcel(String title,
			LinkedHashMap<String, LinkedHashMap<String, String>> datas)
			throws Exception {
		CreateExcelData createExcel = new CreateExcelData(datas);

		createExcel.setTitle(title).setHead().setCellData();

		return createExcel.getWb();
	}

	public InputStream getExcelInputStream(String title,
			LinkedHashMap<String, LinkedHashMap<String, String>> datas)
			throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			createExcel(title, datas).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);
		return is;
	}
}

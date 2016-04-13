package org.kylin.modules.excel.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.kylin.modules.excel.ExcelService;

public class ExcelServiceImpl implements ExcelService {
	public HSSFWorkbook createExcel(String clazz, List<?> datas, String title,
			List<String> fieldsName) throws Exception {
		CreateExcelImpl createExcel = new CreateExcelImpl(clazz, fieldsName);

		createExcel.setTitle(title).setHead().setCellData(datas);

		return createExcel.getWb();
	}

	public InputStream getExcelInputStream(String clazz, List<?> datas,
			String title, List<String> fieldsName) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			createExcel(clazz, datas, title, fieldsName).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] content = os.toByteArray();

		InputStream is = new ByteArrayInputStream(content);

		return is;
	}

	public ByteArrayOutputStream getExcelOutPutStream(String clazz,
			List<?> datas, String title, List<String> fieldsName)
			throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			createExcel(clazz, datas, title, fieldsName).write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
	}
}

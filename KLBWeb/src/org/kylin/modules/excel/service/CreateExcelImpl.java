package org.kylin.modules.excel.service;

import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.kylin.modules.excel.CreateExcel;

public class CreateExcelImpl implements CreateExcel {
	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	private HSSFRow row = null;
	private HSSFCell cell = null;
	private HSSFPatriarch patriarch = null;

	private ExcelInfoImpl excelInfo = null;
	private int rowNum = 0;

	public CreateExcelImpl(String className, List<String> fieldsName) {
		this.wb = new HSSFWorkbook();
		createSheet("sheet1");
		this.excelInfo = new ExcelInfoImpl(className, fieldsName);
	}

	public void createPatriarch() {
		this.patriarch = this.sheet.createDrawingPatriarch();
	}

	public void createRow() {
		this.row = this.sheet.createRow(this.rowNum);
		this.rowNum += 1;
	}

	public void createSheet(String sheetName) {
		this.sheet = this.wb.createSheet(sheetName);
	}

	public void createCell(int cellNum) {
		this.cell = this.row.createCell((short) cellNum);
	}

	public void setCell(String data, int cellNum) {
		createCell(cellNum);

		this.cell.setCellValue(data);
	}

	public CreateExcelImpl setCellData(List<?> datas) throws Exception {
		List list = this.excelInfo.getPropertys();

		for (int j = 0; j < datas.size(); ++j) {
			Object obj = datas.get(j);
			createRow();
			for (int i = 0; i < list.size(); ++i) {
				setCell(this.excelInfo.getContent(obj, (String) list.get(i)), i);
			}
		}
		return this;
	}

	public CreateExcelImpl setCellImg(String[] srcs) throws Exception {
		for (String str : srcs)
			;
		return this;
	}

	public CreateExcelImpl setHead() {
		List datas = this.excelInfo.getPropertyNames();
		if (datas.size() == 0) {
			datas = this.excelInfo.getHeaders();
		}
		createRow();
		for (int i = 0; i < datas.size(); ++i) {
			setCell((String) datas.get(i), i);
		}
		return this;
	}

	public CreateExcelImpl setTitle(String title) {
		createRow();
		createCell(0);

		HSSFFont titlefont = this.wb.createFont();
		titlefont.setFontName("Arial");
		titlefont.setBoldweight((short) 700);
		titlefont.setFontHeight((short) 400);
		HSSFCellStyle titleStyle = this.wb.createCellStyle();
		titleStyle.setFont(titlefont);
		titleStyle.setAlignment((short) 2);
		this.cell.setCellStyle(titleStyle);

		this.cell.setCellValue(title);
		this.sheet.addMergedRegion(new Region(0, (short) 0, 1,
				(short) (this.excelInfo.getNum() - 1)));
		this.rowNum += 1;
		return this;
	}

	public HSSFWorkbook getWb() {
		return this.wb;
	}
}

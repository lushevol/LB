package org.kylin.klb.service.myexcel;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class CreateExcelData {
	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	private HSSFRow row = null;
	private HSSFCell cell = null;
	private int rowNum = 0;
	private int num = 0;
	private LinkedHashMap<String, LinkedHashMap<String, String>> datas;

	public CreateExcelData(
			LinkedHashMap<String, LinkedHashMap<String, String>> datas) {
		this.wb = new HSSFWorkbook();
		this.datas = datas;
		createSheet("sheet1");
	}

	public void createSheet(String sheetName) {
		this.sheet = this.wb.createSheet(sheetName);
	}

	public void createRow() {
		this.row = this.sheet.createRow(this.rowNum);
		this.rowNum += 1;
	}

	public void createCell(int cellNum) {
		this.cell = this.row.createCell((short) cellNum);
	}

	public void setCell(String data, int cellNum) {
		createCell(cellNum);

		this.cell.setCellValue(data);
	}

	public CreateExcelData setHead() {
		createRow();
		int count = 0;

		if ((this.datas != null) && (!this.datas.isEmpty())) {
			Iterator i = this.datas.keySet().iterator();
			if (i.hasNext()) {
				LinkedHashMap data = (LinkedHashMap) this.datas.get(i.next());
				createRow();

				Iterator it = data.keySet().iterator();
				while (it.hasNext()) {
					setCell((String) it.next(), count);
					++count;
				}
			}
		}
		return this;
	}

	public CreateExcelData setCellData() throws Exception {
		if ((this.datas != null) && (!this.datas.isEmpty())) {
			Iterator i = this.datas.keySet().iterator();
			while (i.hasNext()) {
				String s = (String) i.next();
				LinkedHashMap data = (LinkedHashMap) this.datas.get(s);
				createRow();
				int count = 0;
				Iterator it = data.keySet().iterator();
				while (it.hasNext()) {
					setCell((String) data.get(it.next()), count);
					++count;
				}
			}
		}
		return this;
	}

	public int getNum() {
		if ((this.datas != null) && (!this.datas.isEmpty())) {
			this.num = this.datas.size();
		}
		return this.num;
	}

	public CreateExcelData setTitle(String title) {
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
				(short) (getNum() - 1)));
		this.rowNum += 1;
		return this;
	}

	public HSSFWorkbook getWb() {
		return this.wb;
	}
}

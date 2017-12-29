package org.head.cloud.connection.command;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.head.cloud.connection.IExportCommand;

public class ExportCommand implements IExportCommand {

	@Override
	public String exportToExcel(Map<String, Object> params, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("temp");

		// 定义例头
		List<String> cols = (List<String>) params.get("cols");
		int colLen = cols.size();
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < colLen; i++) {
			HSSFCell cell = row.createCell(i); // 创建列头对应个数的单元格
			cell.setCellType(CellType.STRING);
			cell.setCellValue(cols.get(i));
		}

		List<List<Object>> colValues = (List<List<Object>>) params.get("values");

		for (int i = 0, j = colValues.size(); i < j; i++) {
			HSSFRow row1 = sheet.createRow(i+ 1);
			List<Object> values = colValues.get(i);
			for (int m = 0, n = values.size(); m < n; m++) {
				Object value = values.get(m);
				if (value instanceof String) {
					HSSFCell cell = row1.createCell(m, CellType.STRING);
					cell.setCellValue(values.get(m).toString());
				} else if (value instanceof Date) {
					HSSFCell cell = row1.createCell(m);
					cell.setCellValue((Date) values.get(m));
					HSSFCellStyle cellStyle = workBook.createCellStyle();
					HSSFDataFormat format = workBook.createDataFormat();
					cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
					cell.setCellStyle(cellStyle);
				} else if (value instanceof Integer) {
					HSSFCell cell = row1.createCell(m, CellType.NUMERIC);
					cell.setCellValue(Integer.valueOf(value.toString()));
				} else if (value instanceof BigDecimal) {
					HSSFCell cell = row1.createCell(m, CellType.NUMERIC);
					cell.setCellValue(Integer.valueOf(value.toString()));
					HSSFCellStyle cellStyle = workBook.createCellStyle();
					cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					cell.setCellStyle(cellStyle);
				} else {
					HSSFCell cell = row1.createCell(m, CellType.STRING);
					if (value != null)
						cell.setCellValue(value.toString());
					else
						cell.setCellValue("");
				}

			}
		}

		String fileName = System.currentTimeMillis() + ".xls", path = System.getProperty("java.io.tmpdir");
		String filePath = path + fileName;
		File file = new File(filePath);
		try {
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			workBook.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;

	}

}

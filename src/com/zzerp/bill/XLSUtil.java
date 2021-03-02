package com.zzerp.bill;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class XLSUtil {
	
	private Workbook work;
	
	/**
	 * 得到Workbook对象
	 * @param filePath 文件路径
	 * @throws BiffException 
	 * @throws IOException 
	 */
	public XLSUtil(String filePath) throws BiffException, IOException {
		try {
			InputStream is = new FileInputStream(filePath);
			this.work = Workbook.getWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (BiffException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	/**
	 * 得到Sheet，像数组一样，下标从0开始
	 * @param number Sheet数
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws BiffException
	 * @throws IOException
	 */
	public Sheet getSheet(int number) {
		try {
			return work.getSheet(number);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 得到总行数
	 */
	public int getRows(Sheet sheet) {
		return sheet.getRows();
	}

	/**
	 * 得到总列数
	 */
	public int getCells(Sheet sheet) {
		return sheet.getColumns();
	}

	/**
	 * 得到sheet表格中每一个单元格的具体数据
	 * @param sheet 
	 * @param cell 列
	 * @param row 行
	 * @return 
	 */
	public String getDate(Sheet sheet, int cell, int row) {
		return sheet.getCell(cell, cell).getContents();
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "D:\\Downloads\\20140601_20140605_33.xls";
		XLSUtil xls = new XLSUtil(filePath);
		Sheet sheet = xls.getSheet(0);
		int rows = xls.getRows(sheet);
		int cell = xls.getCells(sheet);
		System.out.println(xls.getRows(sheet));
		System.out.println(xls.getCells(sheet));
		for (int i = 2; i < rows; i++) {
			System.out.println("------------" + i);
			for (int j = 0; j < cell; j++) {
				String string = sheet.getCell(j, i).getContents();
				System.out.print(string);
				System.out.print("	");
			}
		}
	}
	
}

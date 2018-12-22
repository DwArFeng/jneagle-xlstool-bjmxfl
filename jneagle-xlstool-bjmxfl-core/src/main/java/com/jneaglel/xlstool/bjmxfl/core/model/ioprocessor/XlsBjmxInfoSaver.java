package com.jneaglel.xlstool.bjmxfl.core.model.ioprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.dwarfeng.dutil.basic.cna.AttributeComplex;
import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.basic.io.StreamSaver;
import com.jneaglel.xlstool.bjmxfl.core.util.Constants;

public class XlsBjmxInfoSaver extends StreamSaver<Collection<AttributeComplex>> {

	private final InputStream templateInputstream;
	private final int dataSheetIndex;
	private final int firstDataRow;
	private final int index_xmh;
	private final int index_bjh;
	private final int index_ljh;
	private final int index_ljmc;
	private final int index_cz;
	private final int index_dx;
	private final int index_dz;
	private final int index_zz;
	private final int index_ts;

	private boolean saveFlag = false;

	public XlsBjmxInfoSaver(OutputStream out, InputStream templateInputstream, int dataSheetIndex, int firstDataRow,
			int index_xmh, int index_bjh, int index_ljh, int index_ljmc, int index_cz, int index_dx, int index_dz,
			int index_zz, int index_ts) {
		super(out);
		this.templateInputstream = templateInputstream;
		this.dataSheetIndex = dataSheetIndex;
		this.firstDataRow = firstDataRow;
		this.index_xmh = index_xmh;
		this.index_bjh = index_bjh;
		this.index_ljh = index_ljh;
		this.index_ljmc = index_ljmc;
		this.index_cz = index_cz;
		this.index_dx = index_dx;
		this.index_dz = index_dz;
		this.index_zz = index_zz;
		this.index_ts = index_ts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Collection<AttributeComplex> collection) throws SaveFailedException, IllegalStateException {
		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		if (saveFlag)
			throw new IllegalStateException("Save method can be called only once");

		saveFlag = true;

		try (Workbook workbook = loadTemplate()) {
			Sheet sheet = workbook.getSheetAt(dataSheetIndex);

			int currentDataIndex = 0;
			for (AttributeComplex attributeComplex : collection) {
				saveRow(sheet, currentDataIndex++, attributeComplex);
			}

			workbook.write(out);

		} catch (Exception e) {
			throw new SaveFailedException("写入XLS数据表的时候发生异常", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SaveFailedException> countinuousSave(Collection<AttributeComplex> collection)
			throws IllegalStateException {
		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		if (saveFlag)
			throw new IllegalStateException("Save method can be called only once");

		final Set<SaveFailedException> exceptions = new LinkedHashSet<>();

		try (Workbook workbook = loadTemplate()) {
			Sheet sheet = workbook.getSheetAt(dataSheetIndex);

			int currentDataIndex = 0;
			for (AttributeComplex attributeComplex : collection) {
				try {
					saveRow(sheet, currentDataIndex++, attributeComplex);
				} catch (Exception e) {
					exceptions.add(new SaveFailedException("写入XLS数据表的时候发生异常", e));
				}
			}

			workbook.write(out);

		} catch (Exception e) {
			exceptions.add(new SaveFailedException("写入XLS数据表的时候发生异常", e));
		}

		return exceptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		super.close();
		templateInputstream.close();
	}

	private Workbook loadTemplate() throws IOException {
		return new HSSFWorkbook(templateInputstream);
	}

	private void saveRow(Sheet sheet, int currentDataIndex, AttributeComplex attributeComplex) throws Exception {
		Row row = sheet.getRow(currentDataIndex + firstDataRow);

		Cell cell_xmh = row.getCell(index_xmh);
		Cell cell_bjh = row.getCell(index_bjh);
		Cell cell_ljh = row.getCell(index_ljh);
		Cell cell_ljmc = row.getCell(index_ljmc);
		Cell cell_cz = row.getCell(index_cz);
		Cell cell_dx = row.getCell(index_dx);
		Cell cell_dz = row.getCell(index_dz);
		Cell cell_zz = row.getCell(index_zz);
		Cell cell_ts = row.getCell(index_ts);

		String string_xmh = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_XMH, String.class);
		String string_bjh = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_BJH, String.class);
		String string_ljh = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_LJH, String.class);
		String string_ljmc = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_LJMC, String.class);
		String string_cz = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_CZ, String.class);
		double double_dx = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_DX, Double.class);
		double double_dz = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_DZ, Double.class);
		double double_zz = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_ZZ, Double.class);
		double double_ts = attributeComplex.get(Constants.ATTRIBUTE_COMPLEX_MARK_TS, Double.class);

		cell_xmh.setCellValue(string_xmh);
		cell_bjh.setCellValue(string_bjh);
		cell_ljh.setCellValue(string_ljh);
		cell_ljmc.setCellValue(string_ljmc);
		cell_cz.setCellValue(string_cz);
		cell_dx.setCellValue(double_dx);
		cell_dz.setCellValue(double_dz);
		cell_zz.setCellValue(double_zz);
		cell_ts.setCellValue(double_ts);
	}

}

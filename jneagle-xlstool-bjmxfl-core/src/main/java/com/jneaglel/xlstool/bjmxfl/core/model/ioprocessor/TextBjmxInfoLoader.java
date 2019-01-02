package com.jneaglel.xlstool.bjmxfl.core.model.ioprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import com.dwarfeng.dutil.basic.cna.AttributeComplex;
import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.dutil.basic.io.StringOutputStream;
import com.dwarfeng.dutil.basic.str.StringUtil;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.DataType;
import com.jneaglel.xlstool.bjmxfl.core.util.Constants;

public class TextBjmxInfoLoader extends StreamLoader<Collection<AttributeComplex>> {

	private final String dataSectionDelimiter;
	private final String fileEncode;
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

	private boolean readFlag = false;

	public TextBjmxInfoLoader(InputStream in, String dataSectionDelimiter, String fileEncode, int firstDataRow,
			int index_xmh, int index_bjh, int index_ljh, int index_ljmc, int index_cz, int index_dx, int index_dz,
			int index_zz, int index_ts) {
		super(in);
		this.dataSectionDelimiter = dataSectionDelimiter;
		this.fileEncode = fileEncode;
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
	public void load(Collection<AttributeComplex> collection) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("Load method can be called only once");

		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		readFlag = true;

		try {
			List<String> dataTextList = loadDataText();

			for (String dataText : dataTextList) {
				loadData(dataText, collection);
			}

		} catch (Exception e) {
			throw new LoadFailedException("读取文本文件时发生异常", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(Collection<AttributeComplex> collection)
			throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("Load method can be called only once");

		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		readFlag = true;

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();

		try {
			List<String> dataTextList = loadDataText();

			for (String dataText : dataTextList) {
				try {
					loadData(dataText, collection);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("读取文本文件时发生异常", e));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("读取文本文件时发生异常", e));
		}

		return exceptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		super.close();
	}

	private List<String> loadDataText() throws Exception {
		String dataText;

		StringOutputStream sout = null;
		try {
			sout = new StringOutputStream(Charset.forName(fileEncode));
			IOUtil.trans(in, sout, 8192);
			sout.flush();
		} finally {
			if (Objects.nonNull(sout))
				sout.close();
		}
		dataText = sout.toString();

		dataText = dataText.replaceAll("\\r", "");
		String[] dataTextArray = dataText.split("\\n");

		if (dataTextArray.length <= firstDataRow) {
			return Collections.emptyList();
		} else {
			List<String> list = new ArrayList<>();
			for (int i = firstDataRow; i < dataTextArray.length; i++) {
				if (!dataTextArray[i].isEmpty())
					list.add(dataTextArray[i]);
			}
			return list;
		}
	}

	private void loadData(String dataText, Collection<AttributeComplex> collection) {
		String[] dataSectionArray = dataText.split(dataSectionDelimiter);

		String xmhString = Optional.ofNullable(getSection(dataSectionArray, index_xmh))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String bjhString = Optional.ofNullable(getSection(dataSectionArray, index_bjh))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String ljhString = Optional.ofNullable(getSection(dataSectionArray, index_ljh))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String ljmcString = Optional.ofNullable(getSection(dataSectionArray, index_ljmc))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String czString = Optional.ofNullable(getSection(dataSectionArray, index_cz))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String dxString = Optional.ofNullable(getSection(dataSectionArray, index_dx))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String dzString = Optional.ofNullable(getSection(dataSectionArray, index_dz))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String zzString = Optional.ofNullable(getSection(dataSectionArray, index_zz))
				.orElseThrow(new LossingPropertyExceptionSuppiler());
		String tsString = Optional.ofNullable(getSection(dataSectionArray, index_ts))
				.orElseThrow(new LossingPropertyExceptionSuppiler());

		Object type_dx;
		Object data_dx;
		{
			AttributeComplex ac = string2AttributeComplex(dxString);
			type_dx = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_TYPE);
			data_dx = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_DATA);
		}

		Object type_dz;
		Object data_dz;
		{
			AttributeComplex ac = string2AttributeComplex(dzString);
			type_dz = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_TYPE);
			data_dz = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_DATA);
		}

		Object type_zz;
		Object data_zz;
		{
			AttributeComplex ac = string2AttributeComplex(zzString);
			type_zz = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_TYPE);
			data_zz = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_DATA);
		}

		Object type_ts;
		Object data_ts;
		{
			AttributeComplex ac = string2AttributeComplex(tsString);
			type_ts = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_TYPE);
			data_ts = ac.get(Constants.ATTRIBUTE_COMPLEX_MARK_DATA);
		}

		AttributeComplex ac = AttributeComplex.newInstance(new Object[] { //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_XMH, xmhString, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_BJH, bjhString, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_LJH, ljhString, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_LJMC, ljmcString, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_CZ, czString, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_DX, data_dx, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_DZ, data_dz, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_ZZ, data_zz, //
				Constants.ATTRIBUTE_COMPLEX_MARK_DATA_TS, data_ts, //
				Constants.ATTRIBUTE_COMPLEX_MARK_TYPE_DX, type_dx, //
				Constants.ATTRIBUTE_COMPLEX_MARK_TYPE_DZ, type_dz, //
				Constants.ATTRIBUTE_COMPLEX_MARK_TYPE_ZZ, type_zz, //
				Constants.ATTRIBUTE_COMPLEX_MARK_TYPE_TS, type_ts,//
		});

		collection.add(ac);
	}

	private String getSection(String[] dataSectionArray, int index) {
		return dataSectionArray.length <= index ? null : dataSectionArray[index];
	}

	private AttributeComplex string2AttributeComplex(String string) {
		if (StringUtil.isNumeric(string)) {
			return AttributeComplex.newInstance(new Object[] { //
					Constants.ATTRIBUTE_COMPLEX_MARK_TYPE, DataType.NUMERIC, //
					Constants.ATTRIBUTE_COMPLEX_MARK_DATA, Double.parseDouble(string),//
			});
		} else {
			return AttributeComplex.newInstance(new Object[] { //
					Constants.ATTRIBUTE_COMPLEX_MARK_TYPE, DataType.STRING, //
					Constants.ATTRIBUTE_COMPLEX_MARK_DATA, string,//
			});
		}
	}

	private class LossingPropertyExceptionSuppiler implements Supplier<IllegalArgumentException> {

		public LossingPropertyExceptionSuppiler() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IllegalArgumentException get() {
			return new IllegalArgumentException("属性缺失");
		}

	}

}

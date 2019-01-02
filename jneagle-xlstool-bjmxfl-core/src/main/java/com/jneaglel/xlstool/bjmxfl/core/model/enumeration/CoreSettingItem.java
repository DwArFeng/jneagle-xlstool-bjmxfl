package com.jneaglel.xlstool.bjmxfl.core.model.enumeration;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.dwarfeng.dutil.develop.setting.AbstractSettingInfo;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.LocaleSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.StringSettingInfo;

public enum CoreSettingItem implements SettingEnumItem {

	/** 国际化地区。 */
	I18N_LOCALE("i18n.locale", new LocaleSettingInfo("zh_CN")),

	/** 是否删除成功导出的原始文件。 */
	POLICY_DELETE_SUCCESSFUL_EXPORTED_SRC_FILE("policy.delete-successful-exported-src-file",
			new BooleanSettingInfo("true")),

	/** 源文件的第一行数据行，从0开始计数。 */
	SRCTEXT_INDEX_ROW_FIRST_DATA("srctext.index.row.first-data", new PositiveIntegerSettingInfo("1")),
	/** 源文件的数据区域分隔符。 */
	SRCTEXT_DATASECTION_DELIMITER("srctext.data-section.delimiter", new StringSettingInfo("\\t")),
	/** 源文件的文本编码。 */
	SRCTEXT_ENCODE("srctext.encode", new StringSettingInfo("GBK")),

	/** 源文件的项目号所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_XMH("srctext.data-section.column.xmh", new PositiveIntegerSettingInfo("0")),
	/** 源文件部件号所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_BJH("srctext.data-section.column.bjh", new PositiveIntegerSettingInfo("1")),
	/** 源文件的零件号所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_LJH("srctext.data-section.column.ljh", new PositiveIntegerSettingInfo("2")),
	/** 源文件的零件名称所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_LJMC("srctext.data-section.column.ljmc", new PositiveIntegerSettingInfo("3")),
	/** 源文件的材质所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_CZ("srctext.data-section.column.cz", new PositiveIntegerSettingInfo("4")),
	/** 源文件的单需所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_DX("srctext.data-section.column.dx", new PositiveIntegerSettingInfo("5")),
	/** 源文件的单重所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_DZ("srctext.data-section.column.dz", new PositiveIntegerSettingInfo("6")),
	/** 源文件的总重所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_ZZ("srctext.data-section.column.zz", new PositiveIntegerSettingInfo("7")),
	/** 源文件的台数所在的数据列，从0开始计数。 */
	SRCTEXT_INDEX_COLUMN_TS("srctext.data-section.column.ts", new PositiveIntegerSettingInfo("8")),

	/** 数据导出的目标表单序号，从0开始计数。 */
	EXPTABLE_INDEX_TARGET_SHEET("exptable.index.row.target-sheet", new PositiveIntegerSettingInfo("0")),
	/** 导出文件的第一行数据行，从0开始计数。 */
	EXPTABLE_INDEX_ROW_FIRST_DATA("exptable.index.row.first-data", new PositiveIntegerSettingInfo("1")),

	/** 源文件的项目号所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_XMH("exptable.data-section.column.xmh", new PositiveIntegerSettingInfo("0")),
	/** 源文件部件号所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_BJH("exptable.data-section.column.bjh", new PositiveIntegerSettingInfo("1")),
	/** 源文件的零件号所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_LJH("exptable.data-section.column.ljh", new PositiveIntegerSettingInfo("2")),
	/** 源文件的零件名称所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_LJMC("exptable.data-section.column.ljmc", new PositiveIntegerSettingInfo("3")),
	/** 源文件的材质所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_CZ("exptable.data-section.column.cz", new PositiveIntegerSettingInfo("4")),
	/** 源文件的单需所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_DX("exptable.data-section.column.dx", new PositiveIntegerSettingInfo("5")),
	/** 源文件的单重所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_DZ("exptable.data-section.column.dz", new PositiveIntegerSettingInfo("6")),
	/** 源文件的总重所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_ZZ("exptable.data-section.column.zz", new PositiveIntegerSettingInfo("7")),
	/** 源文件的台数所在的数据列，从0开始计数。 */
	EXPTABLE_INDEX_COLUMN_TS("exptable.data-section.column.ts", new PositiveIntegerSettingInfo("8")),

	;

	private static final class PositiveIntegerSettingInfo extends AbstractSettingInfo implements SettingInfo {

		private static final int RADIX = 10;
		private String lastCheckedValue = null;
		private Integer lastParsedValue = null;
		private final Lock lock = new ReentrantLock();

		public PositiveIntegerSettingInfo(String defaultValue) throws NullPointerException, IllegalArgumentException {
			super(defaultValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return PositiveIntegerSettingInfo.class.hashCode() * 61 + defaultValue.hashCode() * 17;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (Objects.isNull(obj))
				return false;
			if (!(obj.getClass() == PositiveIntegerSettingInfo.class))
				return false;

			PositiveIntegerSettingInfo that = (PositiveIntegerSettingInfo) obj;
			return Objects.equals(this.defaultValue, that.defaultValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "PositiveIntegerSettingInfo [defaultValue=" + defaultValue + "]";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected boolean isNonNullValid(String value) {
			lock.lock();
			try {
				if (Objects.equals(value, lastCheckedValue))
					return Objects.nonNull(lastParsedValue);

				try {
					lastCheckedValue = value;
					lastParsedValue = Integer.parseInt(value, RADIX);
					if (lastParsedValue < 0) {
						lastParsedValue = null;
						return false;
					}
				} catch (Exception e) {
					lastParsedValue = null;
					return false;
				}
				return true;
			} finally {
				lock.unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Object parseValidValue(String value) {
			lock.lock();
			try {
				if (Objects.equals(value, lastCheckedValue))
					return lastParsedValue;

				try {
					lastCheckedValue = value;
					lastParsedValue = Integer.parseInt(value, RADIX);
					return lastParsedValue;
				} catch (Exception e) {
					lastCheckedValue = null;
					lastParsedValue = null;
					throw new IllegalStateException();
				}
			} finally {
				lock.unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String parseNonNullObject(Object object) {
			if (!(object instanceof Integer))
				return null;

			if ((Integer) object < 0)
				return null;

			return Integer.toString((int) object, RADIX);
		}

	}

	private final String name;
	private final SettingInfo settingInfo;

	private CoreSettingItem(String name, SettingInfo settingInfo) {
		this.name = name;
		this.settingInfo = settingInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SettingInfo getSettingInfo() {
		return settingInfo;
	}
}

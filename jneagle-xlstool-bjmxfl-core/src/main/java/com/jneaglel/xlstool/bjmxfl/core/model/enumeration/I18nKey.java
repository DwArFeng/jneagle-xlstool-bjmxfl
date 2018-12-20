package com.jneaglel.xlstool.bjmxfl.core.model.enumeration;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * 国际化文本键。
 * 
 * @author DwArFeng
 * @since 0.0.0-alpha
 */
public enum I18nKey implements Name {

	LOGGER_1(new DefaultName("logger.1")), //
	LOGGER_2(new DefaultName("logger.2")), //
	LOGGER_3(new DefaultName("logger.3")), //
	LOGGER_4(new DefaultName("logger.4")), //
	LOGGER_5(new DefaultName("logger.5")), //
	LOGGER_6(new DefaultName("logger.6")), //
	LOGGER_7(new DefaultName("logger.7")), //
	LOGGER_8(new DefaultName("logger.8")), //
	LOGGER_9(new DefaultName("logger.9")), //
	LOGGER_10(new DefaultName("logger.10")), //
	LOGGER_11(new DefaultName("logger.11")), //
	LOGGER_12(new DefaultName("logger.12")), //
	LOGGER_13(new DefaultName("logger.13")), //
	LOGGER_14(new DefaultName("logger.14")), //
	LOGGER_15(new DefaultName("logger.15")), //
	LOGGER_16(new DefaultName("logger.16")), //
	LOGGER_17(new DefaultName("logger.17")), //
	LOGGER_18(new DefaultName("logger.18")), //
	LOGGER_19(new DefaultName("logger.19")), //
	LOGGER_20(new DefaultName("logger.20")), //
	LOGGER_21(new DefaultName("logger.21")), //
	LOGGER_22(new DefaultName("logger.22")), //
	LOGGER_23(new DefaultName("logger.23")), //
	LOGGER_24(new DefaultName("logger.24")), //
	LOGGER_25(new DefaultName("logger.25")), //
	;

	private Name name;

	private I18nKey(Name name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dwarfeng.dutil.basic.str.Name#getName()
	 */
	@Override
	public String getName() {
		return name.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name.getName();
	}
}

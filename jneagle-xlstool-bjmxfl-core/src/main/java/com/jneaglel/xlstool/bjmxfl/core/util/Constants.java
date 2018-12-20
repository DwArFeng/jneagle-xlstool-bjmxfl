package com.jneaglel.xlstool.bjmxfl.core.util;

/**
 * 程序常量。
 * 
 * @author DwArFeng
 * @since 0.0.0-alpha
 */
public final class Constants {

	/** 默认的丢失文本字段。 */
	public static final String MISSING_LABEL = "！文本丢失";

	/** 资源列表所在的路径。 */
	public static final String JPATH_RESOURCE_LIST = "/com/jneagle/xlstool/bjmxfl/resources/resource-list.xml";
	/** Jar包内默认的国际化属性文件路径。 */
	public static final String JPATH_DEFAULT_I18N_PROP_FILE = "/com/jneagle/xlstool/bjmxfl/resources/i18n/zh_CN.properties";

	/** 代表强制重置配置文件的命令行参数。 */
	public static final String CLI_OPT_FLAG_CONFIG_FORCE_RESET = "r";

	/** 代表Excel97-03的文件的扩展名。 */
	public static final String FILE_EXTENSION_EXCEL_97_03 = "xls";

	private Constants() {
		// 禁止外部实例化。
	}

}

package com.jneaglel.xlstool.bjmxfl.core;

import com.dwarfeng.dutil.basic.io.CT;
import com.jneaglel.xlstool.bjmxfl.core.control.BJMXFL;

/**
 * 用于输出版本号的工具。
 * 
 * @author DwArFeng
 * @since 1.1.0
 */
public class VersionTrace {

	public static void main(String[] args) {
		CT.trace(BJMXFL.VERSION.getLongName());
	}

}

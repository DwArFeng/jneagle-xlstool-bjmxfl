package com.jneaglel.xlstool.bjmxfl.core.control;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.dwarfeng.dutil.basic.prog.DefaultVersion;
import com.dwarfeng.dutil.basic.prog.Version;
import com.dwarfeng.dutil.basic.prog.VersionType;

/**
 * 
 * @author DwArFeng
 * @since 0.0.0-alpha
 */
public class BJMXFL {

	/** 程序的版本。 */
	public static final Version VERSION = new DefaultVersion.Builder().setType(VersionType.RELEASE)
			.setFirstVersion((byte) 1).setSecondVersion((byte) 1).setThirdVersion((byte) 0).setBuildDate("20181216")
			.setBuildVersion('A').build();
	/** 程序的实例列表，用于持有引用 */
	private static final Set<BJMXFL> INSTANCES = Collections.synchronizedSet(new HashSet<>());

	// --------------------------------------------管理器--------------------------------------------
	/** 模型管理器。 */
	private final ModelManager modelManager = new BJMXFLModelManager(this);
	/** 动作管理器。 */
	private final ActionManager actionManager = new BJMXFLActionManager(this);

}

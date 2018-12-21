package com.jneaglel.xlstool.bjmxfl.core.view.task;

import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;

/**
 * 抽象视图任务。
 * 
 * @author DwArFeng
 * @since 0.0.0-alpha
 */
public abstract class AbstractViewTask extends AbstractTask {

	/** 模型管理器。 */
	protected final ModelManager modelManager;
	/** 动作管理器。 */
	protected final ActionManager actionManager;

	public AbstractViewTask(ModelManager modelManager, ActionManager actionManager) {
		this.modelManager = modelManager;
		this.actionManager = actionManager;
	}

}

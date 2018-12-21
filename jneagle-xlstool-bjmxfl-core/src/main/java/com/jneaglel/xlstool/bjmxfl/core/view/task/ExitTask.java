package com.jneaglel.xlstool.bjmxfl.core.view.task;

import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;

public class ExitTask extends AbstractViewTask {

	public ExitTask(ModelManager modelManager, ActionManager actionManager) {
		super(modelManager, actionManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		actionManager.exit();
	}

}

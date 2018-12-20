package com.jneagle.xlstool.bjmxfl.launcher;

import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.jneaglel.xlstool.bjmxfl.core.control.BJMXFL;

/**
 * 启动任务。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
class StartTask extends AbstractTask {

	private final BJMXFL bjmxfl;
	private final String[] args;

	public StartTask(BJMXFL bjmxfl, String[] args) {
		this.bjmxfl = bjmxfl;
		this.args = args;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// TODO Auto-generated method stub

	}

}

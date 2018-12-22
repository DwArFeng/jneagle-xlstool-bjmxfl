package com.jneagle.xlstool.bjmxfl.launcher;

import com.jneaglel.xlstool.bjmxfl.core.control.BJMXFL;

public class BJMXFLLauncher {

	public static void main(String[] args) throws InterruptedException {
		BJMXFL bjmxfl = new BJMXFL();
		bjmxfl.getActionManager().submit(new StartTask(bjmxfl, args));
		bjmxfl.awaitFinish();
		System.exit(bjmxfl.getExitCode());
	}

}

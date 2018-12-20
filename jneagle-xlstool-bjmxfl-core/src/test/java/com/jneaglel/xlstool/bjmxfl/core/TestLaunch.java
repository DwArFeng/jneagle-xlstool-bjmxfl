package com.jneaglel.xlstool.bjmxfl.core;

import com.jneaglel.xlstool.bjmxfl.core.control.BJMXFL;

public class TestLaunch {

	public static void main(String[] args) throws InterruptedException {
		BJMXFL bjmxfl = new BJMXFL();
		bjmxfl.getActionManager().start(new String[] { "-r" });
		bjmxfl.awaitFinish();
	}

}

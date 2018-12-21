package com.jneaglel.xlstool.bjmxfl.core.control;

import java.io.File;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;

public class BJMXFLModelManager implements ModelManager {

	private final BJMXFL bjmxfl;

	public BJMXFLModelManager(BJMXFL bjmxfl) {
		this.bjmxfl = bjmxfl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Background getBackground() {
		return bjmxfl.getBackground();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncSettingHandler getCoreSettingHandler() {
		return bjmxfl.getCoreSettingHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncListModel<File> getFiles2ImportModel() {
		return bjmxfl.getFiles2ImportModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncI18nHandler getI18nHandler() {
		return bjmxfl.getI18nHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncLoggerHandler getLoggerHandler() {
		return bjmxfl.getLoggerHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncSettingHandler getModalSettingHandler() {
		return bjmxfl.getModalSettingHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncResourceHandler getResourceHandler() {
		return bjmxfl.getResourceHandler();
	}

}

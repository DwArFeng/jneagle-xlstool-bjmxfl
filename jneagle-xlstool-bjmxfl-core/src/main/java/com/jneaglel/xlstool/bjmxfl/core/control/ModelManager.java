package com.jneaglel.xlstool.bjmxfl.core.control;

import java.io.File;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;

public interface ModelManager {

	/**
	 * @return the background
	 */
	public Background getBackground();

	/**
	 * @return the coreSettingHandler
	 */
	public SyncSettingHandler getCoreSettingHandler();

	/**
	 * @return the files2ImportModel
	 */
	public SyncListModel<File> getFiles2ImportModel();

	/**
	 * @return the i18nHandler
	 */
	public SyncI18nHandler getI18nHandler();

	/**
	 * @return the loggerHandler
	 */
	public SyncLoggerHandler getLoggerHandler();

	/**
	 * @return the modalSettingHandler
	 */
	public SyncSettingHandler getModalSettingHandler();

	/**
	 * @return the resourceHandler
	 */
	public SyncResourceHandler getResourceHandler();

}

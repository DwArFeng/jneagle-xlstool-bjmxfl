package com.jneaglel.xlstool.bjmxfl.core.view.task;

import java.awt.Component;
import java.io.File;
import java.util.Locale;
import java.util.Optional;

import javax.swing.JFileChooser;

import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.CoreSettingItem;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.I18nKey;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.ModalSettingItem;
import com.jneaglel.xlstool.bjmxfl.core.util.Constants;

public class ExportFileTask extends AbstractViewTask implements Task {

	private final Component parentComponent;

	public ExportFileTask(ModelManager modelManager, ActionManager actionManager, Component parentComponent) {
		super(modelManager, actionManager);
		this.parentComponent = parentComponent;
	}

	@Override
	protected void todo() throws Exception {
		SyncSettingHandler coreSettingHandler = modelManager.getCoreSettingHandler();
		SyncSettingHandler modalSettingHandler = modelManager.getModalSettingHandler();

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(i18nString(I18nKey.LABEL_7));
		jfc.setLocale(coreSettingHandler.getParsedValidValue(CoreSettingItem.I18N_LOCALE, Locale.class));

		{
			Boolean isLastexportedFileExists;
			File lastexportedFile;

			modalSettingHandler.getLock().readLock().lock();
			try {
				isLastexportedFileExists = modalSettingHandler
						.getParsedValidValue(ModalSettingItem.FLAG_LAST_EXPORTED_FILE_EXISTS, Boolean.class);
				lastexportedFile = modalSettingHandler.getParsedValidValue(ModalSettingItem.FILE_LAST_EXPORTED_FILE,
						File.class);
			} finally {
				modalSettingHandler.getLock().readLock().unlock();
			}

			if (isLastexportedFileExists)
				jfc.setCurrentDirectory(lastexportedFile);
		}
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(true);
		jfc.setAcceptAllFileFilterUsed(false);
		// 打开对话框，让用户选择文件。
		int result = jfc.showSaveDialog(parentComponent);
		// 如果用户按下了确定键，则将文件模型设置为用户选择的文件。
		if (result == JFileChooser.APPROVE_OPTION) {
			actionManager.export(jfc.getSelectedFile());
		}
	}

	private String i18nString(I18nKey i18nKey) {
		return Optional.ofNullable(modelManager)
				.map(manager -> manager.getI18nHandler().getStringOrDefault(i18nKey, Constants.MISSING_LABEL))
				.orElse("null");
	}

}

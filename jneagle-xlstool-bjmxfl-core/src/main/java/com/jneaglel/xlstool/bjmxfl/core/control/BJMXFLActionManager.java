package com.jneaglel.xlstool.bjmxfl.core.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.dwarfeng.dutil.basic.cna.AttributeComplex;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.basic.mea.TimeMeasurer;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.i18n.PropUrlI18nInfo;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.logger.SysOutLoggerInfo;
import com.dwarfeng.dutil.develop.logger.io.Log4jLoggerLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.dutil.develop.resource.io.ResourceResetPolicy;
import com.dwarfeng.dutil.develop.resource.io.XmlJar2FileResourceLoader;
import com.dwarfeng.dutil.develop.setting.SettingUtil;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.dwarfeng.dutil.develop.setting.io.PropSettingValueLoader;
import com.dwarfeng.dutil.develop.setting.io.PropSettingValueSaver;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.CliSettingItem;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.CoreSettingItem;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.I18nKey;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.ModalSettingItem;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.ResourceKey;
import com.jneaglel.xlstool.bjmxfl.core.model.ioprocessor.TextBjmxInfoLoader;
import com.jneaglel.xlstool.bjmxfl.core.model.ioprocessor.XlsBjmxInfoSaver;
import com.jneaglel.xlstool.bjmxfl.core.util.Constants;
import com.jneaglel.xlstool.bjmxfl.core.view.MainFrame;

class BJMXFLActionManager implements ActionManager {

	private final BJMXFL bjmxfl;

	public BJMXFLActionManager(BJMXFL bjmxfl) {
		this.bjmxfl = bjmxfl;
	}

	// --------------------------------------------程序控制--------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(String[] args) throws IllegalStateException, NullPointerException {
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		// 要求程序的运行状态为未启动。
		requireRuntimeState(RuntimeState.NOT_START);
		try {
			// 在置被读取之前，首先使用内置的功能模块。
			applyBuiltinFunctionBeforeApplyConfig();
			// 通知应用程序正在启动。
			info(I18nKey.LOGGER_1);
			// 解析命令行参数。
			parseCliOption(args);
			// 应用程序配置。
			applyConfig();
			// 启动GUI。
			runGUI();
			// 设置程序的运行状态为正在运行。
			setRuntimeState(RuntimeState.RUNNING);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 紧急退出机制。
		}
	}

	/**
	 * 在配置被读取之前，首先使用内置的功能模块。
	 */
	private void applyBuiltinFunctionBeforeApplyConfig() {
		SyncLoggerHandler loggerHandler = bjmxfl.getLoggerHandler();
		SyncI18nHandler i18nHandler = bjmxfl.getI18nHandler();

		SysOutLoggerInfo defaultLoggerInfo = new SysOutLoggerInfo(null, false);
		PropUrlI18nInfo defaultI18nInfo = new PropUrlI18nInfo(null, "初始化用国际化配置",
				BJMXFL.class.getResource(Constants.JPATH_DEFAULT_I18N_PROP_FILE));

		loggerHandler.getLock().writeLock().lock();
		try {
			loggerHandler.clear();
			loggerHandler.add(defaultLoggerInfo);
			loggerHandler.use(defaultLoggerInfo);
		} finally {
			loggerHandler.getLock().writeLock().unlock();
		}

		i18nHandler.getLock().writeLock().lock();
		try {
			i18nHandler.clear();
			i18nHandler.add(defaultI18nInfo);
			i18nHandler.setCurrentLocale(null);
		} finally {
			i18nHandler.getLock().writeLock().unlock();
		}
	}

	private void parseCliOption(String[] args) throws NullPointerException {
		SyncSettingHandler cliSettingHandler = bjmxfl.getCliSettingHandler();

		info(I18nKey.LOGGER_2);

		// 生成程序的命令行选项。
		Options options = new Options();
		options.addOption(Option.builder(Constants.CLI_OPT_FLAG_CONFIG_FORCE_RESET).hasArg(false).build());

		cliSettingHandler.getLock().writeLock().lock();
		try {
			cliSettingHandler.clear();
			SettingUtil.putEnumItems(CliSettingItem.class, cliSettingHandler);
			// 解析命令行。
			CommandLine commandLine = new DefaultParser().parse(options, args);
			// 判断是否需要强制复位配置文件。
			if (commandLine.hasOption(Constants.CLI_OPT_FLAG_CONFIG_FORCE_RESET)) {
				cliSettingHandler.setParsedValue(CliSettingItem.FLAG_CONFIG_FORCE_RESET, true);
			} else {
				cliSettingHandler.setParsedValue(CliSettingItem.FLAG_CONFIG_FORCE_RESET, false);
			}
		} catch (ParseException e) {
			warn(I18nKey.LOGGER_3, e);
		} finally {
			cliSettingHandler.getLock().writeLock().unlock();
		}
	}

	private void applyConfig() throws Exception {
		// 定义命令行变量。
		SyncSettingHandler cliSettingHandler = bjmxfl.getCliSettingHandler();
		final Boolean flag_forceReset = cliSettingHandler.getParsedValidValue(CliSettingItem.FLAG_CONFIG_FORCE_RESET,
				Boolean.class);

		// 加载配置信息。
		loadResource(flag_forceReset);
		// 加载记录器配置。
		loadLoggerHandler();
		// 加器国际化配置。
		loadI18nHandler();
		// 加载核心配置。
		loadCoreSettingHandler();
		// 应用核心配置
		applyCoreSetting();
		// 加载模态配置。
		loadModalSettingHandler();
		// 应用模态配置
		applyModalSetting();
	}

	private InputStream openResourceInputStream(ResourceKey resourceKey) throws IOException {
		Resource resource = bjmxfl.getResourceHandler().get(resourceKey.getName());
		try {
			return resource.openInputStream();
		} catch (IOException e) {
			formatWarn(I18nKey.LOGGER_10, e, resourceKey.getName());
			resource.reset();
			return resource.openInputStream();
		}
	}

	private void loadResource(Boolean flag_forceReset) {
		SyncResourceHandler resourceHandler = bjmxfl.getResourceHandler();

		info(I18nKey.LOGGER_4);
		resourceHandler.getLock().writeLock().lock();
		try {
			resourceHandler.clear();
			// 如果在此处出现异常，则程序无法加载最基本的配置列表，但程序仍然可以继续运行。
			InputStream in;
			try {
				in = BJMXFL.class.getResource(Constants.JPATH_RESOURCE_LIST).openStream();
			} catch (IOException e) {
				formatError(I18nKey.LOGGER_5, e);
				return;
			}
			Set<LoadFailedException> eptSet = new LinkedHashSet<>();
			try (XmlJar2FileResourceLoader loader = new XmlJar2FileResourceLoader(in,
					flag_forceReset ? ResourceResetPolicy.ALWAYS : ResourceResetPolicy.AUTO)) {
				eptSet.addAll(loader.countinuousLoad(resourceHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, in.toString());
			}
			for (LoadFailedException e : eptSet) {
				warn(I18nKey.LOGGER_7, e);
			}
		} finally {
			resourceHandler.getLock().writeLock().unlock();
		}
	}

	private void loadLoggerHandler() {
		SyncLoggerHandler loggerHandler = bjmxfl.getLoggerHandler();

		info(I18nKey.LOGGER_8);
		loggerHandler.getLock().writeLock().lock();
		try {
			loggerHandler.clear();
			InputStream in;
			try {
				in = openResourceInputStream(ResourceKey.LOGGER_SETTING);
			} catch (IOException e) {
				error(I18nKey.LOGGER_9, e);
				return;
			}
			Set<LoadFailedException> eptSet = new LinkedHashSet<>();
			try (Log4jLoggerLoader loader = new Log4jLoggerLoader(in)) {
				eptSet.addAll(loader.countinuousLoad(loggerHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, in.toString());
			}
			for (LoadFailedException e : eptSet) {
				warn(I18nKey.LOGGER_11, e);
			}
			loggerHandler.useAll();
		} finally {
			loggerHandler.getLock().writeLock().unlock();
		}
	}

	private void loadI18nHandler() {
		SyncI18nHandler i18nHandler = bjmxfl.getI18nHandler();

		info(I18nKey.LOGGER_12);
		i18nHandler.getLock().writeLock().lock();
		try {
			i18nHandler.clear();
			InputStream in;
			try {
				in = openResourceInputStream(ResourceKey.I18N_SETTING);
			} catch (IOException e) {
				error(I18nKey.LOGGER_13, e);
				return;
			}
			Set<LoadFailedException> eptSet = new LinkedHashSet<>();
			try (XmlPropFileI18nLoader loader = new XmlPropFileI18nLoader(in)) {
				eptSet.addAll(loader.countinuousLoad(i18nHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, in.toString());
			}
			for (LoadFailedException e : eptSet) {
				warn(I18nKey.LOGGER_14, e);
			}
			i18nHandler.setCurrentLocale(null);
		} finally {
			i18nHandler.getLock().writeLock().unlock();
		}
	}

	private void loadCoreSettingHandler() {
		SyncSettingHandler coreSettingHandler = bjmxfl.getCoreSettingHandler();

		info(I18nKey.LOGGER_15);
		coreSettingHandler.getLock().writeLock().lock();
		try {
			coreSettingHandler.clear();
			SettingUtil.putEnumItems(CoreSettingItem.class, coreSettingHandler);
			InputStream in;
			try {
				in = openResourceInputStream(ResourceKey.CONFIG);
			} catch (IOException e) {
				error(I18nKey.LOGGER_16, e);
				return;
			}
			Set<LoadFailedException> eptSet = new LinkedHashSet<>();
			try (PropSettingValueLoader loader = new PropSettingValueLoader(in, true)) {
				eptSet.addAll(loader.countinuousLoad(coreSettingHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, in.toString());
			}
			for (LoadFailedException e : eptSet) {
				warn(I18nKey.LOGGER_17, e);
			}
		} finally {
			coreSettingHandler.getLock().writeLock().unlock();
		}
	}

	private void applyCoreSetting() {
		SyncSettingHandler coreSettingHandler = bjmxfl.getCoreSettingHandler();
		SyncI18nHandler i18nHandler = bjmxfl.getI18nHandler();

		Locale i18nLocale;

		coreSettingHandler.getLock().readLock().lock();
		try {
			i18nLocale = coreSettingHandler.getParsedValidValue(CoreSettingItem.I18N_LOCALE, Locale.class);
		} finally {
			coreSettingHandler.getLock().readLock().unlock();
		}

		i18nHandler.getLock().writeLock().lock();
		try {
			i18nHandler.setCurrentLocale(i18nLocale);
		} finally {
			i18nHandler.getLock().writeLock().unlock();
		}
	}

	private void loadModalSettingHandler() {
		SyncSettingHandler modalSettingHandler = bjmxfl.getModalSettingHandler();

		info(I18nKey.LOGGER_18);
		modalSettingHandler.getLock().writeLock().lock();
		try {
			modalSettingHandler.clear();
			SettingUtil.putEnumItems(ModalSettingItem.class, modalSettingHandler);
			InputStream in;
			try {
				in = openResourceInputStream(ResourceKey.MODAL);
			} catch (IOException e) {
				error(I18nKey.LOGGER_19, e);
				return;
			}
			Set<LoadFailedException> eptSet = new LinkedHashSet<>();
			try (PropSettingValueLoader loader = new PropSettingValueLoader(in, true)) {
				eptSet.addAll(loader.countinuousLoad(modalSettingHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, in.toString());
			}
			for (LoadFailedException e : eptSet) {
				warn(I18nKey.LOGGER_20, e);
			}
		} finally {
			modalSettingHandler.getLock().writeLock().unlock();
		}
	}

	private void applyModalSetting() {
		SyncSettingHandler modalSettingHandler = bjmxfl.getModalSettingHandler();

		modalSettingHandler.getLock().readLock().lock();
		try {
			// Something to do.
		} finally {
			modalSettingHandler.getLock().readLock().unlock();
		}

	}

	private void runGUI() {
		info(I18nKey.LOGGER_21);

		SyncReferenceModel<MainFrame> mainFrameRef = bjmxfl.getMainFrameRef();

		SwingUtil.invokeInEventQueue(() -> {
			try {
				UIManager.setLookAndFeel(new NimbusLookAndFeel());
			} catch (UnsupportedLookAndFeelException ignore) {
			}

			MainFrame mainFrame = new MainFrame(bjmxfl.getModelManager(), bjmxfl.getActionManager());
			mainFrame.setVisible(true);
			mainFrameRef.set(mainFrame);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exit() throws IllegalStateException {
		// 检查并设置程序的运行状态未正在运行。
		requireRuntimeState(RuntimeState.RUNNING);
		try {
			// 通知应用程序正在退出。
			info(I18nKey.LOGGER_26);
			// 保存程序配置。
			saveConfig();
			// 关闭GUI。
			disposeGUI();
			// 停止后台。
			stopBackground();
			// 设置退出代码。
			setExitCode(0);
			// 将程序的状态设置为已经结束。
			setRuntimeState(RuntimeState.ENDED);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 紧急退出机制。
		}
	}

	private void saveConfig() {
		// 加载模态配置。
		saveModalSettingHandler();
	}

	private OutputStream openResourceOutputStream(ResourceKey resourceKey) throws IOException {
		Resource resource = bjmxfl.getResourceHandler().get(resourceKey.getName());
		try {
			return resource.openOutputStream();
		} catch (IOException e) {
			formatWarn(I18nKey.LOGGER_27, e, resourceKey.getName());
			resource.reset();
			return resource.openOutputStream();
		}
	}

	private void saveModalSettingHandler() {
		SyncSettingHandler modalSettingHandler = bjmxfl.getModalSettingHandler();

		info(I18nKey.LOGGER_23);
		modalSettingHandler.getLock().readLock().lock();
		try {
			OutputStream out;
			try {
				out = openResourceOutputStream(ResourceKey.MODAL);
			} catch (IOException e) {
				error(I18nKey.LOGGER_24, e);
				return;
			}
			Set<SaveFailedException> eptSet = new LinkedHashSet<>();
			try (PropSettingValueSaver saver = new PropSettingValueSaver(out, true)) {
				eptSet.addAll(saver.countinuousSave(modalSettingHandler));
			} catch (IOException e) {
				formatWarn(I18nKey.LOGGER_6, e, out.toString());
			}
			for (SaveFailedException e : eptSet) {
				warn(I18nKey.LOGGER_25, e);
			}
		} finally {
			modalSettingHandler.getLock().readLock().unlock();
		}
	}

	private void disposeGUI() {
		SyncReferenceModel<MainFrame> mainFrameRef = bjmxfl.getMainFrameRef();

		SwingUtil.invokeInEventQueue(() -> {
			mainFrameRef.get().dispose();
		});
	}

	private void stopBackground() {
		Background background = bjmxfl.getBackground();

		background.shutdown();
	}

	private void setExitCode(int code) {
		SyncReferenceModel<Integer> exitCodeRef = bjmxfl.getExitCodeRef();
		exitCodeRef.set(code);
	}

	private void requireRuntimeState(RuntimeState state) {
		ReferenceModel<RuntimeState> runtimeStateRef = bjmxfl.getRuntimeStateRef();
		RuntimeState currentState = runtimeStateRef.get();

		if (currentState != state) {
			throw new IllegalStateException(String.format("非法的运行状态 %s，应该为 %s", currentState, state));
		}
	}

	private void setRuntimeState(RuntimeState state) {
		ReferenceModel<RuntimeState> runtimeStateRef = bjmxfl.getRuntimeStateRef();
		Lock runtimeStateLock = bjmxfl.getRuntimeStateLock();
		Condition runtimeStateCondition = bjmxfl.getRuntimeStateCondition();

		runtimeStateLock.lock();
		try {
			runtimeStateRef.set(state);
			runtimeStateCondition.signalAll();
		} finally {
			runtimeStateLock.unlock();
		}
	}

	// --------------------------------------------模型动作--------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void submit(Task task) throws NullPointerException {
		Objects.requireNonNull(task, "入口参数 task 不能为 null。");
		bjmxfl.getBackground().submit(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFiles2Import(Collection<File> files) throws NullPointerException {
		Objects.requireNonNull(files, "入口参数 files 不能为 null。");

		SyncListModel<File> files2ImportModel = bjmxfl.getFiles2ImportModel();
		SyncSettingHandler modalSettingHandler = bjmxfl.getModalSettingHandler();

		files2ImportModel.getLock().writeLock().lock();
		try {
			files2ImportModel.clear();
			files2ImportModel.addAll(files);
		} finally {
			files2ImportModel.getLock().writeLock().unlock();
		}

		if (files.isEmpty())
			return;

		modalSettingHandler.getLock().writeLock().lock();
		try {
			modalSettingHandler.setParsedValue(ModalSettingItem.FLAG_LAST_IMPORTED_FILE_EXISTS, true);
			modalSettingHandler.setParsedValue(ModalSettingItem.FILE_LAST_IMPORTED_FILE,
					files.stream().findFirst().get().getParentFile());
		} finally {
			modalSettingHandler.getLock().writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void export(File dir) throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(dir, "入口参数 dir 不能为 null。");

		if (!dir.isDirectory())
			throw new IllegalArgumentException("入口参数 dir 必须是一个文件夹");

		SyncListModel<File> files2ImportModel = bjmxfl.getFiles2ImportModel();
		SyncSettingHandler coreSettingHandler = bjmxfl.getCoreSettingHandler();
		SyncSettingHandler modalSettingHandler = bjmxfl.getModalSettingHandler();

		modalSettingHandler.getLock().writeLock().lock();
		try {
			modalSettingHandler.setParsedValue(ModalSettingItem.FLAG_LAST_EXPORTED_FILE_EXISTS, true);
			modalSettingHandler.setParsedValue(ModalSettingItem.FILE_LAST_EXPORTED_FILE, dir);
		} finally {
			modalSettingHandler.getLock().writeLock().unlock();
		}

		info(I18nKey.LOGGER_36);
		TimeMeasurer tm = new TimeMeasurer();
		tm.start();

		final String src_dataSectionDelimiter;
		final String src_fileEncode;
		final int src_firstDataRow;
		final int src_index_xmh;
		final int src_index_bjh;
		final int src_index_ljh;
		final int src_index_ljmc;
		final int src_index_cz;
		final int src_index_dx;
		final int src_index_dz;
		final int src_index_zz;
		final int src_index_ts;

		final int exp_dataSheetIndex;
		final int exp_firstDataRow;
		final int exp_index_xmh;
		final int exp_index_bjh;
		final int exp_index_ljh;
		final int exp_index_ljmc;
		final int exp_index_cz;
		final int exp_index_dx;
		final int exp_index_dz;
		final int exp_index_zz;
		final int exp_index_ts;

		coreSettingHandler.getLock().readLock().lock();
		try {
			src_dataSectionDelimiter = coreSettingHandler
					.getParsedValidValue(CoreSettingItem.SRCTEXT_DATASECTION_DELIMITER, String.class);
			src_fileEncode = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_ENCODE, String.class);
			src_firstDataRow = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_ROW_FIRST_DATA,
					Integer.class);
			src_index_xmh = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_XMH,
					Integer.class);
			src_index_bjh = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_BJH,
					Integer.class);
			src_index_ljh = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_LJH,
					Integer.class);
			src_index_ljmc = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_LJMC,
					Integer.class);
			src_index_cz = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_CZ,
					Integer.class);
			src_index_dx = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_DX,
					Integer.class);
			src_index_dz = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_DZ,
					Integer.class);
			src_index_zz = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_ZZ,
					Integer.class);
			src_index_ts = coreSettingHandler.getParsedValidValue(CoreSettingItem.SRCTEXT_INDEX_COLUMN_TS,
					Integer.class);

			exp_dataSheetIndex = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_TARGET_SHEET,
					Integer.class);
			exp_firstDataRow = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_ROW_FIRST_DATA,
					Integer.class);
			exp_index_xmh = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_XMH,
					Integer.class);
			exp_index_bjh = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_BJH,
					Integer.class);
			exp_index_ljh = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_LJH,
					Integer.class);
			exp_index_ljmc = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_LJMC,
					Integer.class);
			exp_index_cz = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_CZ,
					Integer.class);
			exp_index_dx = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_DX,
					Integer.class);
			exp_index_dz = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_DZ,
					Integer.class);
			exp_index_zz = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_ZZ,
					Integer.class);
			exp_index_ts = coreSettingHandler.getParsedValidValue(CoreSettingItem.EXPTABLE_INDEX_COLUMN_TS,
					Integer.class);
		} finally {
			coreSettingHandler.getLock().readLock().unlock();
		}

		files2ImportModel.getLock().readLock().lock();
		try {
			for (File file : files2ImportModel) {
				formatInfo(I18nKey.LOGGER_31, file.getName());

				List<AttributeComplex> bjmxInfoList = new ArrayList<>();

				// 将文件中的属性输入到 bjmxInfoList 中。
				FileInputStream in;
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					formatWarn(I18nKey.LOGGER_28, e, file.getName());
					continue;
				}
				Set<LoadFailedException> loadEptSet = new LinkedHashSet<>();
				try (TextBjmxInfoLoader loader = new TextBjmxInfoLoader(in, src_dataSectionDelimiter, src_fileEncode,
						src_firstDataRow, src_index_xmh, src_index_bjh, src_index_ljh, src_index_ljmc, src_index_cz,
						src_index_dx, src_index_dz, src_index_zz, src_index_ts)) {
					loadEptSet.addAll(loader.countinuousLoad(bjmxInfoList));
				} catch (IOException e) {
					formatWarn(I18nKey.LOGGER_29, e, file.getName());
				}
				for (LoadFailedException e : loadEptSet) {
					warn(I18nKey.LOGGER_30, e);
				}

				// 将属性文件导出到 xls 电子表中。
				InputStream templateInputstream;
				try {
					templateInputstream = openResourceInputStream(ResourceKey.EXPORT_TEMPLATE);
				} catch (IOException e) {
					error(I18nKey.LOGGER_32, e);
					continue;
				}
				File targetFile = new File(dir, replaceFileExtension(file, Constants.FILE_EXTENSION_EXCEL_97_03));
				try {
					FileUtil.createFileIfNotExists(targetFile);
				} catch (IOException e) {
					formatWarn(I18nKey.LOGGER_33, e, targetFile.getName());
					continue;
				}
				FileOutputStream out;
				try {
					out = new FileOutputStream(targetFile);
				} catch (FileNotFoundException e) {
					formatWarn(I18nKey.LOGGER_34, e, targetFile.getName());
					continue;
				}
				Set<SaveFailedException> saveEptSet = new LinkedHashSet<>();
				try (XlsBjmxInfoSaver saver = new XlsBjmxInfoSaver(out, templateInputstream, exp_dataSheetIndex,
						exp_firstDataRow, exp_index_xmh, exp_index_bjh, exp_index_ljh, exp_index_ljmc, exp_index_cz,
						exp_index_dx, exp_index_dz, exp_index_zz, exp_index_ts)) {
					saveEptSet.addAll(saver.countinuousSave(bjmxInfoList));
				} catch (IOException e) {
					formatWarn(I18nKey.LOGGER_35, e, targetFile.getName());
				}
			}
		} finally {
			files2ImportModel.getLock().readLock().unlock();
		}

		tm.stop();
		formatInfo(I18nKey.LOGGER_37, tm.getTimeMs());
	}

	private String replaceFileExtension(File file, String fileNameExtension) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf('.');
		String substring = fileName.substring(0, lastDotIndex < 0 ? fileName.length() : lastDotIndex);
		return new StringBuilder().append(substring).append('.').append(fileNameExtension).toString();
	}

	// --------------------------------------------日志输出--------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String message) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		bjmxfl.getLoggerHandler().trace(message);
	}

	@SuppressWarnings("unused")
	private void trace(I18nKey key) throws NullPointerException {
		trace(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String message) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		bjmxfl.getLoggerHandler().debug(message);
	}

	@SuppressWarnings("unused")
	private void debug(I18nKey key) throws NullPointerException {
		debug(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String message) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		bjmxfl.getLoggerHandler().info(message);
	}

	private void info(I18nKey key) throws NullPointerException {
		info(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL));
	}

	private void formatInfo(I18nKey key, Object... args) {
		info(String.format(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), args));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		bjmxfl.getLoggerHandler().warn(message);
	}

	@SuppressWarnings("unused")
	private void warn(I18nKey key) throws NullPointerException {
		warn(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL));
	}

	@SuppressWarnings("unused")
	private void formatWarn(I18nKey key, Object... args) {
		warn(String.format(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), args));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message, Throwable t) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		bjmxfl.getLoggerHandler().warn(message, t);
	}

	private void warn(I18nKey key, Throwable t) throws NullPointerException {
		warn(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), t);
	}

	private void formatWarn(I18nKey key, Throwable t, Object... args) {
		warn(String.format(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), args), t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String message, Throwable t) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		bjmxfl.getLoggerHandler().error(message, t);
	}

	private void error(I18nKey key, Throwable t) throws NullPointerException {
		error(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), t);
	}

	private void formatError(I18nKey key, Throwable t, Object... args) {
		error(String.format(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), args), t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(String message, Throwable t) throws NullPointerException {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		bjmxfl.getLoggerHandler().fatal(message, t);
	}

	@SuppressWarnings("unused")
	private void fatal(I18nKey key, Throwable t) throws NullPointerException {
		fatal(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), t);
	}

	@SuppressWarnings("unused")
	private String i18nString(I18nKey key) {
		return bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL);
	}

	@SuppressWarnings("unused")
	private String formatI18nString(I18nKey key, Object... args) {
		return String.format(bjmxfl.getI18nHandler().getStringOrDefault(key, Constants.MISSING_LABEL), args);
	}

}

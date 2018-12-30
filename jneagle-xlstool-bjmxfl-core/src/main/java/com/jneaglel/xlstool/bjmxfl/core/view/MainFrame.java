package com.jneaglel.xlstool.bjmxfl.core.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ListAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ListObverser;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.BJMXFL;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;
import com.jneaglel.xlstool.bjmxfl.core.model.enumeration.I18nKey;
import com.jneaglel.xlstool.bjmxfl.core.util.Constants;
import com.jneaglel.xlstool.bjmxfl.core.view.task.ExitTask;
import com.jneaglel.xlstool.bjmxfl.core.view.task.ExportFileTask;
import com.jneaglel.xlstool.bjmxfl.core.view.task.SetFiles2ImportTask;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 6702289503309634106L;

	private final JPanel contentPane;
	private final JButton btnImport;
	private final JButton btnExport;
	private final JLabel lblBanner;
	private final JLabel lblVersionIndicator;

	private ModelManager modelManager;
	private ActionManager actionManager;

	private final MuaListModel<File> listModel = new MuaListModel<>();

	private final ListObverser<File> listObverser = new ListAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(int index, File element) {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.add(index, element);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(int index, File element) {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.remove(index);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(int index, File oldElement, File newElement) {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.set(index, newElement);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.clear();
			});
		}

	};

	public MainFrame() {
		this(null, null);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(ModelManager modelManager, ActionManager actionManager) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 441, 533);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				checkManagerAndDo(() -> {
					MainFrame.this.actionManager
							.submit(new ExitTask(MainFrame.this.modelManager, MainFrame.this.actionManager));
				});
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		btnImport = new JButton("New button");
		btnImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkManagerAndDo(() -> {
					MainFrame.this.actionManager.submit(new SetFiles2ImportTask(MainFrame.this.modelManager,
							MainFrame.this.actionManager, MainFrame.this));
				});
			}
		});
		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.fill = GridBagConstraints.BOTH;
		gbc_btnImport.insets = new Insets(0, 0, 5, 0);
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 0;
		contentPane.add(btnImport, gbc_btnImport);

		lblBanner = new JLabel("New label");
		GridBagConstraints gbc_lblBanner = new GridBagConstraints();
		gbc_lblBanner.fill = GridBagConstraints.VERTICAL;
		gbc_lblBanner.anchor = GridBagConstraints.WEST;
		gbc_lblBanner.insets = new Insets(0, 0, 5, 0);
		gbc_lblBanner.gridx = 0;
		gbc_lblBanner.gridy = 1;
		contentPane.add(lblBanner, gbc_lblBanner);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		JList<File> list = new JList<>();
		list.setModel(listModel);
		scrollPane.setViewportView(list);

		btnExport = new JButton("New button");
		btnExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.actionManager.submit(
						new ExportFileTask(MainFrame.this.modelManager, MainFrame.this.actionManager, MainFrame.this));
			}
		});
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.insets = new Insets(0, 0, 5, 0);
		gbc_btnExport.fill = GridBagConstraints.BOTH;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 3;
		contentPane.add(btnExport, gbc_btnExport);

		lblVersionIndicator = new JLabel(BJMXFL.VERSION.getLongName());
		GridBagConstraints gbc_lblVersionIndicator = new GridBagConstraints();
		gbc_lblVersionIndicator.fill = GridBagConstraints.VERTICAL;
		gbc_lblVersionIndicator.anchor = GridBagConstraints.EAST;
		gbc_lblVersionIndicator.gridx = 0;
		gbc_lblVersionIndicator.gridy = 4;
		contentPane.add(lblVersionIndicator, gbc_lblVersionIndicator);

		this.modelManager = modelManager;
		this.actionManager = actionManager;

		// 添加观察器。
		Optional.ofNullable(this.modelManager).ifPresent(manager -> {
			manager.getFiles2ImportModel().addObverser(listObverser);
		});

		// 同步模型。
		syncModel();
	}

	/**
	 * @return the modelManager
	 */
	public ModelManager getModelManager() {
		return modelManager;
	}

	/**
	 * @param modelManager
	 *            the modelManager to set
	 */
	public void setModelManager(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	/**
	 * @return the actionManager
	 */
	public ActionManager getActionManager() {
		return actionManager;
	}

	/**
	 * @param actionManager
	 *            the actionManager to set
	 */
	public void setActionManager(ActionManager actionManager) {
		Optional.ofNullable(this.modelManager).ifPresent(manager -> {
			manager.getFiles2ImportModel().removeObverser(listObverser);
		});
		this.actionManager = actionManager;
		Optional.ofNullable(this.modelManager).ifPresent(manager -> {
			manager.getFiles2ImportModel().addObverser(listObverser);
		});
		syncModel();
	}

	private void syncModel() {
		SyncI18nHandler i18nHandler = modelManager.getI18nHandler();
		SyncListModel<File> files2ImportModel = modelManager.getFiles2ImportModel();

		this.setTitle(Constants.MISSING_LABEL);
		btnImport.setText(Constants.MISSING_LABEL);
		btnExport.setText(Constants.MISSING_LABEL);
		lblBanner.setText(Constants.MISSING_LABEL);

		listModel.clear();

		if (Objects.isNull(modelManager)) {
			return;
		}

		i18nHandler.getLock().readLock().lock();
		try {
			this.setTitle(i18nString(I18nKey.LABEL_1));
			btnImport.setText(i18nString(I18nKey.LABEL_2));
			btnExport.setText(i18nString(I18nKey.LABEL_3));
			lblBanner.setText(i18nString(I18nKey.LABEL_4));
		} finally {
			i18nHandler.getLock().readLock().unlock();
		}

		files2ImportModel.getLock().readLock().lock();
		try {
			listModel.addAll(files2ImportModel);
		} finally {
			files2ImportModel.getLock().readLock().unlock();
		}
	}

	private String i18nString(I18nKey i18nKey) {
		return Optional.ofNullable(modelManager)
				.map(manager -> manager.getI18nHandler().getStringOrDefault(i18nKey, Constants.MISSING_LABEL))
				.orElse("null");
	}

	private void checkManagerAndDo(Runnable runnable) {
		if (Objects.isNull(modelManager) || Objects.isNull(actionManager)) {
			return;
		}
		runnable.run();
	}

}

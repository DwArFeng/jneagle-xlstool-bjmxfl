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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;
import com.jneaglel.xlstool.bjmxfl.core.view.task.ExitTask;

public class MainFrame extends JFrame {

	private final JPanel contentPane;
	private JButton btnImport;
	private JButton btnExport;
	private JLabel lblBanner;
	
	private ModelManager modelManager;
	private ActionManager actionManager;
	
	

	public MainFrame() {
		this(null, null);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(ModelManager modelManager, ActionManager actionManager) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 441, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		btnImport = new JButton("New button");
		btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		scrollPane.setViewportView(list);

		btnExport = new JButton("New button");
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.fill = GridBagConstraints.BOTH;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 3;
		contentPane.add(btnExport, gbc_btnExport);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				checkManagerAndDo(() -> {
					MainFrame.this.actionManager
							.submit(new ExitTask(MainFrame.this.modelManager, MainFrame.this.actionManager));
				});
			}
		});

		this.modelManager = modelManager;
		this.actionManager = actionManager;

		// 添加观察器。

		// 同步模型。
	}

	private void checkManagerAndDo(Runnable runnable) {
		if (Objects.isNull(modelManager) || Objects.isNull(actionManager)) {
			return;
		}
		runnable.run();
	}

}

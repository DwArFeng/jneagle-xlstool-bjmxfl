package com.jneaglel.xlstool.bjmxfl.core.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jneaglel.xlstool.bjmxfl.core.control.ActionManager;
import com.jneaglel.xlstool.bjmxfl.core.control.ModelManager;

public class MainFrame extends JFrame {

	private final JPanel contentPane;

	private ModelManager modelManager;
	private ActionManager actionManager;

	public MainFrame() {
		this(null, null);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(ModelManager modelManager, ActionManager actionManager) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}

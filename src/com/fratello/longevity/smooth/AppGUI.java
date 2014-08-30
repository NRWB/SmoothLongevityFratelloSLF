/*
    <Program: SLFFiles; finds copies of files. May also delete copies.>
    Copyright (C) <2014>  <name of author: https://github.com/NRWB>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
// ---------------------------------------------------------------------------
/*
Copyright [2014] [name of copyright owner : https://github.com/NRWB]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.fratello.longevity.smooth;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JProgressBar;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.fratello.longevity.smooth.object.SpecialFile;
import com.fratello.longevity.smooth.swing.auxiliary.DoubleDocListener;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Dimension;

/*
 * Class Description: Largest file in project to setup GUI related
 * features.
 */
public class AppGUI extends Sentinel {
	protected JFrame frmFileSystemSearch;
	private JTextField userSelectedDirectories;
	
	private JFormattedTextField namePercentMatchTextField;
	private JFormattedTextField sizePercentMatchTextField;
	private JFormattedTextField thoroughPercentMatchTextField;

	private JFileChooser chooser;

	private List<File> multiSelection;

	private JProgressBar SentinelProgressBar;
	private JLabel SentinelProgressLabel;

	private boolean ActiveGUI;
	private boolean GUI_Start;
	private boolean GUI_Pause;
	private boolean GUI_Stop;
	private boolean ran_at_least_once;
	
	private JCheckBox chckbxExt;
	private JCheckBox chckbxHash;
	private JComboBox<String> comboBoxHash;
	private JCheckBox chckbxName;
	private JComboBox<String> comboBoxNames;
	private JCheckBox chckbxSize;
	private JComboBox<String> comboBoxSize;
	private JCheckBox chckbxThorough;
	private JCheckBox chckbxExitFast;
	
	private int LabelMaxSize;

	public AppGUI() {
		initialize();
	}

	private void initialize() {
		LabelMaxSize = 0;
		
		ActiveGUI = true;
		GUI_Start = false;
		GUI_Pause = true;
		GUI_Stop = false;
		ran_at_least_once = false;
		
		execute();

		initializeFields();

		frmFileSystemSearch = new JFrame();
		frmFileSystemSearch.setTitle("Smooth Longevity Fratello");
		frmFileSystemSearch.setBounds(100, 100, 450, 300);
		frmFileSystemSearch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFileSystemSearch.setResizable(false);

		UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(Color.black, 1));

		JMenuBar menuBar = new JMenuBar();
		frmFileSystemSearch.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmFirst = new JMenuItem("Open Directory");
		mntmFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFileChooserDir(mntmFirst);
			}
		});
		mnNewMenu.add(mntmFirst);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmFileSystemSearch.dispose();
			}
		});
		mnNewMenu.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		JMenuItem mntmTutorials = new JMenuItem("Tutorials");
		mnHelp.add(mntmTutorials);
		
		JMenuItem mntmCheckForUpdates = new JMenuItem("Check for Updates");
		mnHelp.add(mntmCheckForUpdates);

		
		String ppallink = "https://www.paypal.com/cgi-bin/webscr" +
				"?cmd=" + "_donations" +
				"&business=" + "8YUJNSN6KFV54" +
				"&lc=" + "US" + 
				"&item_name=" + "Personal%20funds%20for%20programming%20at%20university" +
				"&currency_code=" + "USD" + 
				"&bn=" + "PP%2dDonationsBF" + "%3abtn_donateCC_LG%2egif%3aNonHosted";
		
		JButton btnDonate = new JButton("Donate");
		btnDonate.setToolTipText("<html>Open default Internet browser <br>(Chrome, FireFox, etc.)</html>");
		btnDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openWebPage(ppallink);
			}
		});
		btnDonate.setBounds(159, 176, 90, 25);
		menuBar.add(btnDonate);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFileSystemSearch.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// ---------------------------- JPanels ------------------------------
		JPanel panel = new JPanel();
		tabbedPane.addTab("Start Directory", null, panel, null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Search Setup", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Run Program", null, panel_2, null);
		panel_2.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Information", null, panel_3, null);
		panel_3.setBounds(10, 11, 409, 154);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_3.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 1.0, 1.0, 1.0 };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		// ------------- Dynamic Update - Swingworker Components -------------
		SentinelProgressBar = new JProgressBar();
		SentinelProgressBar.setMaximumSize(new Dimension(146, 14));
		SentinelProgressBar.setMaximum(1000);
		SentinelProgressBar.setBounds(74, 187, 146, 14);
		panel_2.add(SentinelProgressBar);

		SentinelProgressLabel = new JLabel();
		SentinelProgressLabel.setToolTipText("Time needed to finish the program in progress");
		SentinelProgressLabel.setBounds(333, 181, 86, 20);
		panel_2.add(SentinelProgressLabel);

		// -------------------------------------------------------------------
		// ------------------------ JPanel Components ------------------------
		JLabel lblCurrentDirectory = new JLabel("Current Directory");
		panel.add(lblCurrentDirectory);

		userSelectedDirectories = new JTextField();
		panel.add(userSelectedDirectories);
		userSelectedDirectories.setColumns(35);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setToolTipText("Locate directory in system");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFileChooserDir(btnBrowse);
			}
		});
		panel.add(btnBrowse);

		JButton btnClear = new JButton("Clear");
		btnClear.setToolTipText("Deletes the current directory shown");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userSelectedDirectories.setText("");
				clearSourceDirectory();
			}
		});
		panel.add(btnClear);
		
		// -------------------------------------------------------------------
		// ----------------------- JPanel_1 Components -----------------------
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Extension :: [Check Box]
		chckbxExt = new JCheckBox("Extension");
		chckbxExt.setToolTipText("Check to ignore file extensions");
		chckbxExt.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setUse("Ext", true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setUse("Ext", false);
			}
		});
		chckbxExt.setBounds(8, 15, 97, 23);
		panel_1.add(chckbxExt);
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Hash :: [Check Box]
		chckbxHash = new JCheckBox("Hash");
		chckbxHash.setToolTipText("Check to compare by hashing files (FAST)");
		chckbxHash.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setUse("Hash", true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setUse("Hash", false);
			}
		});
		chckbxHash.setBounds(8, 45, 97, 23);
		panel_1.add(chckbxHash);
		
		// Hash :: [Combo Box]
		comboBoxHash = new JComboBox<String>();
		comboBoxHash.setToolTipText("<html>Hash algorithm to use in file hashing <br>(note - SHA-512 may not be <br>supported by your computer)</html>");
		comboBoxHash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String UIselect = (String) cb.getSelectedItem();
				cf.setHashString(UIselect);
			}
		});
		comboBoxHash.setModel(new DefaultComboBoxModel<String>(new String[] {"MD5", "SHA-1", "SHA-256", "SHA-512"}));
		comboBoxHash.setBounds(150, 44, 75, 25);
		panel_1.add(comboBoxHash);
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Name :: [Check Box]
		chckbxName = new JCheckBox("Name");
		chckbxName.setToolTipText("Name tool-tip goes here");
		chckbxName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setUse("Name", true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setUse("Name", false);
			}
		});
		chckbxName.setBounds(8, 75, 97, 23);
		panel_1.add(chckbxName);
		
		// Name :: [Text Field]
		namePercentMatchTextField = new JFormattedTextField(new Double(0.0d));
		namePercentMatchTextField.setFormatterFactory(new DoubleDocListener(namePercentMatchTextField));
		namePercentMatchTextField.getDocument().addDocumentListener(new DoubleDocListener(namePercentMatchTextField));
		namePercentMatchTextField.setToolTipText("Match file names by percentage [00.00-100.00]");
		namePercentMatchTextField.setBounds(150, 74, 110, 25);
		panel_1.add(namePercentMatchTextField);
		namePercentMatchTextField.setColumns(10);
		
		// Name :: [Combo Box]
		comboBoxNames = new JComboBox<String>();
		comboBoxNames.setToolTipText("Algorithm to compare names");
		comboBoxNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String UIselect = (String) cb.getSelectedItem();
				cf.setMatchingAlgorithm(UIselect);
			}
		});
		comboBoxNames.setModel(new DefaultComboBoxModel<String>(new String[] { "Bitap", "Cosine",
				"DamerauLevenshtein", "DynamicTimeWarpingStandard1",
				"DynamicTimeWarpingStandard2", "Hamming", "Hirschberg",
				"JaccardIndex", "JaroWinkler", "Levenshtein", "NeedlemanWunsch",
				"SmithWaterman", "SorensenSimilarityIndex", "WagnerFischer" }));
		comboBoxNames.setBounds(265, 74, 150, 25);
		panel_1.add(comboBoxNames);
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Size :: [Check Box]
		chckbxSize = new JCheckBox("Size");
		chckbxSize.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setUse("Size", true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setUse("Size", false);
			}
		});
		chckbxSize.setBounds(8, 105, 97, 23);
		panel_1.add(chckbxSize);
		
		// Size :: [Text Field]
		sizePercentMatchTextField = new JFormattedTextField(new Double(0.0d));
		sizePercentMatchTextField.setFormatterFactory(new DoubleDocListener(sizePercentMatchTextField));
		sizePercentMatchTextField.getDocument().addDocumentListener(new DoubleDocListener(sizePercentMatchTextField));
		sizePercentMatchTextField.setToolTipText("Match file sizes by percentage [00.00-100.00]");
		sizePercentMatchTextField.setBounds(150, 104, 110, 25);
		panel_1.add(sizePercentMatchTextField);
		sizePercentMatchTextField.setColumns(10);
		
		// Size :: [Combo Box]
		comboBoxSize = new JComboBox<String>();
		comboBoxSize.setToolTipText("Specify byte grouping");
		comboBoxSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String UIselect = (String) cb.getSelectedItem();
				cf.setMetricSize(UIselect);
			}
		});
		comboBoxSize.setModel(new DefaultComboBoxModel<String>(new String[] {"BYTE", "KILOBYTE", "MEGABYTE", "GIGABYTE"}));
		comboBoxSize.setBounds(265, 104, 150, 25);
		panel_1.add(comboBoxSize);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Thorough :: [Check Box]
		chckbxThorough = new JCheckBox("Thorough");
		chckbxThorough.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setUse("Thorough", true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setUse("Thorough", false);
			}
		});
		chckbxThorough.setToolTipText("Check byte by byte");
		chckbxThorough.setBounds(8, 135, 97, 23);
		panel_1.add(chckbxThorough);
		
		// Thorough :: [Text Field]
		thoroughPercentMatchTextField = new JFormattedTextField(new Double(0.0d));
		thoroughPercentMatchTextField.setFormatterFactory(new DoubleDocListener(thoroughPercentMatchTextField));
		thoroughPercentMatchTextField.getDocument().addDocumentListener(new DoubleDocListener(thoroughPercentMatchTextField));
		thoroughPercentMatchTextField.setToolTipText("Match file sizes by percentage [00.00-100.00]");
		thoroughPercentMatchTextField.setBounds(150, 134, 110, 25);
		panel_1.add(thoroughPercentMatchTextField);
		thoroughPercentMatchTextField.setColumns(10);
		
		// Thorough :: [Check Box]
		chckbxExitFast = new JCheckBox("Exit Fast");
		chckbxExitFast.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					cf.setThoroughExitFirstByteMisMatch(true);
				else if (e.getStateChange() == ItemEvent.DESELECTED)
					cf.setThoroughExitFirstByteMisMatch(false);
			}
		});
		chckbxExitFast.setToolTipText("<html>When the first byte comparison <br>is a mis-match then stop</html>");
		chckbxExitFast.setBounds(265, 138, 105, 16);
		panel_1.add(chckbxExitFast);
		
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JButton btnSaveSearchSettings = new JButton("Save Search Settings");
		btnSaveSearchSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pollSearchSettings();
				cf.saveObject();
				// System.out.println(cf.toString()); // debug
			}
		});
		btnSaveSearchSettings.setBounds(292, 181, 140, 25);
		panel_1.add(btnSaveSearchSettings);
		
		JButton btnLoadSettings = new JButton("Load Settings");
		btnLoadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!cf.loadObject())
					cf.setDefaultSettings();
				cacheUpdateGUISettings();
			}
		});
		btnLoadSettings.setBounds(148, 181, 140, 25);
		panel_1.add(btnLoadSettings);
		
		JButton btnDefaultSettings = new JButton("Use Default Settings");
		btnDefaultSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cf.setDefaultSettings();
				cacheUpdateGUISettings();
			}
		});
		btnDefaultSettings.setBounds(4, 181, 140, 25);
		panel_1.add(btnDefaultSettings);
		

		// -------------------------------------------------------------------
		// ----------------------- JPanel_2 Components -----------------------
		JLabel labelStatus = new JLabel("Progress");
		labelStatus.setBounds(10, 187, 54, 14);
		panel_2.add(labelStatus);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pollSearchSettings();
				if (!ran_at_least_once)
					ran_at_least_once = true;
				else {
					SentinelProgressBar.setValue(0);
					SentinelProgressLabel.setText("Waiting...");
				}
				try {
					if (multiSelection == null) {
						JOptionPane.showMessageDialog(btnStart,
						    "Please enter at least one directory.",
						    "Press OK to continue",
						    JOptionPane.PLAIN_MESSAGE);
						return;
					}
					setSourceDirectory(multiSelection);
					GUI_Start = true;
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnStart.setBounds(40, 153, 89, 23);
		panel_2.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI_Stop = true;
			}
		});
		btnStop.setBounds(169, 153, 89, 23);
		panel_2.add(btnStop);

		JButton btnNewButton = new JButton("Pause");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI_Pause = true;
			}
		});
		btnNewButton.setBounds(298, 153, 89, 23);
		panel_2.add(btnNewButton);

		JCheckBox chckbxDeleteDuplicateFiles = new JCheckBox("Delete duplicate files");
		chckbxDeleteDuplicateFiles.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				deleteCopyResults = true;
			}
		});
		chckbxDeleteDuplicateFiles.setBounds(230, 10, 150, 16);
		panel_2.add(chckbxDeleteDuplicateFiles);

		JCheckBox chckbxLogDuplicateFiles = new JCheckBox("Save duplicates to file");
		chckbxLogDuplicateFiles.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				saveCopyResults = true;
			}
		});
		chckbxLogDuplicateFiles.setBounds(230, 36, 150, 16);
		panel_2.add(chckbxLogDuplicateFiles);
		
		// -------------------------------------------------------------------
		// ----------------------- JPanel_3 Components -----------------------

		JLabel lblAbout = new JLabel("About");
		GridBagConstraints gbc_lblAbout = new GridBagConstraints();
		gbc_lblAbout.insets = new Insets(0, 0, 5, 5);
		gbc_lblAbout.gridx = 0;
		gbc_lblAbout.gridy = 0;
		panel_3.add(lblAbout, gbc_lblAbout);

		JLabel lblTutorials = new JLabel("Tutorials");
		GridBagConstraints gbc_lblTutorials = new GridBagConstraints();
		gbc_lblTutorials.insets = new Insets(0, 0, 5, 5);
		gbc_lblTutorials.gridx = 1;
		gbc_lblTutorials.gridy = 0;
		panel_3.add(lblTutorials, gbc_lblTutorials);

		JLabel lblUpdates = new JLabel("Updates");
		GridBagConstraints gbc_lblUpdates = new GridBagConstraints();
		gbc_lblUpdates.insets = new Insets(0, 0, 5, 0);
		gbc_lblUpdates.gridx = 2;
		gbc_lblUpdates.gridy = 0;
		panel_3.add(lblUpdates, gbc_lblUpdates);

		JButton btnInformation = new JButton("Information");
		btnInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openWebPage(ppallink);
			}
		});
		GridBagConstraints gbc_btnInformation = new GridBagConstraints();
		gbc_btnInformation.insets = new Insets(0, 0, 0, 5);
		gbc_btnInformation.gridx = 0;
		gbc_btnInformation.gridy = 1;
		panel_3.add(btnInformation, gbc_btnInformation);

		JButton btnExamples = new JButton("Examples");
		btnExamples.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openWebPage(ppallink);
			}
		});
		GridBagConstraints gbc_btnExamples = new GridBagConstraints();
		gbc_btnExamples.insets = new Insets(0, 0, 0, 5);
		gbc_btnExamples.gridx = 1;
		gbc_btnExamples.gridy = 1;
		panel_3.add(btnExamples, gbc_btnExamples);

		JButton btnCheckNow = new JButton("Check Now");
		btnCheckNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openWebPage(ppallink);
			}
		});
		GridBagConstraints gbc_btnCheckNow = new GridBagConstraints();
		gbc_btnCheckNow.gridx = 2;
		gbc_btnCheckNow.gridy = 1;
		panel_3.add(btnCheckNow, gbc_btnCheckNow);
	}

	@Override
	protected void process(List<Integer> chunks) {
		int i = chunks.get(chunks.size() - 1);
		SentinelProgressBar.setValue(i);
		int s = MasterList.size();
		if (i == s)
			SentinelProgressLabel.setText("Done!");
		else
			SentinelProgressLabel.setText("" + i + " of " + LabelMaxSize);
	}

	@Override
	protected Void doInBackground() {
		while (ActiveGUI) {
			GUI_Stop = false;
			if (GUI_Pause)
				GUIPauseWork();
			if (GUI_Start) {
				try {
					generateList();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	protected void done() {
	}
	
	private void pollSearchSettings() {
		cf.setNamePercentageMatchDouble(Double.parseDouble(namePercentMatchTextField.getText()));
		cf.setSizePercentMatchDouble(Double.parseDouble(sizePercentMatchTextField.getText()));
		cf.setPercentMatchThorough(Double.parseDouble(thoroughPercentMatchTextField.getText()));
	}
	
	private void GUIPauseWork() {
		while (GUI_Pause) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (GUI_Start) {
				GUI_Pause = false;
				break;
			}
			if (GUI_Stop) {
				clearFields();
				GUI_Stop = false;
			}
		}
	}
	
	private void delStuff() {
		int totalSize = MasterList.size();
		LabelMaxSize = 2 * totalSize - 1;
		int progCounter = 0;
		
		SpecialFile[] list = new SpecialFile[totalSize];
		
		SentinelProgressLabel.setText("Analyzing file data");
		SentinelProgressBar.setValue(0);
		SentinelProgressBar.setMaximum( 2 * totalSize - 1);
		
		for (int i = 0; i < totalSize; i++) {
			list[i] = MasterList.get(i);
			list[i].setCompareFilter(cf);
			if (GUI_Stop || GUI_Pause) {
				GUI_Pause = true;
				if (GUI_Stop) {
					clearFields();
					GUI_Stop = false;
				}
				GUI_Start = false;
				break;
			}
			publish(++progCounter);
		}
		for (int i = 0; i < totalSize - 1; i++) {
			if (list[i].equals(list[i + 1])) {
				try {
					Files.delete(list[i].getPath());
				} catch (Exception e) {
				    e.printStackTrace();
				}
			}
			if (GUI_Stop || GUI_Pause) {
				GUI_Pause = true;
				if (GUI_Stop) {
					clearFields();
					GUI_Stop = false;
				}
				GUI_Start = false;
				break;
			}
			publish(++progCounter);
		}
	}
	
	private void generateList() throws IOException {
		MasterList.clear();
		SentinelProgressLabel.setText("Gathering files in directory");
		Collection<File> toConvert = new ArrayList<File>();
		for (String sDir : StartingDirectories) {
			toConvert.addAll(FileUtils.listFiles(new File(sDir), null, true));
		}
		SentinelProgressLabel.setText("Collecting file data");
		SentinelProgressBar.setMaximum(toConvert.size());
		int counter = 0;
		LabelMaxSize = MasterList.size();
		for (File sfFile : toConvert) {
			FileInputStream fis = new FileInputStream(sfFile);
			int fileSizeFromFIS = (int) fis.getChannel().size();
			SpecialFile sfConverted = null;
			if (cf.getUse("Thorough")) {
				sfConverted = new SpecialFile(Arrays.asList(
						ArrayUtils.toObject(IOUtils.toByteArray(fis))),	fileSizeFromFIS,
						Paths.get(sfFile.getPath()));
			} else {
				sfConverted = new SpecialFile(fileSizeFromFIS,
						Paths.get(sfFile.getPath()));
			}
			MasterList.add(sfConverted);
			publish(++counter);
			fis.close();
			if (GUI_Stop || GUI_Pause) {
				GUI_Pause = true;
				if (GUI_Stop) {
					clearFields();
					GUI_Stop = false;
				}
				GUI_Start = false;
				break;
			}
		}
		if (deleteCopyResults) {
			delStuff();
		} else if (saveCopyResults) {
			saveDuplicateResults();
		} else if (saveCopyResults && deleteCopyResults) {
			saveDuplicateResults();
			delStuff();
		}
		GUI_Start = false;
		GUI_Pause = true;
	}
	
	private void saveDuplicateResults() {
		Saver slf_save = new Saver();
		int totalSize = MasterList.size();
		LabelMaxSize = 2 * totalSize - 1;
		int progCounter = 0;
		
		SpecialFile[] list = new SpecialFile[totalSize];
		List<SpecialFile> cpyLst = new ArrayList<SpecialFile>();
		
		SentinelProgressLabel.setText("Analyzing file data");
		SentinelProgressBar.setValue(0);
		SentinelProgressBar.setMaximum( 2 * totalSize - 1);
		
		for (int i = 0; i < totalSize; i++) {
			list[i] = MasterList.get(i);
			list[i].setCompareFilter(cf);
			if (GUI_Stop || GUI_Pause) {
				GUI_Pause = true;
				if (GUI_Stop) {
					clearFields();
					GUI_Stop = false;
				}
				GUI_Start = false;
				break;
			}
			publish(++progCounter);
		}
		for (int i = 0; i < totalSize - 1; i++) {
			if (list[i].equals(list[i + 1]))
				cpyLst.add(list[i]);
			if (GUI_Stop || GUI_Pause) {
				GUI_Pause = true;
				if (GUI_Stop) {
					clearFields();
					GUI_Stop = false;
				}
				GUI_Start = false;
				break;
			}
			publish(++progCounter);
		}
		slf_save.writeDataToNewFile(cpyLst);
	}
	
	private void cacheUpdateGUISettings() {
		chckbxExt.setSelected(cf.getUse("Ext"));
		chckbxHash.setSelected(cf.getUse("Hash"));
		chckbxName.setSelected(cf.getUse("Name"));
		chckbxSize.setSelected(cf.getUse("Size"));
		chckbxThorough.setSelected(cf.getUse("Thorough"));
		chckbxExitFast.setSelected(cf.getThoroughExitFirstByteMisMatch());

		comboBoxHash.setSelectedItem(cf.getHashString());
		comboBoxNames.setSelectedItem(cf.getStringAlgorithm());
		comboBoxSize.setSelectedItem(cf.getMetricSize());
		
		namePercentMatchTextField.setValue(new Double(cf.getNamePercentageMatchDouble()));
		sizePercentMatchTextField.setValue(new Double(cf.getSizePercentMatchDouble()));
		thoroughPercentMatchTextField.setValue(new Double(cf.getPercentMatchThorough()));
	}
	
	private void openFileChooserDir(Component componentParent) {
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(componentParent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			multiSelection = new ArrayList<File>();
			multiSelection.addAll(Arrays.asList(chooser.getSelectedFiles()));
			String updateText = "";
			for (File f : multiSelection)
				updateText += "\"" + f.getPath() + "\", ";
			updateText = updateText.substring(0, updateText.lastIndexOf(','));
			userSelectedDirectories.setText(updateText);
		}
	}
	
	private void openWebPage(String urlName) {
		try {
			URL u = new URL(urlName);
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
				desktop.browse(u.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		}
	}
	
}


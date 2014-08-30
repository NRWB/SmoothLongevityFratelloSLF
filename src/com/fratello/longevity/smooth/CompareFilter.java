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

package com.fratello.longevity.smooth.catalog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

/*
 * Class Description: Used to store user-defined GUI values
 * into a Serialize-able CompareFilter object (given user
 * wants to save their settings for later use). Class simply
 * contains data field members, initialization of this object,
 * "getter" and "setter" methods, as well as serialization methods.
 */
public class CompareFilter implements Serializable {
	private static final long serialVersionUID = -2278671792786029416L;
	private boolean useExtension;
	private boolean useHash;
	private String nameHash;
	private boolean useName;
	private double percentageMatchName;
	private String matchAlgorithmName;
	private boolean useSize;
	private double percentageMatchSize;
	private String metricSize;
	private boolean useThorough;
	private double percentageMatchThorough;
	private boolean exitAfterFirstByteMisMatch;
	
	public CompareFilter() {
		initialize(false);
	}
	
	public void initialize(boolean reset) {
		useExtension = false;
		useHash = false;
		nameHash = "MD5";
		useName = false;
		percentageMatchName = 0.0d;
		matchAlgorithmName = "Levenshtein";
		useSize = false;
		percentageMatchSize = 0.0d;
		metricSize = "BYTE";
		useThorough = false;
		percentageMatchThorough = 0.0d;
		exitAfterFirstByteMisMatch = true;
	}
	
	public boolean getUse(String compType) {
		switch (compType) {
		case "Ext":
			return useExtension;
		case "Hash":
			return useHash;
		case "Name":
			return useName;
		case "Size":
			return useSize;
		case "Thorough":
			return useThorough;
		default:
			return false;
		}
	}
	
	public void setUse(String compType, boolean b) {
		switch (compType) {
		case "Ext":
			useExtension = b;
			break;
		case "Hash":
			useHash = b;
			break;
		case "Name":
			useName = b;
			break;
		case "Size":
			useSize = b;
			break;
		case "Thorough":
			useThorough = b;
			break;
		default:
			return;
		}
	}

	// -----------------------------------------------------------------------
	public String getHashString() {
		return nameHash;
	}
	
	public void setHashString(String s) {
		nameHash = s;
	}
	
	// -----------------------------------------------------------------------
	public double getNamePercentageMatchDouble() {
		return percentageMatchName;
	}
	
	public void setNamePercentageMatchDouble(double d) {
		percentageMatchName = d;
	}

	public String getStringAlgorithm() {
		return matchAlgorithmName;
	}
	
	public void setMatchingAlgorithm(String s) {
		matchAlgorithmName = s;
	}
	
	// -----------------------------------------------------------------------
	public String getMetricSize() {
		return metricSize;
	}
	
	public void setMetricSize(String s) {
		metricSize = s;
	}
	
	public double getSizePercentMatchDouble() {
		return percentageMatchSize;
	}
	
	public void setSizePercentMatchDouble(double v) {
		percentageMatchSize = v;
	}
	
	// -----------------------------------------------------------------------
	
	public double getPercentMatchThorough() {
		return percentageMatchThorough;
	}
	
	public void setPercentMatchThorough(double v) {
		percentageMatchThorough = v;
	}
	
	public boolean getThoroughExitFirstByteMisMatch() {
		return exitAfterFirstByteMisMatch;
	}
	
	public void setThoroughExitFirstByteMisMatch(boolean v) {
		exitAfterFirstByteMisMatch = v;
	}
	
	public void setCompareFilter(CompareFilter otherCF) {
		if (otherCF == null)
			return;
		setUse("Ext", otherCF.getUse("Ext"));
		setUse("Hash", otherCF.getUse("Hash"));
		setUse("Name", otherCF.getUse("Name"));
		setUse("Size", otherCF.getUse("Size"));
		setUse("Thorough", otherCF.getUse("Thorough"));
		setHashString(otherCF.getHashString());
		setNamePercentageMatchDouble(otherCF.getNamePercentageMatchDouble());
		setMatchingAlgorithm(otherCF.getStringAlgorithm());
		setMetricSize(otherCF.getMetricSize());
		setSizePercentMatchDouble(otherCF.getSizePercentMatchDouble());
		setPercentMatchThorough(otherCF.getPercentMatchThorough());
		setThoroughExitFirstByteMisMatch(otherCF.getThoroughExitFirstByteMisMatch());
	}
	
	public String toString() {
		return 	"Extension(use): " + (useExtension ? "true" : "false") + "\n" +
				
				"Hash(use/name): " + (useHash ? "true" : "false") +	"/" + nameHash + "\n" +
				
				"Name(use/percentage/algo-ordinal): " + (useName ? "true" : "false") +
				"/" + String.valueOf(percentageMatchName) + "/" + matchAlgorithmName + "\n" +
				
				"Size(use/percentage/metric): " + (useSize ? "true" : "false") + "/" +
				String.valueOf(percentageMatchSize) + "/" + metricSize + "\n" +
				
				"Thorough(use/percentage): " + (useThorough ? "true" : "false") + "/" +
				String.valueOf(percentageMatchThorough) + "/" + (exitAfterFirstByteMisMatch ? "true" : "false");
	}
	
	public void saveObject() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(new File(FileUtils.getTempDirectoryPath() + "NicksProgram"), "defaultsettings.ser")));
			out.writeObject(this);
			out.close();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
		}
	}
	
	public boolean loadObject() {
		try {
			FileInputStream door = new FileInputStream(new File(new File(FileUtils.getTempDirectoryPath() + "NicksProgram"), "defaultsettings.ser"));
			ObjectInputStream reader = new ObjectInputStream(door);
			CompareFilter x = new CompareFilter();
			try {
				x = (CompareFilter) reader.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			reader.close();
			if (x == null)
				return false;
			setCompareFilter(x);
		} catch (IOException e) {
		}
		return true;
	}
	
	public void setDefaultSettings() {
		setUse("Ext", false);
		setUse("Hash", false);
		setUse("Name", false);
		setUse("Size", true);
		setUse("Thorough", false);
		setHashString("MD5");
		setNamePercentageMatchDouble(0.0d);
		setMatchingAlgorithm("Levenshtein");
		setMetricSize("BYTE");
		setSizePercentMatchDouble(75.0d);
		setPercentMatchThorough(0.0d);
		setThoroughExitFirstByteMisMatch(true);
	}
}

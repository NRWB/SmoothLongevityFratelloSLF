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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

/*
 * Class Description: Used to access the system temp directory.
 * Produces input and output assistance.
 */
public class Saver {
	private final static String folderName = "NicksProgram";
	private final static String tempFolder = FileUtils.getTempDirectoryPath();
	
	// -----------------------------------------------------------------------
	private boolean programTempFolderExists() {
		return Files.exists(Paths.get(tempFolder + folderName));
	}
	
	// -----------------------------------------------------------------------
	private String getProgramFolderInTempFolder() {
		return (tempFolder + folderName);
	}
	
	// -----------------------------------------------------------------------
	private boolean createNewTempFolder() {
		if (programTempFolderExists())
			return true;
		File file = new File(getProgramFolderInTempFolder());
		return file.mkdir();
	}
	
	// -----------------------------------------------------------------------
	private String getFileToWriteName() {
		DateFormat dateFormat = new SimpleDateFormat("hh mm ss SSS aa z - MMM dd yyyy G");
		Date date = new Date();
		return "output" + "[" + dateFormat.format(date) + "].txt";
	}
	
	// -----------------------------------------------------------------------
	private String getEligibleOutputFolder() {
		if (programTempFolderExists() || createNewTempFolder())
			return getProgramFolderInTempFolder();
		return FileUtils.getTempDirectoryPath();
	}
	
	// -----------------------------------------------------------------------
	protected void writeDataToNewFile(Collection<?> input) {
		File toWrite = null;
		BufferedWriter writer = null;
		try {
			File dir = new File(getEligibleOutputFolder());
			if (!dir.exists())
				dir.mkdir();
			toWrite = new File(dir, getFileToWriteName());
			if (!toWrite.exists())
				toWrite.createNewFile();
			writer = new BufferedWriter(new FileWriter(toWrite));
			Iterator<?> it = input.iterator();
			while (it.hasNext())
				writer.write(it.next().toString() + System.getProperty("line.separator"));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

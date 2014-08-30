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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.fratello.longevity.smooth.catalog.CompareFilter;
import com.fratello.longevity.smooth.object.SpecialFile;

/*
 * Class Description: Namely used to hold file-related
 * processes that related to the AppGUI class.
 */
public class Sentinel extends SwingWorker<Void, Integer> {

	protected List<SpecialFile> MasterList;
	protected List<String> StartingDirectories;
	protected boolean saveCopyResults;
	protected boolean deleteCopyResults;
	protected CompareFilter cf;

	protected void setSourceDirectory(List<File> folders) {
		clearSourceDirectory();
		List<File> result = new ArrayList<File>();
		if (folders.size() > 0)
			result.addAll(folders);
		for (File f : result)
			StartingDirectories.add(f.getPath());
	}

	protected void clearSourceDirectory() {
		StartingDirectories.clear();
	}
	
	protected void initializeFields() {
		StartingDirectories = new ArrayList<String>();
		MasterList = new ArrayList<SpecialFile>();
		saveCopyResults = false;
		deleteCopyResults = false;
		cf = new CompareFilter();
	}
	
	protected void clearFields() {
		try {
			MasterList.clear();
			StartingDirectories.clear();
			saveCopyResults = false;
			deleteCopyResults = false;
			cf.initialize(true);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Override
	protected Void doInBackground() {
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {
	}

	@Override
	protected void done() {
	}
}

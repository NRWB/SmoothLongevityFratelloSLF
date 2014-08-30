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

package com.fratello.longevity.smooth.object;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fratello.longevity.smooth.algorithm.StringDistance;
import com.fratello.longevity.smooth.catalog.CompareFilter;

/*
 * Class Description: This object holds traits of 
 * files, in order to use in compare file equality.
 */
public class SpecialFile {
	
	/*
	 * Fields
	 */
	
	// Storage for potential byte-level comparison
	private List<Byte> BYTES;
	
	// Stores the size of the file
	private long SIZE;
	
	// Stores the path of the file
	private Path PATH;
	
	// Stores the compare filter options when
	// comparing with another SpecialFile object
	private CompareFilter filter;

	/*
	 * SpecialFile()
	 * Default constructor
	 */
	public SpecialFile() {
		this.BYTES = null;
		this.SIZE = 0;
		this.PATH = null;
		this.filter = new CompareFilter();
		this.filter.initialize(false);
	}

	/*
	 * SpecialFile(long fileSize, Path filePath)
	 * Constructor with two data fields to create
	 */
	public SpecialFile(long fileSize, Path filePath) {
		this.BYTES = null;
		this.SIZE = fileSize;
		this.PATH = filePath;
		this.filter = new CompareFilter();
		this.filter.initialize(false);
	}
	
	/*
	 * SpecialFile(List<Byte> list, long fileSize, Path filePath)
	 * Constructor with three data fields to create
	 */
	public SpecialFile(List<Byte> list, long fileSize, Path filePath) {
		this.BYTES = list;
		this.SIZE = fileSize;
		this.PATH = filePath;
		this.filter = new CompareFilter();
		this.filter.initialize(false);
	}
	
	/*
	 * getPath()
	 * Return the Path object data field member
	 */
	public Path getPath() {
		return this.PATH;
	}
	
	/*
	 * toString()
	 * Overridden toString method, used to produce verbose file output
	 */
	public String toString() {
		String result = "File Path = " + this.PATH + " - Size = " + this.SIZE;
		if (this.BYTES != null)
			result += " - Byte Count = " + this.BYTES.size();
		return result;
	}
	
	/*
	 * hashCode()
	 * Overridden hashCode method
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(2003, 2011).hashCode();
	}
	
	/*
	 * equals(Object o)
	 * Overridden equals method, utilizes the CompareFilter object
	 * to compare this SpecialFile with another SpecialFile "Object o"
	 * Returns false when two objects are not equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SpecialFile) {
			SpecialFile toCompare = (SpecialFile) o;
			if (this.filter == null)
				return false;

			if (this.filter.getUse("Ext") &&
					(!testFilterProperty_Extension(toCompare)))
					return false;

			if (this.filter.getUse("Hash") &&
					(!testFilterProperty_Hash(toCompare)))
					return false;

			if (this.filter.getUse("Name") &&
				   (!testFilterProperty_Name(toCompare)))
					return false;

			if (this.filter.getUse("Size") &&
				   (!testFilterProperty_Size(toCompare)))
					return false;

			if (this.filter.getUse("Thorough") &&
				   (!testFilterProperty_Thorough(toCompare)))
					return false;
		}
		// Return only when all in-use comparison fields
		// fail to return false
		return true;
	}
	
	/*
	 * setCompareFilter(CompareFilter c)
	 * Sets this SpecialFile object's CompareFilter to
	 * the Object given in the parameter "CompareFilter c"
	 */
	public void setCompareFilter(CompareFilter c) {
		this.filter.setCompareFilter(c);
	}
	
	/*
	 * testFilterProperty_Extension(SpecialFile other)
	 * Reads in this object's extension as a String,
	 * in order to compare it with the other specified
	 * SpecialFile object given in the parameters.
	 * Returns true is both extensions/strings match/equal.
	 */
	private boolean testFilterProperty_Extension(SpecialFile other) {
		String ext1 = FilenameUtils.getExtension(this.PATH.toString());
		String ext2 = FilenameUtils.getExtension(other.PATH.toString());
		if (!ext1.equals(ext2))
			return false;
		return true;
	}
	
	/*
	 * testFilterProperty_Hash(SpecialFile other)
	 * Takes 1 or 4 options for a hash algorithm name.
	 * Will attempt to compare the byte-output of the
	 * two files after hashed.
	 * Returns true if both hashes match.
	 */
	private boolean testFilterProperty_Hash(SpecialFile other) {
		String srcHashAlgo = this.filter.getHashString();
		String[] possibilties = new String[] {"MD5", "SHA-1", "SHA-256", "SHA-512"};
		boolean goodToContinue = false;
		for (String s : possibilties)
			if (s.compareTo(srcHashAlgo) == 0) {
				goodToContinue = true;
				break;
			}
		if (!goodToContinue)
			return false;
		try {
			MessageDigest md1 = MessageDigest.getInstance(srcHashAlgo);
			MessageDigest md2 = MessageDigest.getInstance(srcHashAlgo);
			if (!Arrays.equals(md1.digest(Files.readAllBytes(this.PATH)),
							  md2.digest(Files.readAllBytes(other.PATH)))) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/*
	 * testFilterProperty_Name(SpecialFile other)
	 * Takes an algorithm to use in comparing two file names.
	 * Uses predefined match percentage, represented
	 * as a double (limited from 0.0-100.0) in the CompareFilter
	 * object, and returns true if the algorithm's return value
	 * is greater than or equal to the CompareFilter's set percentage.
	 */
	private boolean testFilterProperty_Name(SpecialFile other) {
		String algoName = this.filter.getStringAlgorithm();
		
		String sfName1 = FilenameUtils.getBaseName(this.PATH.toString());
		String sfName2 = FilenameUtils.getBaseName(other.PATH.toString());
		
		double percentFromAlgo = 0.0d;
		
		List<Character> list1 = new ArrayList<Character>();
		List<Character> list2 = new ArrayList<Character>();
		
		for (int i = 0; i < sfName1.length(); i++)
			list1.add(sfName1.charAt(i));
		for (int i = 0; i < sfName2.length(); i++)
			list2.add(sfName2.charAt(i));
		
		switch (algoName) {
		case "Bitap":
			percentFromAlgo = StringDistance.Bitap(sfName1, sfName2, 0);
			break;
		case "Cosine":
			percentFromAlgo = StringDistance.CosineSimilarity(list1, list2);
			break;
		case "DamerauLevenshtein":
			percentFromAlgo = StringDistance.DamerauLevenshtein(sfName1, sfName2);
			break;
		case "DynamicTimeWarpingStandard1":
			percentFromAlgo = StringDistance.DynamicTimeWarpingDistance(sfName1, sfName2);
			break;
		case "DynamicTimeWarpingStandard2":
			percentFromAlgo = StringDistance.DynamicTimeWarpingDistance(sfName1, sfName2, Math.max(sfName1.length(), sfName2.length()));
			break;
		case "Hamming":
			percentFromAlgo = StringDistance.HammingDistance(sfName1, sfName2);
			break;
		case "Hirschberg":
			break;
		case "JaccardIndex":
			percentFromAlgo = StringDistance.JaccardCoefficient(list1, list2);
			break;
		case "JaroWinkler":
			percentFromAlgo = StringDistance.JaroWinkler(sfName1, sfName2);
			break;
		default:
		case "Levenshtein":
			percentFromAlgo = StringDistance.Levenshtein(sfName1, sfName2);
			break;
		case "NeedlemanWunsch":
			percentFromAlgo = StringDistance.NeedlemanWunsch(sfName1, sfName2);
			break;
		case "SmithWaterman":
			percentFromAlgo = StringDistance.SmithWaterman(sfName1, sfName2);
			break;
		case "SorensenSimilarityIndex":
			percentFromAlgo = StringDistance.SorensenSimilarityIndex(list1, list2);
			break;
		case "WagnerFischer":
			percentFromAlgo = StringDistance.WagnerFischer(sfName1, sfName2);
			break;
		}
		return percentFromAlgo >= this.filter.getNamePercentageMatchDouble();
	}
	
	/*
	 * testFilterProperty_Size(SpecialFile other)
	 * checks whether or not two SpecialFile's SIZE data field member
	 * is within a given amount of bytes, in one of four possible
	 * byte-orders (1. (default) BYTE, 2. KILO-, 3. MEGA-, 4.GIGA-)
	 * Returns true if both values are within a percentage of bytes
	 * from one another.
	 */
	private boolean testFilterProperty_Size(SpecialFile other) {
		String byteType = this.filter.getMetricSize();
		BigInteger sourceSize = new BigInteger(String.valueOf(this.SIZE));
		BigInteger targetSize = new BigInteger(String.valueOf(other.SIZE));
		String num = "";
		switch (byteType) {
		default:
		case "BYTE":
			num = "1";
			break;
		case "KILOBYTE":
			num = "1024";
			break;
		case "MEGABYTE":
			num = "1048576";
			break;
		case "GIGABYTE":
			num = "1073741824";
			break;
		}
		sourceSize.multiply(new BigInteger(String.valueOf(num)));
		targetSize.multiply(new BigInteger(String.valueOf(num)));
		boolean sourceSizeIsBiggest = sourceSize.compareTo(targetSize) > 0;
		double requiredPercent = this.filter.getSizePercentMatchDouble();
		if (sourceSizeIsBiggest) {
			double matchPercent = targetSize.divide(sourceSize).doubleValue();
			if (!(matchPercent >= requiredPercent))
				return false;
		} else {
			double matchPercent = sourceSize.divide(targetSize).doubleValue();
			if (!(matchPercent >= requiredPercent))
				return false;
		}
		return true;
	}
	
	/*
	 * testFilterProperty_Thorough(SpecialFile other)
	 * Compares two lists of bytes against each other.
	 * Returns true if percentage computed is greater
	 * than or equal to the CompareFilter's predefined
	 * value.
	 */
	private boolean testFilterProperty_Thorough(SpecialFile other) {
		List<Byte> one = new ArrayList<Byte>(this.BYTES);
		List<Byte> two = new ArrayList<Byte>(other.BYTES);
		double matchCount = 0.0d;
		double totalCount = 0.0d;
		if (one.size() > two.size()) {
			double first = two.size();
			double second = one.size();
			totalCount = first / second;
		} else {
			double first = one.size();
			double second = two.size();
			totalCount = first / second;
		}
		Iterator<Byte> it1 = one.iterator();
		Iterator<Byte> it2 = two.iterator();
		boolean exitfast = this.filter.getThoroughExitFirstByteMisMatch();
		while (it1.hasNext() && it2.hasNext()) {
			if (it1.next() == it2.next()) {
				matchCount += 1.0d;
				continue;
			}
			if (exitfast)
				return false;
		}
		
		if (!((matchCount / totalCount) >= this.filter.getPercentMatchThorough()))
			return false;
		return true;
	}
}

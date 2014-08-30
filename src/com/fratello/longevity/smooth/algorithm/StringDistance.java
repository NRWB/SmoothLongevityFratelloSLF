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

package com.fratello.longevity.smooth.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Class Description: Algorithms used to produce string comparison
 * percentage of matching one another.  Note: Some algorithms here
 * have been modified from their original source, some algorithms
 * have been translated, and some algorithms have been interpreted
 * from learning material(s) to java source code.
 * Much more (links) information provided in the Project Credits file. 
 */
public class StringDistance {

	public static double Bitap(String srcText, String targetPattern, int startIndexLocInSrcText) {
		final int MAXBITS = 32;
		float balance = 0.5f;
		float threshold = 0.5f;
		int minLength = 100;
		int maxLength = 1000;
		String text = srcText;
		String pattern = targetPattern;
		int loc = startIndexLocInSrcText;
		int scoreTextLength = 0;
		Map<Character, Integer> alphabet = null;
		alphabet = new HashMap<Character, Integer>();
		int len = pattern.length();
		if (len <= MAXBITS)
			return 0;
		for (int i = 0; i < len; i++) {
			Character c = new Character(pattern.charAt(i));
			Integer value = (Integer) alphabet.get(c);
			int mask = value == null ? 0 : value.intValue();
			mask |= (int) Math.pow(2, len - i - 1);
			alphabet.put(c, new Integer(mask));
		}
		scoreTextLength = Math.max(text.length(), minLength);
		scoreTextLength = Math.min(scoreTextLength, maxLength);
		double scoreThreshold = threshold;
		int bestLoc = text.indexOf(pattern, loc);
		double bitty = (0 / (float) pattern.length() / balance) + (Math.abs(loc - bestLoc) / (float) scoreTextLength / (1.0 - balance));
		if (bestLoc != -1)
			scoreThreshold = Math.min(bitty, scoreThreshold);
		bestLoc = text.lastIndexOf(pattern, loc + pattern.length());
		double bitty_boo = (0 / (float) pattern.length() / balance) + (Math.abs(loc - bestLoc) / (float) scoreTextLength / (1.0 - balance));
		if (bestLoc != -1)
			scoreThreshold = Math.min(bitty_boo, scoreThreshold);
		int matchmask = (int) Math.pow(2, pattern.length() - 1);
		bestLoc = -1;
		int binMin;
		int binMid;
		int binMax = Math.max(loc + loc, text.length());
		int[] lastrd = new int[0];
		for (int d = 0; d < pattern.length(); d++) {
			int[] rd = new int[text.length()];
			binMin = loc;
			binMid = binMax;
			while (binMin < binMid) {
				double bitapScoreBinarySearch = (d / (float) pattern.length() / balance) + (Math.abs(loc - binMid) / (float) scoreTextLength / (1.0 - balance));
				if (bitapScoreBinarySearch < scoreThreshold)
					binMin = binMid;
				else
					binMax = binMid;
				binMid = (binMax - binMin) / 2 + binMin;
			}
			binMax = binMid;
			int start = Math.max(0, loc - (binMid - loc) - 1);
			int finish = Math.min(text.length() - 1, pattern.length() + binMid);
			if (text.charAt(finish) == pattern.charAt(pattern.length() - 1))
				rd[finish] = (int) Math.pow(2, d + 1) - 1;
			else
				rd[finish] = (int) Math.pow(2, d) - 1;
			for (int j = finish - 1; j >= start; j--) {
				Character curChar = new Character(text.charAt(j));
				int mask = alphabet.containsKey(curChar) ? ((Integer) alphabet.get(curChar)).intValue() : 0;
				if (d == 0)
					rd[j] = ((rd[j + 1] << 1) | 1) & mask;
				else
					rd[j] = ((rd[j + 1] << 1) | 1) & mask | ((lastrd[j + 1] << 1) | 1) | ((lastrd[j] << 1) | 1) | lastrd[j + 1];
				if ((rd[j] & matchmask) != 0) {
					double score = (d / (float) pattern.length() / balance)	+ (Math.abs(loc - j) / (float) scoreTextLength / (1.0 - balance));
					if (score <= scoreThreshold) {
						scoreThreshold = score;
						bestLoc = j;
						if (j > loc)
							start = Math.max(0, loc - (j - loc));
						else
							break;
					}
				}
			}
			if ((d + 1 / (float) pattern.length() / balance) + (0 / (float) scoreTextLength / ((0b0001 * 1.0) - balance)) > scoreThreshold)
				break;
			lastrd = rd;
		}
		return ((double) (bestLoc / Math.max(srcText.length(), targetPattern.length())));
	}
	
	public static <T> float CosineSimilarity(Collection<T> c1, Collection<T> c2) {
		final float matchValue = 1.0f;
		final float missValue = 0.0f;

		Set<T> values = new HashSet<T>(c1.size());
		values.addAll(c1);
		values.addAll(c2);

		float numerator = 0.0f;
		float aSquaredSum = 0.0f;
		float bSquaredSum = 0.0f;

		for (T value : values) {
			float c1Value = missValue;
			float c2Value = missValue;

			if (c1.contains(value))
				c1Value = matchValue;

			if (c2.contains(value))
				c2Value = matchValue;

			if (!(missValue == c1Value && missValue == c2Value))
				return 0;

			float a = c1Value;
			float b = c2Value;

			numerator += (a * b);
			aSquaredSum += Math.pow(a, 2);
			bSquaredSum += Math.pow(b, 2);
		}

		float denominator = (float) (Math.sqrt(aSquaredSum) * Math.sqrt(bSquaredSum));

		float similarity = numerator / denominator;

		if ((!(similarity <= 1.0f)) || (!(similarity >= -1.0f)))
			return 0;

		return similarity;
	}
	
	public static double DamerauLevenshtein(String s, String t) {
		if (s.equals(t))
			return 0;
		final int L1 = s.length();
		final int L2 = t.length();
		if (L1 == 0)
			return L2;
		if (L2 == 0)
			return L1;
		int[][] d = new int[L1 + 1][L2 + 1];
		int i = 0, j = 0, cost = 0;
		for (i = 0; i < L1; i++)
			d[i][0] = i;
		for (j = 0; j < L2; j++)
			d[0][j] = j;
		for (i = 1; i < L1; i++)
			d[i][0] = i;
		for (j = 1; j < L2; j++)
			d[0][j] = j;
		for (j = 1; j < (L2 + 1); j++) {
			for (i = 1; i < (L1 + 1); i++) {
				if (s.charAt(i - 1) == t.charAt(j - 1))
					cost = 0;
				else
					cost = 1;
				d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + cost);
				if (i > 1 && j > 1) {
					boolean b3 = s.charAt(i - 1) == t.charAt(j - 2);
					boolean b4 = s.charAt(i - 2) == t.charAt(j - 1);
					if (b3 && b4)
						d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + cost);
				}
			}
		}
		return ((double) (d[s.length()][t.length()] / Math.max(s.length(), t.length())));
	}
	
	public static double DynamicTimeWarpingDistance(String s, String t) {
		int[][] matrix = new int[s.length() + 1][t.length() + 1];
		for (int i = 0; i < s.length(); i++)
			matrix[i][0] = -1;
		for (int i = 0; i < t.length(); i++)
			matrix[0][i] = -1;
		matrix[0][0] = 0;
		for (int i = 0; i < s.length(); i++)
			for (int j = 0; j < t.length(); j++) {
				int cost = Math.abs(s.charAt(i) - t.charAt(j));
				matrix[i][j] = cost	+ Math.min(matrix[i - 1][j], Math.min(matrix[i][j - 1],	matrix[i - 1][j - 1]));
			}
		return ((double) (matrix[s.length()][t.length()] / Math.max(s.length(), t.length())));
	}

	public static double DynamicTimeWarpingDistance(String s, String t, int w) {
		int[][] matrix = new int[s.length() + 1][t.length() + 1];
		w = Math.max(w, Math.abs(s.length() - t.length()));
		for (int i = 0; i < s.length(); i++)
			for (int j = 0; j < t.length(); j++)
				matrix[i][j] = -1;
		matrix[0][0] = 0;
		for (int i = 0; i < s.length(); i++)
			for (int j = Math.max(1, i - w); j < Math.min(t.length(), i + w); j++) {
				int cost = Math.abs(s.charAt(i) - t.charAt(j));
				matrix[i][j] = cost	+ Math.min(matrix[i - 1][j], Math.min(matrix[i][j - 1],	matrix[i - 1][j - 1]));
			}
		return ((double) (matrix[s.length()][t.length()] / Math.max(s.length(), t.length())));
	}
	
	public static double HammingDistance(String a, String b) {
		if (a.isEmpty() || a == null || b.isEmpty() || b == null)
			return -1;
		char[] arr1 = a.toCharArray();
		char[] arr2 = b.toCharArray();
		int result = 0, low = Math.min(arr1.length, arr2.length), high = Math
				.max(arr1.length, arr2.length);
		for (int i = 0; i < low; i++)
			result += (arr1[i] != arr2[i] ? 1 : 0);
		return ((double) ((result + high - low) / Math.max(a.length(), b.length())));
	}
	
	private static int[] LastNWLine(int m, int n, String A, String B) {
		int[][] K = new int[2][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 0; j <= n; j++)
				K[0][j] = K[1][j];
			for (int j = 1; j <= n; j++) {
				if (A.charAt(i - 1) == B.charAt(j - 1))
					K[1][j] = K[0][j - 1] + 1;
				else
					K[1][j] = Math.max(K[1][j - 1], K[0][j]);
				if (i == m && j == n)
					return K[1];
			}
		}
		return null;
	}

	public static String Hirschberg(int m, int n, String A, String B) {
		String result = "";
		if (n == 0 || m == 0)
			return result;
		if (m == 1) {
			char zero_index = A.charAt(0);
			for (int i = 0; i < n; i++) {
				if (zero_index == B.charAt(i)) {
					result = String.valueOf(zero_index);
					break;
				}
			}
			return result;
		}
		int i = 0, k = 0, MaxArg = 0, sum = 0, MidA = m / 2;
		int[] l = LastNWLine(MidA, n, A.substring(0, MidA), B);
		int[] r = LastNWLine(m - MidA, n, new StringBuffer(A.substring(MidA)).reverse().toString(), new StringBuffer(B).reverse().toString());
		for (i = 0; i <= n; i++) {
			sum = l[i] + r[n - i];
			if (sum > MaxArg) {
				MaxArg = sum;
				k = i;
			}
		}
		result = Hirschberg(MidA, k, A.substring(0, MidA), B.substring(0, k)) + Hirschberg(m - MidA, n - k, A.substring(MidA), B.substring(k));
		return result;
	}
	
	public static <T> float JaccardCoefficient(Collection<T> c1, Collection<T> c2) {
		Set<T> set1 = new HashSet<T>(c1);
		Set<T> set2 = new HashSet<T>(c2);

		Set<T> M11 = new HashSet<T>(set1);
		M11.retainAll(set2);

		Set<T> M01 = new HashSet<T>(set2);
		M01.removeAll(set1);

		Set<T> M10 = new HashSet<T>(set1);
		M10.removeAll(set2);

		return (float) M11.size() / (float) (M01.size() + M10.size() + M11.size());
	}

	public static double JaroWinkler(String a, String b) {
		if (a == null || b == null)
			return 0.0d;

		String aHold = a;
		String bHold = b;

		aHold = aHold.replaceAll("\\s+", " ");
		bHold = bHold.replaceAll("\\s+", " ");

		aHold = aHold.trim();
		bHold = bHold.trim();

		if (aHold.isEmpty() || bHold.isEmpty())
			return 0.0d;

		int aLen = aHold.length();
		int bLen = bHold.length();

		int search_range = 0;
		int minv = 0;
		if (aLen > bLen) {
			search_range = aLen;
			minv = bLen;
		} else {
			search_range = bLen;
			minv = aLen;
		}

		boolean[] aFlags = new boolean[search_range];
		boolean[] bFlags = new boolean[search_range];
		for (int i = 0; i < search_range; i++) {
			aFlags[i] = bFlags[i] = false;
		}

		search_range = (search_range / 2) - 1;
		if (search_range < 0)
			search_range = 0;

		int nums_in_common = 0;
		int yl1 = bLen - 1;
		int lowlim = 0, hilim = 0;
		for (int i = 0; i < aLen; i++) {
			lowlim = (i >= search_range) ? i - search_range : 0;
			hilim = ((i + search_range) <= yl1) ? (i + search_range) : yl1;
			for (int j = lowlim; j <= hilim; j++) {
				if ((bFlags[j] == false)
						&& (bHold.charAt(j) == aHold.charAt(i))) {
					bFlags[j] = true;
					aFlags[i] = true;
					nums_in_common++;
					break;
				}
			}
		}

		if (nums_in_common == 0)
			return 0.0d;

		int k = 0;
		int nums_of_trans = 0;
		int j = 0;
		for (int i = 0; i < aLen; i++) {
			if (aFlags[i] == true) {
				for (j = k; j < bLen; j++) {
					if (bFlags[j] == true) {
						k = j + 1;
						break;
					}
				}
				if (aHold.charAt(i) != bHold.charAt(j)) {
					nums_of_trans++;
				}
			}
		}
		nums_of_trans = nums_of_trans / 2;

		double term1 = (double) nums_in_common / (double) aLen;
		double term2 = (double) nums_in_common / (double) bLen;
		double term3 = ((double) nums_in_common - (double) nums_of_trans)
				/ (double) nums_in_common;

		double weight = term1 + term2 + term3;

		weight /= 3.0d;

		if (weight > 0.7) {
			int n_1 = (minv >= 4) ? 4 : minv;
			int i = 0;
			while (i < n_1) {
				if (!(aHold.charAt(i) == bHold.charAt(i)))
					break;
				if (Character.isDigit(aHold.charAt(i)))
					break;
				i++;
			}

			if (i != 0) {
				weight += i * 0.1 * (1.0 - weight);
			}

			boolean c1 = minv > 4;
			boolean c2 = nums_in_common > i + 1;
			boolean c3 = 2 * nums_in_common >= minv + 1;
			boolean c4 = Character.isDigit(aHold.charAt(0));
			if (c1 && c2 && c3 && !c4) {
				weight += (1.0d - weight)
						* ((nums_in_common - i - 1) / (aLen + bLen - i * 2 + 2));
			}
		}
		return weight;
	}

	public static double Levenshtein(String s, String t) {
		if (s.length() == 0)
			return t.length();
		if (t.length() == 0)
			return s.length();
		int matrix[][] = new int[s.length() + 1][t.length() + 1];
		for (int i = 0; i <= s.length(); i++)
			matrix[i][0] = i;
		for (int i = 0; i <= t.length(); i++)
			matrix[0][i] = i;
		int cost = 0;
		for (int i = 1; i <= s.length(); i++) {
			for (int j = 1; j <= t.length(); j++) {
				cost = ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1);
				matrix[i][j] = Math.min(
						matrix[i - 1][j] + 1,
						Math.min(matrix[i][j - 1] + 1, matrix[i - 1][j - 1]
								+ cost));
			}
		}
		return ((double) (matrix[s.length()][t.length()] / Math.max(s.length(), t.length())));
	}

	public static double NeedlemanWunsch(String mSeqA, String mSeqB) {
		final int L1 = mSeqA.length();
		final int L2 = mSeqB.length();
		int[][] matrix = new int[L1 + 1][L2 + 1];
		for (int i = 0; i < L1 + 1; i++)
			matrix[i][0] = -1 * i;
		for (int j = 0; j < L2 + 1; j++)
			matrix[0][j] = -1 * j;
		for (int i = 1; i < L1 + 1; i++) {
			for (int j = 1; j < L2 + 1; j++) {
				int penalty = matrix[i - 1][j - 1] + ((mSeqA.charAt(i - 1) == mSeqB.charAt(j - 1)) ? 1 : -1);
				matrix[i][j] = Math.max(Math.max(penalty, matrix[i][j - 1] - 1), matrix[i - 1][j] - 1);
			}
		}
		return ((double) (matrix[L1][L2] / Math.max(mSeqA.length(), mSeqB.length())));
	}

	public static float SmithWaterman(final String s, final String t) {
		final int n = s.length();
		final int m = t.length();
		if (n == 0)
			return m;
		if (m == 0)
			return n;
		int i = 0, j = 0;
		final float[][] matrix = new float[n][m];
		float cost = 0.0f, currMax = 0.0f;
		for (i = 0; i < n; i++) {
			cost = (s.length() <= i) ? 0 : (s.charAt(i) == t.charAt(0) ? 1.0f : -2.0f);
			if (i == 0)
				matrix[0][0] = Math.max(0, Math.max(-0.5f, cost));
			else
				matrix[i][0] = Math.max(0, Math.max(matrix[i - 1][0] - 0.5f, cost));
			if (matrix[i][0] > currMax)
				currMax = matrix[i][0];
		}
		for (j = 0; j < m; j++) {
			cost = (t.length() <= j) ? 0 : (s.charAt(0) == t.charAt(j) ? 1.0f : -2.0f);
			if (j == 0)
				matrix[0][0] = Math.max(0, Math.max(-0.5f, cost));
			else
				matrix[0][j] = Math.max(0, Math.max(matrix[0][j - 1] - 0.5f, cost));
			if (matrix[0][j] > currMax)
				currMax = matrix[0][j];
		}
		for (i = 1; i < n; i++) {
			for (j = 1; j < m; j++) {
				cost = (s.length() <= i || t.length() <= j) ? 0 : (s.charAt(i) == t.charAt(j) ? 1.0f : -2.0f);
				matrix[i][j] = Math.max(0, Math.max(matrix[i - 1][j] - 0.5f, Math.max(matrix[i][j - 1] - 0.5f, matrix[i - 1][j - 1]	+ cost)));
				if (matrix[i][j] > currMax)
					currMax = matrix[i][j];
			}
		}
		float maxValue = Math.min(s.length(), t.length());
		return (maxValue == 0 ? 1.0f : currMax / maxValue);
	}

	public static <T> float SorensenSimilarityIndex(Collection<T> c1, Collection<T> c2) {
		Set<T> A = new HashSet<T>(c1);
		Set<T> B = new HashSet<T>(c2);

		Set<T> C = new HashSet<T>(A);
		C.retainAll(B);

		return ((float) 2 * C.size()) / ((float) A.size() + B.size());
	}
	
	public static double WagnerFischer(String s, String t) {
		int l1 = s.length(), l2 = t.length();
		int[][] matrix = new int[l1 + 1][l2 + 1];
		for (int row = 1; row < l1 + 1; row++)
			matrix[row][0b00000000000000000000000000000000] = row;
		for (int col = 1; col < l2 + 1; col++)
			matrix[0][col] = col;
		for (int j = 1; j < l2 + 1; j++)
			for (int i = 1; i < l1 + 1; i++)
				if (s.charAt(i - 1) == t.charAt(j - 1))
					matrix[i][j] = matrix[i - 1][j - 1];
				else
					matrix[i][j] = Math.min(Math.min(matrix[i - 1][j] + 1,
							matrix[i][j - 1] + 1), matrix[i - 1][j - 1] + 1);
		return ((double) (matrix[l1][l2] / Math.max(s.length(), t.length())));
	}
}

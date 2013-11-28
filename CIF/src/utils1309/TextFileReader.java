package utils1309;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class TextFileReader {

	/**
	 * Reads a text file of lines and returns the lines as String elements in a LinkedList
	 * 
	 * @param filePathName
	 *            Full path name of the file to read into memory, e.g. "/Users/minddrill/zz.txt"
	 * @return LinkedList<String> representing lines in the text file
	 * @throws Exception
	 *             When the file specified 1) doesn't exist, 2) is a directory, or 3) user has no permission to read it
	 *             4) is empty
	 */
	public static LinkedList<String> readLineLinkedList(final String filePathName) throws Exception {
		TextFileReader.checkFile(filePathName);
		// Read all lines into memory

		final LinkedList<String> lines = new LinkedList<String>();
		final BufferedReader br = new BufferedReader(new FileReader(new File(filePathName)));
		do {
			lines.add(br.readLine());
		} while (lines.getLast() != null);
		lines.removeLast(); // Last one is null so remove it

		br.close();
		return lines;

	}

	public static ArrayList<String> readLineArrayList(final String filePathName) throws Exception {
		TextFileReader.checkFile(filePathName);
		// Read all lines into memory

		final ArrayList<String> lines = new ArrayList<String>();
		final BufferedReader br = new BufferedReader(new FileReader(new File(filePathName)));
		do {
			lines.add(br.readLine());
		} while (!lines.isEmpty());
		lines.remove(lines.size() - 1); // Last one is null so remove it

		br.close();
		return lines;
	}

	private static void checkFile(final String filePathName) throws Exception {
		// Check validity of file specification

		final File file = new File(filePathName);
		if (!file.exists()) throw new Exception(String.format("The file specified, [%s] doesn't exist", filePathName));
		if (file.isDirectory()) throw new Exception(String.format("The file specified, [%s] is a directory",
				filePathName));
		if (!file.canRead()) throw new Exception(String.format("No permission to read file [%s]", filePathName));

	}
}

package test.com.draiver.core.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommonConsoleUtil {

	public static void clearScreen() {
		lineBreak(50);
	}

	public static String duplicate(String message, int totalInstances) {
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < totalInstances; i++) {
			output.append(message);
		}
		return output.toString();
	}

	public static void print(int indent, String message) {
		System.out.print(duplicate("\t", indent) + message);
	}

	public static void print(String message) {
		print(0, message);
	}

	public static void printLine(int indent, String message) {
		System.out.println(duplicate("\t", indent) + message);
	}

	public static void printLine(String message) {
		printLine(0, message);
	}

	public static void printLineAndBreak(int indent, String message, int numberOfBreaks) {
		printLine(indent, message);
		lineBreak(numberOfBreaks);
	}
	
	public static void printError(Exception e) {
		printError(e.toString());
	}
	public static void printError(String message) {
		lineBreak(2);
		printLine("!!!!!!!!!!!!!!!!!!!ERROR!!!!!!!!!!!!!!!!!!!!!!!!!");
		printLine(message);
		printLineAndBreak("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",2);
	}

	public static void printLineAndBreak(String message, int numberOfBreaks) {
		printLineAndBreak(0, message, numberOfBreaks);
	}

	public static void lineBreak(int numberOfBreaks) {
		for (int i = 0; i < numberOfBreaks; i++) {
			System.out.println();
		}
	}

	public static String readLine() {
		String output = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			output = br.readLine();
		} catch (Exception e) {
			printError(e);
		}
		return output;
	}

	public static String readLine(int indent, String message) {
		print(indent, message);
		return readLine();
	}

	public static String readLine(String message) {
		return readLine(0, message);
	}
}

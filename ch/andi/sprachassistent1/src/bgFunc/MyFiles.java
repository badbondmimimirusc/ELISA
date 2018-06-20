package bgFunc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyFiles {
	
	public final static String PROGRAMS_PATH = "data/programsPath.txt";
	public final static String AUTO_PROGRAMS_PATH = "data/autoProgramsPath.txt";
	public final static String FILES_PATH = "data/filesPath.txt";
	public final static String WEBSITES_PATH = "data/websitesPath.txt";
	public final static String GRAM_FILE = "sphinx_data_small/etc/EntryNames.gram";
	public final static String DICT_FILE = "sphinx_data_small/etc/voxforge_small.dic";
	

	public static String[] getFileContent(String path) {
		List<String> list = new ArrayList<>();
		
		try {
			list.addAll(Files.readAllLines(Paths.get(path), Charset.forName("ISO-8859-1")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] out = (String[]) list.toArray(new String[list.size()]);
		return out;
	}
	
	public static void writeFile(List<String> lines, String path) {
		Path file = Paths.get(path);
		try {
			Files.write(file, lines, Charset.forName("ISO-8859-1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getAllNames(String path) {
		String[] in = getFileContent(path);
		String[] out = new String[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = in[i].split("\\|")[0];
		}
		return out;
	}
	
	public static String[] getAllPaths(String path) {
		String[] in = getFileContent(path);
		String[] out = new String[in.length];
		for(int i = 0; i < in.length; i++) {
			out[i] = in[i].split("\\|")[1];
		}
		return out;
	}
	
	public static boolean replaceOnceInFile(String filePath, String oldString, String newString) {

		String[] lines = getFileContent(filePath);
		
		int matchCount = 0;
		
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].matches(".*"+oldString+".*")) {
				lines[i] = lines[i].replaceFirst(oldString, newString);
				matchCount++;
			}
		}
		
		if(matchCount != 1) {
			//System.err.println("ERROR: (MyFiles.replaceOnceInFile) Konnte \""+oldString+"\" "+matchCount+"-mal in der Datei \""+filePath+"\" finden!");
			return false;
		}

		writeFile(Arrays.asList(lines), filePath);
		return true;
	}
	
	public static boolean replaceProgramInGram(String oldName, String newName) {
		if(removeEntryFromGram("autoPrograms", oldName)) {
			addEntryToGram("autoPrograms", new String[] {newName});
			return true;
		}
		return false;
	}
	
	public static boolean replaceEntryInDict(String oldName, String newName, String sprache) {
		String pronounciation = Words.getPhonemes(sprache, newName);
		
		if(MyFiles.replaceOnceInFile(MyFiles.DICT_FILE, "^_?"+oldName+" .*", newName+" "+pronounciation)) {
			System.out.println("INFO: (MyFiles.replaceEntryInDict) ("+sprache+") Ersetzte "+oldName+" mit "+newName+" ("+pronounciation+")");
			return true;
		}
		System.err.println("ERROR: (MyFiles.replaceEntryInDict) ("+sprache+") Konnte "+oldName+" nicht genau einmal im dict-File finden.");
		return false;
	}
	
	public static void addEntryToGram(String entryType, String[] programNames) {
		System.out.println("addEntryToGram: "+Arrays.toString(programNames));
		String[] lines = MyFiles.getFileContent(GRAM_FILE);
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith("<"+entryType+">")) {
				for (int j = 0; j < programNames.length; j++) {
					if(lines[i].matches("<"+entryType+">\\s*=\\s*<VOID>\\s*;")) {
						lines[i] = lines[i].replace("<VOID>", programNames[j]);
					}else {
						lines[i] = lines[i].replace(";", " | " + programNames[j] + ";");
					}
				}
			}
		}
		
		System.out.println("addEntryToGram write: "+Arrays.toString(lines));

		MyFiles.writeFile(Arrays.asList(lines), GRAM_FILE);
	}
	
	public static void addEntryToDict(String[] entryNames, String[] entryPronounciation) {
		System.out.println("INFO: (MyFiles.addEntryToDict) "+Arrays.toString(entryNames));
		List<String> dictLines = new ArrayList<>();
		dictLines.addAll(Arrays.asList(MyFiles.getFileContent(DICT_FILE)));

		for (int i = 0; i < entryNames.length; i++) {
			dictLines.add(entryNames[i] + " " + entryPronounciation[i]);
		}

		dictLines.sort(null);

		MyFiles.writeFile(dictLines, DICT_FILE);
	}

	public static boolean removeEntryFromGram(String entryType, String name) {
		String[] lines = MyFiles.getFileContent(GRAM_FILE);
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].startsWith("<"+entryType+">")) {
				String oldLine = lines[i];
				lines[i] = lines[i].replaceFirst("\\|\\s*_?"+name+"\\s*\\|", "\\|");
				if(lines[i].equals(oldLine)) {
					lines[i] = lines[i].replaceFirst("=\\s*_?"+name+"\\s*\\|", "=");
					if(lines[i].equals(oldLine)) {
						lines[i] = lines[i].replaceFirst("\\s*\\|\\s*_?"+name+"\\s*;", ";");
						if(lines[i].equals(oldLine)) {
							System.err.println("ERROR: (MyFiles.removeEntryFromGram) Konnte "+name+" nicht im GRAM-File finden");
							return false;
						}
					}
				}
				break;
			}
		}
		
		MyFiles.writeFile(Arrays.asList(lines), GRAM_FILE);
		return true;
	}
}

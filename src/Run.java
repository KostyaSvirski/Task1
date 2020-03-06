import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.deploy.association.utility.WinRegistryWrapper;

public class Run implements RunProgramInterface {

	private File file;
	private String command;
	private String keyFromRegistry;

	private static String pattern = "^[0-9a-zA-Z]{0,256} = [0-9a-zA-Z]{0,256}$";
	private static String resultIncorrect = "resultIncorrect.txt";
	private static String resultCorrect = "file_out.txt";
	private static String fileError = "file_err.txt";
	private static String rkOut = "rk_out.txt";
	private static String rkError = "rk_err.txt";
	private static String cmdResult = "cmd_out.txt";
	private static String cmdResultError = "cmd_err.txt";
	
	private static String HLM = "HKEY_LOCAL_MACHINE";
	private static String HCU = "HKEY_CURRENT_USER";
	private static String HCR = "HKEY_CLASSES_ROOT";
	private static String HU = "HKEY_USERS";
	private static String HCC = "HKEY_CURRENT_CONFIG";

	public Run(File file, String command, String keyFromRegistry) {
		super();
		this.file = file;
		this.command = command;
		this.keyFromRegistry = keyFromRegistry;
	}

	@Override
	public void runCommand() {
		StringBuffer output = new StringBuffer();

		try {
			Process pr;
			try {
				pr = Runtime.getRuntime().exec(command);
				pr.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}
			} catch (IOException e) {
				printErrorFile(e.getMessage(), cmdResultError);
			}
		} catch (InterruptedException e) {
			printErrorFile(e.getMessage(), cmdResultError);
		}
		FileWriter fw;
		try {
			fw = new FileWriter(cmdResult, false);
			fw.write(output.toString() + "\n");
			fw.close();
		} catch (IOException e) {
			printErrorFile(e.getMessage(), cmdResultError);
		}

	}

	private void printIntoIncorectFile() { 
		try {
			FileWriter fw = new FileWriter(resultIncorrect, false);
			FileReader fr = new FileReader(file);
			Scanner sc = new Scanner(fr);
			while (sc.hasNextLine()) {
				fw.write(sc.nextLine() + "\n");
			}
			sc.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String[] createArrayOfStrings(String str) {
		String[] strArray = new String[2];
		int endIndex = str.indexOf(" = ");
		int beginIndex = 0;
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = str.substring(beginIndex, endIndex);
			beginIndex = endIndex + 3;
			endIndex = str.length();
		}
		return strArray;

	}

	private void printErrorFile(String message, String file) {
		try {
			FileWriter fw = new FileWriter(file, false);

			fw.write(message + "\n");

			fw.close();
		} catch (IOException f) {
			f.printStackTrace();
		}
	}

	@Override
	public void readFile() {
		try {
			FileReader fr = new FileReader(file);
			Scanner sc = new Scanner(fr);
			Pattern p = Pattern.compile(pattern);
			while (sc.hasNextLine()) {
				String sTemp = null;
				String str = sc.nextLine();
				Matcher m = p.matcher(str);
				while (m.find()) {
					sTemp = str.substring(m.start(), m.end());
				}
				if (!str.equalsIgnoreCase(sTemp)) {
					printIntoIncorectFile();
					sc.close();
					return;
				}

			}
			sc.close();
			FileReader fr1 = new FileReader(file);
			Scanner sc1 = new Scanner(fr1);
			FileWriter fw = new FileWriter(resultCorrect, false);
			while (sc1.hasNextLine()) {
				String[] strArray = createArrayOfStrings(sc1.nextLine());
				for (int i = 0; i < strArray.length; i++) {
					fw.write(strArray[i] + "\n");
				}
			}

			fw.close();
			sc1.close();

		} catch (FileNotFoundException e) {
			printErrorFile(e.getMessage(), fileError);

		} catch (IOException e) {
			printErrorFile(e.getMessage(), fileError);
		}

	}

	@Override
	public void runWithKeyReester() {
		int endIndex = keyFromRegistry.indexOf("\\");
		if (keyFromRegistry.substring(0, endIndex).equalsIgnoreCase(HLM)) {
			try {
				String key = keyFromRegistry.substring(keyFromRegistry.lastIndexOf("\\") + 1); //key
				String strTest = keyFromRegistry.substring(endIndex + 1, keyFromRegistry.indexOf(key)); //path to key
				String keyNames[] = WinRegistryWrapper.WinRegGetValues(WinRegistryWrapper.HKEY_LOCAL_MACHINE, strTest,
						100); //all keys in directory
				for (String i : keyNames) {
					if (i.equals(key)) {
						try {
							FileWriter fw = new FileWriter(rkOut, false);
							fw.write(i + " = " + WinRegistryWrapper
									.WinRegQueryValueEx(WinRegistryWrapper.HKEY_LOCAL_MACHINE, strTest, i) + "\n");

							fw.close();
						} catch (FileNotFoundException e) {
							printErrorFile(e.getMessage(), rkError);
						}

					}
				}

			} catch (Exception e) {
				printErrorFile("KEY DOESN'T EXIST", rkError);
			}

		} else if (keyFromRegistry.substring(0, endIndex).equalsIgnoreCase(HCU)) {
			try {
				String key = keyFromRegistry.substring(keyFromRegistry.lastIndexOf("\\") + 1);
				String strTest = keyFromRegistry.substring(endIndex + 1, keyFromRegistry.indexOf(key));
				String keyNames[] = WinRegistryWrapper.WinRegGetValues(WinRegistryWrapper.HKEY_CURRENT_USER, strTest,
						100);
				for (String i : keyNames) {
					if (i.equals(key)) {
						try {
							FileWriter fw = new FileWriter(rkOut, false);
							fw.write(i + " = " + WinRegistryWrapper
									.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, strTest, i) + "\n");

							fw.close();
						} catch (FileNotFoundException e) {
							printErrorFile(e.getMessage(), rkError);
						}

					}
				}

			} catch (Exception e) {
				printErrorFile("KEY DOESN'T EXIST", rkError);
			}

		} else if (keyFromRegistry.substring(0, endIndex).equalsIgnoreCase(HCR)) {
			try {
				String key = keyFromRegistry.substring(keyFromRegistry.lastIndexOf("\\") + 1);
				String strTest = keyFromRegistry.substring(endIndex + 1, keyFromRegistry.indexOf(key));
				String keyNames[] = WinRegistryWrapper.WinRegGetValues(WinRegistryWrapper.HKEY_CLASSES_ROOT, strTest,
						100);
				for (String i : keyNames) {
					if (i.equals(key)) {
						try {
							FileWriter fw = new FileWriter(rkOut, false);
							fw.write(i + " = " + WinRegistryWrapper
									.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CLASSES_ROOT, strTest, i) + "\n");

							fw.close();
						} catch (FileNotFoundException e) {
							printErrorFile(e.getMessage(), rkError);
						}

					}
				}

			} catch (Exception e) {
				printErrorFile("KEY DOESN'T EXIST", rkError);
			}

		} else if (keyFromRegistry.substring(0, endIndex).equalsIgnoreCase(HU)) {
			try {
				String key = keyFromRegistry.substring(keyFromRegistry.lastIndexOf("\\") + 1);
				String strTest = keyFromRegistry.substring(endIndex + 1, keyFromRegistry.indexOf(key));
				String keyNames[] = WinRegistryWrapper.WinRegGetValues(WinRegistryWrapper.HKEY_USERS, strTest,
						100);
				for (String i : keyNames) {
					if (i.equals(key)) {
						try {
							FileWriter fw = new FileWriter(rkOut, false);
							fw.write(i + " = " + WinRegistryWrapper
									.WinRegQueryValueEx(WinRegistryWrapper.HKEY_USERS, strTest, i) + "\n");

							fw.close();
						} catch (FileNotFoundException e) {
							printErrorFile(e.getMessage(), rkError);
						}

					}
				}
			} catch (Exception e) {
				printErrorFile("KEY DOESN'T EXIST", rkError);
			}

		} else if (keyFromRegistry.substring(0, endIndex).equalsIgnoreCase(HCC)) {
			try {
				String key = keyFromRegistry.substring(keyFromRegistry.lastIndexOf("\\") + 1);
				String strTest = keyFromRegistry.substring(endIndex + 1, keyFromRegistry.indexOf(key));
				String keyNames[] = WinRegistryWrapper.WinRegGetValues(WinRegistryWrapper.HKEY_CURRENT_CONFIG, strTest,
						100);
				for (String i : keyNames) {
					if (i.equals(key)) {
						try {
							FileWriter fw = new FileWriter(rkOut, false);
							fw.write(i + " = " + WinRegistryWrapper
									.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CURRENT_CONFIG, strTest, i) + "\n");

							fw.close();
						} catch (FileNotFoundException e) {
							printErrorFile(e.getMessage(), rkError);
						}

					}
				}
			} catch (Exception e) {
				printErrorFile("KEY DOESN'T EXIST", rkError);
			}

		} else {
			printErrorFile("KEY DOESN'T EXIST", rkError);
		}

	}

}

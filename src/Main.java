import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		// Scanner sc = new Scanner(System.in);
		Run run = new Run(new File("-f D:\\java\\javaEE\\projects\\TaskIBA\\keys.txt".substring(3)),
				"-cmd whoami".substring(5),
				"-rk HKEY_USERS\\.DEFAULT\\Control Panel\\Accessibility\\HighContrast\\Flags".substring(4));
		run.readFile();
		run.runCommand();
		run.runWithKeyReester();
		// sc.close();
	}

}

// args[0] = "-f D:\\java\\javaEE\\projects\\TaskIBA\\keys.txt"
// args[1] = "-cmd command";
// args[2] = "-rk SOFTWARE\\JavaSoft\\Java Development Kit\\1.8.0_181\\MSI";
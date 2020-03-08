import java.io.File;
import java.io.IOException;

public class Main {
	
	static {
		try {
			System.load("C:\\Program Files\\Java\\jdk1.8.0_181\\jre\\bin\\deploy.dll");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load\n");
			System.exit(1);
		}

	}

	public static void main(String[] args) throws IOException {

		Run run = new Run(new File("-f D:\\java\\javaEE\\projects\\TaskIBA\\keys.txt".substring(3)),
				"-cmd whoami".substring(5),
				"-rk HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\1.8.0_181\\MSI\\INSTALLDIR".substring(4));
		run.readFile();
		run.runCommand();
		run.runWithKeyReester();
		// sc.close();
	}

}

// args[0] = "-f D:\\java\\javaEE\\projects\\TaskIBA\\keys.txt"
// args[1] = "-cmd whoami";
// args[2] = "-rk HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Development Kit\1.8.0_181\MSI\INASTALLDIR";

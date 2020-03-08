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

		// Scanner sc = new Scanner(System.in);
		Run run = new Run(new File(args[0].substring(3)), args[1].substring(5), args[2].substring(4));
		run.readFile();
		run.runCommand();
		run.runWithKeyReester();
		// sc.close();
	}

}

// args[0] = "-f D:\\java\\javaEE\\projects\\TaskIBA\\keys.txt"
// args[1] = "-cmd command";
// args[2] = "-rk SOFTWARE\\JavaSoft\\Java Development Kit\\1.8.0_181\\MSI";
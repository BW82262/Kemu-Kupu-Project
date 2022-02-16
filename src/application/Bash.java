package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Bash {
	public static String runCommand(String cmd) throws IOException {

		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

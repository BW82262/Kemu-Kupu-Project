package application;

import java.io.IOException;

public class Speaker {
	public static void sayWord(String word,double speechSpeed) {

		// Use Duration_Stretch which changes the duration time of the wav.file
		double duration = 1 / speechSpeed;
		try {
			// Create a new temp file and rewrite the old one
			Bash.runCommand("echo '(voice_akl_mi_pk06_cg)' >tempSound.scm");
			// Should be echo '(Parameter.set '\''Duration_Stretch 1.0)'>>temp.scm in
			// terminal
			// Should create a line (Parameter.set 'Duration_Stretch 1.0) in temp file
			Bash.runCommand("echo '(Parameter.set '\\''Duration_Stretch " + String.format("%.1f", duration)
			+ ")'>>tempSound.scm");
			Bash.runCommand("echo '(SayText \"" + word + "\")' >>tempSound.scm");

			Bash.runCommand("festival -b tempSound.scm");

			Bash.runCommand("rm tempSound.scm");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

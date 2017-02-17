package bdma.labos.lambda;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import bdma.labos.lambda.exercises.Exercise1;
import bdma.labos.lambda.exercises.Exercise2;
import bdma.labos.lambda.exercises.Exercise3;
import bdma.labos.lambda.exercises.Exercise4;

public class Lambda {
	
	private static String TWITTER_CONFIG_PATH = "UPDATE ME";
	
	public static void main(String[] args) {
		LogManager.getRootLogger().setLevel(Level.ERROR);
		try {
			if (args[0].equals("-exercise1")) {
				Exercise1.run(TWITTER_CONFIG_PATH);
			} else if (args[0].equals("-exercise2")) {
				Exercise2.run(TWITTER_CONFIG_PATH);
			} else if (args[0].equals("-exercise3")) {
				Exercise3.run();
			} else if (args[0].equals("-exercise4")) {
				Exercise4.run(args[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

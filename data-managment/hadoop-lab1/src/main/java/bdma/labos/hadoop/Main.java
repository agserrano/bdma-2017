package bdma.labos.hadoop;

import java.io.IOException;

import bdma.labos.hadoop.reader.MyHDFSPlainFileReader;
import bdma.labos.hadoop.reader.MyHDFSSequenceFileReader;
import bdma.labos.hadoop.reader.MyReader;
import bdma.labos.hadoop.writer.MyHDFSPlainFileWriter;
import bdma.labos.hadoop.writer.MyHDFSSequenceFileWriter;
import bdma.labos.hadoop.writer.MyStandardOutputWriter;
import bdma.labos.hadoop.writer.MyWriter;

public class Main {
	
	private static final String[] names = new String[] {
		"alc",					// Alchol
		"m_acid",				// Malic Acid
		"ash",					// Ash
		"alc_ash",				// Alcalinity of ash
		"mgn",					// Magnesium
		"t_phenols",			// Total phenols
		"flav",					// Flavanoids
		"nonflav_phenols",		// Nonflavanoid phenols
		"proant",				// Proanthocyanins
		"col",					// Color intensity
		"hue",					// Hue
		"od280/od315",			// OD280/OD315 of diluted wines
		"proline"				// Proline
	};
	
	private static final int[][] intervals = new int[][] {
		new int[] {10, 15},
		new int[] {1, 5},
		new int[] {2, 3},
		new int[] {15, 30},
		new int[] {50, 150},
		new int[] {0, 5},
		new int[] {0, 1},
		new int[] {1, 5},
		new int[] {0, 1},
		new int[] {0, 5},
		new int[] {0, 2},
		new int[] {1, 4},
		new int[] {100, 2000}
	};
	
	private static MyReader input;
	private static MyWriter output;
	private static String file;
	
	public static void read() throws IOException {
		input.open(file);
		String line = input.next();
		while (line != null) {
			if (!line.equals("")) {
				System.out.println(line);
			}
			line = input.next();
		}
		input.close();
	}
	
	public static void write(boolean inst_mode, long number) throws IOException {
		Generator generator = new Generator(1);
		output.open(file);
		int inst; long size; int bytes;
		for (inst = 1, size = 0; ((inst <= number) & inst_mode) ||
				((size < number) & !inst_mode); inst++, size += bytes) {
			String type = generator.nextType();
			output.put("type", type);
			output.put("region", generator.nextRegion(type));
			for (int j = 0; j < intervals.length; j++) {
				output.put(names[j], generator.nextValue(intervals[j][0], intervals[j][1]));
			}
			bytes = output.flush();
		}
		output.close();
	}

	public static void main(String[] args) {
		try {
			if (args[0].equals("write")) {
				if (args[1].equals("-hdfsPlain")) {
					output = new MyHDFSPlainFileWriter();
					file = args[4];
				}
				else if (args[1].equals("-hdfsSequence")) {
					output = new MyHDFSSequenceFileWriter();
					file = args[4];
				}
				else if (args[1].equals("-standard")) {
					output = new MyStandardOutputWriter();
					file = null;
				}
				if (args[2].equals("-instances")) {
					write(true, Integer.parseInt(args[3]));
				}
				else if (args[2].equals("-size")) {
					write(false, (long)(Double.parseDouble(args[3])*1024*1024*1024));
				}
			}
			else if (args[0].equals("read")) {
				if (args[1].equals("-hdfsPlain")) {
					input = new MyHDFSPlainFileReader();
				}
				else if (args[1].equals("-hdfsSequence")) {
					input = new MyHDFSSequenceFileReader();
				}
				file = args[2];
				read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

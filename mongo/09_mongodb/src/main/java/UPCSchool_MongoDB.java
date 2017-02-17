import exercise2.Exercise_2_example;
import exercise2.Exercise_2_model_1;
import exercise2.Exercise_2_model_2;
import exercise2.Exercise_2_model_3;
import util.Utils;

public class UPCSchool_MongoDB {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			throw new Exception("Wrong number of parameters, usage: (exercise2_example,exercise2_model1,exercise2_model2,exercise2_model3); extra parameters specifics for the exercise");
		}
		if (args[0].equals("exercise2_example")) {
			if (args.length != 2 || !Utils.isNumber(args[1])) {
				throw new Exception("[Error ex2_example] params: N (number of documents to create)");
			}
			Exercise_2_example.dataGenerator(Integer.parseInt(args[1]));
		}
		if (args[0].equals("exercise2_model1")) {
			if (args.length != 3 || !Utils.isNumber(args[1])) {
				throw new Exception("[Error exercise2_model1] params: N (number of persons to create). M (number of companies to create)");
			}
			Exercise_2_model_1.dataGenerator(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
		if (args[0].equals("exercise2_model2")) {
			if (args.length != 3 || !Utils.isNumber(args[1])) {
				throw new Exception("[Error exercise2_model1] params: N (number of persons to create). M (number of companies to create)");
			}
			Exercise_2_model_2.dataGenerator(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
		if (args[0].equals("exercise2_model3")) {
			if (args.length != 3 || !Utils.isNumber(args[1])) {
				throw new Exception("[Error exercise2_model1] params: N (number of persons to create). M (number of companies to create)");
			}
			Exercise_2_model_3.dataGenerator(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
		
	}
	
}

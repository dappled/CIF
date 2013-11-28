package myDesignPatterns;

import java.util.Observer;

/**
 * The main that instantiate a subject, two listeners and call run
 * 
 * @author Zhenghong Dong
 * 
 */
public class SubjectExample {

	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			throw new Exception("Usage: SubjectExample -fileLocation");
		Subject subject = new Subject(args[0]);
		Observer curObs = new CurrentObserver();
		Observer avgObs = new AverageObserver();

		subject.addObserver(curObs);
		subject.addObserver(avgObs);
		subject.run();
	}
}

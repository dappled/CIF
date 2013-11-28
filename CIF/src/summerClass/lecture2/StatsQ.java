package summerClass.lecture2;

import java.util.LinkedList;

public class StatsQ extends LinkedList<Double> implements StatsQInterface {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isReady() {
		return super.size() == 900;
	}

	@Override
	public boolean hasElementsToRemove() {
		return super.size() > 900;
	}

}

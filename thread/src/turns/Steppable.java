package turns;

/**
 * Steppable objects that have operate during a turn.
 * @author Niels Thykier
 */
public interface Steppable {
	/**
	 * Operate a single time-unit.
	 */
	public void step();
}

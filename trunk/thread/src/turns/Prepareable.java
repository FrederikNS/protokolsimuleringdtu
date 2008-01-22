package turns;

/**
 * Interface for Steppable objects that require a prepare phase.
 * @author Niels Thykier
 */
public interface Prepareable extends Steppable {
	/**
	 * Make preparations for the step().
	 */
	public void prepare();
}

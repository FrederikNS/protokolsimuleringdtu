package turns;

/**
 * For Steppable objects that require an end-phase. 
 * @author Niels Thykier
 */
public interface EndSteppable extends Steppable {
	/**
	 * The end phase of the tick.
	 */
	public void EndStep();
}

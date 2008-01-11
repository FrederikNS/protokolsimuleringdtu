package turns;

public interface Prepareable extends Steppable {
	/**
	 * Make preparations for the step().
	 */
	public void prepare();
}

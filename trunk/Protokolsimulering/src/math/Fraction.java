package math;

/**
 * Fraction is just that, a fraction. 
 * It contains only integers and keeps itself as precise as possible. 
 * @author Niels Thykier
 */
public class Fraction implements Cloneable, Comparable{
	private int numerator;
	private int denominator;
	
	/**
	 * Creates a fraction with the initial value of 0.
	 */
	public Fraction() {
		this(0,1);
	}
	
	/**
	 * Creates a fraction with the initial values.
	 * Note: if the numerator is 0, the denominator will be reduced to 1!
	 * @param numerator The value of the numerator (top)
	 * @param denominator The value of the denominator (bottom)
	 * @throws ArithmeticException if the denominator is 0.
	 */
	public Fraction(int numerator, int denominator)  throws ArithmeticException {
		this.numerator = numerator;
		setDenominator(denominator);
	}
	
	/**
	 * Evalutes the fraction into its estimated value as a double putting asside precision.
	 * @return the value of the Fraction as a double.
	 */
	public double evaluate() {
		return ((double)numerator)/((double)denominator);
	}
	
	/**
	 * Evaluates the fraction to a single integer (rounded down)
	 * @return The rounded down evaluation.
	 */
	public int evaluateRoundDown() {
		return numerator/denominator;
	}
	

	/**
	 * Evaluates the fraction to a single integer (rounded up)
	 * @return The rounded up evaluation.
	 */
	public int evaluateRoundUp() {
		int result = evaluateRoundDown();
		if(numerator % denominator != 0) {
			++result;
		}
		return result;
	}

	/**
	 * Sets the numerator to a new value.
	 * Note: if the numerator is 0, the denominator will always be 1!
	 * @param numerator The new value.
	 */
	public void setNumerator(int numerator) {
		this.numerator = numerator;
		reduce();
	}
	
	/**
	 * Get the current numerator.
	 * @return The numerator.
	 */
	public int getNumerator() {
		return numerator;
	}
	
	/**
	 * Get the current denominator.
	 * @return The denominator.
	 */
	public int getDenominator() {
		return denominator;
	}
	
	/**
	 * Sets the denominator to a new value.
	 * Note: if the numerator is 0, the denominator will always be 1!
	 * @param denominator The new value.
	 * @throws ArithmeticException If the new denominator is 0.
	 */
	public void setDenominator(int denominator) throws ArithmeticException{
		if(denominator == 0) {
			throw new ArithmeticException("\\ by zero");
		}
		if(numerator != 0 ) {
			this.denominator = denominator;
		}
		reduce();
	}
	
	/**
	 * Test if the fraction is an integer.
	 * @return true if this fraction is an integer.
	 */
	public boolean isInteger() {
		return denominator == 1;
	}
	
	/**
	 * Test if the fraction is negative.
	 * @return true if the fraction is negative.
	 */
	public boolean isNegative() {
		this.reduce();
		return numerator < 0;
	}
	
	/**
	 * Adds the fraction and the integer together as per math rules.
	 * @param amount The integer to add
	 * @return The modified Fraction
	 */
	public Fraction add(int amount) {
		numerator += amount*denominator;
		return this;
	}
	
	/**
	 * Subtracts the integer from the fraction as per math rules.
	 * 
	 * @param amount The integer to subtract
	 * @return The modified Fraction
	 */
	public Fraction subtract(int amount) {
		add(-amount);
		return this;
	}
	
	/**
	 * Multiply the fraction with an integer as per math rules.
	 * 
	 * @param amount The integer to multiply with.
	 * @return The modified Fraction
	 */
	public Fraction multiply(int amount) {
		numerator *= amount;
		return reduce();
	}
	
	/**
	 * Divide the fraction with an integer.
	 * 
	 * @param amount The integer to divide with.
	 * @throws ArithmeticException if amount is 0.
	 * @return The modified Fraction
	 */
	public Fraction divide(int amount) throws ArithmeticException {
		if(amount == 0) {
			throw new ArithmeticException();
		}
		denominator *= amount;
		return reduce();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Fraction) {
			Fraction frac = (Fraction) obj;
			frac.reduce();
			this.reduce();
			return frac.getDenominator() == denominator && frac.getNumerator() == numerator;
		}
		return super.equals(obj);
	}
	
	/**
	 * Attempts to reduce the fraction. 
	 * Note: If the numerator is 0, the denominator will be reduced to 1.
	 * 
	 * While both the denominator and the numerator is equal, they will be divided with two.
	 * Once one (or both) no longer are equal it will start with the lowest of the two of them and 
	 * find the highest possible value to divide them both with and if found, they will be divided with that.
	 * 
	 * @return The reduced Fraction
	 */
	public Fraction reduce() {
		if(numerator == 0) {
			denominator = 1;
			return this;
		}
		if(Math.abs(numerator) == Math.abs(denominator)) {
			//preserve a possible negative value.
			numerator/=denominator; 
			denominator = 1;
			return this;
		}
		if(denominator < 0) {
			numerator = -numerator;
			denominator = -denominator;
		}
		//		While they are both equal, divide by 2.
		while( 0 == (denominator & 1) && 0 == (numerator & 1)) {
			numerator /= 2;
			denominator /= 2;
		}
		
		int max;
		int tempNumerator = Math.abs(numerator);
		int tempDenominator = Math.abs(denominator);
		if(tempNumerator > tempDenominator) {
			max = tempDenominator;
		} else {
			max = tempNumerator;
		}
		if((max & 1) == 0 ) {
			--max;
		}
		for(int i = max ; i > 2 ; i-=2) {
			if( (denominator % i) == 0 && (numerator % i) == 0) {
				denominator /= i;
				numerator /= i;
				break;
			}
		}
		
		return this;
	}
	
	/**
	 * Adds a fraction to this one.
	 * @param toAdd The fraction to add.
	 * @return The modified Fraction
	 */
	public Fraction add(Fraction toAdd) {
		int otherDenominator = toAdd.getDenominator();
		numerator = numerator * otherDenominator + toAdd.getNumerator() * denominator; 
		denominator *= otherDenominator;
		return reduce();
	}
	
	/**
	 * Subtract a fraction from this one.
	 * @param toSubtract The fraction to subtract.
	 * @return The modified Fraction
	 */
	public Fraction subtract(Fraction toSubtract) {
		return this.add(new Fraction(-toSubtract.getNumerator(), toSubtract.getDenominator()));
	}
	
	/**
	 * Multiplies this fraction with another
	 * @param toMultiply The fraction to multiply with.
	 * @return The modified Fraction
	 */
	public Fraction multiply(Fraction toMultiply) {
		numerator *= toMultiply.getNumerator();
		denominator *= toMultiply.getDenominator();
		return reduce();
	}
	
	/**
	 * Divide this fraction with another fraction.
	 * 
	 * This is done by reciprocalizing this fraction and then multiplying it with the other fraction.
	 * 
	 * @param toDivide The fraction to divide with.
	 * @return The modified Fraction
	 */
	public Fraction divide(Fraction toDivide) {
		reciprocalize();
		return multiply(toDivide);
	}
	
	/**
	 * Gets the result of adding two fractions together
	 * 
	 * frac1 + frac2
	 * 
	 * @param frac1 A fraction
	 * @param frac2 A fraction
	 * @return The result
	 */
	public static Fraction add(Fraction frac1, Fraction frac2) {
		return frac1.clone().add(frac2); 
	}
	
	/**
	 * Gets the result of substracting two fractions
	 * 
	 * frac1 - frac2
	 * 
	 * @param frac1 A fraction
	 * @param frac2 A fraction
	 * @return The result
	 */
	public static Fraction subtract(Fraction frac1, Fraction frac2) {
		return frac1.clone().subtract(frac2); 
	}

	/**
	 * Gets the result of dividing two fractions. 
	 * 
	 * frac1 / frac2
	 * 
	 * @param frac1 A fraction
	 * @param frac2 A fraction
	 * @return The result
	 */
	public static Fraction divide(Fraction frac1, Fraction frac2) {
		return frac1.clone().divide(frac2); 
	}

	/**
	 * Gets the result of multiplying two fractions
	 * 
	 * frac1 * frac2
	 * 
	 * @param frac1 A fraction
	 * @param frac2 A fraction
	 * @return The result
	 */
	public static Fraction multiply(Fraction frac1, Fraction frac2) {
		Fraction toReturn = frac1.clone(); 
		toReturn.multiply(frac2);
		return toReturn;
	}
	
	/**
	 * Gets the reciprocal counter-part of this fraction.
	 */
	public Fraction getReciprocal() {
		Fraction frac = this.clone();
		frac.reciprocalize();
		return frac; 
	}
	
	/**
	 * Turns the fraction into its reciprocal counter-part.
	 */
	public void reciprocalize() {
		if(numerator == 0) {
			return;
		}
		int temp = numerator;
		numerator = denominator;
		denominator = temp;
	}		
	
	@Override
	public String toString() {
		if(denominator == 1) {
			return String.valueOf(numerator);
		}
		return "( " + numerator + " / " + denominator + " )";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Fraction clone() throws RuntimeException {
		try {
			return (Fraction) super.clone();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Compares the fraction with value (Integer, Long, Short, Double and another Fraction are supported) and 
	 * returns -1,0,1 per rules of the compareTo(Object arg0) method defined in the Comparable interface.
	 * 
	 * Note: For non-Fraction values, this is only as precise the double value allows it to be.
	 */
	public int compareTo(Object arg0) {
		if(arg0 instanceof Fraction || arg0 instanceof Double) {
			Double value;
			if(arg0 instanceof Fraction) {
				value = ((Fraction) arg0).evaluate();
			} else {
				value = (Double) arg0;
			}
			return Double.compare(this.evaluate(), value);
		}
		else if(arg0 instanceof Integer || arg0 instanceof Long || arg0 instanceof Short) {
			Long value;
			if(arg0 instanceof Integer) {
				value = ((Integer) arg0).longValue();
			} else if(arg0 instanceof Long) {
				value = ((Long) arg0).longValue();
			} else {
				value = ((Short) arg0).longValue();
			}
			if(isInteger()) {
				return -value.compareTo(Long.valueOf(numerator));
			}
			return Double.compare(this.evaluate(), value);
		}
		throw new ClassCastException("Uncompariable type");
	}
		
}

package java8;

import java.util.function.Predicate;

/*
 * Warning!  Making changes will break the Serp tests if they too are not updated!
 */
public class SimpleLambdaUsingClass {
	public void doSomethingWithExpressionLambdas() {
		Predicate<Integer> p = (i) -> i != null;
		Integer i = new Integer(42);
		boolean aResult = p.test(i);
	}
	
	public void doSomethingWithBlockLambdas() {
		Predicate<Integer> p = (i) -> { return i != null; };
		Integer i = new Integer(42);
		boolean aResult = p.test(i);
	}
}

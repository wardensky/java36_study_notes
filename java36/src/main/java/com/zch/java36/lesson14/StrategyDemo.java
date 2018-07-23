package com.zch.java36.lesson14;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyDemo {

}

interface Strategy {
	void strategy(String input);
}

class ConcreteStrategyA implements Strategy {
	private static final Logger LOG = LoggerFactory.getLogger(ConcreteStrategyB.class);

	@Override
	public void strategy(String input) {
		LOG.info("Strategy A for input : {}", input);
	}
}

class ConcreteStrategyB implements Strategy {
	private static final Logger LOG = LoggerFactory.getLogger(ConcreteStrategyB.class);

	@Override
	public void strategy(String input) {
		LOG.info("Strategy B for input : {}", input);
	}
}
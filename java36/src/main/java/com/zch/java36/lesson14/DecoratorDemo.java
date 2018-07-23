package com.zch.java36.lesson14;

/**
 * @Description 装饰器模式
 * @author zch
 * @time 2018年7月23日 下午3:33:30
 * 
 */
public class DecoratorDemo {

}

interface Component {
	void operation();
}

class ConcreteComponent implements Component {
	public void operation() {
		System.out.println("ConcreteComponent");
	}
}

class Decorator implements Component {
	public Decorator(Component component) {
		this.component = component;
	}

	public void operation() {
		component.operation();
	}

	private Component component;
}

class ConcreteDecorator extends Decorator {
	
	public ConcreteDecorator(Component component) {
		super(component);
	}

	public void operation() {
		super.operation();
		addBehavior();
	}

	private void addBehavior() {
		System.out.println("ConcreteDecorator");
	}
}

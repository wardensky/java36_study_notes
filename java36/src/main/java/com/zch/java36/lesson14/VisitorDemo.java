package com.zch.java36.lesson14;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description 访问者模式
 * @author zch
 * @time 2018年7月24日 上午9:48:56
 * 
 */
public class VisitorDemo {
	public static void main(String[] args) {
		List<Element> list = ObjectStruture.getList();
		for (Element e : list) {
			e.accept(new Visitor());
		}
	}

}

abstract class Element {
	public abstract void accept(IVisitor visitor);

	public abstract void doSomething();
}

interface IVisitor {
	public void visit(ConcreteElement1 el1);

	public void visit(ConcreteElement2 el2);
}

class ConcreteElement1 extends Element {
	public void doSomething() {
		System.out.println("这是元素1");
	}

	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}

class ConcreteElement2 extends Element {
	public void doSomething() {
		System.out.println("这是元素2");
	}

	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}

class Visitor implements IVisitor {

	public void visit(ConcreteElement1 el1) {
		el1.doSomething();
	}

	public void visit(ConcreteElement2 el2) {
		el2.doSomething();
	}
}

class ObjectStruture {
	public static List<Element> getList() {
		List<Element> list = new ArrayList<Element>();
		Random ran = new Random();
		for (int i = 0; i < 10; i++) {
			int a = ran.nextInt(100);
			if (a > 50) {
				list.add(new ConcreteElement1());
			} else {
				list.add(new ConcreteElement2());
			}
		}
		return list;
	}
}

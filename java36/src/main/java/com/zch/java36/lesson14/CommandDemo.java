package com.zch.java36.lesson14;

/**
 * @Description 命令模式demo
 * @author zch
 * @time 2018年7月23日 上午11:12:21
 * 
 */
public class CommandDemo {
	public static void main(String[] args) {
		Invoker invoker = new Invoker();
		Receiver receiver = new ConcreteReceiver1();

		Command command = new ConcreteCommand1(receiver);
		invoker.setCommand(command);
		invoker.action();
	}
}

// 通用Receiver类
abstract class Receiver {
	public abstract void doSomething();
}

// 具体Receiver类
class ConcreteReceiver1 extends Receiver {
	// 每个接收者都必须处理一定的业务逻辑
	public void doSomething() {
		System.out.println("ConcreteReceiver1");
	}
}

class ConcreteReceiver2 extends Receiver {
	// 每个接收者都必须处理一定的业务逻辑
	public void doSomething() {
		System.out.println("ConcreteReceiver2");
	}
}

// 抽象Command类
abstract class Command {
	public abstract void execute();
}

// 具体的Command类
class ConcreteCommand1 extends Command {
	// 对哪个Receiver类进行命令处理
	private Receiver receiver;

	// 构造函数传递接收者
	public ConcreteCommand1(Receiver _receiver) {
		this.receiver = _receiver;
	}

	// 必须实现一个命令
	public void execute() {
		// 业务处理
		this.receiver.doSomething();
	}
}

class ConcreteCommand2 extends Command {
	// 哪个Receiver类进行命令处理
	private Receiver receiver;

	// 构造函数传递接收者
	public ConcreteCommand2(Receiver _receiver) {
		this.receiver = _receiver;
	}

	// 必须实现一个命令
	public void execute() {
		// 业务处理
		this.receiver.doSomething();
	}
}

// 调用者Invoker类
class Invoker {
	private Command command;

	public void setCommand(Command _command) {
		this.command = _command;
	}

	public void action() {
		this.command.execute();
	}
}

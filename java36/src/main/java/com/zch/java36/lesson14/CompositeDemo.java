package com.zch.java36.lesson14;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 组合模式
 * @author zch
 * @time 2018年7月23日 下午2:56:02
 * 
 */
public class CompositeDemo {
	public static void main(String[] args) {
		Company root = new ConcreteCompany();
		root.setName("北京总公司");
		root.add(new HRDepartment("总公司人力资源部"));
		root.add(new FinanceDepartment("总公司财务部"));
		Company shandongCom = new ConcreteCompany("山东分公司");
		shandongCom.add(new HRDepartment("山东分公司人力资源部"));
		shandongCom.add(new FinanceDepartment("山东分公司账务部"));
		Company zaozhuangCom = new ConcreteCompany("枣庄办事处");
		zaozhuangCom.add(new FinanceDepartment("枣庄办事处财务部"));
		zaozhuangCom.add(new HRDepartment("枣庄办事处人力资源部"));
		Company jinanCom = new ConcreteCompany("济南办事处");
		jinanCom.add(new FinanceDepartment("济南办事处财务部"));
		jinanCom.add(new HRDepartment("济南办事处人力资源部"));
		shandongCom.add(jinanCom);
		shandongCom.add(zaozhuangCom);
		Company huadongCom = new ConcreteCompany("上海华东分公司");
		huadongCom.add(new HRDepartment("上海华东分公司人力资源部"));
		huadongCom.add(new FinanceDepartment("上海华东分公司账务部"));
		Company hangzhouCom = new ConcreteCompany("杭州办事处");
		hangzhouCom.add(new FinanceDepartment("杭州办事处财务部"));
		hangzhouCom.add(new HRDepartment("杭州办事处人力资源部"));
		Company nanjingCom = new ConcreteCompany("南京办事处");
		nanjingCom.add(new FinanceDepartment("南京办事处财务部"));
		nanjingCom.add(new HRDepartment("南京办事处人力资源部"));
		huadongCom.add(hangzhouCom);
		huadongCom.add(nanjingCom);
		root.add(shandongCom);
		root.add(huadongCom);
		root.display(0);
	}

}

abstract class Company {
	private String name;

	public Company(String name) {
		this.name = name;
	}

	public Company() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected abstract void add(Company company);

	protected abstract void romove(Company company);

	protected abstract void display(int depth);
}

class ConcreteCompany extends Company {
	private List<Company> cList;

	public ConcreteCompany() {
		cList = new ArrayList<Company>();
	}

	public ConcreteCompany(String name) {
		super(name);
		cList = new ArrayList<Company>();
	}

	@Override
	protected void add(Company company) {
		cList.add(company);
	}

	@Override
	protected void display(int depth) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < depth; i++) {
			sb.append("-");
		}
		System.out.println(new String(sb) + this.getName());
		for (Company c : cList) {
			c.display(depth + 2);
		}
	}

	@Override
	protected void romove(Company company) {
		cList.remove(company);
	}
}

class FinanceDepartment extends Company {

	public FinanceDepartment() {

	}

	public FinanceDepartment(String name) {
		super(name);
	}

	@Override
	protected void add(Company company) {

	}

	@Override
	protected void display(int depth) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < depth; i++) {
			sb.append("-");
		}
		System.out.println(new String(sb) + this.getName());
	}

	@Override
	protected void romove(Company company) {

	}

}

class HRDepartment extends Company {

	public HRDepartment() {

	}

	public HRDepartment(String name) {
		super(name);
	}

	@Override
	protected void add(Company company) {

	}

	@Override
	protected void display(int depth) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < depth; i++) {
			sb.append("-");
		}
		System.out.println(new String(sb) + this.getName());
	}

	@Override
	protected void romove(Company company) {

	}

}

package com.zch.java36.lesson8;

import java.util.LinkedList;

public class App {
	public static void main(String[] args) {
		LinkedList<String> aList = new LinkedList<String>();
		aList.add("a");
		aList.add("b");
		aList.add("c");
		aList.add("d");
		aList.add("e");
		
		System.out.println(aList.getFirst());
	}
}

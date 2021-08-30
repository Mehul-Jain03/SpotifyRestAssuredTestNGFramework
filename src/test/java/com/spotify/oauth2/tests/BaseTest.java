package com.spotify.oauth2.tests;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;

public class BaseTest {
	
	@BeforeMethod
	public void beforeMethod(Method m) {
		System.out.println("Starting Test" + m.getName());
		System.out.println("Thread Id" + Thread.currentThread().getId());
	}

}
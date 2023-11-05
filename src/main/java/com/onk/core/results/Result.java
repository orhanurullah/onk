package com.onk.core.results;

import lombok.Getter;

@Getter
public class Result {
	
	private final boolean success;
	private String message;
	
	public Result(boolean success) {
		// TODO Auto-generated constructor stub
		this.success = success;
	}
	
	public Result(boolean success, String message) {
		// TODO Auto-generated constructor stub
		this(success);
		this.message = message;
	}


}

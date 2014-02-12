package com.github.vdurmont.doit.exception;

public class NoConnectedUserException extends DoitException {
	public NoConnectedUserException() {
		super("You have to be connected.");
	}
}

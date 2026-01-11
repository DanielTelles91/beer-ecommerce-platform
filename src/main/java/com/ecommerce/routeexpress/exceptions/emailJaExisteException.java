package com.ecommerce.routeexpress.exceptions;

/**
 *
 * @author Daniel Arantes Telles
 */

public class emailJaExisteException extends RuntimeException {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public emailJaExisteException(String msg) {
		super(msg);
	}
}

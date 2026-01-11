package com.ecommerce.routeexpress.exceptions;

/**
 *
 * @author Daniel Arantes Telles
 */
public class CpfJaExisteException extends RuntimeException {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public CpfJaExisteException(String msg) {
		super(msg);
	}

}

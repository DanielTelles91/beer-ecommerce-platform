package com.ecommerce.routeexpress.exceptions;

/**
*
* @author Daniel Arantes Telles
*/
public class CervejaJaExisteException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CervejaJaExisteException(String msg) {
        super(msg);
    }
}

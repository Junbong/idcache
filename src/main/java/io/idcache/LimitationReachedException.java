package io.idcache;


/**
 * Created by bong on 15. 4. 23.
 */
public class LimitationReachedException extends RuntimeException {
	public LimitationReachedException(String message) {
		super(message);
	}



	public LimitationReachedException(String message, Throwable cause) {
		super(message, cause);
	}



	public LimitationReachedException(Throwable cause) {
		super(cause);
	}



	public LimitationReachedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}



	public LimitationReachedException() {}
}

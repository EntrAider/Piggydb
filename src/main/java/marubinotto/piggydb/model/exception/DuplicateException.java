package marubinotto.piggydb.model.exception;

public class DuplicateException extends Exception {

	public DuplicateException() {
		super();
	}

	public DuplicateException(String message) {
		super(message);
	}

	public DuplicateException(String message, Throwable cause) {
		super(message, cause);
	}
}

package roomescape.exception;

public class NotFoundReservationException extends RuntimeException {
  private static final String message = "cannot find reservation";

  public NotFoundReservationException(Long id) { super(message + " with id: " + id); }
}

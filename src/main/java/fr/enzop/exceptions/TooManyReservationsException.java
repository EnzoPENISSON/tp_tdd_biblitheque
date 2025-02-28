package fr.enzop.exceptions;

public class TooManyReservationsException extends RuntimeException  {

    public TooManyReservationsException(String message) {
        super(message);
    }
    
}

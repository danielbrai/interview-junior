package br.com.brainweb.interview.core.exception;

public class HeroNotFoudException extends ResourceNotFoundException {

    public HeroNotFoudException(String msg) {
        super(msg);
    }
}

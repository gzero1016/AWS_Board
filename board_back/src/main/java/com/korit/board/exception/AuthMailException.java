package com.korit.board.exception;

import lombok.Getter;

import java.util.Map;

public class AuthMailException extends RuntimeException {

    // 생성자를 매개변수로 받아서 예외메세지 터트림
    public AuthMailException(String message) {
        super(message);
    }
}

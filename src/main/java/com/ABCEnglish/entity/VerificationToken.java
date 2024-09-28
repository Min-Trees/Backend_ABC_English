package com.ABCEnglish.entity;

import java.util.UUID;

public class VerificationToken {
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
}

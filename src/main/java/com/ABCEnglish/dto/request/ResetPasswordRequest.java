package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    String email;
    String newPassword;
    String codeVetify;
}
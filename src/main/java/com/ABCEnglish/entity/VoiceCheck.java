package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Voice_Check")
@Getter
@Setter
public class VoiceCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voiceId;

    private String recordedUrl;
    private java.math.BigDecimal score;
    private String errorJson;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;
}

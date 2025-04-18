package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "class_offline_sessions")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassOfflineSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sessionId;
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    ClassOffline classOffline;
    @Temporal(TemporalType.DATE)
    Date sessionDate;

}

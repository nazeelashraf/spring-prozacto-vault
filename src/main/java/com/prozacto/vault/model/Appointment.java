package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Appointment {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long appointmentId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @OneToOne
    @JoinColumn(name="doctor_id")
    @JsonIgnoreProperties("clinic")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name="assistant_id")
    @JsonIgnoreProperties("clinic")
    private Assistant assistant;

    @ManyToOne
    @JoinColumn(name="clinic_id", nullable = false)
    @JsonIgnoreProperties({"doctors", "assistants", "patients", "appointments"})
    private Clinic clinic;

    @OneToOne
    @JoinColumn(name="patient_id")
    @JsonIgnoreProperties("clinic")
    private Patient patient;

}

package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long patientId;

    @ManyToOne
    @JoinColumn(name="clinic_id", nullable = false)
    @JsonIgnoreProperties({"doctors", "assistants", "patients", "appointments"})
    private Clinic clinic;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ApplicationUser user;

    private String firstName;
    private String lastName;

    @Lob @Lazy
    @Column(length = 100000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] data;
}

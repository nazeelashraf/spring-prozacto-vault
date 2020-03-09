package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long patientId;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="patient_clinic_map",
            joinColumns=@JoinColumn(name="patient_id"),
            inverseJoinColumns=@JoinColumn(name="clinic_id")
    )
    @JsonIgnoreProperties({"doctors", "assistants", "patients", "appointments"})
    private Set<Clinic> clinics = new HashSet<>();

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

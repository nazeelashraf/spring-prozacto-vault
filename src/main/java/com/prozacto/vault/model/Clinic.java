package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Clinic {

    @Id @GeneratedValue
    private Long clinicId;

    private String name;

    @OneToMany(mappedBy = "clinic", fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnoreProperties("clinic")
    private Set<Doctor> doctors = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnoreProperties("clinic")
    private Set<Assistant> assistants = new HashSet<>();

    @ManyToMany(mappedBy = "clinics", fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnoreProperties("clinics")
    private Set<Patient> patients = new HashSet<>();

    @OneToMany(mappedBy = "clinic", fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnoreProperties("clinic")
    private Set<Appointment> appointments = new HashSet<>();

}

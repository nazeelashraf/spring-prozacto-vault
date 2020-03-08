package com.prozacto.vault.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Assistant {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long assistantId;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(name="clinic_id", nullable = false)
    @JsonIgnoreProperties({"doctors", "assistants", "patients", "appointments"})
    private Clinic clinic;

    private String firstName;
    private String lastName;
}

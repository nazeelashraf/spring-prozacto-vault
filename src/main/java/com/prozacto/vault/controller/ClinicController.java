package com.prozacto.vault.controller;

import com.prozacto.vault.exception.EmptyFieldException;
import com.prozacto.vault.model.Clinic;
import com.prozacto.vault.repository.ClinicRepository;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClinicController {

    @Autowired
    private ClinicRepository clinicRepository;

    @GetMapping("clinic")
    @Secured({"ROLE_ADMIN"})
    List<Clinic> getClinics(){
        return clinicRepository.findAll();
    }

    @PostMapping("clinic")
    @Secured({"ROLE_ADMIN"})
    Clinic postClinic(@RequestBody Clinic clinic){
        if(clinic.getName()==null) throw new EmptyFieldException("name");
        return clinicRepository.save(clinic);
    }
 }

package com.prozacto.vault.controller;

import com.prozacto.vault.exception.ClinicNotFoundException;
import com.prozacto.vault.exception.DownloadFileException;
import com.prozacto.vault.exception.PatientNotFoundException;
import com.prozacto.vault.exception.UserNotFoundException;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.model.Doctor;
import com.prozacto.vault.model.Patient;
import com.prozacto.vault.model.Role;
import com.prozacto.vault.repository.ClinicRepository;
import com.prozacto.vault.repository.PatientRepository;
import com.prozacto.vault.repository.RoleRepository;
import com.prozacto.vault.repository.UserRepository;
import com.prozacto.vault.security.TokenUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/patient")
    @Secured({"ROLE_DOCTOR", "ROLE_ASSISTANT"})
    List<Patient>  getPatients(@RequestHeader("Authorization") String token){
        return patientRepository.patientsInClinic(tokenUtil.getClinicId(token));
    }

    @PostMapping("/patient")
    @Secured({"ROLE_DOCTOR", "ROLE_ASSISTANT"})
    Patient postPatient(@RequestBody Patient patient){
        ApplicationUser user = userRepository.findById(patient.getUser().getId())
                .orElseThrow(() -> {return new UserNotFoundException(patient.getUser().getId());});
        clinicRepository.findById(patient.getClinic().getClinicId())
                .orElseThrow(() -> {return new ClinicNotFoundException(patient.getClinic().getClinicId());});

        Role role = roleRepository.findByName("ROLE_PATIENT");
        user.setRoles(Arrays.asList(role));
        patient.setUser(user);
        return patientRepository.save(patient);

    }

    @GetMapping("/patient/documents/{patientId}")
    @Secured({"ROLE_DOCTOR"})
    public void getDocuments(@PathVariable("patientId") Long patientId,
                             HttpServletResponse response){

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {return new PatientNotFoundException(patientId);});

        try{

            response.getHeaders().

            InputStream is = new ByteArrayInputStream(patient.getData());
            IOUtils.copy(is, response.getOutputStream());
            response.setContentType(MediaType.);
            response.flushBuffer();
        } catch(Exception e){
            throw new DownloadFileException();
        }
    }

    @PostMapping("/patient/upload")
    @Secured({"ROLE_PATIENT"})
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token
            ){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();

                Patient patient = patientRepository.findPatientByUserId(tokenUtil.getUserId(token));

                patient.setData(bytes);

                patientRepository.save(patient);

                return "Successfully uploaded file!";
            } catch(Exception e){
                return "Failed to upload file";
            }
        }

        return "Failed to upload file";
    }

}

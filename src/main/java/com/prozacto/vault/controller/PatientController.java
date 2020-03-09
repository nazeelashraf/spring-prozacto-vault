package com.prozacto.vault.controller;

import com.prozacto.vault.exception.*;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.model.Clinic;
import com.prozacto.vault.model.Patient;
import com.prozacto.vault.model.Role;
import com.prozacto.vault.repository.ClinicRepository;
import com.prozacto.vault.repository.PatientRepository;
import com.prozacto.vault.repository.RoleRepository;
import com.prozacto.vault.repository.UserRepository;
import com.prozacto.vault.util.TokenUtil;
import com.prozacto.vault.util.ValidationUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ValidationUtil validationUtil;

    @GetMapping("/patient")
    @Secured({"ROLE_DOCTOR", "ROLE_ASSISTANT"})
    List<Patient>  getPatients(@RequestHeader("Authorization") String token){
        return patientRepository.findAllById(patientRepository.patientIdsInClinic(tokenUtil.getClinicId(token)));
    }

    @PostMapping("/patient")
    @Secured({"ROLE_DOCTOR", "ROLE_ASSISTANT"})
    Patient postPatient(@RequestBody Patient patient, @RequestHeader("Authorization") String token){

        validationUtil.validatePatient(patient);

        Long clinicId = tokenUtil.getClinicId(token);

        ApplicationUser user = userRepository.findById(patient.getUser().getId())
                .orElseThrow(() -> {return new UserNotFoundException(patient.getUser().getId());});
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> {return new ClinicNotFoundException(clinicId);});

        patient.getClinics().add(clinic);

        Role role = roleRepository.findByName("ROLE_PATIENT");
        user.setRoles(Arrays.asList(role));
        patient.setUser(user);
        return patientRepository.save(patient);

    }

    @GetMapping("/patient/documents/{patientId}")
    @Secured({"ROLE_DOCTOR"})
    public void getDocuments(@PathVariable("patientId") Long patientId,
                             @RequestHeader("Authorization") String token,
                             HttpServletResponse response){

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {return new PatientNotFoundException(patientId);});

        Long clinicId = tokenUtil.getClinicId(token);

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() ->{return new ClinicNotFoundException(clinicId);});

        if(!patient.getClinics().contains(clinic))
            throw new ForbiddenAccessException(patientId);

        try{
            InputStream is = new ByteArrayInputStream(patient.getData());
            IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/zip");
            response.flushBuffer();
        } catch(Exception e){
            throw new DownloadFileException();
        }
    }

    @GetMapping("/patient/documents")
    @Secured({"ROLE_PATIENT"})
    public void getDocumentsForPatient(@RequestHeader("Authorization") String token,
                             HttpServletResponse response){

        Long userId = tokenUtil.getUserId(token);

        Patient patient = patientRepository.findPatientByUserId(userId);

        try{
            InputStream is = new ByteArrayInputStream(patient.getData());
            IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/zip");
            response.flushBuffer();
        } catch(Exception e){
            throw new DownloadFileException();
        }
    }

    @GetMapping("/patient/clinics")
    @Secured({"ROLE_PATIENT"})
    public List<Clinic> getClinicsWithAccess(@RequestHeader("Authorization") String token,
                                       HttpServletResponse response){

        Long userId = tokenUtil.getUserId(token);

        Patient patient = patientRepository.findPatientByUserId(userId);

        return clinicRepository.findAllById(patientRepository.getClinicIdList(patient.getPatientId())).stream().map(
                (clinic)->{
                    clinic.setAssistants(new HashSet<>());
                    clinic.setAppointments(new HashSet<>());
                    clinic.setPatients(new HashSet<>());

                    return clinic;
        }).collect(Collectors.toList());

    }

    @DeleteMapping("/patient/clinics/{id}")
    @Secured({"ROLE_PATIENT"})
    public String revokeClinicWithAccess(@RequestHeader("Authorization") String token,
                                             @PathVariable("id") Long clinicId,
                                             HttpServletResponse response){

        Long userId = tokenUtil.getUserId(token);

        Patient patient = patientRepository.findPatientByUserId(userId);

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() ->{return new ClinicNotFoundException(clinicId);});

        if(patient.getClinics().contains(clinic))
            patient.getClinics().remove(clinic);

        patientRepository.save(patient);

        return "Access revoked for clinic '"+clinic.getName()+"'.";

    }

    @PutMapping("/patient/clinics/{id}")
    @Secured({"ROLE_PATIENT"})
    public String grantClinicWithAccess(@RequestHeader("Authorization") String token,
                                         @PathVariable("id") Long clinicId,
                                         HttpServletResponse response){

        Long userId = tokenUtil.getUserId(token);

        Patient patient = patientRepository.findPatientByUserId(userId);

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() ->{return new ClinicNotFoundException(clinicId);});

        if(!patient.getClinics().contains(clinic))
            patient.getClinics().add(clinic);

        patientRepository.save(patient);

        return "Access granted for clinic '"+clinic.getName()+"'.";

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

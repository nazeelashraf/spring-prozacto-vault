package com.prozacto.vault.repository;

import com.prozacto.vault.model.Assistant;
import com.prozacto.vault.model.Doctor;
import com.prozacto.vault.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "SELECT * FROM PATIENT WHERE USER_ID=:userId", nativeQuery = true)
    public Patient findPatientByUserId(Long userId);

    @Query(value = "SELECT * FROM PATIENT WHERE CLINIC_ID=:clinicId", nativeQuery = true)
    public List<Patient> patientsInClinic(Long clinicId);

    @Query(value = "SELECT CLINIC_ID FROM PATIENT WHERE USER_ID=:userId", nativeQuery = true)
    public Long getClinicId(Long userId);
}

package com.prozacto.vault.repository;

import com.prozacto.vault.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {


    @Query(value = "SELECT * FROM DOCTOR WHERE USER_ID=:userId", nativeQuery = true)
    public Doctor findDoctorByUserId(Long userId);

    @Query(value = "SELECT * FROM DOCTOR WHERE CLINIC_ID=:clinicId", nativeQuery = true)
    public List<Doctor> doctorsInClinic(Long clinicId);

    @Query(value = "SELECT CLINIC_ID FROM DOCTOR WHERE USER_ID=:userId", nativeQuery = true)
    public Long getClinicId(Long userId);

}

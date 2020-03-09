package com.prozacto.vault.repository;

import com.prozacto.vault.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query(value = "SELECT * FROM APPOINTMENT WHERE CLINIC_ID=:clinicId", nativeQuery = true)
    public List<Appointment> appointmentsInClinic(Long clinicId);

    @Query(value = "SELECT * FROM APPOINTMENT WHERE DOCTOR_ID=:doctorId", nativeQuery = true)
    public List<Appointment> appointmentsWithDoctor(Long doctorId);

    @Query(value = "SELECT * FROM APPOINTMENT WHERE ASSISTANT_ID=:assistantId", nativeQuery = true)
    public List<Appointment> appointmentsWithAssistant(Long assistantId);

    @Query(value = "SELECT * FROM APPOINTMENT WHERE PATIENT_ID=:patientId", nativeQuery = true)
    public List<Appointment> appointmentsWithPatient(Long patientId);
}

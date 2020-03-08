package com.prozacto.vault.repository;

import com.prozacto.vault.model.Assistant;
import com.prozacto.vault.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {

    @Query(value = "SELECT * FROM ASSISTANT WHERE USER_ID=:userId", nativeQuery = true)
    public Assistant findAssistantByUserId(Long userId);

    @Query(value = "SELECT * FROM ASSISTANT WHERE CLINIC_ID=:clinicId", nativeQuery = true)
    public List<Assistant> assistantsInClinic(Long clinicId);

    @Query(value = "SELECT CLINIC_ID FROM ASSISTANT WHERE USER_ID=:userId", nativeQuery = true)
    public Long getClinicId(Long userId);

}

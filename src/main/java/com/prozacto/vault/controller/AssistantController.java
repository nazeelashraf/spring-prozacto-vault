package com.prozacto.vault.controller;

import com.prozacto.vault.exception.ClinicNotFoundException;
import com.prozacto.vault.exception.UserNotFoundException;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.model.Assistant;
import com.prozacto.vault.model.Clinic;
import com.prozacto.vault.model.Role;
import com.prozacto.vault.repository.AssistantRepository;
import com.prozacto.vault.repository.ClinicRepository;
import com.prozacto.vault.repository.RoleRepository;
import com.prozacto.vault.repository.UserRepository;
import com.prozacto.vault.util.TokenUtil;
import com.prozacto.vault.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class AssistantController {

    @Autowired
    private AssistantRepository assistantRepository;

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

    @GetMapping("/assistant")
    @Secured({"ROLE_DOCTOR"})
    List<Assistant> getAssistants(@RequestHeader("Authorization") String token) {
        return assistantRepository.assistantsInClinic(tokenUtil.getClinicId(token));
    }

    @PostMapping("/assistant")
    @Secured({"ROLE_DOCTOR"})
    Assistant postAssistant(@RequestBody Assistant assistant, @RequestHeader("Authorization") String token){

        validationUtil.validateAssistant(assistant);

        ApplicationUser user = userRepository.findById(assistant.getUser().getId())
                .orElseThrow(() -> {return new UserNotFoundException(assistant.getUser().getId());});

        Long clinicId = tokenUtil.getClinicId(token);

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> {return new ClinicNotFoundException(clinicId);});

        assistant.setClinic(clinic);
        Role role = roleRepository.findByName("ROLE_ASSISTANT");
        user.setRoles(Arrays.asList(role));
        assistant.setUser(user);
        return assistantRepository.save(assistant);

    }
}

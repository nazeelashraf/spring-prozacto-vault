package com.prozacto.vault.controller;

import com.prozacto.vault.exception.ClinicNotFoundException;
import com.prozacto.vault.exception.EmptyFieldException;
import com.prozacto.vault.exception.UserNotFoundException;
import com.prozacto.vault.model.*;
import com.prozacto.vault.repository.*;
import com.prozacto.vault.security.TokenUtil;
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

    @GetMapping("/assistant")
    @Secured({"ROLE_DOCTOR"})
    List<Assistant> getAssistants(@RequestHeader("Authorization") String token) {
        return assistantRepository.assistantsInClinic(tokenUtil.getClinicId(token));
    }

    @PostMapping("/assistant")
    @Secured({"ROLE_DOCTOR"})
    Assistant postAssistant(@RequestBody Assistant assistant, @RequestHeader("Authorization") String token){

        if(assistant.getUser()==null) throw new EmptyFieldException("user");
        if(assistant.getUser().getId()==null) throw new EmptyFieldException("user.id");
        if(assistant.getFirstName()==null) throw new EmptyFieldException("firstName");
        if(assistant.getLastName()==null) throw new EmptyFieldException("lastName");
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

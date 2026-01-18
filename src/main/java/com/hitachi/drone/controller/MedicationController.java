package com.hitachi.drone.controller;

import com.hitachi.drone.model.request.MedicationRequestModel;
import com.hitachi.drone.model.response.ApiResponse;
import com.hitachi.drone.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medication")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addMedication(@Valid @RequestBody MedicationRequestModel medicationRequestModel) {
        return new ResponseEntity<>(medicationService.addMedication(medicationRequestModel), HttpStatus.CREATED);
    }

}

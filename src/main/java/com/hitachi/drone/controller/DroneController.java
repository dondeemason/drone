package com.hitachi.drone.controller;

import com.hitachi.drone.model.request.DroneRequestModel;
import com.hitachi.drone.model.response.ApiResponse;
import com.hitachi.drone.model.response.DroneResponse;
import com.hitachi.drone.model.response.DroneWithMedicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hitachi.drone.service.DroneService;

import java.util.List;

@RestController
@RequestMapping("/drone")
public class DroneController {
    private DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<DroneWithMedicationResponse> findDroneWithMedication(@PathVariable String serialNumber) {
        return new ResponseEntity<>(droneService.findDroneWithMedication(serialNumber), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDrone(@Valid @RequestBody DroneRequestModel droneRequestModel) {
        return ResponseEntity.ok(droneService.createDrone(droneRequestModel));
    }

    @PatchMapping("/{serialNumber}/state")
    public ResponseEntity<ApiResponse> moveDroneState(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.moveDroneState(serialNumber));
    }

    @GetMapping("/available")
    public ResponseEntity<List<DroneResponse>> getAvailableDrone() {
        return new ResponseEntity<>(droneService.getDroneAvailability(), HttpStatus.OK);
    }
}

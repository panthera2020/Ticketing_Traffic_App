package com.lastma.services;

import com.lastma.dtos.requests.FindAllVehicleOwnedByDriverRequest;
import com.lastma.dtos.requests.FindVehicleByIdRequest;
import com.lastma.dtos.requests.FindVehicleByPlateNumberRequest;
import com.lastma.dtos.requests.RegisterVehicleRequest;
import com.lastma.dtos.responses.FindAllVehicleOwnedByDriverResponse;
import com.lastma.dtos.responses.FindVehicleByIdResponse;
import com.lastma.dtos.responses.FindVehicleByPlateNumberResponse;
import com.lastma.dtos.responses.RegisterVehicleResponse;

import java.util.List;


public interface VehicleService {
    RegisterVehicleResponse registerVehicle(RegisterVehicleRequest request);

    FindVehicleByIdResponse findVehicleBy(FindVehicleByIdRequest idRequest);

    FindVehicleByPlateNumberResponse findVehicleBy(FindVehicleByPlateNumberRequest plateNumberRequest);

    List<FindAllVehicleOwnedByDriverResponse> findAllVehiclesOwnedBy(FindAllVehicleOwnedByDriverRequest request);
}

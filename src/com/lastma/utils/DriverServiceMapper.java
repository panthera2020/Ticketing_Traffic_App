package com.lastma.utils;

import com.lastma.data.models.Driver;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.responses.FindAllDriverResponse;
import com.lastma.dtos.responses.FindDriverResponse;
import com.lastma.dtos.responses.RegisterDriverResponse;

import java.util.ArrayList;
import java.util.List;

public class DriverServiceMapper {

    public static RegisterDriverResponse mapRegistered(Driver Driver){
        RegisterDriverResponse response = new RegisterDriverResponse();
        response.setId(Driver.getId());
        response.setFullName(Driver.getFullName());
        response.setEmail(Driver.getEmail());
        response.setPhoneNumber(Driver.getPhoneNumber());
        return  response;
    }

    public static Driver map(RegisterDriverRequest driverRequest){
        Driver driver = new Driver();
        driver.setFullName(driverRequest.getFullName());
        driver.setEmail(driverRequest.getMail());
        driver.setPhoneNumber(driverRequest.getPhoneNumber());
        driver.setPassword(driverRequest.getPassword());
        return driver;
    }

    public static FindDriverResponse mapFound(Driver  driver){
        FindDriverResponse response = new FindDriverResponse();
        response.setId(driver.getId());
        response.setFullName(driver.getFullName());
        response.setEmail(driver.getEmail());
        response.setPhoneNumber(driver.getPhoneNumber());
        return  response;
    }

    public static List<FindAllDriverResponse> mapListOf(List<Driver> foundDrivers){
        List<FindAllDriverResponse> responseList = new ArrayList<>();
        for( Driver driver : foundDrivers){
            FindAllDriverResponse response = new FindAllDriverResponse();
            response.setId(driver.getId());
            response.setFullName(driver.getFullName());
            response.setEmail(driver.getEmail());
            response.setPhoneNumber(driver.getPhoneNumber());
            responseList.add(response);
        }
        return responseList;
    }
}

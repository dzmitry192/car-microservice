package com.innowise.carmicroservice.service.impl;

import com.innowise.carmicroservice.service.CarService;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    @Override
    public String getCars() {
        return "All works!!!";
    }
}

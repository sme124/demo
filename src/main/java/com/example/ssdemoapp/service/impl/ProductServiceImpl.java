package com.example.ssdemoapp.service.impl;

import com.example.ssdemoapp.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl {

    public List<ProductDto> getAll() {
        log.info("Get all product details....");

        return Arrays.asList(new ProductDto(1L, "Test product 1"), new ProductDto(2L, "Test Product 2"));
    }
}

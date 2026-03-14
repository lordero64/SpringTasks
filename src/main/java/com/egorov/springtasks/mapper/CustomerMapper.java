package com.egorov.springtasks.mapper;


import com.egorov.springtasks.dto.CustomerDto;
import com.egorov.springtasks.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto dto);

    List<CustomerDto> toDtoList(List<Customer> customers);

    @Mapping(target = "customerId", ignore = true)
    void updateEntity(CustomerDto dto, @MappingTarget Customer customer);
}
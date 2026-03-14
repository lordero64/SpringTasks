package com.egorov.springtasks.mapper;

import com.egorov.springtasks.dto.ProductDto;
import com.egorov.springtasks.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> productList);
    List<Product> toEntityList(List<ProductDto> productDtoList);

    @Mapping(target = "productId", ignore = true)
    void updateEntity(ProductDto dto, @MappingTarget Product product);
}

package com.vklovan.productservice.util;

import com.vklovan.productservice.dto.ProductDto;
import com.vklovan.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product entity);
    Product toEntity(ProductDto productDto);

}

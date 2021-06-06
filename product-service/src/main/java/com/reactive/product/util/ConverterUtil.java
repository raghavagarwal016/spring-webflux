package com.reactive.product.util;

import org.springframework.beans.BeanUtils;

import com.reactive.product.dto.ProductDto;
import com.reactive.product.entity.Product;

public class ConverterUtil {

  public static Product toProductFromProductDto(ProductDto productDto) {
    Product product = new Product();
    BeanUtils.copyProperties(productDto, product);
    return product;
  }

  public static ProductDto toProductDtoFromProduct(Product product) {
    ProductDto productDto = new ProductDto();
    BeanUtils.copyProperties(product, productDto);
    return productDto;
  }

}

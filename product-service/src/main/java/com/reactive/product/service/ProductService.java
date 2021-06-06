package com.reactive.product.service;

import com.reactive.product.dto.ProductDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

  Flux<ProductDto> getAllProducts();

  Mono<ProductDto> findById(String id);

  Mono<ProductDto> saveProduct(Mono<ProductDto> productDto);

  Mono<ProductDto> updateProduct(Mono<ProductDto> productDto);

  Mono<Void> deleteProduct(String id);

  Flux<ProductDto> getProductsInPriceRange(int min, int max);

}

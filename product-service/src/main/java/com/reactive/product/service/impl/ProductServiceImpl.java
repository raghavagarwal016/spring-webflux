package com.reactive.product.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import com.reactive.product.dto.ProductDto;
import com.reactive.product.repository.ProductRepository;
import com.reactive.product.service.ProductService;
import com.reactive.product.util.ConverterUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Sinks.Many<ProductDto> sink;

  @PostConstruct
  public void setUp() {
    ProductDto productDto = new ProductDto("1","4K-tv", 1000);
    ProductDto productDto2 = new ProductDto("2","Slr-Camera", 750);
    ProductDto productDto3 = new ProductDto("3","iphone", 800);
    ProductDto productDto4 = new ProductDto("4","headphone", 100);
    Flux.just(productDto, productDto2, productDto3, productDto4)
        .flatMap(prdDto -> productRepository.insert(Mono.just(ConverterUtil.toProductFromProductDto(prdDto))))
        .subscribe(prdDto -> log.info("insert-" + prdDto));
  }

  @Override
  public Flux<ProductDto> getAllProducts() {
    return productRepository.findAll().map(product -> ConverterUtil.toProductDtoFromProduct(product));
  }

  @Override
  public Mono<ProductDto> findById(String id) {
    return productRepository.findById(id).map(product -> ConverterUtil.toProductDtoFromProduct(product));
  }

  @Override
  public Mono<ProductDto> saveProduct(Mono<ProductDto> productDto) {
    return productDto
        .map(prdDto -> ConverterUtil.toProductFromProductDto(prdDto))
        .flatMap(product -> productRepository.insert(product))
        .map(product -> ConverterUtil.toProductDtoFromProduct(product))
        .doOnNext(this.sink::tryEmitNext);
  }

  @Override
  public Mono<ProductDto> updateProduct(Mono<ProductDto> productDto) {
    return productDto
        .flatMap(prdDto -> {
          return productRepository.findById(prdDto.getId())
              .map(product -> {
                product.setDescription(prdDto.getDescription());
                product.setPrice(prdDto.getPrice());
                return product;
              });
        })
        .flatMap(product -> productRepository.save(product))
        .map(product -> ConverterUtil.toProductDtoFromProduct(product));
  }

  @Override
  public Mono<Void> deleteProduct(String id) {
    return productRepository.deleteById(id);
  }

  @Override
  public Flux<ProductDto> getProductsInPriceRange(int min, int max) {
    return productRepository.findByPriceBetween(Range.closed(min, max))
        .map(product -> ConverterUtil.toProductDtoFromProduct(product));
  }


}

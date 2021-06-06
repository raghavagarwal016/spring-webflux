package com.reactive.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.product.dto.ProductDto;
import com.reactive.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

  @Autowired
  private ProductService productService;

  @Autowired
  private Flux<ProductDto> productBroadcast;

  @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<ProductDto>> getAllProducts() {
    return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<ProductDto>> getAllProducts(@PathVariable("id") String id) {
    log.info(id);
    return
        productService.findById(id)
            .map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping(value = "/price-range", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<ProductDto>> getProductsInPriceRange(@RequestParam("min") int min,
      @RequestParam("max") int max) {
    return new ResponseEntity<>(productService.getProductsInPriceRange(min, max), HttpStatus.OK);

  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<ProductDto>> getProductStream() {
    return new ResponseEntity<>(productBroadcast, HttpStatus.OK);

  }

  @PostMapping("/save")
  public Mono<ResponseEntity<ProductDto>> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
    return
        productService.saveProduct(productDtoMono)
            .map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK));
  }

  @PutMapping("/update")
  public Mono<ResponseEntity<ProductDto>> updateProduct(@RequestBody Mono<ProductDto> productDtoMono) {
    return
        productService.updateProduct(productDtoMono)
            .map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("id") String id) {
    return productService.deleteProduct(id)
        .map(v -> new ResponseEntity<>(v, HttpStatus.OK));
  }

}

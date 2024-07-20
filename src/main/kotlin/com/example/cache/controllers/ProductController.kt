package com.example.cache.controllers

import com.example.cache.models.Product
import com.example.cache.repository.ProductRepository
import org.apache.coyote.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/products")
@CacheConfig(cacheNames = ["products"])
class ProductController {

    @Autowired lateinit var productRepository: ProductRepository
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("{product_id}")
    @Cacheable(value = ["products"], key = "#productId")
    fun detail(@PathVariable("product_id") productId: Long): Product {
        log.info("Getting product with ID {}.", productId)
        return productRepository.findById(productId).orElseThrow { BadRequestException("Product not found") }
    }
}

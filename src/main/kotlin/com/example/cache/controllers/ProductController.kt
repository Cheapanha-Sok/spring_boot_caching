package com.example.cache.controllers

import com.example.cache.cache.ProductService
import com.example.cache.models.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("{product_id}")
    fun detail(@PathVariable("product_id") productId: Long): Product? {
        return productService.detail(productId)
    }
    @GetMapping("/list")
    fun list(): List<Product> {
        return productService.list()
    }
    @PostMapping
    fun add(@RequestBody product: Product): Product {
        return productService.create(product)
    }
    @PutMapping("{product_id}")
    fun update(@PathVariable("product_id") productId: Long, @RequestBody product: Product): Product {
        return productService.update(productId, product)
    }
    @DeleteMapping("{product_id}")
    fun delete(@PathVariable("product_id") productId: Long) {
        return productService.delete(productId)
    }
}

package com.example.cache.cache.impl

import com.example.cache.cache.ProductService
import com.example.cache.models.Product
import com.example.cache.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@CacheConfig(cacheNames = ["products"])
class IProductService : ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["products"], unless = "#result == null")
    override fun list(): List<Product> {
        log.info("Getting list of products")
        return productRepository.findAll()
    }

    @Cacheable(cacheNames = ["products"], key = "#id", unless = "#result == null")
    override fun detail(id: Long): Product? {
        log.info("Getting product with id {}", id)
        return productRepository.findByIdOrNull(id)
    }

    @CacheEvict(cacheNames = ["products"], allEntries = true)
    override fun create(product: Product): Product {
        log.info("Creating product: {}", product)
        return productRepository.save(product)
    }

    @CachePut(cacheNames = ["products"], key = "#id")
    @CacheEvict(cacheNames = ["products"], allEntries = true)
    override fun update(id: Long, updateProduct: Product): Product {
        log.info("Updating product with id {}", id)
        val product = productRepository.findById(id).orElseThrow { IllegalArgumentException("Product not found") }
        product.name = updateProduct.name
        product.description = updateProduct.description
        product.price = updateProduct.price
        productRepository.save(product).let { return it }
    }

    @CacheEvict(value = ["products"], key = "#id", allEntries = true)
    override fun delete(id: Long) {
        val product = productRepository.findById(id).orElseThrow { IllegalArgumentException("Product not found") }
        log.info("Deleting product with id {}", id)
        productRepository.delete(product)
    }
}

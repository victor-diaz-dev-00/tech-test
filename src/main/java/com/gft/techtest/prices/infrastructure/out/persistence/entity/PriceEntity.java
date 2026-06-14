package com.gft.techtest.prices.infrastructure.out.persistence.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "prices")
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_price_brand"))
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_priceproduct"))
    private ProductEntity product;

    @Column(name = "price_list", nullable = false)
    private Integer priceList;

    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endDate;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "curr", length = 3, nullable = false)
    private String currency;

    public Long getId() { return id; }
    public Long getBrandId() {
        return brand != null ? brand.getId() : null;
    }
    public BrandEntity getBrand() {
        return brand;
    }
    public Long getProductId() {
        return product != null ? product.getId() : null;
    }
    public ProductEntity getProduct() { return product; }
    public Integer getPriceList() { return priceList; }
    public OffsetDateTime getStartDate() { return startDate; }
    public OffsetDateTime getEndDate() { return endDate; }
    public Integer getPriority() { return priority; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }

    public void setId(Long id) { this.id = id; }
    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }
    public void setProduct(ProductEntity product) { this.product = product; }
    public void setPriceList(Integer priceList) { this.priceList = priceList; }
    public void setStartDate(OffsetDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(OffsetDateTime endDate) { this.endDate = endDate; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCurrency(String currency) { this.currency = currency; }
}

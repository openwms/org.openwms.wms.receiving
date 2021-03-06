/*
 * Copyright 2005-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.wms.receiving.inventory;

import org.ameba.integration.jpa.ApplicationEntity;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.openwms.core.units.UnitConstants;
import org.openwms.core.units.api.Measurable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Product.
 * 
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "WMS_REC_PRODUCT",
        uniqueConstraints = @UniqueConstraint(name = "UC_REC_PRODUCT_SKU", columnNames = {"C_SKU"})
)
public class Product extends ApplicationEntity implements Comparable<Product>, Serializable {

    /** The product id is part of the unique business key. */
    @Column(name = "C_SKU")
    @NotEmpty
    private String sku;

    /** Textual descriptive text. */
    @Column(name = "C_DESCRIPTION")
    private String description;

    /** Products may be defined with different base units. */
    @Type(type = "org.openwms.core.units.persistence.UnitUserType")
    @Columns(columns = {
            @Column(name = "C_BASE_UNIT_TYPE", nullable = false),
            @Column(name = "C_BASE_UNIT_QTY", length = UnitConstants.QUANTITY_LENGTH, nullable = false)
    })
    //problems when mapping
    //@NotNull
    private Measurable baseUnit;

    /** Dear JPA ... */
    protected Product() {
    }

    /**
     * Create a Product with a sku.
     *
     * @param sku The sku
     */
    public Product(String sku) {
        this.sku = sku;
    }

    /**
     * Get the SKU.
     * 
     * @return the SKU.
     */
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Measurable getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Measurable baseUnit) {
        this.baseUnit = baseUnit;
    }

    /**
     * {@inheritDoc}
     *
     * Uses the sku for comparison
     */
    @Override
    public int compareTo(Product o) {
        return null == o ? -1 : this.sku.compareTo(o.sku);
    }

    /**
     * {@inheritDoc}
     *
     * SKU only.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(sku, product.sku);
    }

    /**
     * {@inheritDoc}
     *
     * SKU only.
     */
    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    /**
     * {@inheritDoc}
     * 
     * Return the SKU;
     */
    @Override
    public String toString() {
        return sku;
    }
}
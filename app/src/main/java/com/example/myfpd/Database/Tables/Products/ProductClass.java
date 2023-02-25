package com.example.myfpd.Database.Tables.Products;

import java.util.Date;

public class ProductClass {
    public long id;
    public String stockCode;
    public Date expirationDate;
    public String categoryName;
    public long categoryId;

    public ProductClass(
            long id,
            String stockCode,
            Date expirationDate,
            String categoryName,
            long categoryId
    ) {
        this.id = id;
        this.stockCode = stockCode;
        this.expirationDate = expirationDate;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }
}

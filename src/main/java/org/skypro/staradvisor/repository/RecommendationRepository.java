package org.skypro.staradvisor.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationRepository{
private final JdbcTemplate jdbcTemplate;

public static final String PRODUCT_TYPE_DEBIT="DEBIT";
public static final String PRODUCT_TYPE_CREDIT="CREDIT";
public static final String PRODUCT_TYPE_SAVING="SAVING";
public static final String PRODUCT_TYPE_INVEST="INVEST";

public static final String TRANSACTION_TYPE_DEPOSIT="DEPOSIT";
public static final String TRANSACTION_TYPE_WITHDRAW="WITHDRAW";

public RecommendationRepository(JdbcTemplate jdbcTemplate){
    this.jdbcTemplate=jdbcTemplate;
}

@Cacheable(value="productTypeCache", key="#user_id.toString() + '-' + #productType")
public boolean userUsesProductType(UUID user_id,String productType){

    return Boolean.TRUE.equals(jdbcTemplate.queryForObject("""
     SELECT COUNT(*) > 0 FROM transactions t
     JOIN products p ON t.product_id = p.id
     WHERE t.user_id = ? AND p.type = ?
     """,Boolean.class,user_id.toString(),productType));
}

@Cacheable(value="transactionSumCache", key="#user_id.toString() + '-' + #transactionType + '-' + #productType")
public BigDecimal getSumByTransactionTypeAndProductType(UUID user_id,String transactionType,String productType){

    return jdbcTemplate.queryForObject("""
     SELECT COALESCE(SUM(t.amount), 0) 
     FROM transactions t
     JOIN products p ON t.product_id = p.id
     WHERE t.user_id = ? AND t.type = ? AND p.type = ?
     """,BigDecimal.class,user_id.toString(),transactionType,productType);
}

@Cacheable(value="transactionByProduct", key="#user_id.toString() + '-' + #transactionType + '-' + #productType")
public Integer getTransactionCountByProductType(@Param("user_id") UUID user_id,@Param("productType") String productType){
    Integer count=jdbcTemplate.queryForObject("""
     SELECT COUNT(*) FROM transactions t
     JOIN products p ON t.product_id = p.id
     WHERE t.user_id = ? AND p.type = ?
     """,Integer.class,user_id.toString(),productType);
    return count!=null?count:0;
}
}

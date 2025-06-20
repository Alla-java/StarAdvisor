package org.skypro.staradvisor.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

@Cacheable(value="productTypeCache", key="#userId.toString() + '-' + #productType")
public boolean userUsesProductType(UUID userId,String productType){

    return Boolean.TRUE.equals(jdbcTemplate.queryForObject("""
     SELECT COUNT(*) > 0 FROM transactions t
     JOIN products p ON t.product_id = p.id
     WHERE t.user_id = ? AND p.type = ?
     """,Boolean.class,userId.toString(),productType));
}

@Cacheable(value="transactionSumCache", key="#userId.toString() + '-' + #transactionType + '-' + #productType")
public BigDecimal getSumByTransactionTypeAndProductType(UUID userId,String transactionType,String productType){

    return jdbcTemplate.queryForObject("""
     SELECT COALESCE(SUM(t.amount), 0) 
     FROM transactions t
     JOIN products p ON t.product_id = p.id
     WHERE t.user_id = ? AND t.type = ? AND p.type = ?
     """,BigDecimal.class,userId.toString(),transactionType,productType);
}

@Cacheable("transactionCountCache")
public Integer getTransactionCountByProductType(UUID userId,String productType){
    return jdbcTemplate.queryForObject("""
     SELECT COUNT(*) FROM transactions t 
     JOIN products p ON t.product_id = p.id 
     WHERE t.user_id = ? AND p.type = ?
     """,Integer.class,userId,productType);
}


@Cacheable("userIdByUsernameCache")
public Optional<UUID> findUserIdByUsername(String username) {
    String sql = "SELECT id FROM users WHERE username = ?";
    List<UUID> results = jdbcTemplate.query(sql, new Object[]{username}, (rs,rowNum) -> UUID.fromString(rs.getString("id")));

    if (results.size() == 1) {
        return Optional.of(results.get(0));
    } else {
        return Optional.empty(); // либо не найдено, либо найдено больше одного
    }
}

@Cacheable("userFullNameByUserIdCache")
public String getUserFullName(UUID userId){
    try{
        return jdbcTemplate.queryForObject("""
         SELECT CONCAT(FIRST_NAME,' ',LAST_NAME) FROM users 
         WHERE id = ?
         """,String.class,userId);
    }catch(EmptyResultDataAccessException e){
        throw new NoSuchElementException("User not found with userId: "+userId);
    }


}

}

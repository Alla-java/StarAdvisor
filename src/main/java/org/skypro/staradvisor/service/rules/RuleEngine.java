package org.skypro.staradvisor.service.rules;


import org.skypro.staradvisor.model.RuleQuery;
import org.skypro.staradvisor.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class RuleEngine{

private final RecommendationRepository recommendationRepository;

public RuleEngine(RecommendationRepository recommendationRepository){
    this.recommendationRepository=recommendationRepository;
}

public boolean evaluate(UUID userId,List<RuleQuery> rules){
    for(RuleQuery rule: rules){
        boolean result=switch(rule.getQuery()){
            case "USER_OF" -> checkUserOf(userId,rule.getArguments().get(0));
            case "ACTIVE_USER_OF" -> checkActiveUserOf(userId,rule.getArguments().get(0));
            case "TRANSACTION_SUM_COMPARE" -> checkTransactionSumCompare(userId,rule.getArguments());
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> checkDepositVsWithdraw(userId,rule.getArguments());
            default -> throw new IllegalArgumentException("Неизвестный тип запроса: "+rule.getQuery());
        };

        if(rule.isNegate()){
            result=!result;
        }

        if(!result)
            return false;
    }
    return true;
}

private boolean checkUserOf(UUID userId,String productType){
    return recommendationRepository.userUsesProductType(userId,productType);
}

private boolean checkActiveUserOf(UUID userId,String productType){
    String sql="""
         SELECT COUNT(*) FROM transactions t
         JOIN products p ON t.product_id = p.id
         WHERE t.user_id = ? AND p.type = ?
     """;
    Integer count=recommendationRepository.getTransactionCountByProductType(userId,productType);
    return count>=5;
}

private boolean checkTransactionSumCompare(UUID userId,List<String> args){
    String productType=args.get(0);
    String transactionType=args.get(1);
    String operator=args.get(2);
    int value=Integer.parseInt(args.get(3));

    BigDecimal sum=recommendationRepository.getSumByTransactionTypeAndProductType(userId,transactionType,productType);

    return compare(sum.intValue(),operator,value);
}

private boolean checkDepositVsWithdraw(UUID userId,List<String> args){
    String productType=args.get(0);
    String operator=args.get(1);

    BigDecimal depositSum=recommendationRepository.getSumByTransactionTypeAndProductType(userId,"DEPOSIT",productType);
    BigDecimal withdrawSum=recommendationRepository.getSumByTransactionTypeAndProductType(userId,"WITHDRAW",productType);

    return compare(depositSum.intValue(),operator,withdrawSum.intValue());
}

private boolean compare(int a,String operator,int b){
    return switch(operator){
        case ">" -> a>b;
        case "<" -> a<b;
        case "=" -> a==b;
        case ">=" -> a>=b;
        case "<=" -> a<=b;
        default -> throw new IllegalArgumentException("Некорректный оператор сравнения: "+operator);
    };
}

}

package edu.uit.se122.server.promotion.internal.engine;

import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import org.mvel2.MVEL;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PromotionEngine {
    // Cache lưu trữ các script đã được biên dịch (Compiled) để chạy cực nhanh
    private final Map<String, Serializable> compiledScriptsCache = new ConcurrentHashMap<>();

    /**
     * Biên dịch hoặc lấy script đã biên dịch từ Cache
     */
    private Serializable getCompiledScript(String script) {
        return compiledScriptsCache.computeIfAbsent(script, MVEL::compileExpression);
    }

    /**
     * Tính toán số tiền được giảm giá
     */
    public BigDecimal executeScript(String script, PromotionContract.OrderContext context, Promotion promotion) {
        try {
            Serializable compiled = getCompiledScript(script);
            Map<String, Object> vars = Map.of("context", context);

            Object result = MVEL.executeExpression(compiled, vars);

            if (result instanceof BigDecimal) {
                context.getPromotionDescriptions().add(promotion.getDescription());
                return (BigDecimal) result;
            } else if (result instanceof Number) {
                context.getPromotionDescriptions().add(promotion.getDescription());
                return BigDecimal.valueOf(((Number) result).doubleValue());
            }
            return BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("Lỗi thực thi Script: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}

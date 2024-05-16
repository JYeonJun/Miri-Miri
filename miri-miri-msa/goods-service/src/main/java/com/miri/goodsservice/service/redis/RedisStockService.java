package com.miri.goodsservice.service.redis;

import com.miri.coremodule.handler.ex.StockNotFoundException;
import com.miri.coremodule.handler.ex.StockUnavailableException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisStockService {
    private static final String GOODS_STOCK_KEY_NAMESPACE = "goods_stock:";

    private RedisTemplate<String, Integer> redisTemplate;
    private RedisTemplate<String, String> redisStringTemplate;
    private ValueOperations<String, Integer> valueOperations;

    public RedisStockService(RedisTemplate<String, Integer> redisTemplate,
                             RedisTemplate<String, String> redisStringTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisStringTemplate = redisStringTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    // 상품 재고 저장
    public void setGoodsStock(Long goodsId, int quantity) {
        valueOperations.setIfAbsent(GOODS_STOCK_KEY_NAMESPACE + goodsId, quantity);
    }

    // 상품 재고 저장(ttl 적용)
    public void setGoodsStock(Long goodsId, int quantity, int timeout, TimeUnit unit) {
        valueOperations.setIfAbsent(GOODS_STOCK_KEY_NAMESPACE + goodsId, quantity, timeout, unit);
    }

    // 상품 재고 조회
    public Integer getGoodsStock(Long goodsId) {
        return valueOperations.get(GOODS_STOCK_KEY_NAMESPACE + goodsId);
    }

    // 상품 재고 증가
    public void increaseGoodsStock(Long goodsId, int quantity) {
        valueOperations.increment(GOODS_STOCK_KEY_NAMESPACE + goodsId, quantity);
    }

    public void increaseGoodsStockWithLua(Long goodsId, int quantity) {
        String luaScript =
                "local stock = redis.call('get', KEYS[1])\n" +
                        "if stock == false then\n" +
                        "   return nil\n" +
                        "else\n" +
                        "   local newStock = redis.call('incrby', KEYS[1], ARGV[1])\n" +
                        "   redis.call('sadd', 'dirty_goods', KEYS[1])\n" + // 'dirty' 목록에 추가
                        "   return newStock\n" +
                        "end";

        // 스크립트 실행
        Long result = redisTemplate.execute(
                new DefaultRedisScript<Long>(luaScript, Long.class),
                Collections.singletonList("goods_stock:" + goodsId),
                quantity);

        if (result == null) {
            log.info("상품 재고 정보가 존재하지 않습니다. goodsId={}", goodsId);
            throw new StockNotFoundException();
        }

        log.info("상품 재고 증가 성공. goodsId={}, newStock={}", goodsId, result);
    }

    public void increaseGoodsStockWithLuaBatch(Map<Long, Integer> goodsIdToQuantityMap) {
        String luaScript =
                "for i=1,#KEYS do\n" +
                        "   local stock = redis.call('get', KEYS[i])\n" +
                        "   if stock ~= false then\n" +
                        "       local newStock = redis.call('incrby', KEYS[i], ARGV[i])\n" +
                        "       redis.call('sadd', 'dirty_goods', KEYS[i])\n" + // 'dirty' 목록에 추가
                        "   end\n" +
                        "end";

        // 스크립트 실행을 위한 키와 값 설정
        List<String> keys = goodsIdToQuantityMap.keySet().stream()
                .map(goodsId -> "goods_stock:" + goodsId)
                .collect(Collectors.toList());
        List<String> values = goodsIdToQuantityMap.values().stream()
                .map(String::valueOf)
                .toList();

        redisTemplate.execute(new DefaultRedisScript<Void>(luaScript, Void.class), keys, values.toArray());
    }

    // 상품 재고 감소
    public void decreaseGoodsStock(Long goodsId, int quantity) {
        valueOperations.increment(GOODS_STOCK_KEY_NAMESPACE + goodsId, -quantity);
    }

    public void decreaseGoodsStockWithLua(Long goodsId, int quantity) {
        String luaScript =
                "local stock = redis.call('get', KEYS[1])\n" +
                        "if stock == false then\n" +
                        "   return -2\n" +
                        "elseif tonumber(stock) >= tonumber(ARGV[1]) then\n" +
                        "   local newStock = tonumber(stock) - tonumber(ARGV[1])\n" +
                        "   redis.call('decrby', KEYS[1], ARGV[1])\n" +
                        "   redis.call('sadd', 'dirty_goods', KEYS[1])\n" + // 'dirty' 목록에 추가
                        "   return newStock\n" +
                        "else\n" +
                        "   return -1\n" +
                        "end";

        Long result = redisTemplate.execute(
                new DefaultRedisScript<Long>(luaScript, Long.class),
                Collections.singletonList("goods_stock:" + goodsId),
                quantity);

        if (result == -1) {
            log.info("상품 재고가 부족합니다. goodsId={}, requestedQuantity={}", goodsId, quantity);
            throw new StockUnavailableException();
        } else if (result == -2) {
            log.info("상품 재고 정보가 존재하지 않습니다. goodsId={}", goodsId);
            throw new StockNotFoundException();
        }

        log.info("상품 재고 감소 성공. goodsId={}, remainingStock={}", goodsId, result);
    }

    public Map<Long, Integer> getDirtyGoodsStockWithLua() {
        String luaScript =
                "local keys = redis.call('smembers', 'dirty_goods')\n"
                        + "local result = {}\n"
                        + "for i, key in ipairs(keys) do\n"
                        + "    local stock = redis.call('get', key)\n"
                        + "    table.insert(result, {key, stock})\n"
                        + "end\n"
                        + "return result\n";

        List results = redisStringTemplate.execute(
                new DefaultRedisScript<>(luaScript, List.class),
                Collections.emptyList());

        Map<Long, Integer> stockMap = new HashMap<>();
        for (Object result : results) {
            List<Object> keyAndStock = (List<Object>) result;
            String key = (String) keyAndStock.get(0);
            Integer stock = Integer.parseInt((String) keyAndStock.get(1));
            Long goodsId = Long.parseLong(key.split(":")[1]);
            stockMap.put(goodsId, stock);
        }

        return stockMap;
    }


    public void clearDirtyGoodsListWithLua() {
        String luaScript =
                "redis.call('del', 'dirty_goods')\n" +
                        "return 1";

        Long result = redisTemplate.execute(
                new DefaultRedisScript<Long>(luaScript, Long.class),
                Collections.emptyList());

        if (result == 1) {
            log.info("dirty_goods 목록이 성공적으로 초기화되었습니다.");
        } else {
            log.error("dirty_goods 목록 초기화 중 오류 발생");
        }
    }


    // 모든 상품 재고 데이터 삭제
    public void deleteAllGoodsStock() {
        // 상품 재고에 해당하는 모든 키를 찾음
        Set<String> keys = redisTemplate.keys(GOODS_STOCK_KEY_NAMESPACE + "*");
        if (keys != null && !keys.isEmpty()) {
            // 찾은 모든 키를 삭제
            redisTemplate.delete(keys);
            log.info("모든 상품 재고 데이터가 삭제되었습니다.");
        } else {
            log.info("삭제할 상품 재고 데이터가 없습니다.");
        }
    }
}

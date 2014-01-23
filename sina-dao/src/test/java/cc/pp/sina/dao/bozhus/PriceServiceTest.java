package cc.pp.sina.dao.bozhus;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.Test;

import cc.pp.sina.domain.bozhus.Bozhu;
import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.domain.bozhus.price.PriceSource;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * User: chenwei@pp.cc
 * Date: 14-1-11
 * Time: 下午11:06.
 */
public class PriceServiceTest {

    private final PriceService priceService = new PriceService();
    private final Bozhu bozhu = new Bozhu(1772605247, "sina");

    @Test
    public void testGetPrices() throws Exception {
        Collection<PriceSource> prices = priceService.getSourcePrices(bozhu);
        List<PriceSource> expectedList = new ArrayList<PriceSource>();
        expectedList.add(new PriceSource(1, "博主报价", "", "", 1).addPrice(new Price(1772605247, 1, 1, 5)).addPrice(new Price(1772605247, 1, 2, 5)).addPrice(new Price(1772605247, 1, 3, 5)).addPrice(new Price(1772605247, 1, 4, 5)));
        expectedList.add(new PriceSource(2, "分析价", "", "", 0).addPrice(new Price(1772605247, 2, 1, 5)).addPrice(new Price(1772605247, 2, 3, 5)));
        for (PriceSource expected : expectedList) {
            assertTrue(prices.contains(expected));
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSourceCRUD() {
        Bozhu bozhu = new Bozhu(8888888888L, "sina");

        // 添加渠道
        PriceSource source = new PriceSource("测试渠道", "", "");
        source = priceService.addSource(bozhu, source);
        assertTrue(source.getId() > 0);
        PriceSource expected = new PriceSource(source.getId(), "测试渠道", "", "", 0);
        assertEquals(expected, source);
        assertEquals(expected, priceService.getSource(bozhu, source.getId()));

        // 更新渠道信息
        source.setQq("qq1111");
        source.setTelephone("phone22222");
        expected = new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 0);
        assertEquals(expected, priceService.addSource(bozhu, source));
        assertEquals(expected, priceService.getSource(bozhu, source.getId()));

        assertTrue(priceService.getSourcePrices(bozhu).isEmpty());

        // 增加价格信息
        source.addPrice(new Price(1, 8));
        expected = new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 0).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8));
        assertEquals(expected, priceService.addSource(bozhu, source));
        assertEquals(expected, priceService.getSource(bozhu, source.getId()));

        // 获取所有渠道
        List<PriceSource> expectedList = new ArrayList<PriceSource>();
        expectedList.add(new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 0).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8)));
        assertEquals(expectedList, priceService.getSourcePrices(bozhu));

        // 直接增加价格信息
        priceService.addPrice(bozhu, source.getId(), new Price(2, 13));
        expectedList.clear();
        expectedList.add(new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 0).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8)).addPrice(new Price(bozhu.getUsername(), source.getId(), 2, 13)));
        assertEquals(expectedList, priceService.getSourcePrices(bozhu));

        // 增加更多渠道
        PriceSource source2 = priceService.addSource(bozhu, new PriceSource("测试渠道2", "111", "222").addPrice(new Price(1, 10)));
        PriceSource source3 = priceService.addSource(bozhu, new PriceSource("测试渠道3", "333", "444").addPrice(new Price(2, 15)));
        Collection<PriceSource> result = priceService.getSourcePrices(bozhu);
        assertEquals(3, result.size());
        assertTrue(result.contains(new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 0).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8)).addPrice(new Price(bozhu.getUsername(), source.getId(), 2, 13))));
        assertTrue(result.contains(new PriceSource(source2.getId(), "测试渠道2", "111", "222", 0).addPrice(new Price(bozhu.getUsername(), source2.getId(), 1, 10))));
        assertTrue(result.contains(new PriceSource(source3.getId(), "测试渠道3", "333", "444", 0).addPrice(new Price(bozhu.getUsername(), source3.getId(), 2, 15))));

        // 默认渠道
        assertNull(priceService.getDefaultPriceSource(bozhu));
        priceService.setDefaultPriceSource(bozhu, source2.getId());
        expected = priceService.addSource(bozhu, new PriceSource(source2.getId(), "测试渠道2", "111", "222", 1).addPrice(new Price(1, 10)));
        assertEquals(expected, priceService.getDefaultPriceSource(bozhu));
        priceService.setDefaultPriceSource(bozhu, source.getId());
        expected = new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 1).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8)).addPrice(new Price(bozhu.getUsername(), source.getId(), 2, 13));
        assertEquals(expected, priceService.getDefaultPriceSource(bozhu));

        result = priceService.getSourcePrices(bozhu);
        assertEquals(3, result.size());
        assertTrue(result.contains(new PriceSource(source.getId(), "测试渠道", "qq1111", "phone22222", 1).addPrice(new Price(bozhu.getUsername(), source.getId(), 1, 8)).addPrice(new Price(bozhu.getUsername(), source.getId(), 2, 13))));
        assertTrue(result.contains(new PriceSource(source2.getId(), "测试渠道2", "111", "222", 0).addPrice(new Price(bozhu.getUsername(), source2.getId(), 1, 10))));
        assertTrue(result.contains(new PriceSource(source3.getId(), "测试渠道3", "333", "444", 0).addPrice(new Price(bozhu.getUsername(), source3.getId(), 2, 15))));

        // 删除价格
        priceService.deletePrice(bozhu, source2.getId(), 1);
        expected = new PriceSource(source2.getId(), "测试渠道2", "111", "222", 0);
        assertEquals(expected, priceService.getSource(bozhu, source2.getId()));

        try {
            priceService.deleteSource(bozhu, source.getId());
            fail();
        } catch (PersistenceException e) {
            // 外键约束
            assertTrue(e.getCause() instanceof MySQLIntegrityConstraintViolationException);
        }
        priceService.setDefaultPriceSource(bozhu, null);
        priceService.deleteSource(bozhu, source.getId());
        assertNull(priceService.getDefaultPriceSource(bozhu));

        priceService.deleteSource(bozhu, source2.getId());
        priceService.deleteSource(bozhu, source3.getId());
        assertEquals(0, priceService.getSourcePrices(bozhu).size());
    }

}

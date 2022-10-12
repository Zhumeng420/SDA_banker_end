import com.xxxx.server.controller.caculator;
import com.xxxx.server.data.simpleBagageData;
import com.xxxx.server.data.specialBagageData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class TestNGTest2Test {
    /***************测试件制(国际航班）************************/
    // 如果是头等舱或商务舱，Type=1,其它舱为2；
    //area 1-5 代表5个区域

    /**
     * 测试空行李列表
     */
    @Test
    void test1(){
        caculator test=new caculator();
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        Assert.assertEquals(test.caculateNation(1,1,0,2,testlist11,testlist12),0);
    }

    /**
     * 测试只含有不可纳入免费额度的特殊行李
     */
    @Test
    void test2(){
        caculator test=new caculator();
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist12.add(new specialBagageData(2,10.0));
        testlist12.add(new specialBagageData(4,10.0));
        testlist12.add(new specialBagageData(5,30.0));
        testlist12.add(new specialBagageData(6,5.0));
        testlist12.add(new specialBagageData(7,32.0));
        testlist12.add(new specialBagageData(8,20.0));
        Assert.assertEquals(test.caculateNation(1,1,0,2,testlist11,testlist12),16090);
    }
    @Test
    void test3(){

    }
}
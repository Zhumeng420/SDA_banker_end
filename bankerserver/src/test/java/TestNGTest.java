import com.xxxx.server.controller.caculator;
import com.xxxx.server.data.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class TestNGTest {
    /***************测试计重制(中国国内，不含港澳台）************************/
    /***
     * 测试单件普通行李，且重量小于免费额度
     */
    @Test
    public void testCaulatorByWeight(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        Assert.assertEquals(test.caculateChina(40.0,500.0,testlist11,testlist12),0);
    }
    /***
     * 测试多件行李（同时包含普通行李和可纳入免费额度的特殊行李），且重量小于免费额度
     */
    @Test
    public void testCaulatorByWeight2(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist12.add(new specialBagageData(1,10.0));
        Assert.assertEquals(test.caculateChina(40.0,500.0,testlist11,testlist12),0);
    }
    /**
     * 测试多件行李（同时包含普通行李\可纳入免费额度的特殊行李\不可纳入免费额度的特殊行李），且重量小于免费额度
     */
    @Test
    public void testCaulatorByWeight3(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist12.add(new specialBagageData(1,10.0));
        testlist12.add(new specialBagageData(2,10.0));
        Assert.assertEquals(test.caculateChina(40.0,500.0,testlist11,testlist12),75);
    }

    /**
     * 测试多件（同时包含普通行李\可纳入免费额度的特殊行李\不可纳入免费额度的特殊行李），但超过免费额度
     */
    @Test
    public void testCaulatorByWeight4(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,20.0));
        testlist12.add(new specialBagageData(1,10.0));
        testlist12.add(new specialBagageData(2,10.0));
        testlist12.add(new specialBagageData(1,30.0));
        Assert.assertEquals(test.caculateChina(40.0,500.0,testlist11,testlist12),225);
    }
    /**
     * 测试空
     */
    @Test
    public void testCaulatorByWeight5(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        Assert.assertEquals(test.caculateChina(40.0,500.0,testlist11,testlist12),0);
    }
    /**
     * 测试包含所以种类的特殊行李，且全不纳入免费额度
     */
    @Test
    public void testCaulatorByWeight6(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist12.add(new specialBagageData(1,10.0));
        testlist12.add(new specialBagageData(2,10.0));
        testlist12.add(new specialBagageData(2,24.0));
        testlist12.add(new specialBagageData(2,44.0));
        testlist12.add(new specialBagageData(8,5.0));
        testlist12.add(new specialBagageData(8,24.0));
        testlist12.add(new specialBagageData(8,33.0));
        testlist12.add(new specialBagageData(3,10.0));
        testlist12.add(new specialBagageData(4,10.0));
        testlist12.add(new specialBagageData(4,24.0));
        testlist12.add(new specialBagageData(5,10.0));
        testlist12.add(new specialBagageData(5,24.0));
        testlist12.add(new specialBagageData(6,5.0));
        testlist12.add(new specialBagageData(7,6.0));
        testlist12.add(new specialBagageData(7,10.0));
        testlist12.add(new specialBagageData(7,23.0));
        Assert.assertEquals(test.caculateNation(1,1,1,1,testlist11,testlist12),43390.0);
    }
    /**
     * 测试包含所有超额区间的普通行李，且全不纳入免费额度*
     */
    @Test
    public void testCaulatorByWeight7(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,24.0));
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,29.0));
        testlist11.add(new simpleBagageData(90.0,20.0,70.0,15.0));
        testlist11.add(new simpleBagageData(90.0,20.0,70.0,24.0));
        testlist12.add(new specialBagageData(1,23.0));
        testlist12.add(new specialBagageData(1,26.0));
        testlist12.add(new specialBagageData(1,32.0));
        Assert.assertEquals(test.caculateNation(2,3,0,0,testlist11,testlist12),11330.0);
    }
    @Test
    public void testCaulatorByWeight8(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,24.0));
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,29.0));
        Assert.assertEquals(test.caculateNation(2,4,0,0,testlist11,testlist12),2760.0);
    }
    @Test
    public void testCaulatorByWeight9(){
        caculator test=new caculator();
        List<simpleBagageData>testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        testlist11.add(new simpleBagageData(10.0,20.0,20.0,24.0));
        Assert.assertEquals(test.caculateNation(2,5,0,0,testlist11,testlist12),830.0);
    }
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
    /*******以下两个测试都和飞行区域无关****************/
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
    /**********以下测试包含所有类型的行李*************/
    /**
     *测试成人票或儿童票，头等舱或公务舱，普通会员，国际区域1
     */
    void reuse(List<simpleBagageData> testlist11,List<specialBagageData>testlist12){
        testlist11.add(new simpleBagageData(13.0,12.0,25.0,43.0));//不超重量，也不超尺寸
        testlist11.add(new simpleBagageData(13.0,53.0,80.0,43.0));//不超重量，但超尺寸
        testlist11.add(new simpleBagageData(24.0,12.0,25.0,43.0));//超重量，但不超尺寸
        testlist11.add(new simpleBagageData(28.0,55.0,83.0,40.0));//超重量，也超尺寸
        testlist11.add(new simpleBagageData(32.0,55.0,83.0,40.0));//超重量，也超尺寸
        testlist12.add(new specialBagageData(1,5.0));
        testlist12.add(new specialBagageData(3,23.0));
        testlist12.add(new specialBagageData(3,32.0));
        testlist12.add(new specialBagageData(6,5.0));
        testlist12.add(new specialBagageData(7,32.0));
    }
    @Test
    void test3(){
        caculator test=new caculator();
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculateNation(1,1,0,2,testlist11,testlist12),25480);
    }
    /**
     *测试成人票或儿童票，经济舱，星空联盟，国际区域2
     */
    @Test
    void test4(){
        caculator test=new caculator();
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculateNation(0,2,3,0,testlist11,testlist12),19240.0);
    }

    /***********以下测试接口的函数**********/
    //测试国内航班+成人票或儿童票+头等舱+普通乘客
    @Test
    void test5(){
        caculator test=new caculator();
        input input1=new input(1,1,0,0,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:10020.0",null));
    }
    //测试国内航班+儿童票+公务舱+凤凰知音白金卡
    @Test
    void test6(){
        caculator test=new caculator();
        input input1=new input(2,2,1,0,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:8820.0",null));
    }
    //测试国内航班+成人票+经济舱+凤凰知音金卡
    @Test
    void test7(){
        caculator test=new caculator();
        input input1=new input(3,1,2,0,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:10020.0",null));
    }
    //测试国际区域1+成人票+头等舱+星空联盟
    @Test
    void test8(){
        caculator test=new caculator();
        input input1=new input(1,1,4,1,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:22860.0",null));
    }
    //测试国际区域2+儿童票+公务舱+凤凰知音白金卡
    @Test
    void test9(){
        caculator test=new caculator();
        input input1=new input(1,2,1,2,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:17040.0",null));
    }
    //测试国际区域3+成人票+超级经济舱+凤凰知音银卡
    @Test
    void test10(){
        caculator test=new caculator();
        input input1=new input(5,1,3,3,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:18290.0",null));
    }
    //测试国际区域4+经济舱+不经过特殊区域+星空联盟金卡
    @Test
    void test11(){
        caculator test=new caculator();
        input input1=new input(3,1,1,4,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:22460.0",null));
    }
    //测试国际区域5+经济舱+经过特殊区域+凤凰知音白金卡
    @Test
    void test12(){
        caculator test=new caculator();
        input input1=new input(3,1,1,4,4000.0,1);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:24050.0",null));
    }
    //测试国际区域5+婴儿票+头等舱+凤凰知音白金卡
    @Test
    void test13(){
        caculator test=new caculator();
        input input1=new input(1,3,1,4,4000.0,0);
        List<simpleBagageData> testlist11=new ArrayList<>();
        List<specialBagageData>testlist12=new ArrayList<>();
        reuse(testlist11,testlist12);
        Assert.assertEquals(test.caculate(new Total(input1,testlist11,testlist12)), new RespBean(200,"计算结果:25640.0",null));
    }

}


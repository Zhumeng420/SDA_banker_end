package com.xxxx.server.controller;
import com.xxxx.server.data.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


@Api(tags = "Controller")
@RestController
@RequestMapping("/plane")
public class caculator {
    private double lastWeight;//按计重制时可免费托运的剩余额度
    /**
     *
     * @param data 由前端传回的旅客信息以及行李列表，并以字符串的形式返回计算结果
     * @return
     */
    @ApiOperation(value = "计算行李重量")
    @PostMapping("/compute")
    public RespBean caculate(@RequestBody Total data){
        System.out.println(data);
        System.out.println(data.data);
        System.out.println(data.simpleBagageDataList);
        System.out.println(data.specialBagageDataList);
        if(data.data.area==0){//如果是国内航班（不含港澳台）则为计重制
            /**********************根据乘客机票的类型分类****************************************/
            if(data.data.passenger==1||data.data.passenger==2){//如果是成人票或者儿童票
                /**************************再根据座舱类型计算免费托运的额度**********************************/
                if(data.data.seat==1){lastWeight=40;}//如果是头等舱，可以免费托运40KG
                else if(data.data.seat==2){lastWeight=30;}//如果是公务舱，可以免费托运30KG
                else if(data.data.seat==3||data.data.seat==4||data.data.seat==5)
                {lastWeight=20;}//如果是经济舱，悦享经济舱，超级经济舱，可以免费托运20KG
            }else{//如果是婴儿票
                lastWeight=10;//则所有座舱类型都是免费托运10KG
            }
            /**************************再根据旅客的会员卡类型计算额外的额度*************************/
            if(data.data.status==1) {//如果是凤凰知音终身白金卡、白金卡
                lastWeight+=30;//则可以扩容30KG
            }else if(data.data.status==2||data.data.status==3||data.data.status==4){//如果是凤凰知音金卡、银卡，星空联盟金卡
                lastWeight+=20;//则可以扩容20KG
            }
            /**************************计算完免费额度之和再开始分析行李列表***************************/
            System.out.println(caculateChina(this.lastWeight, data.data.price, data.simpleBagageDataList, data.specialBagageDataList));
            return RespBean.success("计算结果:"+caculateChina(this.lastWeight, data.data.price, data.simpleBagageDataList, data.specialBagageDataList));
        }else{//其它的则为国际航班，计件制
            //按计件时可免费托运23KG的剩余件数
            Integer lastObjectFor23KG = 0;//按计件时可免费托运32KG的剩余件数
            Integer getLastObjectFor32KG = 0;//初始化
            /**********************根据乘客机票的类型分类****************************************/
            if(data.data.passenger==1|| data.data.passenger==2){//如果是成人票或者儿童票
                /**************************再根据座舱类型计算免费托运的额度**********************************/
                if(data.data.seat==1||data.data.seat==2){//如果是头等舱或公务舱
                    getLastObjectFor32KG +=2;//可以免费托运2件32KG以内的行李
                    /**************************再根据旅客的会员卡类型计算额外的额度*************************/
                    if(data.data.status==1||data.data.status==2||data.data.status==3){//在头等舱或公务舱，凤凰知音白金卡、金卡、银卡可以额外托运1件32KG行李
                        getLastObjectFor32KG++;
                    }
                }else if(data.data.seat==4||data.data.seat==5){//如果是悦享经济舱或超级经济舱
                    lastObjectFor23KG +=2;//可以免费托运2件23KG以内的行李
                    /**************************再根据旅客的会员卡类型计算额外的额度*************************/
                    if(data.data.status==1||data.data.status==2||data.data.status==3){//在悦享经济舱或超级经济舱，凤凰知音白金卡、金卡、银卡可以额外托运1件32KG行李
                        lastObjectFor23KG++;
                    }
                }else if(data.data.seat==3){//如果是经济舱,需要分两类
                    if(data.data.markForEconmyclass==1||data.data.markForEconmyclass==2||data.data.markForEconmyclass==3||data.data.markForEconmyclass==7){
                        lastObjectFor23KG++;
                    }else{
                        lastObjectFor23KG +=2;
                    }
                    /**************************再根据旅客的会员卡类型计算额外的额度*************************/
                    if(data.data.status==1||data.data.status==2||data.data.status==3){//在经济舱，凤凰知音白金卡、金卡、银卡可以额外托运1件32KG行李
                        lastObjectFor23KG++;
                    }
                }
            }else{//如果是婴儿票
                lastObjectFor23KG++;
            }
            /**************************再根据旅客的会员卡类型计算额外的额度*************************/
            if(data.data.status==4){//在所以舱，星空联盟金卡可以额外托运1件32KG行李
                lastObjectFor23KG++;
            }
            /**************************计算完免费额度之和再开始分析行李列表***************************/
            Integer type=0;
            if(data.data.seat==1||data.data.seat==2){//区分头等舱/公务舱和其它舱，便于后面的计算
                type=2;
            }
            System.out.println(caculateNation(type,data.data.area, lastObjectFor23KG, getLastObjectFor32KG,data.simpleBagageDataList,data.specialBagageDataList));
            return RespBean.success("计算结果:"+caculateNation(type,data.data.area, lastObjectFor23KG, getLastObjectFor32KG,data.simpleBagageDataList,data.specialBagageDataList));
        }
    }

    /**
     * 用于计算国际航班，即计件制的行李
     * @param area  航班区间
     * @param last23 剩余可托运的23KG的行李数量
     * @param last32  剩余可托运的32KG行李的数量
     * @param simple  普通行李列表
     * @param special  特殊行李列表
     * @return 返回计件制行李托运的价格
     */
    public double caculateNation(Integer type,Integer area, Integer last23,Integer last32,List<simpleBagageData> simple,List<specialBagageData>special){
        /**********************************计算特殊行李的托运费**************************************/
        double feeOfSpeical=0.0;
        for(int i=0;i<special.size();i++){
            if(special.get(i).type==2){//如果是运动器械第二类
                if(special.get(i).weight>=2&&special.get(i).weight<=23){
                    feeOfSpeical+=2600;
                }else if(special.get(i).weight>23&&special.get(i).weight<=32){
                    feeOfSpeical+=3900;
                }else if(special.get(i).weight>32&&special.get(i).weight<=45){
                    feeOfSpeical+=5200;
                }
            }else if(special.get(i).type==8){//如果是运动器械第三类
                if(special.get(i).weight>=2&&special.get(i).weight<=23){
                    feeOfSpeical+=1300;
                }else if(special.get(i).weight>23&&special.get(i).weight<=32){
                    feeOfSpeical+=2600;
                }else if(special.get(i).weight>32&&special.get(i).weight<=45){
                    feeOfSpeical+=3900;
                }
            }else if(special.get(i).type==4) {//如果是其它第二类
                if(special.get(i).weight>=2&&special.get(i).weight<=23){
                    feeOfSpeical+=490;
                }else if(special.get(i).weight>23&&special.get(i).weight<=32){
                    feeOfSpeical+=3900;
                }
            }else if(special.get(i).type==5) {//如果是其它第三类
                if(special.get(i).weight>=2&&special.get(i).weight<=23){
                    feeOfSpeical+=1300;
                }else if(special.get(i).weight>23&&special.get(i).weight<=32){
                    feeOfSpeical+=2600;
                }
            }else if(special.get(i).type==6) {//如果是其它第四类
                if(special.get(i).weight>=2&&special.get(i).weight<=5){
                    feeOfSpeical+=1300;
                }
            }else if(special.get(i).type==7){//如果是其它第五类
                if(special.get(i).weight>=2&&special.get(i).weight<=8){
                    feeOfSpeical+=3900;
                }else if(special.get(i).weight>8&&special.get(i).weight<=23){
                    feeOfSpeical+=5200;
                }else if(special.get(i).weight>23&&special.get(i).weight<=32){
                    feeOfSpeical+=7800;
                }
            }
        }
        /**********************************计算普通行李的托运费**************************************/
        /**********************************首先根据航班区间划分**************************************/
        /************首先对尺寸进行排序，优先选择尺寸大的行李进入免费托运***************************/
        //创建匿名比较器
        if(simple.size()!=0) {
            simple.sort((o1, o2) -> {
                return (int) ((o2.length + o2.width + o2.height) - (o1.width + o1.height + o1.length));//进行降序排序
            });
        }
        if(area==1){//如果在国际区域一
            return feeOfSpeical+resuing(new extraInfo(380.0,980.0,980.0,1400.0,1400.0,2000.0,3000.0),type,last23,last32,simple,special);
        }else if(area==2){//如果在国际区域二
            return feeOfSpeical+resuing(new extraInfo(280.0,690.0,690.0,1100.0,1100.0,1100.0,1590.0),type,last23,last32,simple,special);
        }else if(area==3){//如果在国际区域三
            return feeOfSpeical+resuing(new extraInfo(520.0,520.0,520.0,520.0,1170.0,1170.0,1590.0),type,last23,last32,simple,special);
        }else if(area==4){//如果在国际区域四
            return feeOfSpeical+resuing(new extraInfo(690.0,1040.0,1040.0,2050.0,1380.0,1380.0,1590.0),type,last23,last32,simple,special);
        }else{
            return feeOfSpeical+resuing(new extraInfo(210.0,520.0,520.0,830.0,830.0,1100.0,1590.0),type,last23,last32,simple,special);
        }
    }

    /**
     * 函数重用，计算五个区域的超额行李
     * @param info
     * @param type
     * @param last23
     * @param last32
     * @param simple
     * @param special
     * @return
     */
    public double resuing(extraInfo info
            ,Integer type,Integer last23,Integer last32,List<simpleBagageData> simple,List<specialBagageData>special){
        /***********************如果是头等舱或经济舱**********************************/
        /*************则在计算超额行李时不存在超重限制，因此优先考虑***********************/
        double feeOfSimple=0.0;//普通行李的托运费用
        Integer freeNums=0;//用来记录免费托运的行李的件数，不一定等于last23+last32之和
        Integer freeSpecial=0;//可以纳入普通行李计算范畴的特殊行李数目
        if(type==1){
            /**********以下遍历普通行李列表，逐个计算********************/
            for(int i=0;i<simple.size();i++){
                if(last23>0&&simple.get(i).weight<=23){//如果23KG还有免费额度，并且行李符合条件
                    last23--;freeNums++;
                }else if(last32>0&&simple.get(i).weight<=32){//如果32KG还有免费额度，并且行李符合条件
                    last32--;freeNums++;
                }else if((simple.get(i).height+simple.get(i).width+simple.get(i).length)>158){//头等舱和商务舱不用考虑超重的情况
                    feeOfSimple+=info.cost3;//由于超尺寸收的额外费用
                }
            }
            /*******以下遍历特殊行李列表(可纳入普通行李计算范围的），逐个计算***********************/
            for(int i=0;i<special.size();i++){
                if(special.get(i).type==1||special.get(i).type==3){
                    if(last23>0&&special.get(i).weight<=23){//如果23KG还有免费额度，并且行李符合条件
                        last23--;freeNums++;
                    }else if(last32>0&&special.get(i).weight<=32){//如果32KG还有免费额度，并且行李符合条件
                        last32--;freeNums++;
                    }else{
                        freeSpecial++;//否则就按件数收费
                    }
                }
            }
        }else{//如果是其它舱，则存在超质量的情况
            /**********以下遍历普通行李列表，逐个计算********************/
            for (int i=0;i<simple.size();i++){
                if(last23>0&&simple.get(i).weight<=23){//如果23KG还有免费额度，并且行李符合条件
                    last23--;freeNums++;
                }else if(last32>0&&simple.get(i).weight<=32){//如果32KG还有免费额度，并且行李符合条件
                    last32--;freeNums++;
                }else if((simple.get(i).height+simple.get(i).width+simple.get(i).length)<=158
                        &&simple.get(i).weight>23&&simple.get(i).weight<=28){//超重量但不超尺寸
                    feeOfSimple+=info.cost1;//由于超尺寸收的额外费用
                }else if((simple.get(i).height+simple.get(i).width+simple.get(i).length)<=158
                        &&simple.get(i).weight>28&&simple.get(i).weight<=32){//超重量但不超尺寸
                    feeOfSimple+=info.cost2;//由于超尺寸收的额外费用
                }else if((simple.get(i).height+simple.get(i).width+simple.get(i).length)>158
                        &&simple.get(i).weight>2&&simple.get(i).weight<=23){//不超重量但超尺寸
                    feeOfSimple+=info.cost3;//由于超尺寸收的额外费用
                }else if((simple.get(i).height+simple.get(i).width+simple.get(i).length)>158
                        &&simple.get(i).weight>23){//超重量且超尺寸
                    feeOfSimple+=info.cost4;//由于超尺寸收的额外费用
                }
            }
            /*******以下遍历特殊行李列表(可纳入普通行李计算范围的），逐个计算***********************/
            for(int i=0;i<special.size();i++){
                if(special.get(i).type==1||special.get(i).type==3){
                    freeSpecial++;
                    if(last23>0&&special.get(i).weight<=23){//如果23KG还有免费额度，并且行李符合条件
                        last23--;freeNums++;
                    }else if(last32>0&&special.get(i).weight<=32){//如果32KG还有免费额度，并且行李符合条件
                        last32--;freeNums++;
                    }else if(special.get(i).weight>23&&special.get(i).weight<=28){//如果超重了，则额外收费
                        feeOfSimple+=info.cost1;
                    }else if(special.get(i).weight>28){
                        feeOfSimple+=info.cost2;
                    }
                }
            }
        }
        /*******然后根据超额行李件数计算超额费用*********************/
        Integer extraNums=simple.size()+freeSpecial-freeNums;
        if(extraNums==1){//如果超额一件
            feeOfSimple+=info.cost5;
        }else if(extraNums==2){//如果超额两件
            feeOfSimple+=info.cost5+info.cost6;
        }else if(extraNums>2){//如果超额大于两件
            feeOfSimple=feeOfSimple+info.cost5+info.cost6+(extraNums-2)*info.cost7;
        }
        return feeOfSimple;
    }
    /**
     * 用于计算国内航班，即计重制的行李
     * @return 返回计重制下行李托运的价格
     */
    public double caculateChina(Double lastWeight,Double costOfTickt,List<simpleBagageData> simple, List<specialBagageData>special){
        /**********************************计算特殊行李的托运费**************************************/
        double feeOfSpeical=0.0;
        for(int i=0;i<special.size();i++){
            if(special.get(i).type!=1&&special.get(i).type!=3){
               feeOfSpeical+=special.get(i).weight*costOfTickt*0.015;
            }
        }
        /**********************************判断行李有没有超出免费托运额度******************************/
        double sums=0;
        for(int i=0;i<simple.size();i++){//普通行李总和
            sums+=simple.get(i).weight;
        }
        for(int i=0;i<special.size();i++){//可免费托运的特殊行李
            if(special.get(i).type==1||special.get(i).type==3){
                sums+=special.get(i).weight;
            }
        }
        if(sums<=lastWeight){//如果所有行李加起来都没有超出免费额度,则返回其它特殊行李的托运费
            return feeOfSpeical;
        }else{//否则将超出的额度减掉，计算其余费用
            return (sums-lastWeight)*costOfTickt*0.015+feeOfSpeical;
        }
    }
    /**
     * 用于计算国内航班，即计重制的行李
     * @return 返回计重制下行李托运的价格
     */
//    public double caculateChina(Double lastWeight,Double costOfTickt,List<simpleBagageData> simple, List<specialBagageData>special){
//        /**********************************计算特殊行李的托运费**************************************/
//        double feeOfSpeical=0.0;
//        for(int i=0;i<special.size();i++){
//            if(special.get(i).type!=1&&special.get(i).type!=3){
//                feeOfSpeical+=special.get(i).weight*costOfTickt*0.015;
//            }
//        }
//        /**********************************判断行李有没有超出免费托运额度******************************/
//        double sums=0;
//        for(int i=0;i<simple.size();i++){//普通行李总和
//            sums+=simple.get(i).weight;
//        }
//        for(int i=0;i<special.size();i++){//可免费托运的特殊行李
//            if(special.get(i).type==1||special.get(i).type==3){
//                sums+=special.get(i).weight;
//            }
//        }
//        if(sums<=lastWeight){//如果所有行李加起来都没有超出免费额度,则返回其它特殊行李的托运费
//            System.out.println(feeOfSpeical);
//            return feeOfSpeical;
//        }
//        /**********************************首先把普通行李和特殊行李统一化处理***************************/
//        List<unify>total=new ArrayList<unify>();//只保留重量信息，找出最优免费行李托运方案之后在原有列表中剔除掉免费托运的行李
//        for(int i=0;i<simple.size();i++){//处理普通行李
//            total.add(new unify(simple.get(i).weight,simple.get(i).length,simple.get(i).width,simple.get(i).height));
//        }
//        for(int i=0;i<special.size();i++){//处理特殊行李
//            total.add(new unify(special.get(i).type,special.get(i).weight));
//        }
//        /*********************************以下问题转换为01背包问题**************************************/
//        /******即在免费托运额度作为背包容量，在可以免费托运的行李中选取出能使背包装的质量最多的物品组合************/
//        /******并通过追溯解将被选出的物品的falg标志为1，将剩下的物品进行超额计算*******************************/
//        /*-----------------------数据处理部分-----------------------*/
//        int nums=0;//可以装进“背包”的物品数量
//        int capacity=(int)(this.lastWeight);//背包的容量（由于规则中的质量都是整数，这里在进行计算前可以强制转换）
//        List<TempStruct>goods=new ArrayList<TempStruct>();//用来将物品和在全局数组中的下标建立一一对应关系
//        for(int i=0;i<total.size();i++){
//            if(total.get(i).type==0||total.get(i).type==1||total.get(i).type==3) {//统计运动器械第一类，其它第一类，和普通行李的数量
//                nums++;
//                goods.add(new TempStruct(total.get(i).weight,i));//建立映射关系
//            }
//        }
//        double[][] dp = new double[nums][];//创建DP数组，行数为物品总数，列数为背包容量+1
//        for (int i = 0; i < nums; i++){
//            dp[i] = new double[capacity+1];
//        }
//        boolean[][] traceBack=new boolean[nums][];//追溯解数组
//        for(int i=0;i<nums;i++){
//            traceBack[i]=new boolean[capacity+1];
//        }
//        /*-----------------------动态规划计算部分--------------------*/
//        for(int j=0;j<capacity+1;j++){//单独处理第0行
//            if(j<goods.get(0).weigth){//如果背包容量小于第一个物品的质量，则dp数组赋0
//                dp[0][j]=0;
//                traceBack[0][j]=false;
//            }else{//否则就装下第一个物品，dp数组赋值第一个物品的质量
//                dp[0][j]=goods.get(0).weigth;
//                traceBack[0][j]=true;
//            }
//        }
//        for(int i=1;i<nums;i++){//从第1行按照状态转移方程处理
//            for(int j=0;j<capacity+1;j++){
//                if(dp[i][j]<goods.get(i).weigth) {//如果当前背包总容量小于当前物品的质量，则不能装当前物品
//                    dp[i][j]=dp[i-1][j];
//                    traceBack[i][j]=false;
//                }else{
//                    dp[i][j]=Math.max(dp[i-1][(int) (j-goods.get(i).weigth)]+goods.get(i).weigth,dp[i-1][j]);
//                }
//            }
//        }
//        /*---------------------追溯解部分---------------------------*/
//        int i=nums-1;int j=capacity;
//        while(i>=0&&j>=0){
//            if(!traceBack[i][j]){//如果当前物品不在最优解方案中，则往上一个物品回退
//                i--;
//            }else{//说明当前物品在最优方案中
//                total.get(goods.get(i).index).flag=true;//将当前物品标记为已处理
//                i--;j-=goods.get(i).weigth;
//            }
//        }
//        /********************免费托运部分处理完成，以下处理超额部分**********************/
//        double simpleExtra=0.0;
//        for(int k=0;k<total.size();k++){
//            if(!total.get(k).flag &&(total.get(k).type==0||total.get(k).type==1||total.get(k).type==3)){
//                simpleExtra+=total.get(k).weight*costOfTickt*0.015;
//            }
//        }
//        System.out.println(simpleExtra+feeOfSpeical);
//        return simpleExtra+feeOfSpeical;
//    }
}



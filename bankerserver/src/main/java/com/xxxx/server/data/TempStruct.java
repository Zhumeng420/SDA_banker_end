package com.xxxx.server.data;

/**
 * 在用01背包求解最优免费托运方案时的临时数据结构，用来存储物品的质量和物品在全局数组中的下标
 */
public class TempStruct {
    public Double weigth;
    public Integer index;
    public TempStruct(Double WEIGHT, Integer INDEX){
        this.weigth=WEIGHT;
        this.index=INDEX;
    }
}

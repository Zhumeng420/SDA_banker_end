package com.xxxx.server.data;

import io.swagger.models.auth.In;

/**
 * 对普通行李和特殊行李做一个统一化的处理
 */
public class unify {
    public Integer type;
    public Double weight;
    public Double length;
    public Double width;
    public Double height;
    public boolean flag;//用于标记当前行李是否已经被纳入普通行李托运范畴
    public Double cost;//超额代价
    public unify(Integer TYPE, Double WEIGHT){//用于特殊行李的构造函数
        this.type=TYPE;
        this.weight=WEIGHT;
        this.length=0.0;
        this.width=0.0;
        this.height=0.0;
        this.flag=false;
        this.cost=0.0;
    }
    public unify(Double WEIGHT, Double LENGTH, Double WIDTH, Double HEIGHT){//用于普通行李的构造函数
        this.height=HEIGHT;
        this.width=WIDTH;
        this.length=LENGTH;
        this.weight=WEIGHT;
        this.type=0;
        this.flag=false;
        this.cost=0.0;
    }
    public unify(Integer TYPE, Double WEIGHT,Double COST){//用于特殊行李的构造函数
        this.type=TYPE;
        this.weight=WEIGHT;
        this.length=0.0;
        this.width=0.0;
        this.height=0.0;
        this.flag=false;
        this.cost=COST;
    }
    public unify(Double WEIGHT, Double LENGTH, Double WIDTH, Double HEIGHT,Double COST){//用于普通行李的构造函数
        this.height=HEIGHT;
        this.width=WIDTH;
        this.length=LENGTH;
        this.weight=WEIGHT;
        this.type=0;
        this.flag=false;
        this.cost=COST;
    }
}

package com.xxxx.server.data;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="普通行李对象", description="")
public class simpleBagageData {
    public Double weight;
    public Double length;
    public Double width;
    public Double height;
    public simpleBagageData(Double WEIGHT, Double LENGTH, Double WIDTH, Double HEIGHT){
        this.weight=WEIGHT;
        this.length=LENGTH;
        this.width=WIDTH;
        this.height=HEIGHT;
    }
}

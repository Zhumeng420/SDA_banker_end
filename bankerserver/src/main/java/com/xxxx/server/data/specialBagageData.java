package com.xxxx.server.data;
import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@ApiModel(value="特殊行李对象", description="")
public class specialBagageData {
    public Double weight;
    public Integer type;
    public specialBagageData(Integer TYPE,Double WEIGHT){
        this.type=TYPE;
        this.weight=WEIGHT;
    }
}

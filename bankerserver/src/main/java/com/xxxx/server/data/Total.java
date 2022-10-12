package com.xxxx.server.data;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="行李列表对象", description="")
public class Total {
    public input data;
    public List<simpleBagageData> simpleBagageDataList;
    public List<specialBagageData> specialBagageDataList;
    public Total(input DATA, List<simpleBagageData> SIMPLELIST,List<specialBagageData> SPECIALLIST){
        this.data=DATA;
        this.simpleBagageDataList=SIMPLELIST;
        this.specialBagageDataList=SPECIALLIST;
    }
}

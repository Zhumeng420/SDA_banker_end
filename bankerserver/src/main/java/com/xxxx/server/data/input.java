package com.xxxx.server.data;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel(value="input对象", description="")
public class input implements Serializable  {
    public Integer seat;
    public Integer passenger;
    public Integer status;
    public Integer area;
    public Double price;
    public Integer markForEconmyclass;
    public input(Integer SEAT,Integer PASSENGER,Integer STATUS,Integer AREA,Double PRICE,Integer MARKFORECONMYCLASS){
        this.seat=SEAT;
        this.passenger=PASSENGER;
        this.status=STATUS;
        this.area=AREA;
        this.price=PRICE;
        this.markForEconmyclass=MARKFORECONMYCLASS;
    }
}

package com.xxxx.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    /*返回的状态码*/
    private long code;
    /*相应的提示信息*/
    private String message;
    /*准备返回对象*/
    private Object object;
    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
        /*成功返回的状态码一般是200*/
    }
    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message,Object object){
        return new RespBean(200,message,object);
        /*成功返回的状态码一般是200*/
    }
    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static  RespBean error(String message){
        return new RespBean(500,message,null);
        /*失败返回的状态一般是500*/
    }
    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static  RespBean error(String message,Object object){
        return new RespBean(500,message,object);
        /*失败返回的状态一般是500*/
    }
}

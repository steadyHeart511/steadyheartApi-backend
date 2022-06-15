package com.steadyheart.springbootinit.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 调用者id
     */
    private Long userId;

    /**
     * 调用接口id
     */
    private Long interfaceId;

    /**
     * 调用总数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}


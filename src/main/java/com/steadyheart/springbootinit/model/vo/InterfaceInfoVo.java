package com.steadyheart.springbootinit.model.vo;

import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import lombok.Data;

/**
 *
 * @author lts
 * @create 2023-10-27 10:26
 */
@Data
public class InterfaceInfoVo extends InterfaceInfo {

    /**
     * 接口的总调用次数
     */
    public Integer totalNum;

}

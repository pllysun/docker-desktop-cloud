package com.cloud.DTO;

import lombok.Data;

@Data
public class DeskTopControlDto {
    /**
     * 使用中的桌面
     */
    Integer useDeskTopCount;

    /**
     * 关机的桌面
     */
    Integer downDeskTopCount;

}

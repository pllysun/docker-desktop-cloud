package com.cloud.DTO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    /**
     * 数据总数
     */
    Long total;

    /**
     * 数据列表
     */
    List rows;
}

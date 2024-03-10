package com.cloud.service;

import com.cloud.DTO.DateImageUseCount;
import com.cloud.DTO.DateSumAndUseDto;
import com.cloud.DTO.DeskTopControlDto;

import java.util.List;

public interface ControlService {
    DeskTopControlDto getDeskTopControl(Integer userId);

    List<DateImageUseCount> getImageUseCount(Integer userId);

    Integer getExpireProportion(Integer userId);

    List<DateSumAndUseDto> getSumAndUse(Integer userId);
}

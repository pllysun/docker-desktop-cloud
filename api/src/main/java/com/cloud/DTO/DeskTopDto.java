package com.cloud.DTO;

import lombok.Data;

@Data
public class DeskTopDto {

    String networkId;

    ImageDto imageDto;

    String containerName;

    String podControllerName;
}

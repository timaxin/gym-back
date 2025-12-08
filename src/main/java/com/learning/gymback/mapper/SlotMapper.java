package com.learning.gymback.mapper;

import com.learning.gymback.entity.Slot;
import com.learning.gymback.dto.SlotCreateRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlotMapper {

    Slot toEntity (SlotCreateRequestDto dto);
}

package com.tenniscourts.guests;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest map(CreateOrUpdateGuestRequestDTO createOrUpdateGuestRequestDTO);

    void updateGuestFromDto(CreateOrUpdateGuestRequestDTO dto, @MappingTarget Guest entity);

    Guest map(GuestDTO source);

    GuestDTO map(Guest source);

    List<GuestDTO> map(List<Guest> source);
}


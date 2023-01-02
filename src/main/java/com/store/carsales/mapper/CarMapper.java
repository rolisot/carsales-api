package com.store.carsales.mapper;

import com.store.carsales.domain.Car;
import com.store.carsales.request.CreateCarRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class CarMapper {
    public static final CarMapper Instance = Mappers.getMapper(CarMapper.class);
    public abstract Car toCarEntity(CreateCarRequest createCarRequest);
}

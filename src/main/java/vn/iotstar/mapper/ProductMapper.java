package vn.iotstar.mapper;

import org.mapstruct.*;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    ProductDTO toDto(Product entity);

    @Mapping(target = "user", ignore = true)
    Product toEntity(ProductDTO dto);
}

package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.AreaRequest;
import com.marketplace.ifba.dto.AreaResponse;
import com.marketplace.ifba.model.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
    public Area toEntity(AreaRequest request) {
        if (request == null) {
            return null;
        }
        Area area = new Area();
        area.setNomeArea(request.nomeArea());
        return area;
    }

    public AreaResponse toDTO(Area area) {
        if (area == null) {
            return null;
        }
        return new AreaResponse(area.getIdArea(), area.getNomeArea());
    }
}

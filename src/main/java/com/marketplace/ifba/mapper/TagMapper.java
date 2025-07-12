package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.TagRequest;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public Tag toEntity(TagRequest request) {
        if (request == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setNomeTag(request.nomeTag());
        return tag;
    }

    public TagResponse toDTO(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagResponse(tag.getIdTag(), tag.getNomeTag());
    }

    public void updateEntityFromRequest(TagRequest request, Tag tag) {
        if (request.nomeTag() != null) {
            tag.setNomeTag(request.nomeTag());
        }
    }
}

package com.example.api.domain.receivingpictures;

public record ReceivingPicturesInfoDTO(
        Long id,
        String name,
        String path
) {
    public ReceivingPicturesInfoDTO(ReceivingPicture receivingPicture) {
        this(
                receivingPicture.getId(),
                receivingPicture.getFile().getName(),
                receivingPicture.getFile().getPath()
        );
    }
}

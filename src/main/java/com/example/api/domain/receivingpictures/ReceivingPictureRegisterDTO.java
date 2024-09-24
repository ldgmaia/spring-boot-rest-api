package com.example.api.domain.receivingpictures;

import com.example.api.domain.files.File;
import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotBlank;

public record ReceivingPictureRegisterDTO(
        @NotBlank
        Receiving receiving,

        @NotBlank
        File file,

        @NotBlank
        User createdBy
) {
}
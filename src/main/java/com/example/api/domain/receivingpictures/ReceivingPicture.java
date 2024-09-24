package com.example.api.domain.receivingpictures;

import com.example.api.domain.files.File;
import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "receiving_pictures")
@Entity(name = "ReceivingPicture")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ReceivingPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiving_id", nullable = false)
    private Receiving receiving;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public ReceivingPicture(ReceivingPictureRegisterDTO receivingItemRegister) {
        this.receiving = receivingItemRegister.receiving();
        this.file = receivingItemRegister.file();
        this.createdBy = receivingItemRegister.createdBy();
    }
}

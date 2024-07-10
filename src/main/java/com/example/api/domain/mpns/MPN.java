package com.example.api.domain.mpns;

import com.example.api.domain.models.Model;
import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "mpns")
@Entity(name = "MPN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MPN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    private Boolean enabled;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "models_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;


    public MPN(MPNRegisterDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.enabled = true;
        this.status = data.status();
        this.model = data.model();

    }

//    public void deactivate() {
//        this.enabled = false;
//    }
}

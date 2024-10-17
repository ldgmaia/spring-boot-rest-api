package com.example.api.domain.itemstatus;

import com.example.api.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "item_statuses")
@Entity(name = "ItemStatus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

//    @Column(name = "created_at", nullable = false, updatable = false)
//    private Timestamp createdAt;
//
//    @Column(name = "updated_at", nullable = false)
//    private Timestamp updatedAt;

    public ItemStatus(String name, User createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}

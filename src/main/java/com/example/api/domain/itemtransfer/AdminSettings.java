package com.example.api.domain.itemtransfer;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "AdminSettings")
@Table(name = "admin_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AdminSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key", nullable = false, unique = true)
    private String key;

    @Column(name = "setting_value", nullable = false)
    private String value;

    @Column(name = "description")
    private String description;

    // Utility methods
    @Getter
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "prohibited_status")
    private Boolean prohibitedStatus;

    @Override
    public String toString() {
        return "AdminSettings{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

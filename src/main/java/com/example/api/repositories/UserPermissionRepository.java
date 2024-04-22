package com.example.api.repositories;

import com.example.api.domain.permission.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    @Query(value = """
            select count(*)
            from users_permissions up
            where permission_id = (
                select id from permissions p where name = :permission
            )
            and user_id = :userId
            """,
            nativeQuery = true)
    int countByUserIdAndPermission(Long userId, String permission);

    default boolean existsByUserIdAndPermission(Long userId, String permission) {
        return countByUserIdAndPermission(userId, permission) > 0;
    }
}

package com.example.api.repositories;

import com.example.api.domain.permission.Role;
import com.example.api.domain.permission.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserRole, Long> {

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM users u
                INNER JOIN users_roles ur ON u.id = ur.user_id
                INNER JOIN roles r ON ur.role_id = r.id
                INNER JOIN permissions_roles pr ON r.id = pr.role_id
                INNER JOIN permissions p ON pr.permission_id = p.id
                WHERE u.id = :userId
                AND p.route = :permission
            ) AS has_permission;
            """,
            nativeQuery = true)
    int countByUserIdAndPermission(Long userId, String permission);

    default boolean existsByUserIdAndPermission(Long userId, String permission) {
        return countByUserIdAndPermission(userId, permission) > 0;
    }

    @Query("SELECT ur.role FROM UserRole ur WHERE ur.user.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}

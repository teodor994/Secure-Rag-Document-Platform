package org.example.application.repository;

import org.example.application.repository.entity.NodeAccessReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

// Read Only full data by all fields in the DB
public interface NodeAccessReadRepository extends JpaRepository<NodeAccessReadEntity, UUID> {
    @Query("""
        SELECT na FROM NodeAccessReadEntity na
        JOIN FETCH na.node
        LEFT JOIN FETCH na.user
        LEFT JOIN FETCH na.role
        LEFT JOIN FETCH na.grantedRole
        LEFT JOIN FETCH na.createdBy
    """)
    List<NodeAccessReadEntity> findAllPermissionsWithDetails();

    @Query("""
        SELECT na FROM NodeAccessReadEntity na
        JOIN FETCH na.node
        LEFT JOIN FETCH na.user
        LEFT JOIN FETCH na.role
        LEFT JOIN FETCH na.grantedRole
        LEFT JOIN FETCH na.createdBy
        WHERE na.node.name= :nodeName AND (na.user IS NOT NULL AND na.user.id = :userId)
                                       OR (na.role IS NOT NULL AND na.role.id = :roleId)
    """)
    List<NodeAccessReadEntity> findAllPermissionsByUserRoleId(@Param("userId") UUID userId, @Param("roleId") UUID roleId, @Param("nodeName") String nodeName);

//    @Query("""
//        SELECT na FROM NodeAccessReadEntity na
//        JOIN FETCH na.node
//        LEFT JOIN FETCH na.user
//        LEFT JOIN FETCH na.role
//        LEFT JOIN FETCH na.grantedRole
//        LEFT JOIN FETCH na.createdBy
//        WHERE na.node.name= :nodeName AND na.role.id= :roleId
//    """)
//    List<NodeAccessReadEntity> findAllPermissionsByNodeName( @Param("nodeName") String nodeName);
}

package com.task_manager.metadata.organization;

import com.task_manager.metadata.organization.models.OrgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrgRepository extends JpaRepository<OrgEntity,Long> {
    Optional<OrgEntity> findByName(String name);

    Void deleteByName(String name);

    @Modifying
    @Query("update OrgEntity org set org.name = :newName, org.title = :title where org.name = :oldName")
    OrgEntity updateOrg(String oldName,  String title, String newName);


    @Query("select org.id from OrgEntity org where org.name = :name")
    Optional<Long> getIdByName(String name);

}

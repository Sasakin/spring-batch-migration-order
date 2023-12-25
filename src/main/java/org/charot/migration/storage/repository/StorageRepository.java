package org.charot.migration.storage.repository;

import org.charot.migration.storage.repository.entity.AllOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<AllOrdersEntity, Long> {
}

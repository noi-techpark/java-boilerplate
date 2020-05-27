package it.bz.opendatahub.project.jpa;

import it.bz.opendatahub.project.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}

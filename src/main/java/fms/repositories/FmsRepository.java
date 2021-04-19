package fms.repositories;

import fms.model.FinanceManagementSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FmsRepository extends JpaRepository<FinanceManagementSystem, Integer> {

}
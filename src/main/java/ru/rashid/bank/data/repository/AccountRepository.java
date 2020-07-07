package ru.rashid.bank.data.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rashid.bank.data.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a.id FROM Account a WHERE a.id in :ids")
    Set<Long> getExistingAccountIds(Collection<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :accountId")
    Account getForUpdate(Long accountId);

    @Modifying
    @Query(value = "insert into Account (id, balance) values (:id, :balance)", nativeQuery = true)
    void insert(Long id, BigDecimal balance);
}

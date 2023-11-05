package com.onk.repository;

import com.onk.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.user.id = :userId ORDER BY a.createdDate DESC")
    List<Address> userAddresses(@Param("userId") Long userId);

}

package com.culture.API.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import com.culture.API.Models.Owner;


public interface OwnerRepository extends JpaRepository<Owner, Long> {

        List<Owner> findAll();
        @Query("SELECT o FROM Owner o WHERE o.privilege != 1")
        List<Owner> findAllWithoutAdmin();
        Owner findByidOwner(int idOwner);
        Owner findByEmailAndPwd(String email, String pwd);
        Owner save(Owner o);
}


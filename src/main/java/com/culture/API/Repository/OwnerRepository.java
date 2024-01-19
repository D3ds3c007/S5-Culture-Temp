package com.culture.API.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.culture.API.Models.Owner;


public interface OwnerRepository extends JpaRepository<Owner, Long> {

        // Field save(Field field);
        List<Owner> findAll();
        Owner findByidOwner(int idOwner);
        Owner save(Owner o);
}


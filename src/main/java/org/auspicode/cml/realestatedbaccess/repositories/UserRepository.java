package org.auspicode.cml.realestatedbaccess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserRepository<T, V> extends JpaRepository<T, V> {

    Optional<T> findByIdNifAndIdIdCardNumberAndIdFullName(String nif, String idCardNumber, String fullName);

    Optional<T> findByIdNif(String nif);
}

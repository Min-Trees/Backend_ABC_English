package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
    @NotNull
    Optional<User> findById(@NotNull Integer userId);
    List<User> findAllByBan24hTrueAndBanUntilBefore(Date now);
}
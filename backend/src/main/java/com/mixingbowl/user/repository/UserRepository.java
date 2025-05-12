package com.mixingbowl.user.repository;

import com.mixingbowl.user.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(Users user) {
        em.persist(user);
    }

    public Optional<Users> findOne(Long id) {
        return Optional.ofNullable(em.find(Users.class, id));
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class).getResultList();
    }

    public List<Users> findByName(String name) {
        return em.createQuery("select u from Users u where u.name = :name", Users.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Optional<Users> findByEmail(String email) {
        List<Users> results = em.createQuery("select u from Users u where u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultList();
        return results.stream().findFirst();
    }
}

package com.hendisantika.springbootrestapipostgresql.repository;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-rest-api-postgresql
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-01-18
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
@RepositoryRestResource
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByName(String name);
}
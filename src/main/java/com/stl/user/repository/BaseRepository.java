package com.stl.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BaseRepository<T extends Dto, ID extends Serializable>
        extends MongoRepository<T, ID>
{

    void delete(Class clazz, ID id);

    T findById(Class clazz,ID id);

    List<T> findAll(Class clazz);

    List<T> insert(List<T> entities);

    T update(Class clazz, ID id, T entity);
    
    T update(Class clazz, ID id, Map<String, Object> fieldSet);
}

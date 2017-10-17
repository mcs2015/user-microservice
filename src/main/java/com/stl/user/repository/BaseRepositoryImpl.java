package com.stl.user.repository;

import com.stl.user.util.Constants;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public class BaseRepositoryImpl<T extends Dto, ID extends Serializable>
        extends SimpleMongoRepository<T, ID> implements BaseRepository<T,ID>
{
    private MongoOperations mongoOperations;

    public BaseRepositoryImpl(MongoEntityInformation<T, ID> metadata,
                              MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
    }


    @Override
    public <S extends T> S insert(S entity) {
        entity.setDeleted(false);
            entity.setCreatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
        entity.setUpdatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
        S entityPersisted = super.insert(entity);
        return entityPersisted;
    }

    @Override
    public void delete(Class clazz, ID id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ID).is(id));

        Update update = new Update();
        update.set(Constants.DELETED, true);
        update.set(Constants.UPDATED_TIMESTAMP, ZonedDateTime.now(ZoneOffset.UTC).toString());


        mongoOperations.findAndModify(
                query, update,
                new FindAndModifyOptions().returnNew(true),
                clazz);
    }

    @Override
    public T findById(Class clazz, ID id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ID).is(id).and(Constants.DELETED).is(false));

        return (T)mongoOperations.findOne(query, clazz);
    }

    @Override
    public List<T> findAll(Class clazz) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.DELETED).is(false));

        return mongoOperations.find(query, clazz);

    }

    public  List<T> insert(List<T> entities) {
        for(T entity: entities){
            entity.setDeleted(false);
            entity.setCreatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
            entity.setUpdatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
        }
        return super.insert(entities);
    }

    public T update(Class clazz, ID id, T entity) {
        T dbEntity = (T)mongoOperations.findOne(
                Query.query(Criteria.where(Constants.DELETED).is(false)
                        .and(Constants.ID).is(id)), clazz);
        if(dbEntity != null){
            entity.setDeleted(dbEntity.isDeleted());
            entity.setCreatedTimestamp(dbEntity.getCreatedTimestamp());
            entity.setUpdatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toString());
            return super.save(entity);
        }else{
            return null;
        }

    }

    public T update(Class clazz, ID id, Map<String, Object> fieldSet) {

        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.ID).is(id));

        Update update = new Update();

        for(String key: fieldSet.keySet()){
            update.set(key, fieldSet.get(key));
        }

        return (T)mongoOperations.findAndModify(
                query, update,
                new FindAndModifyOptions().returnNew(true), clazz);
    }



}

package com.ohx.dto;

import com.ohx.entity.Relationshipobj;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : YINAN
 * @date : 2023/11/9
 * @effect :
 */
@Repository
public interface ActionRepository extends Neo4jRepository<Relationshipobj, Long> {

    @Query("match (n)-[r]-(m) where type(r)={0} return count(r)")
    int countActionType(String name);

}

package com.ohx.dto;

import com.ohx.entity.Area;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AreaRepository extends Neo4jRepository<Area,Long> {

    /**
     * 获取所有港口列表
     * **/
    @Query("match (n:Area) return n")
    Area[] findAllArea();

    /**
     * 根据港口名查港口
     * **/
    @Query("match (n:Area) where n.name={0} return n")
    Area findByname(String name);

}

package com.ohx.dto;

import com.ohx.entity.Warship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface WarshipRepository extends Neo4jRepository<Warship,Long> {

    /**
     * 获取所有船舶列表
     * **/
    @Query("match (n:Warship) return n")
    Warship[] findAllWarship();

    /**
     * 获取某港口所有关联船舶列表
     * **/
    @Query("match (m:Area),(n:Warship) where (m.name={0} or m.CNname={0}) and ((n)-[]->(m) or (m)-[]->(n)) return n")
    Warship[] findByportName(String portName);

    /**
     * 根据船舶名查船
     * **/
    @Query("match (n:Warship) where n.name={0} return n")
    Warship findByname(String name);

    /**
     * 根据类型获取MMSI列表
     * */

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) return distinct n.mmsi")
//    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0);
    @Query("match (n:Warship) where labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0,String type1);
    @Query("match (n:Warship) where labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"] return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0,String type1,String type2);
    @Query("match (n:Warship) where labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"] " +
            "return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0,String type1,String type2,String type3,String type4,String type5);
    @Query("match (n:Warship) where labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[\"Warship\",{6}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"] " +
            "or labels(n)=[{3},\"Warship\"] or labels(n)=[{4},\"Warship\"] " +
            "or labels(n)=[{5},\"Warship\"] or labels(n)=[{6},\"Warship\"] return distinct n.mmsi")
    ArrayList<String> getMmsiByType(String type0,String type1,String type2,String type3,String type4,String type5,String type6);

    /**
     * 根据船舶mmsi查船
     * **/
    @Query("match (n:Warship) where n.mmsi={0} return n.name")
    String findByMmsi(String mmsi);

    /**
     * 查询所有军舰列表
     */

    @Query("match (n:Warship) where not (n:PatrolShip or n:PatrolVessel or n:PatrolAndTrainingVessel or n:OffshorePatrolVessel or n:OffshoreSupportAndAssistanceVessel or n:OceanPatrolVessel or n:SalvageVessel or n:FishingPatrolVessel) return n")
    Warship[] findAllWarshipList();

    /**
     * 查询所有海警列表
     */

    @Query("match (n:Warship) where n:PatrolShip or n:PatrolVessel or n:PatrolAndTrainingVessel or n:OffshorePatrolVessel or n:OffshoreSupportAndAssistanceVessel or n:OceanPatrolVessel or n:SalvageVessel or n:FishingPatrolVessel return n")
    Warship[] findAllCoastGuardList();

//    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) return distinct n.mmsi")
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSA(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSA(String type0,String type1);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSA(String type0,String type1,String type2);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSA(String type0,String type1,String type2,String type3);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and n.country='USA' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSA(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and n.country='JPN' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryJPN(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='USA' or n.country='CAN' or n.country='FR' or n.country='UK' or n.country='DK' or n.country='NO' or n.country='IT' or n.country='ES' or n.country='GER' or n.country='BE' or n.country='LU' or n.country='NL' or n.country='IS' or n.country='PT' or n.country='GR' or n.country='TUR') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryNATO(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUJKA(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='PHI' or n.country='IN' or n.country='IND' or n.country='THA' or n.country='MAS' or n.country='VN') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSAEastAsia(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='CN' or n.country='RUS' or n.country='PRK') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryCRP(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='IND' or n.country='PHI' or n.country='THA' or n.country='SG' or n.country='MAS' or n.country='VN' or n.country='MM' or n.country='LA' or n.country='BN' or n.country='KH') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryASEAN(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and (n.country='USA' or n.country='UK' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryAUKUS(String type0);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and n.country='JPN' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryJPN(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and n.country='JPN' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryJPN(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and n.country='JPN' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryJPN(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and n.country='JPN' return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryJPN(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='USA' or n.country='CAN' or n.country='FR' or n.country='UK' or n.country='DK' or n.country='NO' or n.country='IT' or n.country='ES' or n.country='GER' or n.country='BE' or n.country='LU' or n.country='NL' or n.country='IS' or n.country='PT' or n.country='GR' or n.country='TUR')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryNATO(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='USA' or n.country='CAN' or n.country='FR' or n.country='UK' or n.country='DK' or n.country='NO' or n.country='IT' or n.country='ES' or n.country='GER' or n.country='BE' or n.country='LU' or n.country='NL' or n.country='IS' or n.country='PT' or n.country='GR' or n.country='TUR')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryNATO(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='USA' or n.country='CAN' or n.country='FR' or n.country='UK' or n.country='DK' or n.country='NO' or n.country='IT' or n.country='ES' or n.country='GER' or n.country='BE' or n.country='LU' or n.country='NL' or n.country='IS' or n.country='PT' or n.country='GR' or n.country='TUR')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryNATO(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='USA' or n.country='CAN' or n.country='FR' or n.country='UK' or n.country='DK' or n.country='NO' or n.country='IT' or n.country='ES' or n.country='GER' or n.country='BE' or n.country='LU' or n.country='NL' or n.country='IS' or n.country='PT' or n.country='GR' or n.country='TUR') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryNATO(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='AUS')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUJKA(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='AUS')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUJKA(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUJKA(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUJKA(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='PHI' or n.country='IN' or n.country='IND' or n.country='THA' or n.country='MAS' or n.country='VN') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSAEastAsia(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='PHI' or n.country='IN' or n.country='IND' or n.country='THA' or n.country='MAS' or n.country='VN')  return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSAEastAsia(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='PHI' or n.country='IN' or n.country='IND' or n.country='THA' or n.country='MAS' or n.country='VN') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSAEastAsia(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='USA' or n.country='JPN' or n.country='KR' or n.country='PHI' or n.country='IN' or n.country='IND' or n.country='THA' or n.country='MAS' or n.country='VN') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryUSAEastAsia(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='CN' or n.country='RUS' or n.country='PRK') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryCRP(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='CN' or n.country='RUS' or n.country='PRK') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryCRP(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='CN' or n.country='RUS' or n.country='PRK') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryCRP(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='CN' or n.country='RUS' or n.country='PRK') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryCRP(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='IND' or n.country='PHI' or n.country='THA' or n.country='SG' or n.country='MAS' or n.country='VN' or n.country='MM' or n.country='LA' or n.country='BN' or n.country='KH') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryASEAN(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='IND' or n.country='PHI' or n.country='THA' or n.country='SG' or n.country='MAS' or n.country='VN' or n.country='MM' or n.country='LA' or n.country='BN' or n.country='KH') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryASEAN(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='IND' or n.country='PHI' or n.country='THA' or n.country='SG' or n.country='MAS' or n.country='VN' or n.country='MM' or n.country='LA' or n.country='BN' or n.country='KH') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryASEAN(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='IND' or n.country='PHI' or n.country='THA' or n.country='SG' or n.country='MAS' or n.country='VN' or n.country='MM' or n.country='LA' or n.country='BN' or n.country='KH') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryASEAN(String type0,String type1,String type2,String type3,String type4,String type5);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and (n.country='USA' or n.country='UK' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryAUKUS(String type0,String type1);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and (n.country='USA' or n.country='UK' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryAUKUS(String type0,String type1,String type2);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and (n.country='USA' or n.country='UK' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryAUKUS(String type0,String type1,String type2,String type3);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and (n.country='USA' or n.country='UK' or n.country='AUS') return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryAUKUS(String type0,String type1,String type2,String type3,String type4,String type5);

    // 新逻辑 - new 屎山

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[{0},\"Warship\"]) and ({1}) return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryS(String type0, String condition);

    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"]) and ({2}) return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryS(String type0,String type1, String condition);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[{0},\"Warship\"] " +
            "or labels(n)=[{1},\"Warship\"] or labels(n)=[{2},\"Warship\"]) and ({3}) return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryS(String type0,String type1,String type2, String condition);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"]) and ({4}) return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryS(String type0,String type1,String type2,String type3, String condition);
    @Query("match (n:Warship) where (labels(n)=[\"Warship\",{0}] or labels(n)=[\"Warship\",{1}] " +
            "or labels(n)=[\"Warship\",{2}] or labels(n)=[\"Warship\",{3}] " +
            "or labels(n)=[\"Warship\",{4}] or labels(n)=[\"Warship\",{5}] " +
            "or labels(n)=[{0},\"Warship\"] or labels(n)=[{1},\"Warship\"] " +
            "or labels(n)=[{2},\"Warship\"] or labels(n)=[{3},\"Warship\"] " +
            "or labels(n)=[{4},\"Warship\"] or labels(n)=[{5},\"Warship\"])" +
            " and ({6}) return distinct n.mmsi")
    ArrayList<String> getMMSIByTypeAndCountryS(String type0,String type1,String type2,String type3,String type4,String type5, String condition);

}

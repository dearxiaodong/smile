package springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import springboot.modal.vo.RelationShipVo;
import springboot.modal.vo.RelationShipVoExample;

public interface RelationShipVoMapper {
    long countByExample(RelationShipVoExample example);

    int deleteByExample(RelationShipVoExample example);

    int deleteByPrimaryKey(Integer cid);

    int insert(RelationShipVo record);

    int insertSelective(RelationShipVo record);

    List<RelationShipVo> selectByExample(RelationShipVoExample example);

    RelationShipVo selectByPrimaryKey(Integer cid);

    int updateByExampleSelective(@Param("record") RelationShipVo record, @Param("example") RelationShipVoExample example);

    int updateByExample(@Param("record") RelationShipVo record, @Param("example") RelationShipVoExample example);

    int updateByPrimaryKeySelective(RelationShipVo record);

    int updateByPrimaryKey(RelationShipVo record);
}
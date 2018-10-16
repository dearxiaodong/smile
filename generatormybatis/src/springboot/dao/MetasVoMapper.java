package springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import springboot.modal.vo.MetasVo;
import springboot.modal.vo.MetasVoExample;

public interface MetasVoMapper {
    long countByExample(MetasVoExample example);

    int deleteByExample(MetasVoExample example);

    int deleteByPrimaryKey(Integer mid);

    int insert(MetasVo record);

    int insertSelective(MetasVo record);

    List<MetasVo> selectByExample(MetasVoExample example);

    MetasVo selectByPrimaryKey(Integer mid);

    int updateByExampleSelective(@Param("record") MetasVo record, @Param("example") MetasVoExample example);

    int updateByExample(@Param("record") MetasVo record, @Param("example") MetasVoExample example);

    int updateByPrimaryKeySelective(MetasVo record);

    int updateByPrimaryKey(MetasVo record);
}
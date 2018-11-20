package springboot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import springboot.modal.vo.OptionVo;
import springboot.modal.vo.OptionVoExample;
@Component
public interface OptionVoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    long countByExample(OptionVoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int deleteByExample(OptionVoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int insert(OptionVo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int insertSelective(OptionVo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    List<OptionVo> selectByExample(OptionVoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    OptionVo selectByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int updateByExampleSelective(@Param("record") OptionVo record, @Param("example") OptionVoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int updateByExample(@Param("record") OptionVo record, @Param("example") OptionVoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int updateByPrimaryKeySelective(OptionVo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_options
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    int updateByPrimaryKey(OptionVo record);
}
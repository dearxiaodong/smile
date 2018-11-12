package springboot.service;

import springboot.modal.vo.RelationShipVoKey;

import java.util.List;

/**
 * @author 357069486@qq.com
 * @date 2018-11-12 13:25
 */
public interface IRelationshipService {

    /**
     * 按主键删除
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);

    /**
     * 按主键统计条数
     * @param cid
     * @param mid
     * @return 条数
     */
    Long countById(Integer cid, Integer mid);


    /**
     * 保存對象
     * @param relationshipVoKey
     */
    void insertVo(RelationShipVoKey relationshipVoKey);

    /**
     * 根据id搜索
     * @param cid
     * @param mid
     * @return
     */
    List<RelationShipVoKey> getRelationshipById(Integer cid, Integer mid);


}

package springboot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springboot.dao.RelationShipVoMapper;
import springboot.modal.vo.RelationShipVoExample;
import springboot.modal.vo.RelationShipVoKey;
import springboot.service.IRelationshipService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 357069486@qq.com
 * @date 2018-11-16 14:54
 */
@Service
public class RelationshipServiceImpl implements IRelationshipService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipServiceImpl.class);

    @Resource
    private RelationShipVoMapper relationShipDao;

    @Override
    public void deleteById(Integer cid, Integer mid) {
        RelationShipVoExample relationShipVoExample=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria=relationShipVoExample.createCriteria();
        if (null!=cid){
            criteria.andCidEqualTo(cid);

        }

        if(null!=mid){
            criteria.andMidEqualTo(mid);
        }
        relationShipDao.deleteByExample(relationShipVoExample);

    }

    @Override
    public Long countById(Integer cid, Integer mid) {
        RelationShipVoExample relationShipVoExample=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria=relationShipVoExample.createCriteria();
        if (null!=cid){
            criteria.andCidEqualTo(cid);

        }

        if(null!=mid){
            criteria.andMidEqualTo(mid);
        }
        long num =relationShipDao.countByExample(relationShipVoExample);
        return num;


    }

    @Override
    public void insertVo(RelationShipVoKey relationshipVoKey) {
        relationShipDao.insert(relationshipVoKey);
    }

    @Override
    public List<RelationShipVoKey> getRelationshipById(Integer cid, Integer mid) {

        RelationShipVoExample relationShipVoExample=new RelationShipVoExample();
        RelationShipVoExample.Criteria criteria=relationShipVoExample.createCriteria();
        if (null!=cid){
            criteria.andCidEqualTo(cid);

        }

        if(null!=mid){
            criteria.andMidEqualTo(mid);
        }
        return relationShipDao.selectByExample(relationShipVoExample);
    }
}

package springboot.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springboot.constant.WebConst;
import springboot.dao.MetasVoMapper;
import springboot.dto.MetaDto;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.MetasVo;
import springboot.modal.vo.MetasVoExample;
import springboot.modal.vo.RelationShipVoKey;
import springboot.service.IContentService;
import springboot.service.IMetaService;
import springboot.service.IRelationshipService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 357069486@qq.com
 * @date 2018-11-16 14:54
 */
@Service
public class MetaServiceImpl implements IMetaService {

    @Resource
    private MetasVoMapper metaDao;
    @Resource
    private IRelationshipService relationshipService;

    @Resource
    private IContentService contentService;
    @Override
    public MetaDto getMeta(String type, String name) {

        if(StringUtils.isNotBlank(type)&&StringUtils.isNotBlank(name))
        {
            return metaDao.selectDtoByNameAndType(name,type);

        }

        return null;
    }

    @Override
    public Integer countMeta(Integer mid) {
        return metaDao.countWithSql(mid);
    }

    @Override
    public List<MetasVo> getMetas(String types) {

        if (StringUtils.isNotBlank(types)) {
            MetasVoExample metaVoExample = new MetasVoExample();
            metaVoExample.setOrderByClause("sort desc, mid desc");
            metaVoExample.createCriteria().andTypeEqualTo(types);
            return metaDao.selectByExample(metaVoExample);
        }
        return null;
    }

    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(Integer cid, String name, String type) {
        MetasVoExample metaVoExample = new MetasVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
        List<MetasVo> metaVos = metaDao.selectByExample(metaVoExample);

        int mid;
        MetasVo metas;
        if (metaVos.size() == 1) {
            metas = metaVos.get(0);
            mid = metas.getMid();
        } else if (metaVos.size() > 1) {
            throw new TipException("查询到多条数据");
        } else {
            metas = new MetasVo();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metaDao.insertSelective(metas);
            mid = metas.getMid();
        }
        if (mid != 0) {
            Long count = relationshipService.countById(cid, mid);
            if (count == 0) {
                RelationShipVoKey relationships = new RelationShipVoKey();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipService.insertVo(relationships);
            }
        }
    }

    @Override
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            MetasVoExample metaVoExample = new MetasVoExample();
            metaVoExample.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
            List<MetasVo> metaVos = metaDao.selectByExample(metaVoExample);
            MetasVo metas;
            if (metaVos.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                metas = new MetasVo();
                metas.setName(name);
                if (null != mid) {
                    MetasVo original = metaDao.selectByPrimaryKey(mid);
                    metas.setMid(mid);
                    metaDao.updateByPrimaryKeySelective(metas);
//                    更新原有文章的categories
                    contentService.updateCategory(original.getName(),name);
                } else {
                    metas.setType(type);
                    metaDao.insertSelective(metas);
                }
            }
        }
    }

    @Override
    public List<MetaDto> getMetaList(String type, String orderby, int limit) {
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isBlank(orderby)) {
                orderby = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderby);
            paraMap.put("limit", limit);
            return metaDao.selectFromSql(paraMap);
        }
        return null;
    }

    @Override
    public void delete(int mid) {
        MetasVo metas = metaDao.selectByPrimaryKey(mid);
        if (null != metas) {
            String type = metas.getType();
            String name = metas.getName();

            metaDao.deleteByPrimaryKey(mid);

            List<RelationShipVoKey> rlist = relationshipService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (RelationShipVoKey r : rlist) {
                    ContentVo contents = contentService.getContents(String.valueOf(r.getCid()));
                    if (null != contents) {
                        ContentVo temp = new ContentVo();
                        temp.setCid(r.getCid());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                        }
                        if (type.equals(Types.TAG.getType())) {
                            temp.setTags(reMeta(name, contents.getTags()));
                        }
                        contentService.updateContentByCid(temp);
                    }
                }
            }
            relationshipService.deleteById(null, mid);
        }
    }
    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }
    @Override
    public void saveMeta(MetasVo metas) {
        if (null != metas) {
            metaDao.insertSelective(metas);
        }
    }

    @Override
    public void update(MetasVo metas) {
        if (null != metas && null != metas.getMid()) {
            metaDao.updateByPrimaryKeySelective(metas);
        }
    }
}

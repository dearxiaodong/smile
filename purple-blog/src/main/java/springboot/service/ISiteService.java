package springboot.service;

import springboot.dto.MetaDto;
import springboot.modal.bo.ArchiveBo;
import springboot.modal.bo.BackResponseBo;
import springboot.modal.bo.StatisticsBo;
import springboot.modal.vo.CommentVo;
import springboot.modal.vo.ContentVo;

import java.util.List;

/**
 * @author 357069486@qq.com
 * @date 2018-11-12 13:24
 * 站点服务
 */
public interface ISiteService {

    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    List<CommentVo> recentComments(int limit);

    /**
     * 最新发表的文章
     *
     * @param limit
     * @return
     */
    List<ContentVo> recentContents(int limit);

    /**
     * 查询一条评论
     * @param coid
     * @return
     */
    CommentVo getComment(Integer coid);

    /**
     * 系统备份
     * @param bk_type
     * @param bk_path
     * @param fmt
     * @return
     */
    BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception;


    /**
     * 获取后台统计数据
     *
     * @return
     */
   StatisticsBo getStatistics();

    /**
     * 查询文章归档
     *
     * @return
     */
    List<ArchiveBo> getArchives();

    /**
     * 获取分类/标签列表
     * @return
     */
   List<MetaDto> metas(String type, String orderBy, int limit);


}

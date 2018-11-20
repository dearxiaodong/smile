package springboot.controller.home;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.dto.MetaDto;
import springboot.dto.Types;
import springboot.modal.vo.ContentVo;
import springboot.service.ICommentService;
import springboot.service.IContentService;
import springboot.service.IMetaService;
import springboot.service.ISiteService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 357069486@qq.com
 * @date 2018-11-15 14:12
 */
@Controller
public class IndexController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private IContentService contentService;

    @Autowired
    private ICommentService commentService;

    @Resource
    private IMetaService metaService;

    @Resource
    private ISiteService siteService;
    /**
     * 博客首页
     *
     * @param request
     * @param limit
     * @return
     */
    @GetMapping(value = "/")
    private String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return this.index(request, 1, limit);
    }

    /**
     * 文章页
     *
     * @param request
     * @param p
     * @param limit
     * @return
     */
    @GetMapping(value = "page/{p}")
    public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        // 开启thymeleaf缓存，加快访问速度
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        PageInfo<ContentVo> articles = contentService.getContents(p, limit);
        List<MetaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        request.setAttribute("categories", categories);
        request.setAttribute("articles", articles);
        if (p > 1) {
            this.title(request, "第" + p + "页");
        }
        return this.render("index");
    }





}

package springboot.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import springboot.constant.WebConst;
import springboot.dao.LogVoMapper;
import springboot.modal.vo.LogVo;
import springboot.modal.vo.LogVoExample;
import springboot.service.ILogService;
import springboot.util.DateKit;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author 357069486@qq.com
 * @date 2018-11-14 10:57
 */
@Service
public class LogServiceImpl implements ILogService {

    @Resource
    private LogVoMapper logVoMapper;

    @Override
    public void insertLog(LogVo logVo) {
        logVoMapper.insert(logVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        LogVo logVo=new LogVo();
        logVo.setAction(action);
        logVo.setData(data);
        logVo.setIp(ip);
        logVo.setAuthorId(authorId);
        logVo.setCreated(DateKit.getCurrentUnixTime());
        logVoMapper.insert(logVo);
    }

    @Override
    public List<LogVo> getLogs(int page, int limit) {
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        LogVoExample logVoExample = new LogVoExample();
        logVoExample.setOrderByClause("id desc");
        PageHelper.startPage((page - 1) * limit, limit);
        List<LogVo> logVos = logVoMapper.selectByExample(logVoExample);
        return logVos;
    }
}

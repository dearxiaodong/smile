package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.dao.UserVoMapper;
import springboot.modal.vo.UserVo;
import springboot.modal.vo.UserVoExample;

import java.util.List;

@Service
public class UserServiceTest {



    @Autowired
    private UserVoMapper userVoMapper;
    public List<UserVo> getUser(UserVoExample userVoExample) {
        List<UserVo> list = userVoMapper.selectByExample(userVoExample);
        return list;
    }

    public UserVo getUserById(int userId) {
        UserVo userVo = new UserVo();
        userVo = userVoMapper.selectByPrimaryKey(userId);
        return userVo;

    }


}

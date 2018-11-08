package springboot.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.dao.UserVoMapper;
import springboot.exception.TipException;
import springboot.modal.vo.UserVo;
import springboot.modal.vo.UserVoExample;
import springboot.service.IUserService;
import springboot.util.MD5Util;

import java.util.List;


/**
 * @author 357069486@qq.com
 * @date 2018-11-8 14:21
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserVoMapper userDao;


    @Override
    public Integer insertUser(UserVo userVo) {
            Integer uid =null;
            if(StringUtils.isNotBlank(userVo.getUsername())&& StringUtils.isNotBlank(userVo.getPassword())) {
           //判断用户是否已存在
                UserVoExample userVoExample= new UserVoExample();
                UserVoExample.Criteria criteria=userVoExample.createCriteria();
                criteria.andUsernameEqualTo(userVo.getUsername());
                long count=userDao.countByExample(userVoExample);
                if (count>0){
                    throw new TipException("此用户已存在");
                }
                  //用户密码加密  todo
                String encodePwd= MD5Util.getMD5String(userVo.getPassword());
                userVo.setPassword(encodePwd);
                userDao.insertSelective(userVo);
            }
            return userVo.getUid();
    }

    @Override
    public UserVo queryByUserId(Integer uid) {

        UserVo userVo=null;
        if (uid !=null){

            userVo=userDao.selectByPrimaryKey(uid);
        }

        return userVo;
    }

    @Override
    public void updateByUid(UserVo userVo) {
       if (null==userVo || userVo.getUid()==null){
           throw new TipException("userVo is null");

       }
     int iRet=userDao.updateByPrimaryKeySelective(userVo);

       if (iRet!=1){
           throw  new TipException("update user by uid and retrun is not one");
       }


    }

    @Override
    public void deleteByUid(Integer uid) {
    // todo


    }

    @Override
    public UserVo login(String userName, String password) {
        if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            throw  new TipException("用户名和密码不能为空");
        }
        UserVoExample userVoExample= new UserVoExample();
        UserVoExample.Criteria criteria=userVoExample.createCriteria();
        criteria.andUsernameEqualTo(userName);
        long count=userDao.countByExample(userVoExample);
        if (count<1){
            throw new TipException("此用户不存在");
        }
        String pwd=MD5Util.getMD5String(password);
        criteria.andPasswordEqualTo(pwd);

        List<UserVo>  userVoList=userDao.selectByExample(userVoExample);
        if (userVoList.size()!=1){
            throw new TipException("用户名或者密码错误");

        }
            return userVoList.get(0);
    }
}

package springboot.service;

import org.apache.catalina.User;
import springboot.modal.vo.UserVo;

/**
 * @author 357069486@qq.com
 * @date 2018-11-8 14:13
 */
public interface IUserService {

    /**
     *  新增用户
     * @param userVo
     * @return 主键
     */

    Integer insertUser(UserVo userVo);

    /**
     *  根据uid 查询用户
     * @param uid
     * @return
     */
    UserVo queryByUserId(Integer uid);


    /**
     * 更新用户
     * @param userVo
     * @return
     */
    void updateByUid(UserVo userVo);

    /**
     * 删除用户
     * @param uid
     * @return
     */
    void deleteByUid(Integer uid);

    /**
     * d登录
     * @param userName
     * @param password
     * @return
     */
    UserVo login(String userName,String password);


}

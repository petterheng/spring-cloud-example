package cn.sh.user.service;


import cn.sh.common.entity.User;

import java.util.List;

/**
 * @author sh
 */
public interface UserService {

    User getUserById(Long id);

    List<User> findAll(List<Long> idList);
}

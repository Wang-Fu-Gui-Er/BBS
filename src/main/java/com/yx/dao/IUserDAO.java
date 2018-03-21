package com.yx.dao;

import com.yx.po.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 * User,Integer 类名，主键类型
 * 直接实现JPA的方法
 */
@Repository//表明是DAO层
@Transactional
public interface IUserDAO extends CrudRepository<User,Integer> {

    // 登录验证【保证注册时用户名不重复】
    @Query("select u from User u where username =:username and password =:password")
    User login(@Param("username") String username,@Param("password") String password);

    //通过主键查找user【用于登录后显示头像】
    User findOne(Integer userid);
    // 注册时保存新用户
    User save(User user);
    // 注册时检验是否存在该用户
    @Query("select u from User u where username =:username")
    User queryByUsername(@Param("username") String username);
}

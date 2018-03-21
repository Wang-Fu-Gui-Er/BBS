package com.yx.dao;

import com.yx.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional //事务处理
public interface IArticleDAO extends CrudRepository<Article,Integer> {
    // 查询所有主帖，得分页
    @Query("select a from Article a where rootid=:rootid")
    Page<Article> queryArticlesByPage(Pageable pageable,@Param("rootid") int rootid);

    // 删除主帖,把它的从帖也得全删了
    @Modifying
    @Query("delete from Article where artid=:artid or rootid=:rootid")
    void delArticles(@Param("artid") int artid, @Param("rootid") int rootid);

    // 增加帖子
     Article save(Article article);

     //查询同一主帖的全部从帖
    @Query("select a from Article a where rootid=:rootid order by artid")
    List<Article> queryArticleByRootid(@Param("rootid") int rootid);

    //根据主键查询帖子
    @Query("select a from Article a where artid=:artid")
    Article queryArticleByArtid(@Param("artid") int artid);

    //根据主键artid删除帖子【删除从帖】
    void delete(int artid);
}

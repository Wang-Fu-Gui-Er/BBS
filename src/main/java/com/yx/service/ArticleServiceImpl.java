package com.yx.service;

import com.yx.dao.IArticleDAO;
import com.yx.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl {
    @Autowired
    private IArticleDAO articleDAO;

    public Page<Article> queryArticlesByPage(Pageable pageable,int rootid){
        return articleDAO.queryArticlesByPage(pageable, rootid);
    }

    public void delArticles(int artid, int rootid){
        articleDAO.delArticles(artid,rootid);
    }

    public Article save(Article article){
        return articleDAO.save(article);
    }

    public List<Article> queryArticleByRootid(int rootid){
        return articleDAO.queryArticleByRootid(rootid);
    }

    public Article queryArticleByArtid(int artid){
        return articleDAO.queryArticleByArtid(artid);
    }

    public void delete(int artid){
        articleDAO.delete(artid);
    }
}

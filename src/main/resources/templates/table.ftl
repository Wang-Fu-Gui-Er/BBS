<div id="contentwrapper">
    <div class="main_content">
        <div class="row-fluid">
            <div class="span12">
                <h2 class="heading">江 湖 日 报，最 新 鲜 的 江 湖 日 志</h2>
                <div class="alert alert-error">
                    <a class="close" data-dismiss="alert">×</a>
                    <strong>操作信息:
                    <#if loginUser??>
                        欢迎 <font size="3" color="black">${loginUser.username}</font>！
                    <#else>
                        <#if (loginMsg??)>
                            ${loginMsg}
                        <#else>
                            欢迎游客
                        </#if>
                    </#if>
                    <#if (regMsg??)>
                    ${regMsg}
                    </#if>
                    </strong>
                </div>
                <div class="btn-group sepH_b">
                    <#assign cruPage = articles.getNumber()>
                    <button data-toggle="dropdown" class="btn dropdown-toggle">
                        行数 <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="article?action=queryAllArticle&curPage=0&pageSize=5">
                                默认5行
                            </a>
                        </li>
                        <li>
                            <a href="article?action=queryAllArticle&curPage=0&pageSize=10">
                                每页10行
                            </a>
                        </li>
                        <li>
                            <a href="article?action=queryAllArticle&curPage=0&pageSize=2">
                                每页2行
                            </a>
                        </li>
                    </ul>
                </div>
                <table class="table table-bordered table-striped table_vam"
                       id="dt_gal">
                    <thead>
                    <tr>
                        <th class="table_checkbox">序号</th>
                        <th style="width: 50px">发布人</th>
                        <th style="width: 100px">主帖标题</th>
                        <th style="width: 420px">主帖内容</th>
                        <th style="width: 60px">发布日期</th>
                        <th style="width: 60px">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list articles.content as article>
                    <tr>
                        <td>${article_index+1}</td>
                        <td><label>${article.user.username}</label>
                                <img src="user?action=headPic&userid=${article.user.userid}"
                                     alt="加载失败" style="height: 70px; width: 100px"/>
                        </td>
                        <td>${article.title}</td>
                        <td>${article.content}</td>
                        <td>${article.time}</td>
                        <td>
                            <#if loginUser??>
                                <#assign userid=loginUser.userid>
                            <#else >
                                <#assign userid=-1>
                            </#if>
                            <a href="#rshow" title="回复" data-toggle="modal"
                               id="myp" data-backdrop="static"
                               onclick="rshow(${article.artid},${userid},${article.user.userid})">
                                <i class="icon-eye-open"></i>
                            </a>
                            <!-- 是本人贴可以删除和修改 -->
                        <#if (loginUser??) && article.user.userid==loginUser.userid>
                            <a
                                href="article?action=delZC&artid=${article.artid}"
                                title="删除本帖"><i class="icon-trash"></i></a>
                        </#if>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    <#include "page.ftl" encoding="utf-8">
    </div>
</div>
<!--分页设置-->
<div class="pagination">
    <ul>
        <li>
            <a href="article?action=queryAllArticle&curPage=0&pageSize=${pageSize}">首页</a>
            <#assign pageSize = pageSize>
            <#if articles.hasPrevious()>
                <a href="article?action=queryAllArticle&curPage=${articles.getNumber()-1}&pageSize=${pageSize}">前一页</a>
            </#if>
        </li>
        <li>
            <#list 0..articles.getTotalPages()-1 as i>
                <a href="article?action=queryAllArticle&curPage=${i}&pageSize=${pageSize}">${i+1}</a>
            </#list>
        </li>
        <li>
            <#if articles.hasNext()>
                <a href="article?action=queryAllArticle&curPage=${articles.getNumber()+1}&pageSize=${pageSize}">下一页</a>
            </#if>
        </li>
        <li>
            <#--分页，Spring是从1开始，Freemarker是从0开始-->
            <a href="article?action=queryAllArticle&curPage=${articles.getTotalPages()-1}&pageSize=${pageSize}">尾页</a>
        </li>
    </ul>
</div>
<html>
    <head></head>
    <body>

        ${username}
        <#if age gte 18>
        <p >成年人</p>
        </#if>
        <p>学生列表</p>
        <#list  stus as stu  >

            <p>${stu.name}:${stu.age}</p>

        </#list>


       <p>${num?c}</p>

        <p>${a}</p>

    </body>
</html>
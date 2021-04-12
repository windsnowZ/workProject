package com.portal;

import com.portal.entity.ESUser;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    private ElasticsearchRestTemplate template;


     @Test
     public void creteatIndex  (){

         //创建索引库
         template.createIndex(ESUser.class);

         //设置mappings
         template.putMapping(ESUser.class);


     }


     @Test
     public void saveDoc (){

         ESUser doc = new ESUser();
         doc.setId(2);
         doc.setAddress("我爱台湾省");
         doc.setJob("后台开发工程师");

         ESUser save = template.save(doc);

     }


     @Test
     public void testDelete (){

         String delete = template.delete("1", ESUser.class);

     }


     @Test
     //match_all
     public void testQuery1 (){

         MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();


         NativeSearchQuery build = new NativeSearchQueryBuilder()
             //指定查询方式
             .withQuery(matchAllQueryBuilder)
             .build();

         SearchHits<ESUser> res = template.search(build, ESUser.class);

         //获取总记录数
         long totalHits = res.getTotalHits();
         System.out.println("总命中数"+totalHits);

         //获取文档列表
         List<ESUser> docs = new ArrayList();
         List<SearchHit<ESUser>> searchHits = res.getSearchHits();
         searchHits.forEach(hit->{
             System.out.println(hit);
             //获取_source
             ESUser content = hit.getContent();
             System.out.println(content);
             docs.add(content);
         });

         System.out.println("=============================");
         docs.forEach(System.out::println);

     }

     //match
    @Test
    public void testQuery2 (){


         //查询方式
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address","我喜欢武汉");


         NativeSearchQuery build = new NativeSearchQueryBuilder()
             //查询方式
             .withQuery(matchQueryBuilder)
             .build();


        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取命中文档列表
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();

        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            ESUser content = hit.getContent();
            esUsers.add(content);

        });


        esUsers.forEach(System.out::println);

    }

    @Test
    public void testQuer3 (){

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("address","武汉市");

        NativeSearchQuery build = new NativeSearchQueryBuilder()
            //查询方式
            .withQuery(termQueryBuilder)
            .build();

        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取命中文档列表
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();

        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            ESUser content = hit.getContent();
            esUsers.add(content);

        });


        esUsers.forEach(System.out::println);

    }


    @Test
    public void testQuery4 (){

         //范围查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").lte(30).gte(18);


        NativeSearchQuery build = new NativeSearchQueryBuilder()
            //查询方式
            .withQuery(rangeQueryBuilder)
            .build();

        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取命中文档列表
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();

        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            ESUser content = hit.getContent();
            esUsers.add(content);

        });


        esUsers.forEach(System.out::println);
    }


    @Test
    public void testQuery5 (){

        //范围查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").lte(30).gte(18);

        //设置分页信息
        PageRequest pageRequest = PageRequest.of(1,2);

        NativeSearchQuery build = new NativeSearchQueryBuilder()
            //查询方式
            .withQuery(rangeQueryBuilder)
            //设置分页
            .withPageable(pageRequest)
            .build();

        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取当前页数据
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();

        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            ESUser content = hit.getContent();
            esUsers.add(content);

        });


        esUsers.forEach(System.out::println);
    }


    @Test
    public void testQuery6 (){

        //复合查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address","武汉市的人");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("job","工程师1");


        //must
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);



        //设置分页信息
        PageRequest pageRequest = PageRequest.of(0,200);

        NativeSearchQuery build = new NativeSearchQueryBuilder()
            //查询方式
            .withQuery(boolQueryBuilder)
            //设置分页
            .withPageable(pageRequest)
            .build();

        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取当前页数据
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();

        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            ESUser content = hit.getContent();
            esUsers.add(content);

        });


        esUsers.forEach(System.out::println);
    }

    @Test
    public void testQuery7 (){

         //查询方式
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.
            matchQuery("address","武汉的人");


         //设置高亮域
        HighlightBuilder addressHighLightBuilder = getHighlightBuilder("address");


        NativeSearchQuery build = new NativeSearchQueryBuilder()
            //查询方式
            .withQuery(matchQueryBuilder)
            //设置高亮
            .withHighlightBuilder(addressHighLightBuilder)
            .build();

        SearchHits<ESUser> searchHits = template.search(build, ESUser.class);
        long totalHits = searchHits.getTotalHits();//命中记录数
        System.out.println("命中记录数"+totalHits);

        //获取当前页数据
        List<SearchHit<ESUser>> searchHits1 = searchHits.getSearchHits();
        List<ESUser> esUsers = new ArrayList<>();
        searchHits1.forEach(hit->{
            //获取source
            ESUser content = hit.getContent();


            //取高亮
            Map<String, List<String>> highlightFields = hit.getHighlightFields();

            Set<String> keySet = highlightFields.keySet();

            for (String highLightField : keySet) {
                if("address".equals(highLightField)){
                    String addressHighLightValue = highlightFields.get(highLightField).get(0);
                    content.setAddress(addressHighLightValue);
                }
            }


            esUsers.add(content);


        });


        esUsers.forEach(System.out::println);




    }

    // 设置高亮字段
    private HighlightBuilder getHighlightBuilder(String... fields) {
        // 高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //生成高亮查询器
        for (String field : fields) {
            highlightBuilder.field(field);//高亮查询字段
        }
        highlightBuilder.requireFieldMatch(false);     //如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        return highlightBuilder;
    }




}
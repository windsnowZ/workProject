package com.portal.service.impl;

import com.api.goods.GoodsClient;
import com.portal.entity.ESGoods;
import com.portal.entity.SearchPageResult;
import com.portal.entity.goods.WxbGoods;
import com.portal.service.ISearchService;
import com.portal.vo.PageResultVO;
import com.portal.vo.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>title: com.portal.service.impl</p>
 * author zhuximing
 * description:
 */
@Service
public class SearchService implements ISearchService {

    @Autowired
    private GoodsClient goodsClient;


    @Autowired
    private ElasticsearchRestTemplate template;


    @Override
    public Result initData2es() {

        //1：获取审核通过的spu信息
        List<WxbGoods> auditSpuInfo = goodsClient.findAuditSpuInfo();


        //2:同步索引库
        auditSpuInfo.forEach(spu->{

            //spu->esGoods
            ESGoods esGoods = new ESGoods();
            esGoods.setSpuId(spu.getId());

            esGoods.setBrandId(spu.getBrand().getId());
            esGoods.setBrandName(spu.getBrand().getName());


            esGoods.setCid1id(spu.getCat1().getId());
            esGoods.setCat1name(spu.getCat1().getName());
            esGoods.setCid2id(spu.getCat2().getId());
            esGoods.setCat2name(spu.getCat2().getName());
            esGoods.setCid3id(spu.getCat3().getId());
            esGoods.setCat3name(spu.getCat3().getName());

            esGoods.setCreateTime(new Date());
            esGoods.setGoodsName(spu.getGoodsName());
            esGoods.setPrice(spu.getPrice().doubleValue());
            esGoods.setSmallPic(spu.getSmallPic());


            template.save(esGoods);
        });





        return new Result(true,"初始化成功");
    }

    @Override
    public Result goods2es(WxbGoods spu) {

        //spu->esGoods
        ESGoods esGoods = new ESGoods();
        esGoods.setSpuId(spu.getId());

        esGoods.setBrandId(spu.getBrand().getId());
        esGoods.setBrandName(spu.getBrand().getName());


        esGoods.setCid1id(spu.getCat1().getId());
        esGoods.setCat1name(spu.getCat1().getName());
        esGoods.setCid2id(spu.getCat2().getId());
        esGoods.setCat2name(spu.getCat2().getName());
        esGoods.setCid3id(spu.getCat3().getId());
        esGoods.setCat3name(spu.getCat3().getName());

        esGoods.setCreateTime(new Date());
        esGoods.setGoodsName(spu.getGoodsName());
        esGoods.setPrice(spu.getPrice().doubleValue());
        esGoods.setSmallPic(spu.getSmallPic());


        template.save(esGoods);



        return new Result(true,"同步成功");
    }

    @Override
    public PageResultVO<ESGoods> search(Map searchMap) {
        if (searchMap == null)
            return new PageResultVO<>(false,"参数不合法");

        //1:指定查询方式【复合查询】
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(searchMap.get("keyword"))){
            //goodsName
            MatchQueryBuilder goodsNameQueryBuilder = QueryBuilders.
                matchQuery("goodsName",searchMap.get("keyword"));
            //brandName
            MatchQueryBuilder brandNameQueryBuilder = QueryBuilders.
                matchQuery("brandName",searchMap.get("keyword"));
            //cat3name
            MatchQueryBuilder cat3nameQueryBuilder = QueryBuilders.
                matchQuery("cat3name",searchMap.get("keyword"));

            boolQueryBuilder.should(goodsNameQueryBuilder);
            boolQueryBuilder.should(brandNameQueryBuilder);
            boolQueryBuilder.should(cat3nameQueryBuilder);




        }




        //2：设置分页
        int page = 0;
        if (!StringUtils.isEmpty(searchMap.get("page"))) {
            page = (int) searchMap.get("page");
            page --;
        }

        int size = 2;
        if (!StringUtils.isEmpty(searchMap.get("size"))) {
            size = (int) searchMap.get("size");
        }

        PageRequest pageRequest = PageRequest.of(page,size);


        //3:设置高亮
        HighlightBuilder goodsNameHighlightBuilder = getHighlightBuilder("goodsName");

        //4:聚合【brandName，cat3name】
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brandName_agg")
            .field("brandName.keyword");

        TermsAggregationBuilder cat3nameAgg = AggregationBuilders.terms("cat3name_agg")
            .field("cat3name.keyword");


        //5:过滤【品牌、第三级分类】
        if(!StringUtils.isEmpty(searchMap.get("brandNameFilter"))){

            TermQueryBuilder brandNameFilterBuilder = QueryBuilders.
                termQuery("brandName.keyword",searchMap.get("brandNameFilter"));
            boolQueryBuilder.filter(brandNameFilterBuilder);

        }

        if (!StringUtils.isEmpty(searchMap.get("cat3NameFilter"))) {

            TermQueryBuilder cat3NameFilterBuilder = QueryBuilders.
                termQuery("cat3name.keyword",searchMap.get("cat3NameFilter"));
            boolQueryBuilder.filter(cat3NameFilterBuilder);
        }


        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
            //设置查询方式
            .withQuery(boolQueryBuilder)
            //设置分页
            .withPageable(pageRequest)
            //设置高亮
            .withHighlightBuilder(goodsNameHighlightBuilder)
            //设置聚合
            .addAggregation(brandNameAgg)
            .addAggregation(cat3nameAgg);


        SortBuilder priceSortBuilder = null;
        //6:排序【价格】
        if (!StringUtils.isEmpty(searchMap.get("priceSort"))) {
            if("desc".equals(searchMap.get("priceSort"))){
                priceSortBuilder = SortBuilders.fieldSort("price")
                    .order(SortOrder.DESC);
            }else{
                 priceSortBuilder = SortBuilders.fieldSort("price")
                    .order(SortOrder.ASC);
            }

            nativeSearchQueryBuilder.withSort(priceSortBuilder);

        }

        NativeSearchQuery builder =nativeSearchQueryBuilder
            .build();

        SearchHits<ESGoods> res = template.search(builder, ESGoods.class);

        //获取分页信息
        long totalHits = res.getTotalHits();
        //获取当前页数据
        List<ESGoods> esGoodsList = new ArrayList<>();
        List<SearchHit<ESGoods>> searchHits = res.getSearchHits();
        searchHits.forEach(hit->{
            ESGoods content = hit.getContent();
            //取高亮
            Map<String, List<String>> highlightFields = hit.getHighlightFields();
            Set<String> keySet = highlightFields.keySet();
            keySet.forEach(key->{
               if("goodsName".equals(key)){
                   List<String> list = highlightFields.get(key);
                   content.setGoodsName(list.get(0));
               }
            });


            esGoodsList.add(content);
        });


        //获取聚合信息
        Aggregations aggregations = res.getAggregations();
        List cat3NameList = new ArrayList();
        //cat3Name_agg
        Terms cat3Name_agg = aggregations.get("cat3name_agg");
        List<? extends Terms.Bucket> buckets = cat3Name_agg.getBuckets();
        buckets.forEach(b->{
            cat3NameList.add( b.getKey());
        });



        //获取品牌的聚合信息
        List brandNameList = new ArrayList();
        Terms brandName_agg = aggregations.get("brandName_agg");
        brandName_agg.getBuckets().forEach(b->{
            brandNameList.add(b.getKey());
        });

        //PageResultVO
        SearchPageResult pageinfo = new SearchPageResult();
        pageinfo.setTotal(totalHits);
        pageinfo.setData(esGoodsList);
        pageinfo.setBrandNameList(brandNameList);
        pageinfo.setCat3NameList(cat3NameList);

        return pageinfo;
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
package com.portal.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
//indexName索引库名称
//shards索引库的分片数
//replicas每片的副本数
@Document(indexName = "es_user_3",shards = 2,replicas = 1)
public class ESUser {

    //_id不给值es默认生成
    //@Id  id属性值映射给_id
    @Id
    private Integer id;
    //type 默认值FieldType.Auto【根据属性的java类型自动确定es文档的域类型】
    //index 默认值true   如果域参与搜索、过滤、排序、聚合 index必须为true，否则是false 比如商品图片url
    //store 默认值false  当该域不需要在客户端显示，可以将 store设置为false 比如：商品详情信息
    //analyzer 默认值为“”  如果域需要中文分词，那么analyzer="ik_max_word"
    @Field(store = true,type = FieldType.Integer)
    private Integer account_number;
    @Field(store = true,type = FieldType.Integer)
    private Integer balance;
    // FieldType.Keywords 是不会分词的，如果需要分词那么FieldType.Text【默认会分词】
    @Field(store = true,type = FieldType.Keyword)
    private String firstname;
    @Field(store = true,type = FieldType.Keyword)
    private String lastname;
    @Field(store = true,type = FieldType.Integer)
    private Integer age;
    @Field(store = true,type = FieldType.Integer)
    private Integer gender;
    @Field(store = true,type = FieldType.Text,analyzer = "ik_max_word")
    private String address;
    @Field(store = true,type = FieldType.Text,analyzer = "ik_max_word")
    private String job;
    @Field(store = true,type = FieldType.Keyword)
    private String email;
    @Field(store = true,type = FieldType.Keyword)
    private String city;
    @Field(store = true,type = FieldType.Keyword)
    private String state;

}
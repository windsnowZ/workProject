package com.portal.service.impl;

import com.api.order.OrderClient;
import com.github.wxpay.sdk.WXPayRequest;
import com.github.wxpay.sdk.WXPayUtil;
import com.portal.config.MyConfig;
import com.portal.entity.order.WxbOrder;
import com.portal.entity.order.WxbOrderItems;
import com.portal.service.IPayService;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>title: com.portal.service.impl</p>
 * author zhuximing
 * description:
 */
@Service
@RefreshScope
public class WXPayService implements IPayService {


    @Autowired
    private OrderClient orderClient;

    @Value("${weixin.notifyUrl}")
    private String notifyUrl;


    @Override
    public Result pay(String orderId) {

        try {
            //查询订单信息
            WxbOrder orderInfo = orderClient.findOrderById(orderId);

            //计算总金额
            Integer totalFee = 0;
            for (WxbOrderItems item : orderInfo.getItems()) {
                Integer buyNum = item.getBuyNum();
                String skuPrice = item.getSkuPrice();
                double buyPrice = Double.parseDouble(skuPrice);
                //1  2.88
                int itemPrice = (int) (buyNum * buyPrice * 100);
                totalFee += itemPrice;
            }
            //调用微信的统一下单接口，下单
            String payUrl = weixinPay(orderId, totalFee, notifyUrl);

            Map data = new HashMap();
            data.put("payUrl", payUrl);
            data.put("totalFee", totalFee);
            data.put("orderId", orderId);

            return new Result(true, "下单成功", data);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "下单失败");
        }


    }

    @Override
    public Result status(String orderId) throws Exception {


        //1:构建配置对象
        MyConfig config = new MyConfig();
        //2:封装请求参数
        Map params = new HashMap();
        params.put("appid", config.getAppID());
        params.put("mch_id", config.getMchID());
        params.put("out_trade_no", orderId);
        params.put("nonce_str", WXPayUtil.generateNonceStr());
        //3:map转xml
        String requestXML = WXPayUtil.generateSignedXml(params, config.getKey());
        //4:发送请求WXPayRequest request = new WXPayRequest(config);
        WXPayRequest request = new WXPayRequest(config);
        String resXml = request.requestWithoutCert("/pay/orderquery", UUID.randomUUID().toString(),
            requestXML, false);

        System.out.println(resXml);

        //5:响应结果转map集合
        Map<String, String> resMap = WXPayUtil.xmlToMap(resXml);
        //6:获取支付状态
        System.out.println(resMap.get("trade_state"));


        return new Result(true, "success", resMap.get("trade_state"));
    }

    @Override
    public Result close(String orderId) throws Exception {
        //1:构建配置对象
        MyConfig config = new MyConfig();
        //2:封装请求参数
        Map params = new HashMap();
        params.put("appid", config.getAppID());
        params.put("mch_id", config.getMchID());
        params.put("out_trade_no", orderId);
        params.put("nonce_str", WXPayUtil.generateNonceStr());
        //3:map转xml
        String reqeustXML = WXPayUtil.generateSignedXml(params, config.getKey());
        //4:发送请求WXPayRequest request = new WXPayRequest(config);
        WXPayRequest request = new WXPayRequest(config);
        String resXML = request.requestWithoutCert("/pay/closeorder", UUID.randomUUID().toString(),
            reqeustXML, false);
        //5:响应结果转map集合
        Map<String, String> resMap = WXPayUtil.xmlToMap(resXML);
        //6:获取关闭结果
        System.out.println(resMap.get("result_code"));
        System.out.println(resMap.get("result_msg"));

        if("SUCCESS".equals(resMap.get("result_code"))){
            return new Result(true,"success");
        }else{

            return  new Result(false,"fail");
        }


    }

    private String weixinPay(String orderId, Integer fee, String notifyUrl) throws Exception {
        //1.构建配置
        MyConfig config = new MyConfig();
        //2.封装请求数据
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("appid", config.getAppID());
        requestParams.put("mch_id", config.getMchID());
        requestParams.put("nonce_str", WXPayUtil.generateNonceStr());
        requestParams.put("body", "微分销");
        requestParams.put("out_trade_no", orderId);//业务系统订单号
        requestParams.put("total_fee", fee + "");//订单金额，单位为分
        requestParams.put("spbill_create_ip", "127.0.0.1");
        requestParams.put("notify_url", notifyUrl);
        requestParams.put("trade_type", "NATIVE");//扫码支付

        //3.将map转xml串
        String reqeuestParamsXml = WXPayUtil.generateSignedXml(requestParams, config.getKey());

        //4.发请求
        WXPayRequest request = new WXPayRequest(config);
        String resultXml = request.requestWithoutCert("/pay/unifiedorder", UUID.randomUUID().toString(), reqeuestParamsXml, false);
        System.out.println(resultXml);

        //5.将resultXml转map集合
        final Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

        System.out.println(resultMap.get("code_url"));


        return resultMap.get("code_url");

    }
}
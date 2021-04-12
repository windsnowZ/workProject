package com.portal.controller;

import com.api.order.OrderClient;
import com.github.wxpay.sdk.WXPayUtil;
import com.portal.config.MyConfig;
import com.portal.entity.order.WxbOrder;
import com.portal.service.IPayService;
import com.portal.vo.Result;
import com.portal.websocket.Websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>title: com.portal.controller</p>
 * author zhuximing
 * description:
 */
@RestController
@RequestMapping("pay")
public class PayController {

    @Autowired
    private IPayService payService;


    @RequestMapping("order")
    public Result order(String orderId){

        return payService.pay(orderId);
    }



    @Autowired
    private OrderClient orderClient;

    @RequestMapping("/notify")
    public String  notify(HttpServletRequest request){

        System.out.println("微信支付系统成功回调");
        InputStream inStream;
        try{
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inStream.read(buffer)) != -1){
                outSteam.write(buffer,0,len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(),"utf-8");
            System.out.println(result);

            //对xml进行解析 map
            Map<String,String> map = WXPayUtil.xmlToMap(result);
            //验证签名
            boolean signatureValid = WXPayUtil.isSignatureValid(map,
                new MyConfig().getKey());

            //获取订单id
            String orderId = map.get("out_trade_no");


            if(signatureValid&&"SUCCESS".equals(map.get("result_code"))){
                //修改订单状态(支付状态、订单状态、支付时间、修改时间)
                WxbOrder order = new WxbOrder();
                order.setOrderId(orderId);
                order.setUpdateTime(new Date());
                order.setState(1);
                orderClient.update(order);

                //通知前端
                Websocket.sendMessageToClient(orderId,"success");

            }

            Map data = new HashMap();
            data.put("return_code","SUCCESS");
            data.put("return_msg","OK");

            //map转xml
            String returnXml = WXPayUtil.generateSignedXml(data, new MyConfig().getKey());
            return returnXml;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
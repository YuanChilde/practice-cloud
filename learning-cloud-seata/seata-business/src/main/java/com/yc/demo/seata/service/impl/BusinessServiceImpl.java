/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yc.demo.seata.service.impl;


import com.yc.demo.seata.common.dto.BusinessDTO;
import com.yc.demo.seata.common.dto.CommodityDTO;
import com.yc.demo.seata.common.dto.OrderDTO;
import com.yc.demo.seata.common.enums.RspStatusEnum;
import com.yc.demo.seata.common.exception.DefaultException;
import com.yc.demo.seata.common.response.ObjectResponse;
import com.yc.demo.seata.common.service.OrderDubboService;
import com.yc.demo.seata.common.service.StockDubboService;
import com.yc.demo.seata.service.BusinessService;
import io.seata.core.context.RootContext;

import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @Author: heshouyou
 * @Description Dubbo业务发起方逻辑
 * @Date Created in 2019/1/14 18:36
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @DubboReference(version = "1.0.0")
    private StockDubboService stockDubboService;

    @DubboReference(version = "1.0.0")
    private OrderDubboService orderDubboService;

    private boolean flag;

    /**
     * 处理业务逻辑
     *
     * @Param:
     * @Return:
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000) //, name = "dubbo-gts-seata-example"
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        System.out.println("开始全局事务，XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ObjectResponse stockResponse = stockDubboService.decreaseStock(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        //打开注释测试事务发生异常后，全局回滚功能
/*        if (!flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }*/

        if (stockResponse.getStatus() != 200 || response.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }
}

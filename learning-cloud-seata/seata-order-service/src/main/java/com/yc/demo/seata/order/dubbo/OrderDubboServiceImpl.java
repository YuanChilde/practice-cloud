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
package com.yc.demo.seata.order.dubbo;


import com.yc.demo.seata.common.dto.OrderDTO;
import com.yc.demo.seata.common.response.ObjectResponse;
import com.yc.demo.seata.common.service.OrderDubboService;
import com.yc.demo.seata.order.service.ITOrderService;

import io.seata.core.context.RootContext;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 15:59
 */
@DubboService(version = "1.0.0",timeout = 3000)
public class OrderDubboServiceImpl implements OrderDubboService {

    @Autowired
    private ITOrderService orderService;

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        System.out.println("全局事务id ：" + RootContext.getXID());
        return orderService.createOrder(orderDTO);
    }
}
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
package com.yc.demo.seata.stock.service.impl;




import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yc.demo.seata.common.dto.CommodityDTO;
import com.yc.demo.seata.common.enums.RspStatusEnum;
import com.yc.demo.seata.common.response.ObjectResponse;
import com.yc.demo.seata.stock.entity.TStock;
import com.yc.demo.seata.stock.mapper.TStockMapper;
import com.yc.demo.seata.stock.service.ITStockService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
public class TStockServiceImpl extends ServiceImpl<TStockMapper, TStock> implements ITStockService {

    @Override
    public ObjectResponse decreaseStock(CommodityDTO commodityDTO) {
        int stock = baseMapper.decreaseStock(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (stock > 0) {
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }

        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }
}

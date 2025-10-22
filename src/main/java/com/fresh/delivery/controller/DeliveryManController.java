package com.fresh.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fresh.common.response.PageResult;
import com.fresh.common.response.Result;
import com.fresh.core.entity.DeliveryMan;
import com.fresh.core.service.DeliveryManService;
import com.fresh.delivery.dto.DeliveryManRequest;
import com.fresh.delivery.vo.DeliveryManVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/delivery/deliveryman")
public class DeliveryManController {

    @Resource
    private DeliveryManService deliveryManService;

    @GetMapping("/list")
    public Result<PageResult<DeliveryManVO>> getDeliveryManList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<DeliveryMan> page = deliveryManService.getDeliveryManPage(pageNum, pageSize);
        
        List<DeliveryManVO> voList = page.getRecords().stream().map(item -> {
            DeliveryManVO vo = new DeliveryManVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        
        return Result.success(PageResult.of(page.getTotal(), voList));
    }
    @PostMapping("/add")
    public Result<Void> addDeliveryMan(@RequestBody DeliveryManRequest request) {
        DeliveryMan deliveryMan = new DeliveryMan();
        BeanUtils.copyProperties(request, deliveryMan);
        
        boolean success = deliveryManService.addDeliveryMan(deliveryMan);
        return success ? Result.success() : Result.error("添加失败");
    }

    @PutMapping("/update")
    public Result<Void> updateDeliveryMan(@RequestBody DeliveryManRequest request) {
        if (request.getId() == null) {
            return Result.error("ID不能为空");
        }
        
        DeliveryMan deliveryMan = new DeliveryMan();
        BeanUtils.copyProperties(request, deliveryMan);
        
        boolean success = deliveryManService.updateDeliveryMan(deliveryMan);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDeliveryMan(@PathVariable Long id) {
        boolean success = deliveryManService.deleteDeliveryMan(id);
        return success ? Result.success() : Result.error("删除失败");
    }
}
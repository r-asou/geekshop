/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.ApiType;
import co.jueyi.geekshop.common.RequestContext;
import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.entity.FacetValueEntity;
import co.jueyi.geekshop.entity.ProductFacetValueJoinEntity;
import co.jueyi.geekshop.mapper.FacetEntityMapper;
import co.jueyi.geekshop.mapper.FacetValueEntityMapper;
import co.jueyi.geekshop.mapper.ProductFacetValueJoinEntityMapper;
import co.jueyi.geekshop.types.facet.FacetValue;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoader;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Created on Nov, 2020 by @author bobo
 */
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class ProductFacetValuesDataLoader implements MappedBatchLoaderWithContext<Long, List<FacetValue>> {
    private final ProductFacetValueJoinEntityMapper productFacetValueJoinEntityMapper;
    private final FacetValueEntityMapper facetValueEntityMapper;
    private final FacetEntityMapper facetEntityMapper;

    @Override
    public CompletionStage<Map<Long, List<FacetValue>>> load(Set<Long> productIds, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, List<FacetValue>> facetValuesMap = new HashMap<>();
            productIds.forEach(id -> facetValuesMap.put(id, new ArrayList<>()));

            QueryWrapper<ProductFacetValueJoinEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(ProductFacetValueJoinEntity::getProductId, productIds);
            List<ProductFacetValueJoinEntity> joinEntities =
                    productFacetValueJoinEntityMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(joinEntities)) return facetValuesMap;

            Set<Long> facetValueIds = joinEntities.stream()
                    .map(ProductFacetValueJoinEntity::getFacetValueId).collect(Collectors.toSet());
            List<FacetValueEntity> facetValueEntities = facetValueEntityMapper.selectBatchIds(facetValueIds);
            if (CollectionUtils.isEmpty(facetValueEntities)) return facetValuesMap;

            Map<Object, Object> optionsMap = environment.getKeyContexts();
            RequestContext ctx = (RequestContext) optionsMap.get(productIds.iterator().next());
            boolean isShop = ApiType.SHOP.equals(ctx.getApiType());
            if (isShop) { // 对于ShopAPI，过滤出和公开的Facet所对应的FacetValues
                List<Long> facetIds = facetValueEntities.stream()
                        .map(FacetValueEntity::getFacetId).distinct().collect(Collectors.toList());
                List<FacetEntity> facetEntities = this.facetEntityMapper.selectBatchIds(facetIds);
                Map<Long, FacetEntity> facetEntityMap = facetEntities.stream()
                        .collect(toMap(FacetEntity::getId, facetEntity -> facetEntity));
                facetValueEntities = facetValueEntities.stream()
                        .filter(facetValueEntity -> {
                            FacetEntity facetEntity = facetEntityMap.get(facetValueEntity.getFacetId());
                            if (facetEntity.isVisibleToPublic()) return true;
                            return false;
                        }).collect(Collectors.toList());
            }

            Map<Long, FacetValueEntity> facetValueEntityMap = facetValueEntities.stream()
                    .collect(Collectors.toMap(FacetValueEntity::getId, facetValueEntity -> facetValueEntity));
            if (CollectionUtils.isEmpty(facetValueEntityMap)) return facetValuesMap;

            joinEntities.forEach(facetValueJoinEntity -> {
                Long productId = facetValueJoinEntity.getProductId();
                Long facetValueId = facetValueJoinEntity.getFacetValueId();
                List<FacetValue> facetValueList = facetValuesMap.get(productId);
                FacetValueEntity facetValueEntity = facetValueEntityMap.get(facetValueId);
                FacetValue facetValue = BeanMapper.patch(facetValueEntity, FacetValue.class);
                facetValueList.add(facetValue);
            });

            return facetValuesMap;
        });
    }
}

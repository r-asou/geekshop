/*
 * Copyright (c) 2020 掘艺网络(jueyi.co).
 * All rights reserved.
 */

package co.jueyi.geekshop.resolver.dataloader;

import co.jueyi.geekshop.common.utils.BeanMapper;
import co.jueyi.geekshop.entity.FacetEntity;
import co.jueyi.geekshop.mapper.FacetEntityMapper;
import co.jueyi.geekshop.types.facet.Facet;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created on Nov, 2020 by @author bobo
 */
public class FacetValueFacetDataLoader implements MappedBatchLoader<Long, Facet> {
    private final FacetEntityMapper facetEntityMapper;

    public FacetValueFacetDataLoader(FacetEntityMapper facetEntityMapper) {
        this.facetEntityMapper = facetEntityMapper;
    }

    @Override
    public CompletionStage<Map<Long, Facet>> load(Set<Long> facetIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<FacetEntity> facetEntityList = this.facetEntityMapper.selectBatchIds(facetIds);
            List<Facet> facetList = facetEntityList.stream()
                    .map(facetEntity -> BeanMapper.map(facetEntity, Facet.class)).collect(Collectors.toList());
            Map<Long, Facet> facetMap = facetList.stream().collect(Collectors.toMap(Facet::getId, facet -> facet));
            return facetMap;
        });
    }
}

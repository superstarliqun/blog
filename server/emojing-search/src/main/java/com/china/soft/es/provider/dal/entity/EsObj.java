package com.china.soft.es.provider.dal.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;

@Data
public class EsObj extends PageDTO {

    private String indexName;
    private String typeName;
    private String id;
    private Object dta;
}

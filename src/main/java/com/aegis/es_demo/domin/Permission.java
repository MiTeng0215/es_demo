package com.aegis.es_demo.domin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(value = "标识")
    private Integer id;

    @ApiModelProperty(value = "权限编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "权限类型")
    private Integer type;

    @ApiModelProperty(value = "资源路径，功能链接")
    private String url;



}

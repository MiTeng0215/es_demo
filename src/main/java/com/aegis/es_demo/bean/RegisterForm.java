package com.aegis.es_demo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("注册表单")
public class RegisterForm {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "电话号码")
    private String phone;
}

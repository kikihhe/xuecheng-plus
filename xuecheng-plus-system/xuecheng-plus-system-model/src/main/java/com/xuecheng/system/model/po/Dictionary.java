package com.xuecheng.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("dictionary")
public class Dictionary implements Serializable {
    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("11111");
        System.out.println(encode);
    }

    private static final long serialVersionUID = 1L;

    /**
     * id标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据字典名称
     */
    private String name;

    /**
     * 数据字典代码
     */
    private String code;

    /**
     * 数据字典项--json格式
            [{
                  "sd_name": "低级",
                  "sd_id": "200001",
                  "sd_status": "1"
               }, {
                  "sd_name": "中级",
                  "sd_id": "200002",
                  "sd_status": "1"
               }, {
                  "sd_name": "高级",
                  "sd_id": "200003",
                  "sd_status": "1"
               }]
     */
    private String itemValues;




}

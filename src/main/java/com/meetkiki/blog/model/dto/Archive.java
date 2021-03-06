package com.meetkiki.blog.model.dto;

import com.meetkiki.blog.model.entity.Contents;
import io.github.biezhi.anima.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章归档
 * <p>
 * Created by biezhi on 2017/2/23.
 */
@Data
public class Archive extends Model {

    private String         dateStr;
    private Date           date;
    private String         count;
    private List<Contents> articles;

}

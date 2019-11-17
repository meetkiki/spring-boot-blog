package com.meetkiki.blog.controller.admin;

import com.meetkiki.blog.annotation.SysLog;
import com.meetkiki.blog.constants.TaleConst;
import com.meetkiki.blog.controller.BaseController;
import com.meetkiki.blog.model.dto.RestResponse;
import com.meetkiki.blog.model.dto.Statistics;
import com.meetkiki.blog.model.dto.Types;
import com.meetkiki.blog.model.entity.Attach;
import com.meetkiki.blog.model.entity.Comments;
import com.meetkiki.blog.model.entity.Contents;
import com.meetkiki.blog.model.entity.Users;
import com.meetkiki.blog.service.SiteService;
import com.meetkiki.blog.utils.DateUtils;
import com.meetkiki.blog.utils.ImageUtils;
import com.meetkiki.blog.utils.TaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 后台控制器
 * Created by biezhi on 2017/2/21.
 */
@Slf4j
@Controller
@RequestMapping("admin")
public class AdminIndexController extends BaseController {


    @Resource
    private SiteService siteService;

    /**
     * 仪表盘
     */
    @GetMapping(value = {"/index",""})
    public String index(HttpServletRequest request) {
        List<Comments> comments   = siteService.recentComments(5);
        List<Contents> contents   = siteService.getContens(Types.RECENT_ARTICLE, 5);
        Statistics statistics = siteService.getStatistics();

        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "admin/profile";
    }

    /**
     * 上传文件接口
     */
    @SysLog("上传附件")
    @PostMapping("/api/attach/upload")
    @ResponseBody
    public RestResponse<?> upload(HttpServletRequest request) {

        log.info("UPLOAD DIR = {}", TaleUtils.UP_DIR);

        Users users      = this.user();
        Integer      uid        = users.getUid();
        List<Attach> errorFiles = new ArrayList<>();
        List<Attach> urls       = new ArrayList<>();

//        Map<String, FileItem> fileItems = request.fileItems();
//        if (null == fileItems || fileItems.size() == 0) {
//            return RestResponse.fail("请选择文件上传");
//        }
//
//        fileItems.forEach((fileName, fileItem) -> {
//            String fname = fileItem.getFileName();
//            if ((fileItem.getLength() / 1024) <= TaleConst.MAX_FILE_SIZE) {
//                String fkey = TaleUtils.getFileKey(fname);
//
//                String ftype    = fileItem.getContentType().contains("image") ? Types.IMAGE : Types.FILE;
//                String filePath = TaleUtils.UP_DIR + fkey;
//
//
//                try {
//                    Files.write(Paths.get(filePath), fileItem.getData());
//                    if (TaleUtils.isImage(new File(filePath))) {
//                        String newFileName       = TaleUtils.getFileName(fkey);
//                        String thumbnailFilePath = TaleUtils.UP_DIR + fkey.replace(newFileName, "thumbnail_" + newFileName);
//                        ImageUtils.cutCenterImage(CLASSPATH + fkey, thumbnailFilePath, 270, 380);
//
//                    }
//                } catch (IOException e) {
//                    log.error("", e);
//                }
//
//                Attach attach = new Attach();
//                attach.setFname(fname);
//                attach.setAuthorId(uid);
//                attach.setFkey(fkey);
//                attach.setFtype(ftype);
//                attach.setCreated(DateUtils.nowUnix());
//                attach.save();
//
//                urls.add(attach);
//                siteService.cleanCache(Types.SYS_STATISTICS);
//            } else {
//                Attach attach = new Attach();
//                attach.setFname(fname);
//                errorFiles.add(attach);
//            }
//        });
//
//        if (errorFiles.size() > 0) {
//            return RestResponse.fail().payload(errorFiles);
//        }
        return RestResponse.ok(urls);
    }


}

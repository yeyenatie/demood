package com.paperai.controller;

import com.paperai.common.Result;
import com.paperai.dto.PaperSaveRequest;
import com.paperai.entity.Paper;
import com.paperai.service.PaperService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 论文接口：列表、详情、创建、保存、删除
 */
@RestController
@RequestMapping("/api/paper")
public class PaperController {

    @Resource
    private PaperService paperService;

    @GetMapping("/list")
    public Result<List<Paper>> list(HttpServletRequest request) {
        return Result.ok(paperService.listByUser(userId(request)));
    }

    @GetMapping("/{id}")
    public Result<Paper> get(@PathVariable Long id, HttpServletRequest request) {
        return Result.ok(paperService.getById(userId(request), id));
    }

    @PostMapping
    public Result<Paper> create(@RequestBody PaperSaveRequest req, HttpServletRequest request) {
        return Result.ok(paperService.create(userId(request), req));
    }

    @PutMapping("/{id}")
    public Result<Paper> update(@PathVariable Long id, @RequestBody PaperSaveRequest req,
                                HttpServletRequest request) {
        return Result.ok(paperService.update(userId(request), id, req));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        paperService.delete(userId(request), id);
        return Result.ok();
    }

    private Long userId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}

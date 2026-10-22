package com.paperai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paperai.common.BizException;
import com.paperai.dto.PaperSaveRequest;
import com.paperai.entity.Paper;
import com.paperai.mapper.PaperMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 论文服务：论文的创建、查询、保存、删除（均按用户隔离）
 */
@Service
public class PaperService {

    @Resource
    private PaperMapper paperMapper;

    public List<Paper> listByUser(Long userId) {
        return paperMapper.selectList(new LambdaQueryWrapper<Paper>()
                .eq(Paper::getUserId, userId)
                .orderByDesc(Paper::getUpdateTime));
    }

    public Paper getById(Long userId, Long id) {
        Paper paper = paperMapper.selectById(id);
        if (paper == null || !paper.getUserId().equals(userId)) {
            throw new BizException(404, "论文不存在");
        }
        return paper;
    }

    public Paper create(Long userId, PaperSaveRequest req) {
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new BizException(400, "论文题目不能为空");
        }
        Paper paper = new Paper();
        paper.setUserId(userId);
        paper.setTitle(req.getTitle().trim());
        paper.setKeywords(req.getKeywords());
        paper.setPaperType(req.getPaperType() == null ? "毕业论文" : req.getPaperType());
        paper.setResearchDirection(req.getResearchDirection());
        paper.setOutline(req.getOutline());
        paper.setContent(req.getContent());
        paper.setStatus(req.getStatus() == null ? "draft" : req.getStatus());
        paperMapper.insert(paper);
        return paper;
    }

    public Paper update(Long userId, Long id, PaperSaveRequest req) {
        Paper paper = getById(userId, id);
        if (req.getTitle() != null) {
            paper.setTitle(req.getTitle().trim());
        }
        if (req.getKeywords() != null) {
            paper.setKeywords(req.getKeywords());
        }
        if (req.getPaperType() != null) {
            paper.setPaperType(req.getPaperType());
        }
        if (req.getResearchDirection() != null) {
            paper.setResearchDirection(req.getResearchDirection());
        }
        if (req.getOutline() != null) {
            paper.setOutline(req.getOutline());
        }
        if (req.getContent() != null) {
            paper.setContent(req.getContent());
        }
        if (req.getStatus() != null) {
            paper.setStatus(req.getStatus());
        }
        paperMapper.updateById(paper);
        return paper;
    }

    public void delete(Long userId, Long id) {
        Paper paper = getById(userId, id);
        paperMapper.deleteById(paper.getId());
    }
}

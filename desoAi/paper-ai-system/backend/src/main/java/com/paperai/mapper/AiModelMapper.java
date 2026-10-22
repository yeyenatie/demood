package com.paperai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.paperai.entity.AiModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiModelMapper extends BaseMapper<AiModel> {
}

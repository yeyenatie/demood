-- 论文写作 AI 辅助系统 数据库初始化脚本
-- 使用方式：在 MySQL 中执行本脚本（Navicat / 命令行均可）

CREATE DATABASE IF NOT EXISTS paper_ai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE paper_ai;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
  `password`    VARCHAR(100) NOT NULL COMMENT '密码（BCrypt 加密）',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

-- 论文表
CREATE TABLE IF NOT EXISTS `paper` (
  `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '论文ID',
  `user_id`            BIGINT       NOT NULL COMMENT '所属用户ID',
  `title`              VARCHAR(200) NOT NULL COMMENT '论文题目',
  `keywords`           VARCHAR(255) DEFAULT '' COMMENT '关键词/研究方向',
  `paper_type`         VARCHAR(20)  DEFAULT '毕业论文' COMMENT '论文类型',
  `research_direction` VARCHAR(500) DEFAULT '' COMMENT '研究方向描述',
  `outline`            TEXT         COMMENT '大纲（JSON）',
  `content`            LONGTEXT     COMMENT '正文内容（JSON）',
  `status`             VARCHAR(20)  DEFAULT 'draft' COMMENT '状态：draft-草稿 done-已完成',
  `create_time`        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '论文表';

-- 用户自设 AI 模型配置表
CREATE TABLE IF NOT EXISTS `ai_setting` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `user_id`     BIGINT       NOT NULL COMMENT '所属用户ID',
  `api_key`     VARCHAR(200) DEFAULT '' COMMENT '大模型 API Key',
  `base_url`    VARCHAR(200) DEFAULT '' COMMENT 'API Base URL',
  `model`       VARCHAR(100) DEFAULT '' COMMENT '模型名称',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户 AI 模型配置表';

-- 用户自配 AI 模型表（可添加多个，生成时可选择）
CREATE TABLE IF NOT EXISTS `ai_model` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '模型ID',
  `user_id`     BIGINT       NOT NULL COMMENT '所属用户ID',
  `name`        VARCHAR(64)  NOT NULL COMMENT '模型显示名',
  `api_key`     VARCHAR(200) DEFAULT '' COMMENT 'API Key',
  `base_url`    VARCHAR(200) DEFAULT '' COMMENT 'API Base URL',
  `model_name`  VARCHAR(100) DEFAULT '' COMMENT '模型名称',
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户自配 AI 模型表';

-- AI 生成次数配额表（按用户 + 周记录，跨周自动重置）
CREATE TABLE IF NOT EXISTS `ai_usage` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id`     BIGINT      NOT NULL COMMENT '所属用户ID',
  `week_key`    VARCHAR(10) NOT NULL COMMENT '周标识，如 2026-W42',
  `count`       INT         NOT NULL DEFAULT 0 COMMENT '本周已用次数',
  `update_time` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_week` (`user_id`, `week_key`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'AI 生成次数配额表';

<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">论文写作 AI</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1f2d52"
        text-color="#aeb9e0"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/workspace">
          <el-icon><EditPen /></el-icon>
          <span>创作中心</span>
        </el-menu-item>
        <el-menu-item index="/papers">
          <el-icon><FolderOpened /></el-icon>
          <span>我的论文</span>
        </el-menu-item>
        <el-menu-item index="/settings">
          <el-icon><Setting /></el-icon>
          <span>AI 模型</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <span class="header-title">论文写作 AI 辅助系统</span>
        <el-dropdown @command="onCommand">
          <span class="user">
            {{ username }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeMenu = computed(() => route.path)
const username = localStorage.getItem('username') || '用户'

function onCommand(cmd) {
  if (cmd === 'logout') {
    localStorage.clear()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}
.aside {
  background: #1f2d52;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}
.aside :deep(.el-menu) {
  border-right: none;
}
.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}
.header-title {
  font-size: 16px;
  color: #1f2d52;
  font-weight: 600;
}
.user {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #1f2d52;
}
.main {
  background: #f5f7fa;
  padding: 20px;
}
</style>

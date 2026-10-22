<template>
  <div class="auth-page">
    <el-card class="auth-card">
      <div class="auth-logo">📝</div>
      <h1 class="auth-title">论文写作 AI 辅助系统</h1>
      <p class="auth-sub">AI 论文生成 · 机器学习 · 自然语言处理</p>

      <el-form :model="form" @keyup.enter="onLogin">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            show-password
            placeholder="密码"
            :prefix-icon="Lock"
            size="large"
          />
        </el-form-item>
        <el-button
          type="primary"
          size="large"
          class="auth-btn"
          :loading="loading"
          @click="onLogin"
        >
          登 录
        </el-button>
      </el-form>

      <div class="auth-footer">
        还没有账号？
        <router-link to="/register">立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../api'

const router = useRouter()
const form = reactive({ username: '', password: '' })
const loading = ref(false)

async function onLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const data = await login(form)
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    ElMessage.success('登录成功')
    router.push('/workspace')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f2d52 0%, #2f54eb 55%, #6d8bff 100%);
}
.auth-card {
  width: 400px;
  border-radius: 12px;
  padding: 16px 8px;
}
.auth-logo {
  text-align: center;
  font-size: 40px;
  margin-bottom: 8px;
}
.auth-title {
  text-align: center;
  font-size: 22px;
  color: #1f2d52;
  margin-bottom: 4px;
}
.auth-sub {
  text-align: center;
  color: #8c8c8c;
  font-size: 13px;
  margin-bottom: 24px;
}
.auth-btn {
  width: 100%;
  margin-top: 8px;
  background: #2f54eb;
  border-color: #2f54eb;
}
.auth-footer {
  text-align: center;
  margin-top: 16px;
  color: #8c8c8c;
  font-size: 13px;
}
.auth-footer a {
  color: #2f54eb;
  text-decoration: none;
}
</style>

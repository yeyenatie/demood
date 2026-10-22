<template>
  <div class="page-card">
    <div class="toolbar">
      <h2>我的论文</h2>
      <el-button type="primary" @click="router.push('/workspace')">＋ 新建论文</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="papers"
      empty-text="暂无论文，去创作中心写一篇吧"
    >
      <el-table-column prop="title" label="论文题目" min-width="240" show-overflow-tooltip />
      <el-table-column prop="paperType" label="类型" width="100" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type || 'info'">
            {{ statusMap[row.status]?.label || row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="170" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="open(row)">继续创作</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { paperList, paperDelete } from '../api'

const router = useRouter()
const papers = ref([])
const loading = ref(false)

const statusMap = {
  draft: { label: '草稿', type: 'info' },
  generating: { label: '生成中', type: 'warning' },
  done: { label: '已完成', type: 'success' }
}

onMounted(load)

async function load() {
  loading.value = true
  try {
    papers.value = await paperList()
  } finally {
    loading.value = false
  }
}

function open(row) {
  router.push({ path: '/workspace', query: { id: row.id } })
}

async function remove(row) {
  await ElMessageBox.confirm(`确定删除《${row.title}》吗？删除后不可恢复。`, '提示', {
    type: 'warning'
  })
  await paperDelete(row.id)
  ElMessage.success('已删除')
  load()
}
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.toolbar h2 {
  color: #1f2d52;
}
</style>

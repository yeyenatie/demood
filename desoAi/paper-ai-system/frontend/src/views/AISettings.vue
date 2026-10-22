<template>
  <div class="page-card models-card">
    <div class="head">
      <div>
        <h3>AI 模型管理</h3>
        <p class="tip">
          添加你自己的大模型 API（OpenAI 兼容接口），可添加多个。生成论文时可在创作中心选择
          「默认模型」或任意一个你自己的模型；选自己的模型不消耗每周次数。
        </p>
      </div>
      <el-button type="primary" @click="openDialog()">＋ 添加模型</el-button>
    </div>

    <el-table :data="models" v-loading="loading" style="width: 100%">
      <el-table-column prop="name" label="显示名" min-width="140" />
      <el-table-column label="API Key" min-width="180">
        <template #default="{ row }">
          <span class="key-mask">{{ maskKey(row.apiKey) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="baseUrl" label="Base URL" min-width="220" />
      <el-table-column prop="modelName" label="模型名称" min-width="140" />
      <el-table-column label="操作" width="230">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="primary" @click="testRow(row)">测试连接</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="还没有自配模型，点击右上角「添加模型」" :image-size="80" />
      </template>
    </el-table>

    <el-alert
      v-if="testMessage"
      :type="testOk ? 'success' : 'error'"
      :title="testMessage"
      :closable="false"
      style="margin-top: 12px"
    />

    <!-- 新增 / 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑模型' : '添加模型'" width="520px">
      <el-form label-width="110px">
        <el-form-item label="显示名" required>
          <el-input v-model="form.name" placeholder="如：我的 DeepSeek / 智谱 GLM" />
        </el-form-item>
        <el-form-item label="API Key" required>
          <el-input
            v-model="form.apiKey"
            type="password"
            show-password
            placeholder="sk-xxx"
          />
        </el-form-item>
        <el-form-item label="Base URL">
          <el-input v-model="form.baseUrl" placeholder="https://api.deepseek.com/v1" />
        </el-form-item>
        <el-form-item label="模型名称">
          <el-input v-model="form.modelName" placeholder="deepseek-chat" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="save">保存</el-button>
          <el-button :loading="testing" @click="testForm">测试连接</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAiModels, createAiModel, updateAiModel, deleteAiModel, testAiSetting } from '../api'

const models = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const saving = ref(false)
const testing = ref(false)
const testMessage = ref('')
const testOk = ref(false)

const emptyForm = () => ({ id: null, name: '', apiKey: '', baseUrl: 'https://api.deepseek.com/v1', modelName: 'deepseek-chat' })
const form = reactive(emptyForm())

onMounted(load)

async function load() {
  loading.value = true
  try {
    models.value = (await getAiModels()) || []
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  testMessage.value = ''
  Object.assign(form, emptyForm(), row || {})
  dialogVisible.value = true
}

function maskKey(key) {
  if (!key) return '未填写'
  if (key.length <= 8) return '****'
  return `${key.slice(0, 6)}****${key.slice(-4)}`
}

async function save() {
  if (!form.name.trim()) {
    ElMessage.warning('请填写模型显示名')
    return
  }
  if (!form.apiKey.trim()) {
    ElMessage.warning('请填写 API Key')
    return
  }
  saving.value = true
  try {
    if (form.id) {
      await updateAiModel(form.id, form)
      ElMessage.success('模型已更新')
    } else {
      await createAiModel(form)
      ElMessage.success('模型已添加')
    }
    dialogVisible.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function remove(row) {
  await ElMessageBox.confirm(`确定删除模型「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteAiModel(row.id)
  ElMessage.success('已删除')
  load()
}

async function runTest(payload) {
  testMessage.value = ''
  if (!payload.apiKey) {
    testMessage.value = '请先填写 API Key 再测试'
    testOk.value = false
    return
  }
  testing.value = true
  try {
    const data = await testAiSetting(payload)
    testOk.value = !!data.ok
    testMessage.value = data.message || (data.ok ? '连接成功' : '连接失败')
  } finally {
    testing.value = false
  }
}

function testRow(row) {
  runTest({ apiKey: row.apiKey, baseUrl: row.baseUrl, modelName: row.modelName })
}

function testForm() {
  runTest({ apiKey: form.apiKey, baseUrl: form.baseUrl, modelName: form.modelName })
}
</script>

<style scoped>
.models-card {
  max-width: 980px;
}
.head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 16px;
}
.key-mask {
  font-family: Consolas, Menlo, monospace;
  color: #606266;
}
</style>

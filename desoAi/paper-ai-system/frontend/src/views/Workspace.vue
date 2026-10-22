<template>
  <div class="workspace">
    <div class="quota-bar">
      <el-select v-model="selectedModelId" style="width: 280px" placeholder="选择生成使用的模型">
        <el-option label="默认模型（每周限次数）" :value="null" />
        <el-option v-for="m in models" :key="m.id" :label="m.name" :value="m.id" />
      </el-select>
      <el-tag v-if="selectedModel" type="success" effect="light" round>
        使用自配模型：{{ selectedModel.name }}（不消耗周次数）
      </el-tag>
      <el-tag v-else :type="quota.remaining > 0 ? 'warning' : 'danger'" effect="light" round>
        本周默认模型剩余：{{ quota.remaining }} / {{ quota.limit }} 次
      </el-tag>
      <span class="quota-tip">选默认模型每周限 {{ quota.limit }} 次；选自己的模型不占次数</span>
    </div>
    <el-steps :active="activeStep" align-center finish-status="success" class="steps">
      <el-step title="研究方向" />
      <el-step title="选题与大纲" />
      <el-step title="章节生成" />
      <el-step title="全文预览" />
    </el-steps>

    <!-- ==================== 第 1 步：研究方向 ==================== -->
    <div v-show="activeStep === 0" class="page-card">
      <h3>① 输入研究方向</h3>
      <p class="tip">
        描述你的研究方向，AI 将自动提取关键词并推荐论文题目。例如：基于深度学习的图像识别在医疗影像中的应用
      </p>
      <el-input
        v-model="form.researchDirection"
        type="textarea"
        :rows="4"
        placeholder="请输入研究方向描述…"
      />
      <div class="row-actions">
        <el-select v-model="form.paperType" style="width: 160px">
          <el-option label="毕业论文" value="毕业论文" />
          <el-option label="期刊论文" value="期刊论文" />
          <el-option label="课程论文" value="课程论文" />
        </el-select>
        <el-button type="primary" :loading="topicsLoading" :disabled="quotaExhausted" @click="recommendTopics">
          智能推荐题目
        </el-button>
        <el-button type="success" :disabled="!form.title" @click="activeStep = 1">
          下一步
        </el-button>
      </div>
    </div>

    <!-- ==================== 第 2 步：选题与大纲 ==================== -->
    <div v-show="activeStep === 1" class="page-card">
      <h3>② 选择题目并生成大纲</h3>

      <div v-if="topics.length" class="topics">
        <div
          v-for="(t, i) in topics"
          :key="i"
          class="topic"
          :class="{ active: form.title === t.title }"
          @click="form.title = t.title"
        >
          <div class="topic-title">{{ t.title }}</div>
          <div class="topic-reason">{{ t.reason }}</div>
        </div>
      </div>

      <el-input v-model="form.title" placeholder="也可以手动输入 / 修改论文题目" class="mt" />

      <div class="row-actions">
        <el-button @click="activeStep = 0">上一步</el-button>
        <el-button type="primary" :loading="outlineLoading" :disabled="!form.title || quotaExhausted" @click="generateOutline">
          生成论文大纲
        </el-button>
        <el-button :disabled="!outline.length" @click="recommendTopics">重新推荐题目</el-button>
      </div>

      <div v-if="outline.length" class="outline mt">
        <h4>大纲预览（可直接编辑章节标题）</h4>
        <div v-for="(sec, si) in outline" :key="sec.uid" class="sec">
          <div class="sec-row">
            <el-input v-model="sec.title" size="small" class="sec-title-input" />
            <el-button
              v-if="sec.title !== '摘要' && sec.title !== '参考文献'"
              link
              type="primary"
              size="small"
              @click="addChild(si)"
            >
              ＋ 添加小节
            </el-button>
          </div>
          <div v-if="sec.children && sec.children.length" class="subs">
            <div v-for="(child, ci) in sec.children" :key="child.uid" class="sub">
              <el-input v-model="child.title" size="small" />
              <el-button link type="danger" size="small" @click="removeChild(si, ci)">删除</el-button>
            </div>
          </div>
        </div>
        <div class="row-actions">
          <el-button size="small" @click="addSection">＋ 添加章节</el-button>
          <el-button type="success" @click="activeStep = 2">确认大纲，开始写作 →</el-button>
        </div>
      </div>
    </div>

    <!-- ==================== 第 3 步：章节生成 ==================== -->
    <div v-show="activeStep === 2" class="page-card">
      <h3>③ 逐章生成正文</h3>
      <p class="tip">点击「生成」逐节生成内容，生成后可直接在文本框中编辑。建议按顺序从上到下生成。</p>

      <div v-for="sec in outline" :key="sec.uid" class="sec-block">
        <div class="sec-head">
          <span class="sec-name">{{ sec.title }}</span>
          <el-button
            v-if="sec.title !== '参考文献'"
            size="small"
            type="primary"
            plain
            :loading="genLoading[sec.uid]"
            :disabled="quotaExhausted"
            @click="genOne(sec)"
          >
            生成
          </el-button>
        </div>

        <!-- 章节导语：有子章节的章节，其「生成」结果显示在这里 -->
        <el-input
          v-if="sec.children && sec.children.length"
          v-model="sectionTexts[sec.uid]"
          type="textarea"
          :rows="4"
          class="gen-text"
          placeholder="点击上方「生成」按钮，生成本章导语…"
          style="margin-bottom: 12px"
        />

        <div v-if="sec.children && sec.children.length" class="children">
          <div v-for="child in sec.children" :key="child.uid" class="child-block">
            <div class="child-head">
              <span class="child-name">{{ child.title }}</span>
              <el-button
                size="small"
                type="primary"
                plain
                :loading="genLoading[child.uid]"
                :disabled="quotaExhausted"
                @click="genOne(child)"
              >
                生成
              </el-button>
            </div>
            <el-input
              v-model="sectionTexts[child.uid]"
              type="textarea"
              :rows="6"
              class="gen-text"
              placeholder="点击上方「生成」按钮，AI 将自动生成该节内容…"
            />
          </div>
        </div>
        <template v-else>
          <el-input
            v-model="sectionTexts[sec.uid]"
            type="textarea"
            :rows="8"
            class="gen-text"
            placeholder="点击上方「生成」按钮，AI 将自动生成该节内容…"
          />
        </template>
      </div>

      <div class="row-actions">
        <el-button type="primary" :loading="genAllLoading" :disabled="quotaExhausted" @click="generateAll">
          一键生成全文
        </el-button>
        <el-button @click="activeStep = 1">上一步</el-button>
        <el-button :disabled="!hasAnyText" type="primary" @click="activeStep = 3">预览全文 →</el-button>
        <el-button @click="savePaper(false)">保存草稿</el-button>
      </div>
    </div>

    <!-- ==================== 第 4 步：全文预览 ==================== -->
    <div v-show="activeStep === 3" class="page-card">
      <h3>④ 全文预览</h3>
      <div class="preview" v-html="previewHtml"></div>
      <div class="row-actions">
        <el-button @click="activeStep = 2">上一步</el-button>
        <el-button type="primary" @click="savePaper(true)">保存论文</el-button>
        <el-button @click="downloadDoc">下载 Word 草稿</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { genTopics, genOutline, genSection, paperCreate, paperUpdate, paperGet, getAiQuota, getAiModels } from '../api'

const route = useRoute()
const router = useRouter()

const activeStep = ref(0)
const topicsLoading = ref(false)
const outlineLoading = ref(false)
const genLoading = reactive({})

const form = reactive({ researchDirection: '', paperType: '毕业论文', title: '' })
const topics = ref([])
const outline = ref([])
const sectionTexts = reactive({})
const paperId = ref(null)
const quota = ref({ used: 0, limit: 10, remaining: 10 })
// 用户的自配模型列表与当前选择（null = 默认模型）
const models = ref([])
const selectedModelId = ref(null)
const selectedModel = computed(() => models.value.find((m) => m.id === selectedModelId.value) || null)
// 用默认模型且次数用尽时为 true（选了自己的模型则永不禁用）
const quotaExhausted = computed(() => !selectedModel.value && quota.value.remaining <= 0)

let uidSeq = 1
const uid = () => `s${uidSeq++}`

// 刷新本周 AI 生成剩余次数
async function refreshQuota() {
  try {
    quota.value = await getAiQuota()
  } catch {
    /* 配额接口失败不阻塞使用 */
  }
}

// 加载用户自配模型列表
async function loadModels() {
  try {
    models.value = (await getAiModels()) || []
  } catch {
    /* 错误已由拦截器提示 */
  }
}

// ==================== 初始化：从“我的论文”继续创作 ====================
onMounted(async () => {
  refreshQuota()
  loadModels()
  const id = route.query.id
  if (!id) return
  try {
    const paper = await paperGet(id)
    paperId.value = paper.id
    form.title = paper.title || ''
    form.paperType = paper.paperType || '毕业论文'
    form.researchDirection = paper.researchDirection || ''
    if (paper.outline) {
      try {
        outline.value = normalize(JSON.parse(paper.outline))
      } catch {
        outline.value = []
      }
    }
    if (paper.content) {
      try {
        const map = JSON.parse(paper.content)
        Object.keys(map).forEach((k) => {
          sectionTexts[k] = map[k]
        })
      } catch {
        /* 内容为空时忽略 */
      }
    }
    activeStep.value = outline.value.length ? 2 : 0
  } catch {
    /* 错误信息已由请求拦截器提示 */
  }
})

// ==================== 数据工具 ====================
function normalize(list) {
  return (list || []).map((s) => {
    if (!s.uid) s.uid = uid()
    if (!Array.isArray(s.points)) s.points = []
    if (Array.isArray(s.children)) {
      s.children = s.children.map((c) => {
        if (!c.uid) c.uid = uid()
        if (!Array.isArray(c.points)) c.points = []
        return c
      })
    }
    return s
  })
}

function escapeHtml(str) {
  return String(str || '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
}

// ==================== 第 1 步：题目推荐 ====================
async function recommendTopics() {
  if (!form.researchDirection.trim()) {
    ElMessage.warning('请先输入研究方向')
    return
  }
  topicsLoading.value = true
  try {
    const data = await genTopics({
      researchDirection: form.researchDirection,
      paperType: form.paperType,
      modelId: selectedModelId.value
    })
    topics.value = data || []
    if (topics.value.length) {
      form.title = topics.value[0].title
      ElMessage.success(`已为您推荐 ${topics.value.length} 个题目`)
    }
    refreshQuota()
  } finally {
    topicsLoading.value = false
  }
}

// ==================== 第 2 步：大纲生成 ====================
async function generateOutline() {
  outlineLoading.value = true
  try {
    if (!paperId.value) {
      const paper = await paperCreate({
        title: form.title,
        keywords: form.researchDirection,
        paperType: form.paperType,
        researchDirection: form.researchDirection
      })
      paperId.value = paper.id
    }
    const data = await genOutline({
      paperId: paperId.value,
      title: form.title,
      keywords: form.researchDirection,
      paperType: form.paperType,
      modelId: selectedModelId.value
    })
    outline.value = normalize(data?.sections || [])
    ElMessage.success('大纲生成成功，可编辑后开始写作')
    refreshQuota()
  } finally {
    outlineLoading.value = false
  }
}

function addSection() {
  outline.value.push({
    uid: uid(),
    title: '新增章节',
    points: [],
    children: [{ uid: uid(), title: '小节标题', points: [] }]
  })
}

function addChild(si) {
  outline.value[si].children.push({ uid: uid(), title: '小节标题', points: [] })
}

function removeChild(si, ci) {
  outline.value[si].children.splice(ci, 1)
}

// ==================== 第 3 步：章节生成 ====================
async function genOne(node) {
  genLoading[node.uid] = true
  try {
    const data = await genSection({
      title: form.title,
      sectionName: node.title,
      keywords: form.researchDirection,
      points: (node.points || []).join('；'),
      context: '',
      modelId: selectedModelId.value
    })
    sectionTexts[node.uid] = data?.content || ''
    refreshQuota()
  } finally {
    delete genLoading[node.uid]
  }
}

// 一键生成全文：按顺序生成所有章节导语与各小节（参考文献除外）
const genAllLoading = ref(false)
async function generateAll() {
  const nodes = []
  for (const sec of outline.value) {
    if (sec.title === '参考文献') continue
    nodes.push(sec)
    if (sec.children && sec.children.length) {
      nodes.push(...sec.children)
    }
  }
  if (!nodes.length) return
  genAllLoading.value = true
  try {
    for (const node of nodes) {
      const data = await genSection({
        title: form.title,
        sectionName: node.title,
        keywords: form.researchDirection,
        points: (node.points || []).join('；'),
        context: '',
        modelId: selectedModelId.value
      })
      sectionTexts[node.uid] = data?.content || ''
    }
    ElMessage.success('全文生成完成，可预览或继续编辑')
    refreshQuota()
  } finally {
    genAllLoading.value = false
  }
}

// ==================== 第 4 步：预览 / 保存 / 下载 ====================
const hasAnyText = computed(() =>
  Object.keys(sectionTexts).some((k) => (sectionTexts[k] || '').trim())
)

const previewHtml = computed(() => {
  const parts = [`<h1 class="pv-title">${escapeHtml(form.title)}</h1>`]
  for (const sec of outline.value) {
    if (sec.children && sec.children.length) {
      parts.push(`<h2>${escapeHtml(sec.title)}</h2>`)
      if ((sectionTexts[sec.uid] || '').trim()) {
        parts.push(`<div class="pv-para">${paraHtml(sec)}</div>`)
      }
      for (const child of sec.children) {
        parts.push(`<h3>${escapeHtml(child.title)}</h3>`)
        parts.push(`<div class="pv-para">${paraHtml(child)}</div>`)
      }
    } else {
      parts.push(`<h2>${escapeHtml(sec.title)}</h2>`)
      parts.push(`<div class="pv-para">${paraHtml(sec)}</div>`)
    }
  }
  return parts.join('')
})

function paraHtml(node) {
  const text = sectionTexts[node.uid] || ''
  return text
    .split(/\n+/)
    .map((p) => `<p>${escapeHtml(p)}</p>`)
    .join('')
}

async function savePaper(done) {
  if (!form.title.trim()) {
    ElMessage.warning('论文题目不能为空')
    return
  }
  if (!paperId.value) {
    const paper = await paperCreate({
      title: form.title,
      keywords: form.researchDirection,
      paperType: form.paperType,
      researchDirection: form.researchDirection
    })
    paperId.value = paper.id
  }
  await paperUpdate(paperId.value, {
    title: form.title,
    keywords: form.researchDirection,
    paperType: form.paperType,
    researchDirection: form.researchDirection,
    outline: JSON.stringify(outline.value),
    content: JSON.stringify(sectionTexts),
    status: done ? 'done' : 'draft'
  })
  ElMessage.success(done ? '论文已保存' : '草稿已保存')
}

function downloadDoc() {
  const body = previewHtml.value
    .replace(/class="pv-title"/g, 'style="text-align:center;font-size:22pt"')
    .replace(/<h2>/g, '<h2 style="font-size:16pt">')
    .replace(/<h3>/g, '<h3 style="font-size:14pt">')
    .replace(/<p>/g, '<p style="text-indent:2em;line-height:1.8;font-size:12pt">')
  const html = `<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word"><head><meta charset="utf-8"><title>${escapeHtml(form.title)}</title></head><body>${body}</body></html>`
  const blob = new Blob(['\ufeff' + html], { type: 'application/msword' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${form.title || '论文草稿'}.doc`
  a.click()
  URL.revokeObjectURL(url)
  ElMessage.success('已生成 Word 草稿文件')
}
</script>

<style scoped>
.workspace {
  max-width: 1080px;
  margin: 0 auto;
}
.quota-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}
.quota-tip {
  color: #909399;
  font-size: 12px;
}
.steps {
  margin-bottom: 24px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(31, 58, 147, 0.06);
}
.topics {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 12px;
  margin-top: 16px;
}
.topic {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 14px;
  cursor: pointer;
  transition: all 0.2s;
}
.topic:hover {
  border-color: #2f54eb;
}
.topic.active {
  border-color: #2f54eb;
  background: #f0f5ff;
}
.topic-title {
  font-weight: 600;
  color: #1f2d52;
  margin-bottom: 6px;
}
.topic-reason {
  font-size: 12px;
  color: #909399;
}
.outline h4 {
  color: #1f2d52;
  margin-bottom: 12px;
}
.sec {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
}
.sec-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.sec-title-input {
  flex: 1;
  font-weight: 600;
}
.subs {
  margin-top: 8px;
  display: grid;
  gap: 6px;
}
.sub {
  display: flex;
  align-items: center;
  gap: 8px;
}
.sub :deep(.el-input) {
  flex: 1;
}
.sec-block {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 14px;
  margin-bottom: 16px;
}
.sec-head,
.child-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}
.sec-name {
  font-weight: 600;
  color: #1f2d52;
  font-size: 15px;
}
.child-name {
  color: #303133;
  font-size: 14px;
}
.children {
  display: grid;
  gap: 12px;
}
.gen-text :deep(.el-textarea__inner) {
  font-family: inherit;
  line-height: 1.7;
}
.preview {
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 32px 40px;
  line-height: 1.9;
}
.preview :deep(.pv-title) {
  text-align: center;
  font-size: 22px;
  color: #1f2d52;
  margin-bottom: 20px;
}
.preview :deep(h2) {
  color: #1f2d52;
  font-size: 18px;
  margin: 24px 0 12px;
}
.preview :deep(h3) {
  color: #303133;
  font-size: 15px;
  margin: 16px 0 8px;
}
.preview :deep(p) {
  text-indent: 2em;
  margin-bottom: 8px;
}
</style>

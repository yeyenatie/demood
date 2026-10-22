import request from './request'

// ============ 用户 ============
export const login = (data) => request.post('/user/login', data)
export const register = (data) => request.post('/user/register', data)

// ============ AI 生成 ============
export const genTopics = (data) => request.post('/ai/topics', data)
export const genOutline = (data) => request.post('/ai/outline', data)
export const genSection = (data) => request.post('/ai/section', data)

// ============ AI 模型管理（多模型） ============
export const getAiModels = () => request.get('/ai/models')
export const createAiModel = (data) => request.post('/ai/models', data)
export const updateAiModel = (id, data) => request.put(`/ai/models/${id}`, data)
export const deleteAiModel = (id) => request.delete(`/ai/models/${id}`)
export const testAiSetting = (data) => request.post('/ai/test', data)

// ============ AI 生成配额 ============
export const getAiQuota = () => request.get('/ai/quota')

// ============ 论文 ============
export const paperList = () => request.get('/paper/list')
export const paperGet = (id) => request.get(`/paper/${id}`)
export const paperCreate = (data) => request.post('/paper', data)
export const paperUpdate = (id, data) => request.put(`/paper/${id}`, data)
export const paperDelete = (id) => request.delete(`/paper/${id}`)

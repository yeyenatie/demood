import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 180000
})

// 请求拦截：自动携带 token
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截：统一处理业务码
request.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data.code !== 200) {
      ElMessage.error(data.msg || '请求失败')
      if (data.code === 401) {
        localStorage.clear()
        router.push('/login')
      }
      return Promise.reject(new Error(data.msg || '请求失败'))
    }
    return data.data
  },
  (err) => {
    ElMessage.error(err.response?.data?.msg || '网络异常，请检查后端服务是否已启动')
    return Promise.reject(err)
  }
)

export default request

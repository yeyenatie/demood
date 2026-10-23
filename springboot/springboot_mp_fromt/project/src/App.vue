<template>
  <div id="app">
    <!-- 临时导航：仅用于验证四个路由可切换，后续替换为正式电商导航栏 -->
    <nav class="nav">
      <router-link to="/">首页</router-link>
      <router-link to="/category">分类</router-link>
      <router-link to="/cart">购物车</router-link>
      <router-link to="/mine">我的</router-link>
    </nav>
    <router-view />
  </div>
</template>

<style>
body {
  margin: 0;
}

.nav {
  display: flex;
  gap: 24px;
  padding: 12px 24px;
  border-bottom: 1px solid #ebeef5;
}

.nav a {
  color: #303133;
  text-decoration: none;
}

.nav a.router-link-active {
  color: #409eff;
  font-weight: 600;
}

.page {
  padding: 24px;
}
</style>
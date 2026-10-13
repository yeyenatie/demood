# -*- coding: utf-8 -*-
"""
动态爱心弹窗效果
==================
功能：
  1. 利用心形参数方程计算爱心轮廓坐标
  2. 分三阶段展示：爱心轮廓 → 爱心内部填充 → 全屏随机覆盖
  3. 随机显示温馨话语
  4. 支持自定义提示语、窗口大小及持续时间

按 ESC 键可随时退出。
"""

import tkinter as tk
import random
import math
import time

# ============== 可配置参数 ==============

# 爱心颜色（从外到内渐变）
HEART_COLORS = [
    "#ff1744", "#ff4081", "#f06292", "#f48fb1",
    "#f8bbd0", "#ec407a", "#e91e63", "#d81b60"
]

# 全屏覆盖阶段的温馨颜色
WARM_COLORS = [
    "#ff6b9d", "#ff8fab", "#ffc2d1", "#bde0fe",
    "#caffbf", "#fdffb6", "#ffd6a5", "#ffadad",
    "#bdb2ff", "#a0c4ff"
]

# 温馨话语（可自行添加/修改）
SWEET_MESSAGES = [
    "我爱你 ❤",
    "你是我的唯一",
    "遇见你真好",
    "想和你看遍世界",
    "你笑起来真好看",
    "有你在身边很幸福",
    "你是我的小太阳",
    "每天都更爱你一点",
    "你是最棒的",
    "感谢有你",
    "你就是我的全世界",
    "愿得一人心，白首不分离",
    "往后余生都是你",
    "你是我的心上人",
    "爱你三千遍",
]

# 弹窗大小
WINDOW_WIDTH = 120
WINDOW_HEIGHT = 80

# 各阶段弹窗显示时长（毫秒）
POPUP_DURATION = 2500

# 阶段之间的间隔（毫秒）
PHASE_INTERVAL = 800

# 爱心轮廓点数量（越多爱心越精细）
HEART_OUTLINE_POINTS = 60

# 爱心内部填充点数量
HEART_FILL_POINTS = 120

# 全屏覆盖弹窗数量
FULLSCREEN_POPUPS = 80

# 弹窗出现间隔（毫秒），越小越密集
SPAWN_INTERVAL = 30

# ==========================================


class HeartPopupApp:
    """动态爱心弹窗应用"""

    def __init__(self, custom_messages=None, duration=None,
                 window_size=None):
        """
        初始化应用

        Args:
            custom_messages: 自定义提示语列表，为 None 则使用默认
            duration: 每个弹窗显示时长（毫秒）
            window_size: 弹窗大小元组 (宽, 高)
        """
        # 应用用户自定义配置
        if custom_messages is not None:
            self.messages = custom_messages
        else:
            self.messages = SWEET_MESSAGES

        if duration is not None:
            self.popup_duration = duration
        else:
            self.popup_duration = POPUP_DURATION

        if window_size is not None:
            self.win_w, self.win_h = window_size
        else:
            self.win_w, self.win_h = WINDOW_WIDTH, WINDOW_HEIGHT

        # 创建隐藏的主窗口（所有弹窗基于此）
        self.root = tk.Tk()
        self.root.withdraw()

        # 获取屏幕尺寸
        self.screen_w = self.root.winfo_screenwidth()
        self.screen_h = self.root.winfo_screenheight()

        # 管理所有弹窗，便于统一清理
        self.all_windows = []
        self.after_ids = []

        # 绑定 ESC 退出
        self.root.bind("<Escape>", lambda e: self.quit_app())

    # ---------- 爱心坐标计算 ----------

    def _heart_parametric(self, t, scale=1.0):
        """
        心形参数方程

        Args:
            t: 参数 (0 ~ 2π)
            scale: 缩放系数

        Returns:
            (x, y) 坐标
        """
        x = 16 * math.sin(t) ** 3
        y = 13 * math.cos(t) - 5 * math.cos(2 * t) \
            - 2 * math.cos(3 * t) - math.cos(4 * t)
        return x * scale, y * scale

    def compute_heart_outline(self, num_points=HEART_OUTLINE_POINTS):
        """
        计算爱心轮廓上的点（屏幕坐标）

        Args:
            num_points: 轮廓点数量

        Returns:
            [(x, y), ...] 屏幕坐标列表
        """
        # 爱心缩放：让爱心大约占屏幕的 60%
        scale = min(self.screen_w, self.screen_h) / 40.0
        cx, cy = self.screen_w / 2, self.screen_h / 2

        points = []
        for i in range(num_points):
            t = 2 * math.pi * i / num_points
            hx, hy = self._heart_parametric(t, scale)
            # 注意：屏幕 y 轴向下，心形方程 y 轴向上，所以取反
            sx = int(cx + hx)
            sy = int(cy - hy)
            points.append((sx, sy))
        return points

    def compute_heart_fill(self, num_points=HEART_FILL_POINTS):
        """
        计算爱心内部随机填充点（屏幕坐标）
        使用拒绝采样：在包围盒内随机取点，保留在心形内部的点

        Args:
            num_points: 填充点数量

        Returns:
            [(x, y), ...] 屏幕坐标列表
        """
        scale = min(self.screen_w, self.screen_h) / 40.0
        cx, cy = self.screen_w / 2, self.screen_h / 2

        points = []
        # 心形大致范围 x: [-17, 17], y: [-12, 16]
        max_tries = num_points * 20
        tries = 0

        while len(points) < num_points and tries < max_tries:
            tries += 1
            # 在包围盒内随机采样
            rx = random.uniform(-17, 17) * scale
            ry = random.uniform(-16, 12) * scale

            # 判断是否在心形内部：将点反向映射到参数方程
            # 简化做法：用一个「心形隐函数」不等式判断
            # (x^2 + y^2 - 1)^3 - x^2 * y^3 < 0 表示在心形内部
            nx = rx / scale
            ny = -ry / scale  # 翻转 y
            val = (nx ** 2 + ny ** 2 - 1) ** 3 - nx ** 2 * ny ** 3

            if val < 0:
                sx = int(cx + rx)
                sy = int(cy + ry)
                # 保证点在屏幕内
                if 0 < sx < self.screen_w - self.win_w and \
                   0 < sy < self.screen_h - self.win_h:
                    points.append((sx, sy))

        return points

    # ---------- 弹窗创建 ----------

    def create_popup(self, x, y, message=None, bg_color=None,
                     text_color="white"):
        """
        在指定位置创建一个弹窗

        Args:
            x, y: 屏幕坐标（左上角）
            message: 显示的文字，None 则随机
            bg_color: 背景色，None 则随机
            text_color: 文字颜色
        """
        if bg_color is None:
            bg_color = random.choice(HEART_COLORS)
        if message is None:
            message = random.choice(self.messages)

        win = tk.Toplevel(self.root)
        # 去掉标题栏和边框
        win.overrideredirect(True)
        # 置顶显示
        win.attributes("-topmost", True)
        # 设置背景色
        win.configure(bg=bg_color)

        # 设置位置和大小
        x = max(0, min(x, self.screen_w - self.win_w))
        y = max(0, min(y, self.screen_h - self.win_h))
        win.geometry(f"{self.win_w}x{self.win_h}+{x}+{y}")

        # 创建文字标签，完全填满窗口
        label = tk.Label(
            win,
            text=message,
            bg=bg_color,
            fg=text_color,
            font=("微软雅黑", 10, "bold"),
            borderwidth=0,
            highlightthickness=0,
            relief="flat",
            wraplength=self.win_w - 10,
            justify="center"
        )
        label.pack(fill="both", expand=True, padx=0, pady=0)

        # 点击弹窗可单独关闭
        win.bind("<Button-1>", lambda e: self._close_window(win))

        self.all_windows.append(win)

        # 设置自动关闭
        auto_close = self.root.after(
            self.popup_duration, lambda: self._close_window(win)
        )
        self.after_ids.append(auto_close)

    def _close_window(self, win):
        """安全关闭一个弹窗"""
        try:
            if win.winfo_exists():
                win.destroy()
            if win in self.all_windows:
                self.all_windows.remove(win)
        except tk.TclError:
            pass

    # ---------- 三阶段动画 ----------

    def phase1_heart_outline(self):
        """阶段一：用弹窗勾勒爱心轮廓"""
        outline_points = self.compute_heart_outline()
        self._spawn_sequence(outline_points, HEART_COLORS,
                             done_callback=self._after_phase1)

    def _after_phase1(self):
        """阶段一完成后的过渡"""
        self.root.after(PHASE_INTERVAL, self.phase2_heart_fill)

    def phase2_heart_fill(self):
        """阶段二：用弹窗填充爱心内部"""
        fill_points = self.compute_heart_fill()
        # 从两端同时开始画
        half = len(fill_points) // 2
        first_half = fill_points[:half]
        second_half = list(reversed(fill_points[half:]))

        # 交替从两端取点
        interleaved = []
        for i in range(max(len(first_half), len(second_half))):
            if i < len(first_half):
                interleaved.append(first_half[i])
            if i < len(second_half):
                interleaved.append(second_half[i])

        self._spawn_sequence(interleaved, HEART_COLORS,
                             done_callback=self._after_phase2)

    def _after_phase2(self):
        """阶段二完成后的过渡"""
        self.root.after(PHASE_INTERVAL, self.phase3_fullscreen)

    def phase3_fullscreen(self):
        """阶段三：全屏随机覆盖"""
        points = []
        for _ in range(FULLSCREEN_POPUPS):
            x = random.randint(0, self.screen_w - self.win_w)
            y = random.randint(0, self.screen_h - self.win_h)
            points.append((x, y))

        self._spawn_sequence(points, WARM_COLORS)

    def _spawn_sequence(self, points, color_pool, done_callback=None,
                        idx=0):
        """
        按间隔依次生成弹窗序列

        Args:
            points: 坐标列表
            color_pool: 颜色池
            done_callback: 全部完成后的回调
            idx: 当前处理到的索引
        """
        if idx >= len(points):
            if done_callback:
                done_callback()
            return

        x, y = points[idx]
        color = random.choice(color_pool)
        self.create_popup(x, y, bg_color=color)

        # 安排下一个
        next_id = self.root.after(
            SPAWN_INTERVAL,
            lambda: self._spawn_sequence(points, color_pool,
                                         done_callback, idx + 1)
        )
        self.after_ids.append(next_id)

    # ---------- 生命周期 ----------

    def run(self):
        """启动应用"""
        print("=" * 50)
        print("  动态爱心弹窗已启动")
        print(f"  屏幕尺寸: {self.screen_w} x {self.screen_h}")
        print(f"  弹窗尺寸: {self.win_w} x {self.win_h}")
        print(f"  温馨话语: {len(self.messages)} 条")
        print("  按 ESC 键可随时退出")
        print("=" * 50)

        # 延迟一小段时间让主窗口准备好
        self.root.after(500, self.phase1_heart_outline)
        self.root.mainloop()

    def quit_app(self):
        """退出应用，清理所有资源"""
        print("\n正在退出，清理所有弹窗...")

        # 取消所有待执行的定时器
        for aid in self.after_ids:
            try:
                self.root.after_cancel(aid)
            except (tk.TclError, ValueError):
                pass
        self.after_ids.clear()

        # 销毁所有弹窗
        for win in list(self.all_windows):
            try:
                if win.winfo_exists():
                    win.destroy()
            except tk.TclError:
                pass
        self.all_windows.clear()

        # 销毁主窗口
        try:
            self.root.quit()
            self.root.destroy()
        except tk.TclError:
            pass

        print("已退出。")


# ============== 程序入口 ==============

def main():
    """
    程序入口

    可在此处自定义配置，例如：
      custom_msgs = ["我的专属消息1", "我的专属消息2"]
      app = HeartPopupApp(custom_messages=custom_msgs,
                          duration=3000,
                          window_size=(150, 100))
    """
    app = HeartPopupApp()
    app.run()


if __name__ == "__main__":
    main()

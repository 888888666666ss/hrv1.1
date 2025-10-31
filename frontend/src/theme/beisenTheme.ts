// 北森风格主题配置
export const beisenTheme = {
  token: {
    // 主色调 - 专业的蓝色系
    colorPrimary: '#1890ff',
    colorSuccess: '#52c41a',
    colorWarning: '#faad14',
    colorError: '#ff4d4f',
    colorInfo: '#1890ff',
    
    // 中性色 - 高端灰色系
    colorTextBase: '#262626',
    colorText: '#262626',
    colorTextSecondary: '#8c8c8c',
    colorTextTertiary: '#bfbfbf',
    colorTextQuaternary: '#d9d9d9',
    
    // 背景色 - 干净现代
    colorBgContainer: '#ffffff',
    colorBgElevated: '#ffffff',
    colorBgLayout: '#f5f6fa',
    colorBgSpotlight: '#fafbfc',
    
    // 边框和分割线
    colorBorder: '#e8e9eb',
    colorBorderSecondary: '#f0f1f3',
    
    // 圆角 - 现代化
    borderRadius: 8,
    borderRadiusLG: 12,
    borderRadiusSM: 6,
    
    // 阴影 - 精致立体感
    boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
    boxShadowSecondary: '0 4px 12px rgba(0,0,0,0.08)',
    boxShadowTertiary: '0 6px 16px rgba(0,0,0,0.10)',
    
    // 字体
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif',
    fontSize: 14,
    fontSizeLG: 16,
    fontSizeXL: 20,
    fontSizeHeading1: 38,
    fontSizeHeading2: 30,
    fontSizeHeading3: 24,
    fontSizeHeading4: 20,
    fontSizeHeading5: 16,
    
    // 间距
    padding: 16,
    paddingLG: 24,
    paddingXL: 32,
    margin: 16,
    marginLG: 24,
    marginXL: 32,
  },
  components: {
    // Layout 布局
    Layout: {
      siderBg: '#0a1628', // 深蓝色侧边栏
      triggerBg: '#1890ff',
      triggerColor: '#ffffff',
      headerBg: '#ffffff',
      headerHeight: 64,
      headerPadding: '0 24px',
      footerBg: '#fafbfc',
      bodyBg: '#f5f6fa',
    },
    
    // Menu 菜单
    Menu: {
      darkItemBg: 'transparent',
      darkItemSelectedBg: 'rgba(24, 144, 255, 0.2)',
      darkItemHoverBg: 'rgba(255, 255, 255, 0.08)',
      darkItemColor: 'rgba(255, 255, 255, 0.85)',
      darkItemSelectedColor: '#ffffff',
      darkItemHoverColor: '#ffffff',
      itemMarginInline: 4,
      itemBorderRadius: 8,
      itemHeight: 48,
      collapsedIconSize: 20,
    },
    
    // Card 卡片
    Card: {
      headerBg: '#fafbfc',
      boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
      borderRadiusLG: 12,
      paddingLG: 24,
    },
    
    // Button 按钮
    Button: {
      borderRadius: 8,
      primaryColor: '#ffffff',
      primaryBg: '#1890ff',
      defaultBorderColor: '#d9d9d9',
      defaultColor: '#595959',
      defaultBg: '#ffffff',
      dangerColor: '#ffffff',
      dangerBg: '#ff4d4f',
      ghostBg: 'transparent',
      linkColor: '#1890ff',
      linkHoverColor: '#40a9ff',
      linkActiveColor: '#096dd9',
      textHoverBg: 'rgba(0,0,0,0.04)',
      defaultGhostColor: '#1890ff',
      defaultGhostBorderColor: '#1890ff',
    },
    
    // Table 表格
    Table: {
      headerBg: '#fafbfc',
      headerColor: '#262626',
      headerSortActiveBg: '#f0f0f0',
      headerSortHoverBg: '#f5f5f5',
      bodySortBg: '#fafafa',
      rowHoverBg: '#f5f7fa',
      rowSelectedBg: '#e6f7ff',
      rowSelectedHoverBg: '#bae7ff',
      rowExpandedBg: '#fbfbfb',
      borderColor: '#f0f0f0',
      headerBorderRadius: 8,
    },
    
    // Input 输入框
    Input: {
      borderRadius: 8,
      paddingInline: 12,
      paddingBlock: 8,
      hoverBorderColor: '#40a9ff',
      focusBorderColor: '#1890ff',
      activeBorderColor: '#1890ff',
    },
    
    // Modal 对话框
    Modal: {
      borderRadiusLG: 12,
      headerBg: '#fafbfc',
      titleColor: '#262626',
      contentBg: '#ffffff',
    },
    
    // Statistic 统计数值
    Statistic: {
      titleFontSize: 14,
      contentFontSize: 24,
      titleColor: '#8c8c8c',
      contentColor: '#262626',
    },
    
    // Progress 进度条
    Progress: {
      defaultColor: '#1890ff',
      remainingColor: 'rgba(0, 0, 0, 0.06)',
      lineBorderRadius: 4,
      circleTextColor: '#262626',
    },
    
    // Tag 标签
    Tag: {
      borderRadiusSM: 6,
      defaultBg: '#fafafa',
      defaultColor: '#595959',
    },
    
    // Badge 徽标
    Badge: {
      dotSize: 8,
      fontSize: 12,
      fontWeight: 'normal',
      lineHeight: 1.5,
      textColor: '#ffffff',
    },
    
    // Drawer 抽屉
    Drawer: {
      colorBgElevated: '#ffffff',
      colorText: '#262626',
      colorTextTertiary: '#8c8c8c',
      borderRadiusLG: 12,
    },
    
    // Avatar 头像
    Avatar: {
      borderRadius: 8,
      containerSize: 32,
      containerSizeLG: 40,
      containerSizeXL: 48,
      textFontSize: 14,
      textFontSizeLG: 16,
      textFontSizeXL: 18,
    },
    
    // Descriptions 描述列表
    Descriptions: {
      labelBg: '#fafbfc',
      titleColor: '#262626',
      titleMarginBottom: 16,
      itemPaddingBottom: 12,
    },
    
    // Timeline 时间轴
    Timeline: {
      itemPaddingBottom: 16,
      dotBorderWidth: 2,
      dotSize: 10,
      tailColor: '#f0f0f0',
      tailWidth: 2,
    },
    
    // List 列表
    List: {
      itemPadding: '12px 0',
      itemPaddingLG: '16px 24px',
      headerBg: '#fafbfc',
      footerBg: '#fafbfc',
      emptyTextPadding: '32px 0',
      metaMarginBottom: 4,
      avatarMarginRight: 12,
      titleMarginBottom: 4,
      descriptionFontSize: 14,
    }
  },
  
  // 自定义算法
  algorithm: 'defaultAlgorithm',
};

export default beisenTheme;
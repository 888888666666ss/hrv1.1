import { useState } from 'react';
import { 
  Layout, 
  Avatar, 
  Button, 
  Input, 
  Card, 
  Badge, 
  Row, 
  Col, 
  Typography,
  Tabs,
  Tag,
  message
} from 'antd';
import {
  HomeOutlined,
  UserOutlined,
  TeamOutlined,
  BarChartOutlined,
  CalendarOutlined,
  FileTextOutlined,
  BellOutlined,
  SearchOutlined,
  SettingOutlined,
  MessageOutlined,
  UploadOutlined,
  QuestionCircleOutlined,
  DownOutlined,
  LeftOutlined,
  RightOutlined
} from '@ant-design/icons';
import CandidateManagement from './components/CandidateManagement';
import InterviewManagement from './components/InterviewManagement';
import JobManagement from './components/JobManagement';
import TeamManagement from './components/TeamManagement';
import DataAnalytics from './components/DataAnalytics';

// 内联样式对象
const styles = {
  beisenLayout: {
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    background: '#f5f6fa'
  },
  sider: {
    background: 'white',
    boxShadow: '2px 0 6px rgba(0, 21, 41, 0.1)'
  },
  header: {
    background: 'white',
    borderBottom: '1px solid #f0f0f0',
    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.06)'
  },
  statsCard: {
    borderRadius: '12px',
    border: '1px solid #e8e9eb',
    boxShadow: '0 2px 8px rgba(0, 0, 0, 0.06)',
    transition: 'all 0.3s ease',
    overflow: 'hidden'
  },
  modernButton: {
    borderRadius: '8px',
    fontWeight: '500',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.08)',
    transition: 'all 0.3s ease'
  },
  modernSearch: {
    borderRadius: '8px',
    border: '1px solid #d9d9d9',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.02)',
    transition: 'all 0.3s ease'
  },
  pulse: {
    animation: 'pulse 2s infinite'
  },
  fadeIn: {
    animation: 'fadeIn 0.6s ease-out'
  },
  slideUp: {
    animation: 'slideUp 0.6s ease-out'
  }
};

const { Header, Sider, Content } = Layout;
const { Title, Text } = Typography;
const { TabPane } = Tabs;

function BeisenHRApp() {
  const [currentTab, setCurrentTab] = useState('tasks');
  const [searchValue, setSearchValue] = useState('');
  const [selectedSidebarIndex, setSelectedSidebarIndex] = useState(0);
  const [selectedWeekDay, setSelectedWeekDay] = useState(3); // 周四默认选中
  const [currentPage, setCurrentPage] = useState('dashboard'); // 新增：当前页面状态

  // 搜索功能
  const handleSearch = () => {
    if (!searchValue.trim()) {
      message.warning('请输入搜索关键词');
      return;
    }
    console.log('搜索候选人:', searchValue);
    message.success(`正在搜索: ${searchValue}`);
    // TODO: 实现搜索逻辑
  };

  // 创建面试功能
  const handleCreateInterview = () => {
    console.log('创建新面试');
    message.info('正在打开创建面试窗口...');
    // TODO: 打开创建面试的模态框
  };

  // 侧边栏点击
  const handleSidebarClick = (index: number) => {
    setSelectedSidebarIndex(index);
    const menuLabels = ['首页', '数据分析', '候选人', '职位管理', '团队管理', '面试日历'];
    const pageMapping = ['dashboard', 'analytics', 'candidates', 'jobs', 'team', 'interviews'];
    
    console.log('切换到菜单项:', index);
    message.info(`切换到: ${menuLabels[index]}`);
    
    // 切换页面
    setCurrentPage(pageMapping[index]);
  };

  // 周历点击
  const handleWeekDayClick = (index: number) => {
    setSelectedWeekDay(index);
    console.log('选择日期:', weekDays[index]);
    message.success(`已选择: ${weekDays[index]?.day} ${weekDays[index]?.date}日`);
    // TODO: 加载选中日期的面试安排
  };

  // 面试操作
  const handleInterviewAction = (action: string, item: any) => {
    console.log('面试操作:', action, item);
    if (action === '取消预约') {
      message.warning(`正在取消 ${item.candidate} 的面试预约`);
    } else {
      message.info(`执行操作: ${action}`);
    }
    // TODO: 实现具体的面试操作逻辑
  };

  // 统计卡片点击
  const handleStatsCardClick = (statType: string) => {
    console.log('点击统计卡片:', statType);
    message.info(`查看${statType}详情`);
    // TODO: 跳转到对应的详情页面
  };

  // Tab切换处理
  const handleTabChange = (key: string) => {
    setCurrentTab(key);
    const tabNames = { tasks: '任务', process: '流程', others: '其它' };
    message.info(`切换到: ${tabNames[key as keyof typeof tabNames]}`);
  };

  // 渲染主内容区域
  const renderMainContent = () => {
    switch (currentPage) {
      case 'candidates':
        return <CandidateManagement />;
      case 'interviews':
        return <InterviewManagement />;
      case 'analytics':
        return <DataAnalytics />;
      case 'jobs':
        return <JobManagement />;
      case 'team':
        return <TeamManagement />;
      default:
        return renderDashboardContent();
    }
  };

  // 渲染仪表板内容
  const renderDashboardContent = () => {
    return (
      <div>
        {/* 用户问候区域 */}
        <div style={{ background: 'white', borderBottom: '1px solid #f0f0f0', padding: '20px 24px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
              <Avatar size={48} style={{ background: '#1890ff' }}>张</Avatar>
              <div>
                <Title level={4} style={{ margin: 0 }}>张经理, 下午好</Title>
                <Text type="secondary">人力资源部 · 招聘经理 · 北京市朝阳区</Text>
              </div>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
              <Input
                placeholder="搜索候选人姓名、职位等信息"
                prefix={<SearchOutlined />}
                value={searchValue}
                onChange={(e) => setSearchValue(e.target.value)}
                onPressEnter={handleSearch}
                style={{ width: '300px', ...styles.modernSearch }}
              />
              <Button 
                type="primary" 
                style={styles.modernButton}
                onClick={handleSearch}
              >
                搜索
              </Button>
              <Button 
                style={styles.modernButton}
                onClick={handleCreateInterview}
              >
                新建面试
              </Button>
            </div>
          </div>

          {/* Tab切换 */}
          <Tabs activeKey={currentTab} onChange={handleTabChange} style={{ marginTop: '16px' }}>
            <TabPane tab="任务" key="tasks" />
            <TabPane tab="流程" key="process" />
            <TabPane tab="其它" key="others" />
          </Tabs>
        </div>

        {/* 主内容区域 */}
        <Content style={{ background: '#f5f6fa' }}>
          <Row gutter={24}>
            {/* 左侧主要内容 */}
            <Col span={18}>
              {/* 流程统计卡片 */}
              <Row gutter={16} style={{ marginBottom: '24px' }}>
                {processStats.map((stat, index) => (
                  <Col span={4.8} key={index}>
                    <Card 
                      style={{ 
                        borderLeft: `4px solid ${stat.borderColor}`,
                        height: '120px',
                        ...styles.statsCard,
                        ...styles.fadeIn,
                        cursor: 'pointer'
                      }}
                      onClick={() => handleStatsCardClick(stat.title)}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.boxShadow = '0 4px 16px rgba(0, 0, 0, 0.1)';
                        e.currentTarget.style.transform = 'translateY(-2px)';
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.06)';
                        e.currentTarget.style.transform = 'translateY(0)';
                      }}
                    >
                      <div style={{ textAlign: 'center' }}>
                        <Text type="secondary" style={{ fontSize: '12px', display: 'block', marginBottom: '8px' }}>
                          {stat.title}
                        </Text>
                        <Title level={2} style={{ margin: '8px 0', color: '#262626' }}>
                          {stat.count}
                        </Title>
                        <Text type="secondary" style={{ fontSize: '11px' }}>
                          {stat.subtitle}
                        </Text>
                      </div>
                    </Card>
                  </Col>
                ))}
              </Row>

              {/* 面试安排日历 */}
              <Card style={{ borderRadius: '12px', ...styles.fadeIn }}>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '20px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <div style={{ width: '4px', height: '20px', background: '#fa8c16', borderRadius: '2px' }} />
                    <Title level={4} style={{ margin: 0 }}>我的面试安排</Title>
                    <Badge count="更新时间" style={{ backgroundColor: '#f0f0f0', color: '#8c8c8c' }} />
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                    <Button type="text" size="small">回到今天</Button>
                    <Button type="text" size="small">快速查看</Button>
                    <Button 
                      type="primary" 
                      size="small" 
                      style={styles.modernButton}
                      onClick={handleCreateInterview}
                    >
                      创建面试
                    </Button>
                    <Button type="text" size="small">数据同步设置</Button>
                    <DownOutlined style={{ color: '#8c8c8c' }} />
                  </div>
                </div>

                {/* 周历 */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '24px' }}>
                  <Button type="text" size="small" icon={<LeftOutlined />} />
                  
                  <div style={{ flex: 1, display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: '8px' }}>
                    {weekDays.map((day, index) => (
                      <div
                        key={index}
                        onClick={() => handleWeekDayClick(index)}
                        style={{
                          textAlign: 'center',
                          padding: '12px 8px',
                          borderRadius: '8px',
                          background: day.active ? '#1890ff' : 'transparent',
                          color: day.active ? 'white' : '#262626',
                          cursor: 'pointer',
                          transition: 'all 0.3s ease'
                        }}
                        onMouseEnter={(e) => {
                          if (!day.active) {
                            e.currentTarget.style.background = '#f0f0f0';
                          }
                        }}
                        onMouseLeave={(e) => {
                          if (!day.active) {
                            e.currentTarget.style.background = 'transparent';
                          }
                        }}
                      >
                        <div style={{ fontSize: '12px', marginBottom: '4px' }}>{day.day}</div>
                        <div style={{ fontSize: '18px', fontWeight: '600' }}>{day.date}</div>
                      </div>
                    ))}
                  </div>

                  <Button type="text" size="small" icon={<RightOutlined />} />
                </div>

                {/* 面试列表 */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {interviewSchedule.map((item, index) => (
                    <div
                      key={index}
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '16px',
                        padding: '16px',
                        borderRadius: '8px',
                        background: '#fafafa',
                        transition: 'all 0.3s ease'
                      }}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.boxShadow = '0 4px 12px rgba(0, 0, 0, 0.1)';
                        e.currentTarget.style.transform = 'translateY(-1px)';
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.boxShadow = 'none';
                        e.currentTarget.style.transform = 'translateY(0)';
                      }}
                    >
                      <Text style={{ width: '100px', fontSize: '13px', color: '#8c8c8c' }}>
                        {item.time}
                      </Text>
                      
                      <div style={{ flex: 1 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                          <Tag color="cyan">{item.status}</Tag>
                          <Text strong>{item.candidate}</Text>
                        </div>
                        <Text type="secondary" style={{ fontSize: '12px' }}>
                          {item.type} · {item.location}
                        </Text>
                      </div>

                      {item.badge && (
                        <Badge color={item.badgeColor} text={item.badge} />
                      )}
                      {item.result && (
                        <Text style={{ color: '#1890ff', fontSize: '13px' }}>{item.result}</Text>
                      )}
                      {item.action && (
                        <Button 
                          type="link" 
                          size="small" 
                          style={{ color: '#1890ff' }}
                          onClick={() => handleInterviewAction(item.action, item)}
                        >
                          {item.action}
                        </Button>
                      )}
                    </div>
                  ))}
                </div>
              </Card>
            </Col>

            {/* 右侧统计栏 */}
            <Col span={6}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {rightSideStats.map((stat, index) => (
                  <Card 
                    key={index} 
                    style={{
                      ...styles.statsCard,
                      ...styles.slideUp
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.boxShadow = '0 4px 16px rgba(0, 0, 0, 0.1)';
                      e.currentTarget.style.transform = 'translateY(-2px)';
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.06)';
                      e.currentTarget.style.transform = 'translateY(0)';
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '8px' }}>
                      <Title level={2} style={{ margin: 0, color: '#262626' }}>
                        {stat.value}
                      </Title>
                      <Text style={{ fontSize: '13px', color: '#8c8c8c' }}>
                        {stat.label}
                      </Text>
                    </div>
                    <Text type="secondary" style={{ fontSize: '11px' }}>
                      {stat.subtitle}
                    </Text>
                  </Card>
                ))}

                {/* 特殊的Offer卡片 */}
                <Card 
                  style={{
                    background: 'linear-gradient(135deg, #fff7e6 0%, #e6f7ff 100%)',
                    border: '1px solid #ffd591',
                    ...styles.statsCard,
                    ...styles.slideUp
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.boxShadow = '0 4px 16px rgba(0, 0, 0, 0.1)';
                    e.currentTarget.style.transform = 'translateY(-2px)';
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.06)';
                    e.currentTarget.style.transform = 'translateY(0)';
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '12px' }}>
                    <Title level={2} style={{ margin: 0, color: '#fa8c16' }}>2</Title>
                    <Text style={{ fontSize: '13px', color: '#262626' }}>待发放Offer</Text>
                  </div>
                  <Text type="secondary" style={{ fontSize: '11px', display: 'block', marginBottom: '16px' }}>
                    查看详情 &gt;
                  </Text>
                  <div style={{ textAlign: 'center' }}>
                    <div style={{
                      width: '80px',
                      height: '80px',
                      background: '#fff2e8',
                      borderRadius: '40px',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      margin: '0 auto',
                      fontSize: '32px'
                    }}>
                      💡
                    </div>
                  </div>
                </Card>
              </div>
            </Col>
          </Row>
        </Content>
      </div>
    );
  };

  // 侧边栏菜单配置
  const sidebarMenus = [
    { icon: <HomeOutlined />, active: selectedSidebarIndex === 0, label: '首页' },
    { icon: <BarChartOutlined />, active: selectedSidebarIndex === 1, label: '数据分析' },
    { icon: <UserOutlined />, active: selectedSidebarIndex === 2, label: '候选人' },
    { icon: <FileTextOutlined />, active: selectedSidebarIndex === 3, label: '职位管理' },
    { icon: <TeamOutlined />, active: selectedSidebarIndex === 4, label: '团队管理' },
    { icon: <CalendarOutlined />, active: selectedSidebarIndex === 5, label: '面试日历' }
  ];

  // 招聘流程统计数据
  const processStats = [
    { 
      title: '简历筛选', 
      count: 870, 
      subtitle: '待处理简历 45', 
      color: 'orange',
      borderColor: '#fa8c16'
    },
    { 
      title: '初试', 
      count: 20, 
      subtitle: '本周已安排 12', 
      color: 'blue',
      borderColor: '#1890ff'
    },
    { 
      title: '复试', 
      count: 36, 
      subtitle: '本周已安排 8', 
      color: 'cyan',
      borderColor: '#13c2c2'
    },
    { 
      title: 'Offer', 
      count: 8, 
      subtitle: '待发放 3', 
      color: 'purple',
      borderColor: '#722ed1'
    },
    { 
      title: '入职', 
      count: 5, 
      subtitle: '本月入职 5', 
      color: 'green',
      borderColor: '#52c41a'
    }
  ];

  // 面试安排数据
  const interviewSchedule = [
    {
      time: '09:00 - 10:00',
      status: '已完成',
      candidate: '高级产品经理 - 王安',
      type: '电话面试',
      location: '视频面试',
      badge: '面试',
      badgeColor: 'green'
    },
    {
      time: '14:00 - 16:00',
      status: '已完成',
      candidate: '2023级校产品经理 - 集体面试入职',
      type: '视频面试',
      location: '会议室A',
      result: '2 通过'
    },
    {
      time: '16:30 - 19:00',
      status: '已完成',
      candidate: '高级产品经理 - 初级',
      type: '视频面试',
      location: '在线会议',
      action: '取消预约'
    },
    {
      time: '20:00 - 20:30',
      status: '已完成',
      candidate: '高级产品经理 - 王安生',
      type: '视频面试',
      location: '会议室B',
      action: '取消预约'
    }
  ];

  // 右侧统计卡片
  const rightSideStats = [
    { value: 0, label: '待跟进的候选人', subtitle: '已完成跟进 0' },
    { value: 42, label: '面试通过率', subtitle: '本月数据统计' },
    { value: 5, label: '待反馈的面试', subtitle: '需要填写评价 5' },
    { value: 5, label: '待确认的入职', subtitle: '本月待入职 5' }
  ];

  // 周历数据
  const weekDays = [
    { day: "周一", date: "26", active: selectedWeekDay === 0 },
    { day: "周二", date: "27", active: selectedWeekDay === 1 },
    { day: "周三", date: "28", active: selectedWeekDay === 2 },
    { day: "周四", date: "29", active: selectedWeekDay === 3 },
    { day: "周五", date: "30", active: selectedWeekDay === 4 },
    { day: "周六", date: "1", active: selectedWeekDay === 5 },
    { day: "周日", date: "2", active: selectedWeekDay === 6 }
  ];

  return (
    <div style={styles.beisenLayout}>
      <Layout style={{ minHeight: '100vh' }}>
        {/* 左侧导航栏 */}
        <Sider width={64} style={styles.sider}>
          <div style={{ padding: '16px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '16px' }}>
            {sidebarMenus.map((menu, index) => (
              <Button
                key={index}
                type={menu.active ? 'primary' : 'text'}
                shape="circle"
                size="large"
                icon={menu.icon}
                onClick={() => handleSidebarClick(index)}
                title={menu.label}
                style={{ 
                  width: '40px', 
                  height: '40px',
                  color: menu.active ? '#1890ff' : '#8c8c8c',
                  ...styles.modernButton,
                  ...(menu.active ? styles.pulse : {})
                }}
              />
            ))}
          </div>
        </Sider>

        <Layout>
          {/* 顶部导航 */}
          <Header style={styles.header}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
                <div style={{ display: 'flex', gap: '4px' }}>
                  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: '#ff4d4f' }} />
                  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: '#faad14' }} />
                  <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: '#52c41a' }} />
                </div>
                <Title level={4} style={{ margin: 0, color: '#262626' }}>
                  {currentPage === 'dashboard' ? '招聘管理工作台' : 
                   currentPage === 'candidates' ? '候选人管理' :
                   currentPage === 'interviews' ? '面试管理' :
                   currentPage === 'analytics' ? '数据分析' :
                   currentPage === 'jobs' ? '职位管理' :
                   currentPage === 'team' ? '团队管理' : '招聘管理工作台'}
                </Title>
              </div>
            </div>
          </Header>

          {/* 子导航栏 */}
          <div style={{ background: 'white', borderBottom: '1px solid #f0f0f0', padding: '12px 24px' }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <HomeOutlined style={{ color: '#13c2c2', fontSize: '18px' }} />
                  <Text strong style={{ fontSize: '16px' }}>Baiao</Text>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <SettingOutlined style={{ fontSize: '14px', color: '#8c8c8c' }} />
                  <Text type="secondary">全局视野工作台</Text>
                  <DownOutlined style={{ fontSize: '12px', color: '#8c8c8c' }} />
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <Button type="text" shape="circle" icon={<SearchOutlined />} />
                <Badge dot>
                  <Button type="text" shape="circle" icon={<BellOutlined />} />
                </Badge>
                <Badge dot>
                  <Button type="text" shape="circle" icon={<MessageOutlined />} />
                </Badge>
                <Button type="text" shape="circle" icon={<UploadOutlined />} />
                <Button type="text" shape="circle" icon={<SettingOutlined />} />
                <Button type="text" shape="circle" icon={<QuestionCircleOutlined />} />
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <Avatar size="small" style={{ background: '#1890ff' }}>张</Avatar>
                  <Text>张经理</Text>
                  <DownOutlined style={{ fontSize: '12px', color: '#8c8c8c' }} />
                </div>
              </div>
            </div>
          </div>

          {/* 动态渲染主内容 */}
          {renderMainContent()}
        </Layout>
      </Layout>
    </div>
  );
}

export default BeisenHRApp;
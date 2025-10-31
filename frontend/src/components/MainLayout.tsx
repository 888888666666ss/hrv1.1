import React from 'react';
import { Layout, Menu, Avatar, Dropdown, Typography, Space, Button } from 'antd';
import { UserOutlined, LogoutOutlined, SettingOutlined, DashboardOutlined, TeamOutlined, CalendarOutlined } from '@ant-design/icons';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import type { RootState, AppDispatch } from '../store';
import { logout } from '../store/authSlice';

const { Header, Sider, Content } = Layout;
const { Title } = Typography;

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useSelector((state: RootState) => state.auth);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人信息',
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '设置',
    },
    {
      type: 'divider' as const,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  const mainMenuItems = [
    {
      key: 'dashboard',
      icon: <DashboardOutlined />,
      label: '仪表盘',
      onClick: () => navigate('/dashboard'),
    },
    {
      key: 'recruitment',
      icon: <TeamOutlined />,
      label: '招聘管理',
      children: [
        {
          key: 'jobs',
          label: '职位管理',
          onClick: () => navigate('/'),
        },
        {
          key: 'candidates',
          label: '候选人管理',
          onClick: () => navigate('/candidates'),
        },
        {
          key: 'resumes',
          label: '简历管理',
          onClick: () => navigate('/resumes'),
        },
        {
          key: 'platforms',
          label: '第三方平台',
          onClick: () => navigate('/platforms'),
        },
      ],
    },
    {
      key: 'hr',
      icon: <UserOutlined />,
      label: '人事管理',
      children: [
        {
          key: 'employees',
          label: '员工档案',
          onClick: () => navigate('/employees'),
        },
        {
          key: 'departments',
          label: '组织架构',
          onClick: () => navigate('/departments'),
        },
      ],
    },
    {
      key: 'attendance',
      icon: <CalendarOutlined />,
      label: '考勤管理',
      onClick: () => navigate('/attendance'),
    },
  ];

  // 根据当前路径确定选中的菜单项
  const getSelectedKeys = () => {
    const path = location.pathname;
    if (path === '/') return ['jobs'];
    if (path === '/candidates') return ['candidates'];
    if (path === '/resumes') return ['resumes'];
    if (path === '/platforms') return ['platforms'];
    if (path === '/employees') return ['employees'];
    if (path === '/departments') return ['departments'];
    if (path === '/attendance') return ['attendance'];
    if (path === '/dashboard') return ['dashboard'];
    return ['jobs'];
  };

  const getOpenKeys = () => {
    const path = location.pathname;
    if (path === '/' || path === '/candidates' || path === '/resumes' || path === '/platforms') return ['recruitment'];
    if (path === '/employees' || path === '/departments') return ['hr'];
    return ['recruitment'];
  };

  const getPageTitle = () => {
    const path = location.pathname;
    if (path === '/') return '职位管理';
    if (path === '/candidates') return '候选人管理';
    if (path === '/resumes') return '简历管理';
    if (path === '/platforms') return '第三方平台管理';
    if (path === '/employees') return '员工档案';
    if (path === '/departments') return '组织架构';
    if (path === '/attendance') return '考勤管理';
    if (path === '/dashboard') return '仪表盘';
    return '职位管理';
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        theme="light"
        width={250}
        style={{
          boxShadow: '2px 0 8px rgba(0, 0, 0, 0.05)',
        }}
      >
        {/* Logo区域 */}
        <div style={{
          height: '64px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          borderBottom: '1px solid #f0f0f0',
          marginBottom: '8px'
        }}>
          <Title level={4} style={{ margin: 0, color: '#1f2937' }}>
            智能HR系统
          </Title>
        </div>

        {/* 导航菜单 */}
        <Menu
          mode="inline"
          selectedKeys={getSelectedKeys()}
          defaultOpenKeys={getOpenKeys()}
          items={mainMenuItems}
          style={{ border: 'none' }}
        />
      </Sider>

      <Layout>
        {/* 头部 */}
        <Header style={{
          padding: '0 24px',
          background: '#fff',
          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.05)',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center'
        }}>
          <div>
            <Title level={4} style={{ margin: 0, color: '#1f2937' }}>
              {getPageTitle()}
            </Title>
          </div>

          <Space>
            <Dropdown
              menu={{ items: userMenuItems }}
              placement="bottomRight"
              trigger={['click']}
            >
              <Button type="text" style={{ height: 'auto', padding: '8px 12px' }}>
                <Space>
                  <Avatar icon={<UserOutlined />} size="small" />
                  <span>{user?.realName || user?.username}</span>
                </Space>
              </Button>
            </Dropdown>
          </Space>
        </Header>

        {/* 内容区域 */}
        <Content style={{
          margin: '24px',
          padding: '24px',
          background: '#f5f5f5',
          minHeight: 'calc(100vh - 112px)'
        }}>
          {children}
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Button, Alert, Space } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import type { RootState, AppDispatch } from '../store';
import { setCredentials } from '../store/authSlice';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const dispatch = useDispatch<AppDispatch>();
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);

  // 如果未认证，显示临时访问选项
  if (!isAuthenticated) {
    const handleGuestAccess = () => {
      // 设置临时用户身份，方便演示
      const mockAuthData = {
        user: {
          id: 999,
          username: 'guest',
          email: 'guest@demo.com',
          realName: '访客用户',
          role: 'guest'
        },
        token: 'demo-token-' + Date.now(),
        type: 'Bearer'
      };
      
      dispatch(setCredentials(mockAuthData));
    };

    return (
      <div style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        padding: '20px'
      }}>
        <div style={{
          background: 'white',
          padding: '40px',
          borderRadius: '15px',
          boxShadow: '0 10px 30px rgba(0,0,0,0.3)',
          textAlign: 'center',
          maxWidth: '500px',
          width: '100%'
        }}>
          <UserOutlined style={{ fontSize: '48px', color: '#667eea', marginBottom: '20px' }} />
          <h1 style={{ color: '#667eea', marginBottom: '20px' }}>智能HR管理系统</h1>
          
          <Alert
            message="系统演示模式"
            description="后端服务正在开发中，您可以以访客身份体验前端界面功能"
            type="info"
            showIcon
            style={{ marginBottom: '30px', textAlign: 'left' }}
          />
          
          <Space direction="vertical" size="middle" style={{ width: '100%' }}>
            <Button
              type="primary"
              size="large"
              onClick={handleGuestAccess}
              style={{
                width: '100%',
                height: '50px',
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                border: 'none',
                fontSize: '16px',
                fontWeight: 'bold'
              }}
            >
              以访客身份体验系统
            </Button>
            
            <Button
              type="default"
              size="large"
              href="/login"
              style={{
                width: '100%',
                height: '50px',
                fontSize: '16px',
                borderColor: '#667eea',
                color: '#667eea'
              }}
            >
              正式登录
            </Button>
          </Space>
          
          <p style={{ marginTop: '20px', color: '#666', fontSize: '14px' }}>
            访客模式仅供演示，数据不会真实保存
          </p>
        </div>
      </div>
    );
  }

  return <>{children}</>;
};

export default ProtectedRoute;
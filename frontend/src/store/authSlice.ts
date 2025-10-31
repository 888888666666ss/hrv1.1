// 认证状态管理
export interface AuthState {
  isAuthenticated: boolean;
  user: {
    id: string;
    name: string;
    email: string;
    role: string;
  } | null;
}

export const initialAuthState: AuthState = {
  isAuthenticated: false,
  user: null
};

// 简单的认证状态管理
export const useAuthStore = () => {
  return {
    isAuthenticated: true, // 暂时设为true，方便演示
    user: {
      id: '1',
      name: '系统管理员',
      email: 'admin@hr-system.com',
      role: 'admin'
    }
  };
};

// 导出需要的函数
export const logout = () => {
  // 登出逻辑
};

export const setCredentials = (credentials: any) => {
  // 设置凭证逻辑
};

// 默认导出
export default {
  useAuthStore,
  logout,
  setCredentials
};
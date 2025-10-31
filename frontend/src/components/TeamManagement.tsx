import React, { useState } from 'react';
import {
  Card,
  Table,
  Button,
  Input,
  Select,
  Avatar,
  Tag,
  Modal,
  Form,
  Row,
  Col,
  Space,
  Typography,
  Statistic,
  message,
  Progress,
  List,
  Tabs,
  Divider,
  Rate,
  Tooltip,
  Badge,
  Tree
} from 'antd';
import {
  TeamOutlined,
  UserOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  StarOutlined,
  MailOutlined,
  PhoneOutlined,
  EnvironmentOutlined,
  CalendarOutlined,
  ApartmentOutlined,
  CrownOutlined,
  TrophyOutlined,
  BookOutlined,
  SearchOutlined,
  FilterOutlined,
  SettingOutlined
} from '@ant-design/icons';

const { Option } = Select;
const { Title, Text } = Typography;
const { TextArea } = Input;
const { TabPane } = Tabs;

// 团队管理组件
export const TeamManagement: React.FC = () => {
  const [activeTab, setActiveTab] = useState('members');
  const [searchValue, setSearchValue] = useState('');
  const [selectedDepartment, setSelectedDepartment] = useState('all');
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedMember, setSelectedMember] = useState<any>(null);
  const [form] = Form.useForm();

  // 模拟团队成员数据
  const teamMembers = [
    {
      id: '1',
      name: '张经理',
      email: 'zhang.manager@company.com',
      phone: '138-0013-8001',
      avatar: 'ZJL',
      position: '招聘经理',
      department: '人力资源部',
      level: 'senior',
      status: 'active',
      joinDate: '2022-03-15',
      location: '北京',
      skills: ['招聘', '面试', '人才评估', '团队管理'],
      performance: 92,
      projects: 15,
      interviews: 128,
      hires: 45,
      teamLead: true,
      reports: 3
    },
    {
      id: '2',
      name: '李小美',
      email: 'li.xiaomei@company.com',
      phone: '138-0013-8002',
      avatar: 'LXM',
      position: '高级招聘专员',
      department: '人力资源部',
      level: 'middle',
      status: 'active',
      joinDate: '2023-01-10',
      location: '北京',
      skills: ['简历筛选', '候选人沟通', '数据分析'],
      performance: 88,
      projects: 8,
      interviews: 95,
      hires: 28,
      teamLead: false,
      reports: 0
    },
    {
      id: '3',
      name: '王小强',
      email: 'wang.xiaoqiang@company.com',
      phone: '138-0013-8003',
      avatar: 'WXQ',
      position: '招聘专员',
      department: '人力资源部',
      level: 'junior',
      status: 'active',
      joinDate: '2023-06-20',
      location: '上海',
      skills: ['电话沟通', '信息收集', '初步筛选'],
      performance: 78,
      projects: 5,
      interviews: 62,
      hires: 18,
      teamLead: false,
      reports: 0
    },
    {
      id: '4',
      name: '陈小红',
      email: 'chen.xiaohong@company.com',
      phone: '138-0013-8004',
      avatar: 'CXH',
      position: 'HRBP',
      department: '人力资源部',
      level: 'senior',
      status: 'active',
      joinDate: '2021-09-01',
      location: '深圳',
      skills: ['员工关系', '绩效管理', '组织发展', '培训'],
      performance: 95,
      projects: 12,
      interviews: 0,
      hires: 0,
      teamLead: true,
      reports: 2
    }
  ];

  // 部门组织架构数据
  const departmentTree = [
    {
      title: '人力资源部',
      key: '0-0',
      children: [
        {
          title: '招聘组',
          key: '0-0-0',
          children: [
            { title: '张经理 (招聘经理)', key: '0-0-0-0' },
            { title: '李小美 (高级招聘专员)', key: '0-0-0-1' },
            { title: '王小强 (招聘专员)', key: '0-0-0-2' }
          ]
        },
        {
          title: 'HRBP组',
          key: '0-0-1',
          children: [
            { title: '陈小红 (HRBP)', key: '0-0-1-0' }
          ]
        }
      ]
    }
  ];

  // 绩效评估数据
  const performanceData = [
    { month: '1月', score: 85 },
    { month: '2月', score: 88 },
    { month: '3月', score: 92 },
    { month: '4月', score: 89 },
    { month: '5月', score: 94 },
    { month: '6月', score: 91 }
  ];

  // 获取等级配置
  const getLevelConfig = (level: string) => {
    const configs = {
      junior: { color: 'cyan', text: '初级', icon: '👶' },
      middle: { color: 'blue', text: '中级', icon: '👨‍💼' },
      senior: { color: 'purple', text: '高级', icon: '👑' },
      expert: { color: 'gold', text: '专家', icon: '🏆' }
    };
    return configs[level as keyof typeof configs] || configs.junior;
  };

  // 获取状态配置
  const getStatusConfig = (status: string) => {
    const configs = {
      active: { color: 'success', text: '在职' },
      vacation: { color: 'warning', text: '休假' },
      inactive: { color: 'error', text: '离职' }
    };
    return configs[status as keyof typeof configs] || configs.active;
  };

  // 添加团队成员
  const handleAddMember = () => {
    setSelectedMember(null);
    setModalVisible(true);
    form.resetFields();
  };

  // 编辑团队成员
  const handleEditMember = (member: any) => {
    setSelectedMember(member);
    setModalVisible(true);
    form.setFieldsValue(member);
  };

  // 删除团队成员
  const handleDeleteMember = (id: string) => {
    message.success('团队成员已移除');
  };

  // 表格列配置
  const columns = [
    {
      title: '成员信息',
      key: 'memberInfo',
      render: (record: any) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <Badge 
            dot={record.status === 'active'} 
            status={getStatusConfig(record.status).color as any}
          >
            <Avatar size={40} style={{ background: '#1890ff' }}>
              {record.avatar}
            </Avatar>
          </Badge>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Text strong>{record.name}</Text>
              {record.teamLead && (
                <CrownOutlined style={{ color: '#faad14' }} title="团队负责人" />
              )}
            </div>
            <div style={{ fontSize: '12px', color: '#8c8c8c' }}>
              {record.position} · {record.department}
            </div>
          </div>
        </div>
      )
    },
    {
      title: '职级',
      dataIndex: 'level',
      key: 'level',
      render: (level: string) => {
        const config = getLevelConfig(level);
        return (
          <Tag color={config.color}>
            {config.icon} {config.text}
          </Tag>
        );
      }
    },
    {
      title: '工作地点',
      dataIndex: 'location',
      key: 'location',
      render: (location: string) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
          <EnvironmentOutlined />
          {location}
        </div>
      )
    },
    {
      title: '技能标签',
      dataIndex: 'skills',
      key: 'skills',
      render: (skills: string[]) => (
        <div>
          {skills.slice(0, 2).map(skill => (
            <Tag key={skill} color="blue" style={{ marginBottom: '2px' }}>
              {skill}
            </Tag>
          ))}
          {skills.length > 2 && (
            <Tag color="default">+{skills.length - 2}</Tag>
          )}
        </div>
      )
    },
    {
      title: '绩效评分',
      dataIndex: 'performance',
      key: 'performance',
      render: (score: number) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Progress
            type="circle"
            size={40}
            percent={score}
            strokeColor={score >= 90 ? '#52c41a' : score >= 80 ? '#1890ff' : '#faad14'}
            format={() => score}
          />
          <div>
            <div style={{ fontSize: '12px', color: '#8c8c8c' }}>
              {score >= 90 ? '优秀' : score >= 80 ? '良好' : '一般'}
            </div>
          </div>
        </div>
      )
    },
    {
      title: '工作统计',
      key: 'workStats',
      render: (record: any) => (
        <div>
          <div style={{ fontSize: '12px' }}>
            项目: {record.projects} | 面试: {record.interviews}
          </div>
          <div style={{ fontSize: '12px', color: '#8c8c8c' }}>
            入职: {record.hires} | 下属: {record.reports}
          </div>
        </div>
      )
    },
    {
      title: '入职时间',
      dataIndex: 'joinDate',
      key: 'joinDate',
      render: (date: string) => (
        <Text type="secondary">{date}</Text>
      )
    },
    {
      title: '操作',
      key: 'actions',
      render: (record: any) => (
        <Space>
          <Tooltip title="查看详情">
            <Button
              type="link"
              icon={<UserOutlined />}
              onClick={() => handleEditMember(record)}
            />
          </Tooltip>
          <Tooltip title="发送邮件">
            <Button
              type="link"
              icon={<MailOutlined />}
              onClick={() => message.info(`发送邮件给 ${record.name}`)}
            />
          </Tooltip>
          <Tooltip title="编辑信息">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEditMember(record)}
            />
          </Tooltip>
          <Tooltip title="移除成员">
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => handleDeleteMember(record.id)}
            />
          </Tooltip>
        </Space>
      )
    }
  ];

  // 筛选数据
  const filteredMembers = teamMembers.filter(member => {
    const matchesSearch = !searchValue || 
      member.name.toLowerCase().includes(searchValue.toLowerCase()) ||
      member.position.toLowerCase().includes(searchValue.toLowerCase()) ||
      member.email.toLowerCase().includes(searchValue.toLowerCase());
    
    const matchesDepartment = selectedDepartment === 'all' || member.department === selectedDepartment;
    
    return matchesSearch && matchesDepartment;
  });

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <Title level={3} style={{ margin: 0 }}>团队管理</Title>
        <Button 
          type="primary" 
          icon={<PlusOutlined />}
          onClick={handleAddMember}
        >
          添加成员
        </Button>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab}>
        {/* 团队成员 */}
        <TabPane 
          tab={
            <span>
              <TeamOutlined />
              团队成员
            </span>
          } 
          key="members"
        >
          {/* 统计卡片 */}
          <Row gutter={16} style={{ marginBottom: '24px' }}>
            <Col span={6}>
              <Card>
                <Statistic
                  title="团队总人数"
                  value={teamMembers.length}
                  prefix={<TeamOutlined />}
                  valueStyle={{ color: '#1890ff' }}
                />
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <Statistic
                  title="平均绩效"
                  value={Math.round(teamMembers.reduce((sum, m) => sum + m.performance, 0) / teamMembers.length)}
                  suffix="/100"
                  prefix={<TrophyOutlined />}
                  valueStyle={{ color: '#52c41a' }}
                />
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <Statistic
                  title="本月招聘"
                  value={teamMembers.reduce((sum, m) => sum + m.hires, 0)}
                  prefix={<UserOutlined />}
                  valueStyle={{ color: '#722ed1' }}
                />
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <Statistic
                  title="团队负责人"
                  value={teamMembers.filter(m => m.teamLead).length}
                  prefix={<CrownOutlined />}
                  valueStyle={{ color: '#faad14' }}
                />
              </Card>
            </Col>
          </Row>

          {/* 搜索和筛选 */}
          <Card style={{ marginBottom: '24px' }}>
            <Row gutter={16} align="middle">
              <Col span={8}>
                <Input
                  placeholder="搜索成员姓名、职位或邮箱"
                  prefix={<SearchOutlined />}
                  value={searchValue}
                  onChange={(e) => setSearchValue(e.target.value)}
                  allowClear
                />
              </Col>
              <Col span={4}>
                <Select
                  style={{ width: '100%' }}
                  value={selectedDepartment}
                  onChange={setSelectedDepartment}
                  placeholder="选择部门"
                >
                  <Option value="all">全部部门</Option>
                  <Option value="人力资源部">人力资源部</Option>
                  <Option value="技术部">技术部</Option>
                  <Option value="产品部">产品部</Option>
                </Select>
              </Col>
              <Col span={4}>
                <Button icon={<FilterOutlined />}>高级筛选</Button>
              </Col>
              <Col span={8}>
                <div style={{ textAlign: 'right' }}>
                  <Text type="secondary">
                    显示 {filteredMembers.length} / {teamMembers.length} 个成员
                  </Text>
                </div>
              </Col>
            </Row>
          </Card>

          {/* 成员列表 */}
          <Card>
            <Table
              columns={columns}
              dataSource={filteredMembers}
              rowKey="id"
              pagination={{
                pageSize: 10,
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: (total, range) => `${range[0]}-${range[1]} 共 ${total} 条`
              }}
            />
          </Card>
        </TabPane>

        {/* 组织架构 */}
        <TabPane 
          tab={
            <span>
              <ApartmentOutlined />
              组织架构
            </span>
          } 
          key="organization"
        >
          <Card>
            <div style={{ display: 'flex', gap: '24px' }}>
              <div style={{ flex: 1 }}>
                <Title level={5}>部门结构</Title>
                <Tree
                  showLine
                  defaultExpandAll
                  treeData={departmentTree}
                  onSelect={(keys, info) => {
                    console.log('选中节点:', keys, info);
                  }}
                />
              </div>
              <div style={{ flex: 2 }}>
                <Title level={5}>部门详情</Title>
                <Card size="small" style={{ background: '#fafafa' }}>
                  <Row gutter={16}>
                    <Col span={8}>
                      <Statistic title="部门人数" value={teamMembers.length} />
                    </Col>
                    <Col span={8}>
                      <Statistic title="负责人数量" value={teamMembers.filter(m => m.teamLead).length} />
                    </Col>
                    <Col span={8}>
                      <Statistic title="平均工龄" value="2.5年" />
                    </Col>
                  </Row>
                </Card>
                
                <Divider />
                
                <Title level={6}>部门成员分布</Title>
                <List
                  size="small"
                  dataSource={teamMembers}
                  renderItem={(member) => (
                    <List.Item>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', width: '100%' }}>
                        <Avatar size="small">{member.avatar}</Avatar>
                        <div style={{ flex: 1 }}>
                          <Text>{member.name}</Text>
                          <br />
                          <Text type="secondary" style={{ fontSize: '12px' }}>{member.position}</Text>
                        </div>
                        <Tag color={getLevelConfig(member.level).color}>
                          {getLevelConfig(member.level).text}
                        </Tag>
                      </div>
                    </List.Item>
                  )}
                />
              </div>
            </div>
          </Card>
        </TabPane>

        {/* 绩效管理 */}
        <TabPane 
          tab={
            <span>
              <StarOutlined />
              绩效管理
            </span>
          } 
          key="performance"
        >
          <Row gutter={16}>
            <Col span={16}>
              <Card title="团队绩效趋势">
                <div style={{ height: '300px', display: 'flex', alignItems: 'end', justifyContent: 'space-around', padding: '20px' }}>
                  {performanceData.map((data, index) => (
                    <div key={index} style={{ textAlign: 'center' }}>
                      <div
                        style={{
                          width: '40px',
                          height: `${data.score * 2}px`,
                          background: 'linear-gradient(135deg, #1890ff, #722ed1)',
                          borderRadius: '4px 4px 0 0',
                          marginBottom: '8px'
                        }}
                      />
                      <Text style={{ fontSize: '12px' }}>{data.month}</Text>
                      <br />
                      <Text style={{ fontSize: '12px', color: '#8c8c8c' }}>{data.score}</Text>
                    </div>
                  ))}
                </div>
              </Card>
            </Col>
            <Col span={8}>
              <Space direction="vertical" style={{ width: '100%' }}>
                <Card title="绩效排行榜">
                  <List
                    size="small"
                    dataSource={teamMembers.sort((a, b) => b.performance - a.performance)}
                    renderItem={(member, index) => (
                      <List.Item>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', width: '100%' }}>
                          <div style={{ 
                            width: '24px', 
                            height: '24px', 
                            borderRadius: '50%', 
                            background: index === 0 ? '#faad14' : index === 1 ? '#8c8c8c' : index === 2 ? '#d4b106' : '#f0f0f0',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            color: 'white',
                            fontSize: '12px',
                            fontWeight: 'bold'
                          }}>
                            {index + 1}
                          </div>
                          <Avatar size="small">{member.avatar}</Avatar>
                          <div style={{ flex: 1 }}>
                            <Text>{member.name}</Text>
                          </div>
                          <Text strong style={{ color: '#52c41a' }}>{member.performance}</Text>
                        </div>
                      </List.Item>
                    )}
                  />
                </Card>
                
                <Card title="绩效分布">
                  <div>
                    <div style={{ marginBottom: '8px' }}>
                      <Text>优秀 (90+)</Text>
                      <div style={{ float: 'right' }}>
                        {teamMembers.filter(m => m.performance >= 90).length}人
                      </div>
                    </div>
                    <div style={{ marginBottom: '8px' }}>
                      <Text>良好 (80-89)</Text>
                      <div style={{ float: 'right' }}>
                        {teamMembers.filter(m => m.performance >= 80 && m.performance < 90).length}人
                      </div>
                    </div>
                    <div>
                      <Text>一般 (70-79)</Text>
                      <div style={{ float: 'right' }}>
                        {teamMembers.filter(m => m.performance >= 70 && m.performance < 80).length}人
                      </div>
                    </div>
                  </div>
                </Card>
              </Space>
            </Col>
          </Row>
        </TabPane>
      </Tabs>

      {/* 添加/编辑成员模态框 */}
      <Modal
        title={selectedMember ? '编辑成员信息' : '添加团队成员'}
        open={modalVisible}
        onCancel={() => {
          setModalVisible(false);
          setSelectedMember(null);
        }}
        width={600}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={(values) => {
            console.log('成员信息:', values);
            message.success(selectedMember ? '成员信息更新成功' : '团队成员添加成功');
            setModalVisible(false);
            setSelectedMember(null);
          }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="姓名" name="name" rules={[{ required: true }]}>
                <Input placeholder="请输入成员姓名" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="邮箱" name="email" rules={[{ required: true, type: 'email' }]}>
                <Input placeholder="请输入邮箱地址" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="手机号" name="phone" rules={[{ required: true }]}>
                <Input placeholder="请输入手机号码" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="职位" name="position" rules={[{ required: true }]}>
                <Input placeholder="请输入职位名称" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={8}>
              <Form.Item label="部门" name="department" rules={[{ required: true }]}>
                <Select placeholder="选择部门">
                  <Option value="人力资源部">人力资源部</Option>
                  <Option value="技术部">技术部</Option>
                  <Option value="产品部">产品部</Option>
                  <Option value="设计部">设计部</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item label="职级" name="level" rules={[{ required: true }]}>
                <Select placeholder="选择职级">
                  <Option value="junior">初级</Option>
                  <Option value="middle">中级</Option>
                  <Option value="senior">高级</Option>
                  <Option value="expert">专家</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item label="工作地点" name="location" rules={[{ required: true }]}>
                <Select placeholder="选择工作地点">
                  <Option value="北京">北京</Option>
                  <Option value="上海">上海</Option>
                  <Option value="深圳">深圳</Option>
                  <Option value="杭州">杭州</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="技能标签" name="skills">
            <Select mode="tags" placeholder="输入技能标签">
              <Option value="招聘">招聘</Option>
              <Option value="面试">面试</Option>
              <Option value="人才评估">人才评估</Option>
              <Option value="团队管理">团队管理</Option>
            </Select>
          </Form.Item>

          <Form.Item label="入职时间" name="joinDate" rules={[{ required: true }]}>
            <Input type="date" />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit">
                {selectedMember ? '保存修改' : '添加成员'}
              </Button>
              <Button onClick={() => {
                setModalVisible(false);
                setSelectedMember(null);
              }}>
                取消
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default TeamManagement;
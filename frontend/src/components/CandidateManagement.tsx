import React, { useState } from 'react';
import {
  Card,
  Table,
  Button,
  Input,
  Select,
  Badge,
  Avatar,
  Tag,
  Progress,
  Drawer,
  Descriptions,
  Rate,
  Modal,
  Form,
  Row,
  Col,
  Space,
  Typography,
  Statistic,
  message,
  Upload,
  Divider
} from 'antd';
import {
  SearchOutlined,
  FilterOutlined,
  UserOutlined,
  DownloadOutlined,
  EyeOutlined,
  PhoneOutlined,
  MailOutlined,
  CalendarOutlined,
  StarOutlined,
  FileTextOutlined,
  RobotOutlined,
  BulbOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  ExclamationCircleOutlined,
  UploadOutlined
} from '@ant-design/icons';
import { useHRStore } from '../store/hrStore';

const { Option } = Select;
const { Title, Text } = Typography;
const { TextArea } = Input;

// 候选人管理组件
export const CandidateManagement: React.FC = () => {
  const [searchValue, setSearchValue] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('all');
  const [selectedCandidate, setSelectedCandidate] = useState<any>(null);
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [aiModalVisible, setAiModalVisible] = useState(false);
  const [addCandidateVisible, setAddCandidateVisible] = useState(false);
  
  const { candidates, getFilteredCandidates, updateCandidate, addCandidate } = useHRStore();

  // AI评分配置
  const getAIScoreConfig = (score: number) => {
    if (score >= 90) return { color: '#52c41a', status: '优秀匹配', icon: '🎯' };
    if (score >= 80) return { color: '#1890ff', status: '良好匹配', icon: '👍' };
    if (score >= 70) return { color: '#faad14', status: '一般匹配', icon: '🤔' };
    return { color: '#ff4d4f', status: '需要关注', icon: '⚠️' };
  };

  // 状态标签配置
  const getStatusTag = (status: string) => {
    const statusConfig = {
      pending: { color: 'default', text: '待处理' },
      screening: { color: 'processing', text: '筛选中' },
      interviewing: { color: 'warning', text: '面试中' },
      offered: { color: 'success', text: '已发Offer' },
      hired: { color: 'success', text: '已入职' },
      rejected: { color: 'error', text: '已拒绝' }
    };
    const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.pending;
    return <Tag color={config.color}>{config.text}</Tag>;
  };

  // 查看候选人详情
  const handleViewCandidate = (candidate: any) => {
    setSelectedCandidate(candidate);
    setDrawerVisible(true);
  };

  // AI智能筛选
  const handleAIFilter = () => {
    setAiModalVisible(true);
    message.info('正在进行AI智能筛选分析...');
  };

  // 批量操作
  const handleBatchOperation = (operation: string, selectedRows: any[]) => {
    message.success(`已对 ${selectedRows.length} 个候选人执行${operation}操作`);
  };

  // 更新候选人状态
  const handleStatusChange = (candidateId: string, newStatus: string) => {
    updateCandidate(candidateId, { status: newStatus });
    message.success('候选人状态已更新');
  };

  // 表格列配置
  const columns = [
    {
      title: '候选人',
      key: 'candidate',
      render: (record: any) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <Avatar size={40} style={{ background: '#1890ff' }}>
            {record.name.charAt(0)}
          </Avatar>
          <div>
            <div style={{ fontWeight: '500' }}>{record.name}</div>
            <div style={{ fontSize: '12px', color: '#8c8c8c' }}>{record.email}</div>
          </div>
        </div>
      )
    },
    {
      title: '应聘职位',
      dataIndex: 'jobTitle',
      key: 'jobTitle',
      render: (text: string) => <Text strong>{text}</Text>
    },
    {
      title: 'AI匹配度',
      dataIndex: 'aiScore',
      key: 'aiScore',
      render: (score: number) => {
        const config = getAIScoreConfig(score);
        return (
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Progress
              type="circle"
              size={40}
              percent={score}
              strokeColor={config.color}
              format={() => `${score}`}
            />
            <div>
              <div style={{ fontSize: '12px', color: config.color }}>
                {config.icon} {config.status}
              </div>
            </div>
          </div>
        );
      }
    },
    {
      title: '技能标签',
      dataIndex: 'skills',
      key: 'skills',
      render: (skills: string[]) => (
        <div>
          {skills.slice(0, 3).map(skill => (
            <Tag key={skill} color="blue" style={{ marginBottom: '4px' }}>
              {skill}
            </Tag>
          ))}
          {skills.length > 3 && (
            <Tag color="default">+{skills.length - 3}</Tag>
          )}
        </div>
      )
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => getStatusTag(status)
    },
    {
      title: '申请时间',
      dataIndex: 'appliedAt',
      key: 'appliedAt',
      render: (date: string) => (
        <Text type="secondary">{date}</Text>
      )
    },
    {
      title: '操作',
      key: 'actions',
      render: (record: any) => (
        <Space>
          <Button
            type="link"
            icon={<EyeOutlined />}
            onClick={() => handleViewCandidate(record)}
          >
            查看
          </Button>
          <Select
            size="small"
            value={record.status}
            style={{ width: '100px' }}
            onChange={(value) => handleStatusChange(record.id, value)}
          >
            <Option value="pending">待处理</Option>
            <Option value="screening">筛选中</Option>
            <Option value="interviewing">面试中</Option>
            <Option value="offered">已发Offer</Option>
            <Option value="hired">已入职</Option>
            <Option value="rejected">已拒绝</Option>
          </Select>
        </Space>
      )
    }
  ];

  // 筛选候选人数据
  const filteredCandidates = candidates.filter(candidate => {
    const matchesSearch = !searchValue || 
      candidate.name.toLowerCase().includes(searchValue.toLowerCase()) ||
      candidate.email.toLowerCase().includes(searchValue.toLowerCase()) ||
      candidate.jobTitle.toLowerCase().includes(searchValue.toLowerCase());
    
    const matchesStatus = selectedStatus === 'all' || candidate.status === selectedStatus;
    
    return matchesSearch && matchesStatus;
  });

  return (
    <div style={{ padding: '24px' }}>
      {/* 页面标题和统计 */}
      <div style={{ marginBottom: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <Title level={3} style={{ margin: 0 }}>候选人管理</Title>
          <Space>
            <Button 
              type="primary" 
              icon={<RobotOutlined />}
              onClick={handleAIFilter}
            >
              AI智能筛选
            </Button>
            <Button 
              icon={<UploadOutlined />}
              onClick={() => setAddCandidateVisible(true)}
            >
              添加候选人
            </Button>
          </Space>
        </div>
        
        {/* 统计卡片 */}
        <Row gutter={16} style={{ marginBottom: '24px' }}>
          <Col span={6}>
            <Card>
              <Statistic
                title="总候选人"
                value={candidates.length}
                prefix={<UserOutlined />}
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="待筛选"
                value={candidates.filter(c => c.status === 'pending' || c.status === 'screening').length}
                prefix={<FilterOutlined />}
                valueStyle={{ color: '#faad14' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="面试中"
                value={candidates.filter(c => c.status === 'interviewing').length}
                prefix={<CalendarOutlined />}
                valueStyle={{ color: '#52c41a' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="平均匹配度"
                value={Math.round(candidates.reduce((sum, c) => sum + c.aiScore, 0) / candidates.length)}
                suffix="%"
                prefix={<StarOutlined />}
                valueStyle={{ color: '#722ed1' }}
              />
            </Card>
          </Col>
        </Row>
      </div>

      {/* 搜索和筛选栏 */}
      <Card style={{ marginBottom: '24px' }}>
        <Row gutter={16} align="middle">
          <Col span={8}>
            <Input
              placeholder="搜索候选人姓名、邮箱或职位"
              prefix={<SearchOutlined />}
              value={searchValue}
              onChange={(e) => setSearchValue(e.target.value)}
              allowClear
            />
          </Col>
          <Col span={4}>
            <Select
              style={{ width: '100%' }}
              value={selectedStatus}
              onChange={setSelectedStatus}
              placeholder="选择状态"
            >
              <Option value="all">全部状态</Option>
              <Option value="pending">待处理</Option>
              <Option value="screening">筛选中</Option>
              <Option value="interviewing">面试中</Option>
              <Option value="offered">已发Offer</Option>
              <Option value="hired">已入职</Option>
              <Option value="rejected">已拒绝</Option>
            </Select>
          </Col>
          <Col span={4}>
            <Button icon={<FilterOutlined />}>高级筛选</Button>
          </Col>
          <Col span={8}>
            <div style={{ textAlign: 'right' }}>
              <Text type="secondary">
                显示 {filteredCandidates.length} / {candidates.length} 个候选人
              </Text>
            </div>
          </Col>
        </Row>
      </Card>

      {/* 候选人列表表格 */}
      <Card>
        <Table
          columns={columns}
          dataSource={filteredCandidates}
          rowKey="id"
          rowSelection={{
            type: 'checkbox',
            onChange: (selectedRowKeys, selectedRows) => {
              // 处理选中行
            }
          }}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => `${range[0]}-${range[1]} 共 ${total} 条`
          }}
        />
      </Card>

      {/* 候选人详情抽屉 */}
      <Drawer
        title="候选人详情"
        width={600}
        open={drawerVisible}
        onClose={() => setDrawerVisible(false)}
        extra={
          <Space>
            <Button icon={<PhoneOutlined />}>联系</Button>
            <Button icon={<MailOutlined />}>发邮件</Button>
            <Button type="primary" icon={<CalendarOutlined />}>安排面试</Button>
          </Space>
        }
      >
        {selectedCandidate && (
          <div>
            {/* 基本信息 */}
            <div style={{ textAlign: 'center', marginBottom: '24px' }}>
              <Avatar size={80} style={{ background: '#1890ff', marginBottom: '16px' }}>
                {selectedCandidate.name.charAt(0)}
              </Avatar>
              <Title level={4}>{selectedCandidate.name}</Title>
              <Text type="secondary">{selectedCandidate.jobTitle}</Text>
              <div style={{ marginTop: '16px' }}>
                <Progress
                  type="circle"
                  size={80}
                  percent={selectedCandidate.aiScore}
                  strokeColor={getAIScoreConfig(selectedCandidate.aiScore).color}
                  format={() => (
                    <div style={{ textAlign: 'center' }}>
                      <div style={{ fontSize: '16px', fontWeight: 'bold' }}>
                        {selectedCandidate.aiScore}
                      </div>
                      <div style={{ fontSize: '10px' }}>AI匹配</div>
                    </div>
                  )}
                />
              </div>
            </div>

            <Divider />

            {/* 详细信息 */}
            <Descriptions column={1} bordered size="small">
              <Descriptions.Item label="邮箱">
                {selectedCandidate.email}
              </Descriptions.Item>
              <Descriptions.Item label="电话">
                {selectedCandidate.phone}
              </Descriptions.Item>
              <Descriptions.Item label="工作经验">
                {selectedCandidate.experience}
              </Descriptions.Item>
              <Descriptions.Item label="教育背景">
                {selectedCandidate.education}
              </Descriptions.Item>
              <Descriptions.Item label="技能标签">
                <div>
                  {selectedCandidate.skills.map((skill: string) => (
                    <Tag key={skill} color="blue" style={{ marginBottom: '4px' }}>
                      {skill}
                    </Tag>
                  ))}
                </div>
              </Descriptions.Item>
              <Descriptions.Item label="当前状态">
                {getStatusTag(selectedCandidate.status)}
              </Descriptions.Item>
              <Descriptions.Item label="申请时间">
                {selectedCandidate.appliedAt}
              </Descriptions.Item>
              <Descriptions.Item label="来源渠道">
                {selectedCandidate.source}
              </Descriptions.Item>
            </Descriptions>

            {/* 备注信息 */}
            <div style={{ marginTop: '24px' }}>
              <Title level={5}>备注信息</Title>
              <Card size="small" style={{ background: '#fafafa' }}>
                <Text>{selectedCandidate.notes}</Text>
              </Card>
            </div>

            {/* AI分析报告 */}
            <div style={{ marginTop: '24px' }}>
              <Title level={5}>
                <RobotOutlined style={{ color: '#1890ff' }} /> AI分析报告
              </Title>
              <Card size="small">
                <div style={{ marginBottom: '16px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                    <BulbOutlined style={{ color: '#faad14' }} />
                    <Text strong>匹配度分析</Text>
                  </div>
                  <Text type="secondary">
                    基于职位要求和候选人背景，AI系统评估该候选人匹配度为 {selectedCandidate.aiScore}%
                  </Text>
                </div>
                
                <div style={{ marginBottom: '16px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                    <CheckCircleOutlined style={{ color: '#52c41a' }} />
                    <Text strong>优势特点</Text>
                  </div>
                  <ul style={{ margin: 0, paddingLeft: '20px' }}>
                    <li>技术栈匹配度高，具备所需核心技能</li>
                    <li>工作经验丰富，符合岗位要求</li>
                    <li>教育背景优秀，学习能力强</li>
                  </ul>
                </div>

                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                    <ExclamationCircleOutlined style={{ color: '#faad14' }} />
                    <Text strong>关注点</Text>
                  </div>
                  <ul style={{ margin: 0, paddingLeft: '20px' }}>
                    <li>建议进一步了解项目经验细节</li>
                    <li>可重点考察团队协作能力</li>
                  </ul>
                </div>
              </Card>
            </div>
          </div>
        )}
      </Drawer>

      {/* AI智能筛选模态框 */}
      <Modal
        title={
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <RobotOutlined style={{ color: '#1890ff' }} />
            AI智能筛选
          </div>
        }
        open={aiModalVisible}
        onCancel={() => setAiModalVisible(false)}
        width={800}
        footer={[
          <Button key="cancel" onClick={() => setAiModalVisible(false)}>
            取消
          </Button>,
          <Button key="submit" type="primary">
            开始筛选
          </Button>
        ]}
      >
        <div style={{ padding: '16px 0' }}>
          <Title level={5}>设置筛选条件</Title>
          <Form layout="vertical">
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item label="最低AI匹配度">
                  <Select defaultValue="70" style={{ width: '100%' }}>
                    <Option value="60">60%</Option>
                    <Option value="70">70%</Option>
                    <Option value="80">80%</Option>
                    <Option value="90">90%</Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label="工作经验要求">
                  <Select defaultValue="any" style={{ width: '100%' }}>
                    <Option value="any">不限</Option>
                    <Option value="1">1年以上</Option>
                    <Option value="3">3年以上</Option>
                    <Option value="5">5年以上</Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>
            <Form.Item label="必需技能">
              <Select mode="multiple" placeholder="选择必需技能" style={{ width: '100%' }}>
                <Option value="React">React</Option>
                <Option value="TypeScript">TypeScript</Option>
                <Option value="Node.js">Node.js</Option>
                <Option value="Python">Python</Option>
                <Option value="Java">Java</Option>
              </Select>
            </Form.Item>
            <Form.Item label="教育背景">
              <Select defaultValue="any" style={{ width: '100%' }}>
                <Option value="any">不限</Option>
                <Option value="bachelor">本科</Option>
                <Option value="master">硕士</Option>
                <Option value="phd">博士</Option>
              </Select>
            </Form.Item>
          </Form>
        </div>
      </Modal>

      {/* 添加候选人模态框 */}
      <Modal
        title="添加候选人"
        open={addCandidateVisible}
        onCancel={() => setAddCandidateVisible(false)}
        width={600}
        footer={null}
      >
        <Form
          layout="vertical"
          onFinish={(values) => {
            // 添加候选人逻辑
            console.log('添加候选人:', values);
            message.success('候选人添加成功');
            setAddCandidateVisible(false);
          }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="姓名" name="name" rules={[{ required: true }]}>
                <Input placeholder="请输入候选人姓名" />
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
              <Form.Item label="电话" name="phone" rules={[{ required: true }]}>
                <Input placeholder="请输入联系电话" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="应聘职位" name="jobTitle" rules={[{ required: true }]}>
                <Input placeholder="请输入应聘职位" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item label="工作经验" name="experience">
            <Input placeholder="如：5年" />
          </Form.Item>
          <Form.Item label="教育背景" name="education">
            <Input placeholder="如：本科" />
          </Form.Item>
          <Form.Item label="技能标签" name="skills">
            <Select mode="tags" placeholder="输入技能标签" style={{ width: '100%' }}>
              <Option value="React">React</Option>
              <Option value="TypeScript">TypeScript</Option>
              <Option value="Node.js">Node.js</Option>
            </Select>
          </Form.Item>
          <Form.Item label="简历上传" name="resume">
            <Upload.Dragger
              accept=".pdf,.doc,.docx"
              beforeUpload={() => false}
              maxCount={1}
            >
              <p className="ant-upload-drag-icon">
                <FileTextOutlined />
              </p>
              <p className="ant-upload-text">点击或拖拽文件到此区域上传</p>
              <p className="ant-upload-hint">支持 PDF、Word 格式</p>
            </Upload.Dragger>
          </Form.Item>
          <Form.Item label="备注" name="notes">
            <TextArea rows={3} placeholder="请输入备注信息" />
          </Form.Item>
          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit">
                添加候选人
              </Button>
              <Button onClick={() => setAddCandidateVisible(false)}>
                取消
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CandidateManagement;
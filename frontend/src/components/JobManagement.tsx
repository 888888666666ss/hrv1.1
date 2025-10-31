import React, { useState } from 'react';
import {
  Card,
  Table,
  Button,
  Input,
  Select,
  Badge,
  Tag,
  Modal,
  Form,
  Row,
  Col,
  Space,
  Typography,
  Statistic,
  message,
  Switch,
  InputNumber,
  Divider,
  Progress,
  Tooltip,
  Popconfirm
} from 'antd';
import {
  PlusOutlined,
  SearchOutlined,
  FilterOutlined,
  EditOutlined,
  DeleteOutlined,
  EyeOutlined,
  UserOutlined,
  TeamOutlined,
  DollarOutlined,
  EnvironmentOutlined,
  CalendarOutlined,
  FileTextOutlined,
  ShareAltOutlined,
  StarOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons';

const { Option } = Select;
const { Title, Text } = Typography;
const { TextArea } = Input;

// 职位管理组件
export const JobManagement: React.FC = () => {
  const [searchValue, setSearchValue] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('all');
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedJob, setSelectedJob] = useState<any>(null);
  const [viewModalVisible, setViewModalVisible] = useState(false);
  const [form] = Form.useForm();

  // 模拟职位数据
  const jobData = [
    {
      id: '1',
      title: '高级前端开发工程师',
      department: '技术部',
      location: '北京',
      type: 'full-time',
      status: 'active',
      salaryMin: 20000,
      salaryMax: 35000,
      applicants: 45,
      interviews: 12,
      offers: 3,
      hires: 1,
      publishDate: '2024-10-15',
      deadline: '2024-11-30',
      requirements: ['3年以上React开发经验', '熟悉TypeScript', '有组件库开发经验'],
      responsibilities: ['负责前端架构设计', '开发核心组件', '指导初级开发者'],
      benefits: ['五险一金', '年终奖', '股票期权', '弹性工作'],
      urgent: true,
      remote: true
    },
    {
      id: '2',
      title: 'AI算法工程师',
      department: '算法部',
      location: '上海',
      type: 'full-time',
      status: 'active',
      salaryMin: 25000,
      salaryMax: 45000,
      applicants: 23,
      interviews: 8,
      offers: 2,
      hires: 0,
      publishDate: '2024-10-20',
      deadline: '2024-12-15',
      requirements: ['硕士以上学历', '深度学习框架经验', 'Python编程能力'],
      responsibilities: ['算法模型设计', '数据分析优化', '技术方案评估'],
      benefits: ['高薪酬', '技术津贴', '学习基金', '健身房'],
      urgent: false,
      remote: false
    },
    {
      id: '3',
      title: '产品经理',
      department: '产品部',
      location: '深圳',
      type: 'full-time',
      status: 'draft',
      salaryMin: 18000,
      salaryMax: 30000,
      applicants: 0,
      interviews: 0,
      offers: 0,
      hires: 0,
      publishDate: '2024-10-25',
      deadline: '2024-12-31',
      requirements: ['5年以上产品经验', 'B端产品背景', '数据驱动思维'],
      responsibilities: ['产品规划设计', '需求分析整理', '项目协调推进'],
      benefits: ['竞争薪资', '期权激励', '培训机会', '团建活动'],
      urgent: false,
      remote: true
    },
    {
      id: '4',
      title: 'UI/UX设计师',
      department: '设计部',
      location: '北京',
      type: 'contract',
      status: 'closed',
      salaryMin: 15000,
      salaryMax: 25000,
      applicants: 67,
      interviews: 15,
      offers: 5,
      hires: 2,
      publishDate: '2024-09-01',
      deadline: '2024-10-31',
      requirements: ['3年以上设计经验', '熟练使用Figma', '有移动端设计经验'],
      responsibilities: ['界面设计优化', '用户体验研究', '设计规范制定'],
      benefits: ['设计津贴', '创意奖金', '设备补贴', '灵活工时'],
      urgent: false,
      remote: true
    }
  ];

  // 状态配置
  const getStatusConfig = (status: string) => {
    const configs = {
      active: { color: 'success', text: '招聘中', icon: <CheckCircleOutlined /> },
      draft: { color: 'default', text: '草稿', icon: <EditOutlined /> },
      paused: { color: 'warning', text: '暂停', icon: <ClockCircleOutlined /> },
      closed: { color: 'error', text: '已关闭', icon: <ExclamationCircleOutlined /> }
    };
    return configs[status as keyof typeof configs] || configs.active;
  };

  // 职位类型配置
  const getTypeConfig = (type: string) => {
    const configs = {
      'full-time': { color: 'blue', text: '全职' },
      'part-time': { color: 'cyan', text: '兼职' },
      contract: { color: 'orange', text: '合同工' },
      intern: { color: 'purple', text: '实习生' }
    };
    return configs[type as keyof typeof configs] || configs['full-time'];
  };

  // 创建职位
  const handleCreateJob = () => {
    setSelectedJob(null);
    setModalVisible(true);
    form.resetFields();
  };

  // 编辑职位
  const handleEditJob = (job: any) => {
    setSelectedJob(job);
    setModalVisible(true);
    form.setFieldsValue({
      ...job,
      salary: [job.salaryMin, job.salaryMax]
    });
  };

  // 查看职位详情
  const handleViewJob = (job: any) => {
    setSelectedJob(job);
    setViewModalVisible(true);
  };

  // 删除职位
  const handleDeleteJob = (id: string) => {
    message.success('职位已删除');
  };

  // 发布职位
  const handlePublishJob = (id: string) => {
    message.success('职位已发布');
  };

  // 暂停招聘
  const handlePauseJob = (id: string) => {
    message.warning('职位招聘已暂停');
  };

  // 表格列配置
  const columns = [
    {
      title: '职位信息',
      key: 'jobInfo',
      render: (record: any) => (
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
            <Text strong style={{ fontSize: '16px' }}>{record.title}</Text>
            {record.urgent && (
              <Tag color="red" style={{ fontSize: '11px' }}>急招</Tag>
            )}
            {record.remote && (
              <Tag color="green" style={{ fontSize: '11px' }}>远程</Tag>
            )}
          </div>
          <div style={{ fontSize: '12px', color: '#8c8c8c' }}>
            <EnvironmentOutlined /> {record.location} · {record.department}
          </div>
        </div>
      )
    },
    {
      title: '职位类型',
      dataIndex: 'type',
      key: 'type',
      render: (type: string) => {
        const config = getTypeConfig(type);
        return <Tag color={config.color}>{config.text}</Tag>;
      }
    },
    {
      title: '薪资范围',
      key: 'salary',
      render: (record: any) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
          <DollarOutlined style={{ color: '#52c41a' }} />
          <Text>{record.salaryMin / 1000}K - {record.salaryMax / 1000}K</Text>
        </div>
      )
    },
    {
      title: '招聘进度',
      key: 'progress',
      render: (record: any) => (
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px' }}>
            <Text style={{ fontSize: '12px' }}>应聘: {record.applicants}</Text>
            <Text style={{ fontSize: '12px' }}>面试: {record.interviews}</Text>
          </div>
          <Progress 
            percent={record.applicants > 0 ? Math.round((record.hires / record.applicants) * 100) : 0}
            size="small"
            strokeColor="#52c41a"
          />
          <Text style={{ fontSize: '11px', color: '#8c8c8c' }}>
            入职: {record.hires} / Offer: {record.offers}
          </Text>
        </div>
      )
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const config = getStatusConfig(status);
        return (
          <Badge 
            status={config.color as any} 
            text={config.text}
          />
        );
      }
    },
    {
      title: '发布时间',
      key: 'dates',
      render: (record: any) => (
        <div>
          <div style={{ fontSize: '12px' }}>发布: {record.publishDate}</div>
          <div style={{ fontSize: '12px', color: '#8c8c8c' }}>截止: {record.deadline}</div>
        </div>
      )
    },
    {
      title: '操作',
      key: 'actions',
      render: (record: any) => (
        <Space direction="vertical" size="small">
          <Space>
            <Tooltip title="查看详情">
              <Button
                type="link"
                size="small"
                icon={<EyeOutlined />}
                onClick={() => handleViewJob(record)}
              />
            </Tooltip>
            <Tooltip title="编辑职位">
              <Button
                type="link"
                size="small"
                icon={<EditOutlined />}
                onClick={() => handleEditJob(record)}
              />
            </Tooltip>
            <Tooltip title="分享职位">
              <Button
                type="link"
                size="small"
                icon={<ShareAltOutlined />}
              />
            </Tooltip>
          </Space>
          <Space>
            {record.status === 'draft' && (
              <Button
                type="link"
                size="small"
                style={{ color: '#52c41a' }}
                onClick={() => handlePublishJob(record.id)}
              >
                发布
              </Button>
            )}
            {record.status === 'active' && (
              <Button
                type="link"
                size="small"
                style={{ color: '#faad14' }}
                onClick={() => handlePauseJob(record.id)}
              >
                暂停
              </Button>
            )}
            <Popconfirm
              title="确定要删除这个职位吗？"
              onConfirm={() => handleDeleteJob(record.id)}
            >
              <Button
                type="link"
                size="small"
                danger
                icon={<DeleteOutlined />}
              />
            </Popconfirm>
          </Space>
        </Space>
      )
    }
  ];

  // 筛选数据
  const filteredJobs = jobData.filter(job => {
    const matchesSearch = !searchValue || 
      job.title.toLowerCase().includes(searchValue.toLowerCase()) ||
      job.department.toLowerCase().includes(searchValue.toLowerCase()) ||
      job.location.toLowerCase().includes(searchValue.toLowerCase());
    
    const matchesStatus = selectedStatus === 'all' || job.status === selectedStatus;
    
    return matchesSearch && matchesStatus;
  });

  return (
    <div style={{ padding: '24px' }}>
      {/* 页面标题和统计 */}
      <div style={{ marginBottom: '24px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <Title level={3} style={{ margin: 0 }}>职位管理</Title>
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={handleCreateJob}
          >
            发布职位
          </Button>
        </div>
        
        {/* 统计卡片 */}
        <Row gutter={16} style={{ marginBottom: '24px' }}>
          <Col span={6}>
            <Card>
              <Statistic
                title="总职位数"
                value={jobData.length}
                prefix={<FileTextOutlined />}
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="招聘中"
                value={jobData.filter(j => j.status === 'active').length}
                prefix={<CheckCircleOutlined />}
                valueStyle={{ color: '#52c41a' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="总应聘人数"
                value={jobData.reduce((sum, j) => sum + j.applicants, 0)}
                prefix={<UserOutlined />}
                valueStyle={{ color: '#722ed1' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="平均转化率"
                value={Math.round(
                  (jobData.reduce((sum, j) => sum + j.hires, 0) / 
                   jobData.reduce((sum, j) => sum + j.applicants, 0)) * 100
                )}
                suffix="%"
                prefix={<StarOutlined />}
                valueStyle={{ color: '#faad14' }}
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
              placeholder="搜索职位名称、部门或地点"
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
              <Option value="active">招聘中</Option>
              <Option value="draft">草稿</Option>
              <Option value="paused">暂停</Option>
              <Option value="closed">已关闭</Option>
            </Select>
          </Col>
          <Col span={4}>
            <Button icon={<FilterOutlined />}>高级筛选</Button>
          </Col>
          <Col span={8}>
            <div style={{ textAlign: 'right' }}>
              <Text type="secondary">
                显示 {filteredJobs.length} / {jobData.length} 个职位
              </Text>
            </div>
          </Col>
        </Row>
      </Card>

      {/* 职位列表表格 */}
      <Card>
        <Table
          columns={columns}
          dataSource={filteredJobs}
          rowKey="id"
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => `${range[0]}-${range[1]} 共 ${total} 条`
          }}
        />
      </Card>

      {/* 创建/编辑职位模态框 */}
      <Modal
        title={selectedJob ? '编辑职位' : '发布新职位'}
        open={modalVisible}
        onCancel={() => {
          setModalVisible(false);
          setSelectedJob(null);
        }}
        width={800}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={(values) => {
            console.log('职位信息:', values);
            message.success(selectedJob ? '职位更新成功' : '职位发布成功');
            setModalVisible(false);
            setSelectedJob(null);
          }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="职位名称" name="title" rules={[{ required: true }]}>
                <Input placeholder="请输入职位名称" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="所属部门" name="department" rules={[{ required: true }]}>
                <Select placeholder="选择部门">
                  <Option value="技术部">技术部</Option>
                  <Option value="产品部">产品部</Option>
                  <Option value="设计部">设计部</Option>
                  <Option value="算法部">算法部</Option>
                  <Option value="市场部">市场部</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={8}>
              <Form.Item label="工作地点" name="location" rules={[{ required: true }]}>
                <Select placeholder="选择工作地点">
                  <Option value="北京">北京</Option>
                  <Option value="上海">上海</Option>
                  <Option value="深圳">深圳</Option>
                  <Option value="杭州">杭州</Option>
                  <Option value="广州">广州</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item label="职位类型" name="type" rules={[{ required: true }]}>
                <Select placeholder="选择职位类型">
                  <Option value="full-time">全职</Option>
                  <Option value="part-time">兼职</Option>
                  <Option value="contract">合同工</Option>
                  <Option value="intern">实习生</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item label="薪资范围(K)" name="salary" rules={[{ required: true }]}>
                <Input.Group compact>
                  <InputNumber min={1} max={999} placeholder="最低薪资" style={{ width: '45%' }} />
                  <span style={{ margin: '0 8px' }}>-</span>
                  <InputNumber min={1} max={999} placeholder="最高薪资" style={{ width: '45%' }} />
                </Input.Group>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="招聘截止日期" name="deadline" rules={[{ required: true }]}>
                <Input type="date" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="职位标签">
                <Space>
                  <Form.Item name="urgent" valuePropName="checked" style={{ margin: 0 }}>
                    <Switch checkedChildren="急招" unCheckedChildren="普通" />
                  </Form.Item>
                  <Form.Item name="remote" valuePropName="checked" style={{ margin: 0 }}>
                    <Switch checkedChildren="远程" unCheckedChildren="现场" />
                  </Form.Item>
                </Space>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="职位要求" name="requirements">
            <Select mode="tags" placeholder="输入职位要求，按回车添加">
              <Option value="本科及以上学历">本科及以上学历</Option>
              <Option value="3年以上工作经验">3年以上工作经验</Option>
              <Option value="熟悉React框架">熟悉React框架</Option>
            </Select>
          </Form.Item>

          <Form.Item label="岗位职责" name="responsibilities">
            <TextArea rows={3} placeholder="请详细描述岗位职责" />
          </Form.Item>

          <Form.Item label="福利待遇" name="benefits">
            <Select mode="tags" placeholder="输入福利待遇">
              <Option value="五险一金">五险一金</Option>
              <Option value="年终奖">年终奖</Option>
              <Option value="股票期权">股票期权</Option>
              <Option value="弹性工作">弹性工作</Option>
            </Select>
          </Form.Item>

          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit">
                {selectedJob ? '保存修改' : '发布职位'}
              </Button>
              <Button onClick={() => {
                setModalVisible(false);
                setSelectedJob(null);
              }}>
                取消
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>

      {/* 职位详情查看模态框 */}
      <Modal
        title="职位详情"
        open={viewModalVisible}
        onCancel={() => setViewModalVisible(false)}
        width={700}
        footer={[
          <Button key="edit" type="primary" onClick={() => {
            setViewModalVisible(false);
            handleEditJob(selectedJob);
          }}>
            编辑职位
          </Button>,
          <Button key="close" onClick={() => setViewModalVisible(false)}>
            关闭
          </Button>
        ]}
      >
        {selectedJob && (
          <div>
            {/* 职位基本信息 */}
            <div style={{ marginBottom: '24px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
                <Title level={4} style={{ margin: 0 }}>{selectedJob.title}</Title>
                {selectedJob.urgent && <Tag color="red">急招</Tag>}
                {selectedJob.remote && <Tag color="green">远程</Tag>}
                {getStatusConfig(selectedJob.status).color && (
                  <Badge 
                    status={getStatusConfig(selectedJob.status).color as any} 
                    text={getStatusConfig(selectedJob.status).text}
                  />
                )}
              </div>
              
              <Row gutter={16}>
                <Col span={8}>
                  <Text type="secondary">部门：</Text>
                  <Text>{selectedJob.department}</Text>
                </Col>
                <Col span={8}>
                  <Text type="secondary">地点：</Text>
                  <Text>{selectedJob.location}</Text>
                </Col>
                <Col span={8}>
                  <Text type="secondary">类型：</Text>
                  <Tag color={getTypeConfig(selectedJob.type).color}>
                    {getTypeConfig(selectedJob.type).text}
                  </Tag>
                </Col>
              </Row>
            </div>

            <Divider />

            {/* 薪资和时间信息 */}
            <Row gutter={16} style={{ marginBottom: '24px' }}>
              <Col span={12}>
                <Card size="small" style={{ background: '#f6ffed' }}>
                  <Statistic
                    title="薪资范围"
                    value={`${selectedJob.salaryMin / 1000}K - ${selectedJob.salaryMax / 1000}K`}
                    prefix={<DollarOutlined />}
                  />
                </Card>
              </Col>
              <Col span={12}>
                <Card size="small" style={{ background: '#f0f5ff' }}>
                  <div>
                    <Text type="secondary">发布时间：</Text>
                    <Text>{selectedJob.publishDate}</Text>
                  </div>
                  <div style={{ marginTop: '8px' }}>
                    <Text type="secondary">截止时间：</Text>
                    <Text>{selectedJob.deadline}</Text>
                  </div>
                </Card>
              </Col>
            </Row>

            {/* 招聘数据统计 */}
            <Row gutter={8} style={{ marginBottom: '24px' }}>
              <Col span={6}>
                <Card size="small">
                  <Statistic title="应聘" value={selectedJob.applicants} valueStyle={{ color: '#1890ff' }} />
                </Card>
              </Col>
              <Col span={6}>
                <Card size="small">
                  <Statistic title="面试" value={selectedJob.interviews} valueStyle={{ color: '#722ed1' }} />
                </Card>
              </Col>
              <Col span={6}>
                <Card size="small">
                  <Statistic title="Offer" value={selectedJob.offers} valueStyle={{ color: '#faad14' }} />
                </Card>
              </Col>
              <Col span={6}>
                <Card size="small">
                  <Statistic title="入职" value={selectedJob.hires} valueStyle={{ color: '#52c41a' }} />
                </Card>
              </Col>
            </Row>

            {/* 职位要求 */}
            <div style={{ marginBottom: '16px' }}>
              <Title level={5}>职位要求</Title>
              <ul>
                {selectedJob.requirements.map((req: string, index: number) => (
                  <li key={index}>{req}</li>
                ))}
              </ul>
            </div>

            {/* 岗位职责 */}
            <div style={{ marginBottom: '16px' }}>
              <Title level={5}>岗位职责</Title>
              <ul>
                {selectedJob.responsibilities.map((resp: string, index: number) => (
                  <li key={index}>{resp}</li>
                ))}
              </ul>
            </div>

            {/* 福利待遇 */}
            <div>
              <Title level={5}>福利待遇</Title>
              <div>
                {selectedJob.benefits.map((benefit: string) => (
                  <Tag key={benefit} color="blue" style={{ marginBottom: '4px' }}>
                    {benefit}
                  </Tag>
                ))}
              </div>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default JobManagement;
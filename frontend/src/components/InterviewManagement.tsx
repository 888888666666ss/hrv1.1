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
  Modal,
  Form,
  Row,
  Col,
  Space,
  Typography,
  DatePicker,
  TimePicker,
  Rate,
  message,
  Tabs,
  Calendar,
  List,
  Divider,
  Tooltip,
  Popconfirm
} from 'antd';
import {
  CalendarOutlined,
  VideoCameraOutlined,
  UserOutlined,
  ClockCircleOutlined,
  EnvironmentOutlined,
  StarOutlined,
  EditOutlined,
  DeleteOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  PhoneOutlined,
  TeamOutlined,
  FileTextOutlined,
  SendOutlined,
  PlusOutlined
} from '@ant-design/icons';
import type { Dayjs } from 'dayjs';

const { Option } = Select;
const { Title, Text } = Typography;
const { TextArea } = Input;
const { TabPane } = Tabs;
const { RangePicker } = DatePicker;

// 面试管理组件
export const InterviewManagement: React.FC = () => {
  const [activeTab, setActiveTab] = useState('schedule');
  const [selectedDate, setSelectedDate] = useState<Dayjs | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [evaluationVisible, setEvaluationVisible] = useState(false);
  const [selectedInterview, setSelectedInterview] = useState<any>(null);
  const [form] = Form.useForm();

  // 模拟面试数据
  const interviewData = [
    {
      id: '1',
      candidateName: '张小明',
      candidateAvatar: 'ZXM',
      position: '高级前端开发工程师',
      interviewer: '李经理',
      date: '2024-11-01',
      time: '09:00-10:00',
      type: 'online',
      status: 'scheduled',
      location: '视频会议室A',
      round: 1,
      notes: '技术面试，重点考察React和TypeScript能力'
    },
    {
      id: '2',
      candidateName: '王小红',
      candidateAvatar: 'WXH',
      position: 'AI算法工程师',
      interviewer: '张总监',
      date: '2024-11-01',
      time: '14:00-15:30',
      type: 'offline',
      status: 'completed',
      location: '会议室B',
      round: 2,
      notes: '终面，考察综合能力和团队协作',
      evaluation: {
        technical: 4,
        communication: 5,
        teamwork: 4,
        overall: 4,
        feedback: '技术能力扎实，沟通表达清晰，团队协作意识强'
      }
    },
    {
      id: '3',
      candidateName: '李小强',
      candidateAvatar: 'LXQ',
      position: '产品经理',
      interviewer: '陈总',
      date: '2024-11-02',
      time: '10:00-11:00',
      type: 'online',
      status: 'cancelled',
      location: '视频会议室C',
      round: 1,
      notes: '候选人临时有事，面试取消'
    }
  ];

  // 状态配置
  const getStatusConfig = (status: string) => {
    const configs = {
      scheduled: { color: 'processing', text: '已安排', icon: <CalendarOutlined /> },
      completed: { color: 'success', text: '已完成', icon: <CheckCircleOutlined /> },
      cancelled: { color: 'error', text: '已取消', icon: <ExclamationCircleOutlined /> },
      inProgress: { color: 'warning', text: '进行中', icon: <ClockCircleOutlined /> }
    };
    return configs[status as keyof typeof configs] || configs.scheduled;
  };

  // 面试类型配置
  const getTypeConfig = (type: string) => {
    return type === 'online' 
      ? { icon: <VideoCameraOutlined />, text: '视频面试', color: '#1890ff' }
      : { icon: <EnvironmentOutlined />, text: '现场面试', color: '#52c41a' };
  };

  // 创建面试
  const handleCreateInterview = () => {
    setModalVisible(true);
    form.resetFields();
  };

  // 编辑面试
  const handleEditInterview = (interview: any) => {
    setSelectedInterview(interview);
    setModalVisible(true);
    form.setFieldsValue(interview);
  };

  // 删除面试
  const handleDeleteInterview = (id: string) => {
    message.success('面试已删除');
  };

  // 面试评价
  const handleEvaluate = (interview: any) => {
    setSelectedInterview(interview);
    setEvaluationVisible(true);
  };

  // 表格列配置
  const columns = [
    {
      title: '候选人',
      key: 'candidate',
      render: (record: any) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <Avatar style={{ background: '#1890ff' }}>
            {record.candidateAvatar}
          </Avatar>
          <div>
            <div style={{ fontWeight: '500' }}>{record.candidateName}</div>
            <div style={{ fontSize: '12px', color: '#8c8c8c' }}>{record.position}</div>
          </div>
        </div>
      )
    },
    {
      title: '面试时间',
      key: 'datetime',
      render: (record: any) => (
        <div>
          <div>{record.date}</div>
          <div style={{ fontSize: '12px', color: '#8c8c8c' }}>{record.time}</div>
        </div>
      )
    },
    {
      title: '面试官',
      dataIndex: 'interviewer',
      key: 'interviewer',
      render: (text: string) => (
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <UserOutlined />
          {text}
        </div>
      )
    },
    {
      title: '面试类型',
      dataIndex: 'type',
      key: 'type',
      render: (type: string) => {
        const config = getTypeConfig(type);
        return (
          <Tag icon={config.icon} color={config.color}>
            {config.text}
          </Tag>
        );
      }
    },
    {
      title: '轮次',
      dataIndex: 'round',
      key: 'round',
      render: (round: number) => `第${round}轮`
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
      title: '操作',
      key: 'actions',
      render: (record: any) => (
        <Space>
          {record.status === 'completed' && (
            <Tooltip title="查看评价">
              <Button
                type="link"
                icon={<StarOutlined />}
                onClick={() => handleEvaluate(record)}
              >
                评价
              </Button>
            </Tooltip>
          )}
          {record.status === 'scheduled' && (
            <>
              <Tooltip title="编辑面试">
                <Button
                  type="link"
                  icon={<EditOutlined />}
                  onClick={() => handleEditInterview(record)}
                />
              </Tooltip>
              <Tooltip title="开始面试">
                <Button type="link" style={{ color: '#52c41a' }}>
                  开始
                </Button>
              </Tooltip>
            </>
          )}
          <Popconfirm
            title="确定要删除这个面试安排吗？"
            onConfirm={() => handleDeleteInterview(record.id)}
          >
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
            />
          </Popconfirm>
        </Space>
      )
    }
  ];

  // 日历中的面试数据
  const getListData = (value: Dayjs) => {
    const dateStr = value.format('YYYY-MM-DD');
    return interviewData.filter(item => item.date === dateStr);
  };

  // 日历单元格渲染
  const dateCellRender = (value: Dayjs) => {
    const listData = getListData(value);
    return (
      <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
        {listData.map(item => (
          <li key={item.id}>
            <Badge 
              status={getStatusConfig(item.status).color as any}
              text={
                <span style={{ fontSize: '12px' }}>
                  {item.time.split('-')[0]} {item.candidateName}
                </span>
              }
            />
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <Title level={3} style={{ margin: 0 }}>面试管理</Title>
        <Button 
          type="primary" 
          icon={<PlusOutlined />}
          onClick={handleCreateInterview}
        >
          安排面试
        </Button>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab}>
        {/* 面试日历 */}
        <TabPane 
          tab={
            <span>
              <CalendarOutlined />
              面试日历
            </span>
          } 
          key="schedule"
        >
          <Card>
            <Calendar 
              dateCellRender={dateCellRender}
              onSelect={(date) => setSelectedDate(date)}
            />
          </Card>
        </TabPane>

        {/* 面试列表 */}
        <TabPane 
          tab={
            <span>
              <TeamOutlined />
              面试列表
            </span>
          } 
          key="list"
        >
          <Card>
            <div style={{ marginBottom: '16px' }}>
              <Row gutter={16}>
                <Col span={6}>
                  <Input
                    placeholder="搜索候选人或面试官"
                    prefix={<UserOutlined />}
                    allowClear
                  />
                </Col>
                <Col span={4}>
                  <Select placeholder="面试状态" style={{ width: '100%' }} allowClear>
                    <Option value="scheduled">已安排</Option>
                    <Option value="completed">已完成</Option>
                    <Option value="cancelled">已取消</Option>
                  </Select>
                </Col>
                <Col span={4}>
                  <Select placeholder="面试类型" style={{ width: '100%' }} allowClear>
                    <Option value="online">视频面试</Option>
                    <Option value="offline">现场面试</Option>
                  </Select>
                </Col>
                <Col span={6}>
                  <RangePicker style={{ width: '100%' }} />
                </Col>
              </Row>
            </div>
            
            <Table
              columns={columns}
              dataSource={interviewData}
              rowKey="id"
              pagination={{
                pageSize: 10,
                showTotal: (total, range) => `${range[0]}-${range[1]} 共 ${total} 条`
              }}
            />
          </Card>
        </TabPane>

        {/* 面试统计 */}
        <TabPane 
          tab={
            <span>
              <StarOutlined />
              面试统计
            </span>
          } 
          key="statistics"
        >
          <Row gutter={16}>
            <Col span={6}>
              <Card>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#1890ff' }}>
                    {interviewData.length}
                  </div>
                  <div style={{ color: '#8c8c8c' }}>总面试数</div>
                </div>
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#52c41a' }}>
                    {interviewData.filter(i => i.status === 'completed').length}
                  </div>
                  <div style={{ color: '#8c8c8c' }}>已完成</div>
                </div>
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#faad14' }}>
                    {interviewData.filter(i => i.status === 'scheduled').length}
                  </div>
                  <div style={{ color: '#8c8c8c' }}>待进行</div>
                </div>
              </Card>
            </Col>
            <Col span={6}>
              <Card>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#722ed1' }}>
                    85%
                  </div>
                  <div style={{ color: '#8c8c8c' }}>通过率</div>
                </div>
              </Card>
            </Col>
          </Row>
        </TabPane>
      </Tabs>

      {/* 创建/编辑面试模态框 */}
      <Modal
        title={selectedInterview ? '编辑面试' : '安排面试'}
        open={modalVisible}
        onCancel={() => {
          setModalVisible(false);
          setSelectedInterview(null);
        }}
        width={600}
        footer={null}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={(values) => {
            console.log('面试安排:', values);
            message.success(selectedInterview ? '面试修改成功' : '面试安排成功');
            setModalVisible(false);
            setSelectedInterview(null);
          }}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="候选人" name="candidateName" rules={[{ required: true }]}>
                <Select placeholder="选择候选人" showSearch>
                  <Option value="张小明">张小明 - 高级前端开发工程师</Option>
                  <Option value="王小红">王小红 - AI算法工程师</Option>
                  <Option value="李小强">李小强 - 产品经理</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="面试官" name="interviewer" rules={[{ required: true }]}>
                <Select placeholder="选择面试官">
                  <Option value="李经理">李经理 - 技术总监</Option>
                  <Option value="张总监">张总监 - 算法专家</Option>
                  <Option value="陈总">陈总 - 产品总监</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="面试日期" name="date" rules={[{ required: true }]}>
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="面试时间" name="time" rules={[{ required: true }]}>
                <TimePicker.RangePicker style={{ width: '100%' }} format="HH:mm" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item label="面试类型" name="type" rules={[{ required: true }]}>
                <Select placeholder="选择面试类型">
                  <Option value="online">
                    <VideoCameraOutlined /> 视频面试
                  </Option>
                  <Option value="offline">
                    <EnvironmentOutlined /> 现场面试
                  </Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="面试轮次" name="round" rules={[{ required: true }]}>
                <Select placeholder="选择轮次">
                  <Option value={1}>第一轮（初试）</Option>
                  <Option value={2}>第二轮（复试）</Option>
                  <Option value={3}>第三轮（终面）</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="面试地点/会议室" name="location" rules={[{ required: true }]}>
            <Input placeholder="请输入面试地点或会议室信息" />
          </Form.Item>

          <Form.Item label="面试备注" name="notes">
            <TextArea rows={3} placeholder="请输入面试注意事项或考察重点" />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit">
                {selectedInterview ? '保存修改' : '安排面试'}
              </Button>
              <Button onClick={() => {
                setModalVisible(false);
                setSelectedInterview(null);
              }}>
                取消
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>

      {/* 面试评价模态框 */}
      <Modal
        title="面试评价"
        open={evaluationVisible}
        onCancel={() => setEvaluationVisible(false)}
        width={600}
        footer={null}
      >
        {selectedInterview && (
          <div>
            {/* 候选人信息 */}
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '24px' }}>
              <Avatar size={48} style={{ background: '#1890ff' }}>
                {selectedInterview.candidateAvatar}
              </Avatar>
              <div>
                <Title level={5} style={{ margin: 0 }}>{selectedInterview.candidateName}</Title>
                <Text type="secondary">{selectedInterview.position}</Text>
              </div>
            </div>

            <Divider />

            {selectedInterview.evaluation ? (
              // 查看已有评价
              <div>
                <Title level={5}>面试评价结果</Title>
                <Row gutter={16} style={{ marginBottom: '16px' }}>
                  <Col span={6}>
                    <div style={{ textAlign: 'center' }}>
                      <div>技术能力</div>
                      <Rate disabled value={selectedInterview.evaluation.technical} />
                    </div>
                  </Col>
                  <Col span={6}>
                    <div style={{ textAlign: 'center' }}>
                      <div>沟通表达</div>
                      <Rate disabled value={selectedInterview.evaluation.communication} />
                    </div>
                  </Col>
                  <Col span={6}>
                    <div style={{ textAlign: 'center' }}>
                      <div>团队协作</div>
                      <Rate disabled value={selectedInterview.evaluation.teamwork} />
                    </div>
                  </Col>
                  <Col span={6}>
                    <div style={{ textAlign: 'center' }}>
                      <div>综合评价</div>
                      <Rate disabled value={selectedInterview.evaluation.overall} />
                    </div>
                  </Col>
                </Row>
                <div>
                  <Text strong>面试反馈：</Text>
                  <div style={{ marginTop: '8px', padding: '12px', background: '#fafafa', borderRadius: '6px' }}>
                    {selectedInterview.evaluation.feedback}
                  </div>
                </div>
              </div>
            ) : (
              // 填写评价表单
              <Form
                layout="vertical"
                onFinish={(values) => {
                  console.log('面试评价:', values);
                  message.success('评价提交成功');
                  setEvaluationVisible(false);
                }}
              >
                <Title level={5}>面试评价</Title>
                <Row gutter={16}>
                  <Col span={6}>
                    <Form.Item label="技术能力" name="technical" rules={[{ required: true }]}>
                      <Rate />
                    </Form.Item>
                  </Col>
                  <Col span={6}>
                    <Form.Item label="沟通表达" name="communication" rules={[{ required: true }]}>
                      <Rate />
                    </Form.Item>
                  </Col>
                  <Col span={6}>
                    <Form.Item label="团队协作" name="teamwork" rules={[{ required: true }]}>
                      <Rate />
                    </Form.Item>
                  </Col>
                  <Col span={6}>
                    <Form.Item label="综合评价" name="overall" rules={[{ required: true }]}>
                      <Rate />
                    </Form.Item>
                  </Col>
                </Row>
                
                <Form.Item label="面试反馈" name="feedback" rules={[{ required: true }]}>
                  <TextArea rows={4} placeholder="请详细描述面试过程中的亮点、不足和建议" />
                </Form.Item>

                <Form.Item label="面试结果" name="result" rules={[{ required: true }]}>
                  <Select placeholder="选择面试结果">
                    <Option value="pass">通过</Option>
                    <Option value="reject">不通过</Option>
                    <Option value="pending">待定</Option>
                  </Select>
                </Form.Item>

                <Form.Item>
                  <Space>
                    <Button type="primary" htmlType="submit" icon={<SendOutlined />}>
                      提交评价
                    </Button>
                    <Button onClick={() => setEvaluationVisible(false)}>
                      取消
                    </Button>
                  </Space>
                </Form.Item>
              </Form>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
};

export default InterviewManagement;
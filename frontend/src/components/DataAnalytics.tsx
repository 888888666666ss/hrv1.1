import React, { useState } from 'react';
import {
  Card,
  Row,
  Col,
  Statistic,
  Progress,
  Select,
  DatePicker,
  Space,
  Typography,
  Tag,
  Table,
  List,
  Tabs,
  Divider,
  Tooltip
} from 'antd';
import {
  BarChartOutlined,
  UserOutlined,
  CalendarOutlined,
  TrophyOutlined,
  RiseOutlined,
  FallOutlined,
  TeamOutlined,
  FileTextOutlined,
  DollarOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  StarOutlined,
  ThunderboltOutlined,
  FireOutlined
} from '@ant-design/icons';

const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TabPane } = Tabs;

// 数据分析组件
export const DataAnalytics: React.FC = () => {
  const [timeRange, setTimeRange] = useState('month');
  const [activeTab, setActiveTab] = useState('overview');

  // 模拟数据
  const overviewStats = [
    {
      title: '���应聘人数',
      value: 1245,
      change: 12.5,
      trend: 'up',
      icon: <UserOutlined />,
      color: '#1890ff'
    },
    {
      title: '面试完成率',
      value: 78,
      suffix: '%',
      change: 3.2,
      trend: 'up',
      icon: <CalendarOutlined />,
      color: '#52c41a'
    },
    {
      title: '入职转化率',
      value: 35,
      suffix: '%',
      change: -2.1,
      trend: 'down',
      icon: <TrophyOutlined />,
      color: '#faad14'
    },
    {
      title: '平均招聘周期',
      value: 18,
      suffix: '天',
      change: -1.5,
      trend: 'up',
      icon: <ClockCircleOutlined />,
      color: '#722ed1'
    }
  ];

  // 招聘漏斗数据
  const funnelData = [
    { stage: '简历投递', count: 1245, rate: 100, color: '#1890ff' },
    { stage: '简历筛选', count: 890, rate: 71.5, color: '#52c41a' },
    { stage: '初试邀请', count: 456, rate: 36.6, color: '#faad14' },
    { stage: '复试通过', count: 234, rate: 18.8, color: '#722ed1' },
    { stage: 'Offer发放', count: 156, rate: 12.5, color: '#f5222d' },
    { stage: '成功入职', count: 89, rate: 7.1, color: '#13c2c2' }
  ];

  // 部门招聘分析
  const departmentData = [
    { department: '技术部', target: 50, completed: 38, rate: 76 },
    { department: '产品部', target: 20, completed: 18, rate: 90 },
    { department: '设计部', target: 15, completed: 12, rate: 80 },
    { department: '市场部', target: 25, completed: 15, rate: 60 },
    { department: '运营部', target: 30, completed: 22, rate: 73 }
  ];

  // 招聘来源分析
  const sourceData = [
    { source: '招聘网站', count: 456, rate: 36.6, color: '#1890ff' },
    { source: '内推', count: 298, rate: 23.9, color: '#52c41a' },
    { source: '猎头', count: 187, rate: 15.0, color: '#faad14' },
    { source: '校招', count: 156, rate: 12.5, color: '#722ed1' },
    { source: '社交媒体', count: 89, rate: 7.1, color: '#f5222d' },
    { source: '其他', count: 59, rate: 4.7, color: '#13c2c2' }
  ];

  // 月度趋势数据
  const monthlyTrend = [
    { month: '1月', applications: 89, interviews: 45, hires: 12 },
    { month: '2月', applications: 156, interviews: 78, hires: 18 },
    { month: '3月', applications: 234, interviews: 124, hires: 28 },
    { month: '4月', applications: 189, interviews: 98, hires: 22 },
    { month: '5月', applications: 298, interviews: 156, hires: 35 },
    { month: '6月', applications: 279, interviews: 145, hires: 31 }
  ];

  // 热门职位排行
  const popularJobs = [
    { position: '前端开发工程师', applications: 234, avgSalary: '25K', competition: 'high' },
    { position: 'AI算法工程师', applications: 189, avgSalary: '35K', competition: 'high' },
    { position: '产品经理', applications: 156, avgSalary: '22K', competition: 'medium' },
    { position: 'UI设计师', applications: 123, avgSalary: '18K', competition: 'medium' },
    { position: '数据分析师', applications: 98, avgSalary: '20K', competition: 'low' }
  ];

  const getCompetitionColor = (level: string) => {
    switch (level) {
      case 'high': return '#f5222d';
      case 'medium': return '#faad14';
      case 'low': return '#52c41a';
      default: return '#8c8c8c';
    }
  };

  const getCompetitionText = (level: string) => {
    switch (level) {
      case 'high': return '竞争激烈';
      case 'medium': return '竞争适中';
      case 'low': return '竞争较少';
      default: return '未知';
    }
  };

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <Title level={3} style={{ margin: 0 }}>数据分析</Title>
        <Space>
          <Select value={timeRange} onChange={setTimeRange} style={{ width: 120 }}>
            <Option value="week">本周</Option>
            <Option value="month">本月</Option>
            <Option value="quarter">本季度</Option>
            <Option value="year">本年</Option>
          </Select>
          <RangePicker />
        </Space>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab}>
        {/* 总览分析 */}
        <TabPane 
          tab={
            <span>
              <BarChartOutlined />
              总览分析
            </span>
          } 
          key="overview"
        >
          {/* 核心指标 */}
          <Row gutter={16} style={{ marginBottom: '24px' }}>
            {overviewStats.map((stat, index) => (
              <Col span={6} key={index}>
                <Card>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <div>
                      <Text type="secondary" style={{ fontSize: '14px' }}>{stat.title}</Text>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginTop: '8px' }}>
                        <span style={{ fontSize: '24px', fontWeight: 'bold', color: stat.color }}>
                          {stat.value}
                        </span>
                        {stat.suffix && (
                          <span style={{ fontSize: '16px', color: stat.color }}>{stat.suffix}</span>
                        )}
                      </div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', marginTop: '4px' }}>
                        {stat.trend === 'up' ? (
                          <RiseOutlined style={{ color: '#52c41a' }} />
                        ) : (
                          <FallOutlined style={{ color: '#f5222d' }} />
                        )}
                        <Text style={{ 
                          fontSize: '12px', 
                          color: stat.trend === 'up' ? '#52c41a' : '#f5222d' 
                        }}>
                          {Math.abs(stat.change)}% 相比上期
                        </Text>
                      </div>
                    </div>
                    <div style={{ 
                      fontSize: '24px', 
                      color: stat.color,
                      opacity: 0.6
                    }}>
                      {stat.icon}
                    </div>
                  </div>
                </Card>
              </Col>
            ))}
          </Row>

          {/* 招聘漏斗分析 */}
          <Row gutter={16} style={{ marginBottom: '24px' }}>
            <Col span={16}>
              <Card title="招聘漏斗分析">
                <div style={{ padding: '20px 0' }}>
                  {funnelData.map((item, index) => (
                    <div key={index} style={{ marginBottom: '16px' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                        <Text strong>{item.stage}</Text>
                        <div>
                          <Text strong style={{ color: item.color }}>{item.count}</Text>
                          <Text type="secondary" style={{ marginLeft: '8px' }}>({item.rate}%)</Text>
                        </div>
                      </div>
                      <Progress
                        percent={item.rate}
                        strokeColor={item.color}
                        showInfo={false}
                        strokeWidth={12}
                      />
                    </div>
                  ))}
                </div>
              </Card>
            </Col>
            <Col span={8}>
              <Card title="招聘来源分析">
                <div style={{ padding: '10px 0' }}>
                  {sourceData.map((item, index) => (
                    <div key={index} style={{ 
                      display: 'flex', 
                      justifyContent: 'space-between', 
                      alignItems: 'center',
                      marginBottom: '12px',
                      padding: '8px',
                      borderRadius: '6px',
                      background: `${item.color}10`
                    }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <div style={{ 
                          width: '8px', 
                          height: '8px', 
                          borderRadius: '50%', 
                          background: item.color 
                        }} />
                        <Text>{item.source}</Text>
                      </div>
                      <div>
                        <Text strong style={{ color: item.color }}>{item.count}</Text>
                        <Text type="secondary" style={{ marginLeft: '4px', fontSize: '12px' }}>
                          ({item.rate}%)
                        </Text>
                      </div>
                    </div>
                  ))}
                </div>
              </Card>
            </Col>
          </Row>

          {/* 月度趋势 */}
          <Card title="月度招聘趋势">
            <div style={{ 
              display: 'flex', 
              justifyContent: 'space-around', 
              alignItems: 'end', 
              height: '200px',
              padding: '20px 0'
            }}>
              {monthlyTrend.map((data, index) => (
                <div key={index} style={{ textAlign: 'center', position: 'relative' }}>
                  {/* 应聘柱状图 */}
                  <div style={{ display: 'flex', alignItems: 'end', gap: '4px', marginBottom: '8px' }}>
                    <Tooltip title={`应聘: ${data.applications}`}>
                      <div style={{
                        width: '20px',
                        height: `${data.applications * 0.5}px`,
                        background: 'linear-gradient(135deg, #1890ff, #722ed1)',
                        borderRadius: '2px'
                      }} />
                    </Tooltip>
                    <Tooltip title={`面试: ${data.interviews}`}>
                      <div style={{
                        width: '20px',
                        height: `${data.interviews * 0.8}px`,
                        background: 'linear-gradient(135deg, #52c41a, #13c2c2)',
                        borderRadius: '2px'
                      }} />
                    </Tooltip>
                    <Tooltip title={`入职: ${data.hires}`}>
                      <div style={{
                        width: '20px',
                        height: `${data.hires * 3}px`,
                        background: 'linear-gradient(135deg, #faad14, #f5222d)',
                        borderRadius: '2px'
                      }} />
                    </Tooltip>
                  </div>
                  <Text style={{ fontSize: '12px' }}>{data.month}</Text>
                </div>
              ))}
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '24px', marginTop: '16px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <div style={{ width: '12px', height: '12px', background: '#1890ff', borderRadius: '2px' }} />
                <Text style={{ fontSize: '12px' }}>应聘</Text>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <div style={{ width: '12px', height: '12px', background: '#52c41a', borderRadius: '2px' }} />
                <Text style={{ fontSize: '12px' }}>面试</Text>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <div style={{ width: '12px', height: '12px', background: '#faad14', borderRadius: '2px' }} />
                <Text style={{ fontSize: '12px' }}>入职</Text>
              </div>
            </div>
          </Card>
        </TabPane>

        {/* 部门分析 */}
        <TabPane 
          tab={
            <span>
              <TeamOutlined />
              部门分析
            </span>
          } 
          key="department"
        >
          <Row gutter={16}>
            <Col span={16}>
              <Card title="部门招聘完成情况">
                <Table
                  dataSource={departmentData}
                  rowKey="department"
                  pagination={false}
                  columns={[
                    {
                      title: '部门',
                      dataIndex: 'department',
                      key: 'department',
                      render: (text: string) => <Text strong>{text}</Text>
                    },
                    {
                      title: '招聘目标',
                      dataIndex: 'target',
                      key: 'target',
                      render: (value: number) => <Text>{value}人</Text>
                    },
                    {
                      title: '已完成',
                      dataIndex: 'completed',
                      key: 'completed',
                      render: (value: number) => <Text style={{ color: '#52c41a' }}>{value}人</Text>
                    },
                    {
                      title: '完成率',
                      key: 'progress',
                      render: (record: any) => (
                        <div style={{ width: '150px' }}>
                          <Progress 
                            percent={record.rate} 
                            size="small"
                            strokeColor={record.rate >= 80 ? '#52c41a' : record.rate >= 60 ? '#faad14' : '#f5222d'}
                          />
                        </div>
                      )
                    },
                    {
                      title: '状态',
                      key: 'status',
                      render: (record: any) => {
                        if (record.rate >= 80) {
                          return <Tag color="success">优秀</Tag>;
                        } else if (record.rate >= 60) {
                          return <Tag color="warning">良好</Tag>;
                        } else {
                          return <Tag color="error">需改进</Tag>;
                        }
                      }
                    }
                  ]}
                />
              </Card>
            </Col>
            <Col span={8}>
              <Card title="部门招聘排行">
                <List
                  dataSource={departmentData.sort((a, b) => b.completed - a.completed)}
                  renderItem={(item, index) => (
                    <List.Item>
                      <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
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
                          fontWeight: 'bold',
                          marginRight: '12px'
                        }}>
                          {index + 1}
                        </div>
                        <div style={{ flex: 1 }}>
                          <Text>{item.department}</Text>
                        </div>
                        <Text strong style={{ color: '#52c41a' }}>{item.completed}人</Text>
                      </div>
                    </List.Item>
                  )}
                />
              </Card>
            </Col>
          </Row>
        </TabPane>

        {/* 职位分析 */}
        <TabPane 
          tab={
            <span>
              <FileTextOutlined />
              职位分析
            </span>
          } 
          key="position"
        >
          <Card title="热门职位排行">
            <Table
              dataSource={popularJobs}
              rowKey="position"
              pagination={false}
              columns={[
                {
                  title: '排名',
                  key: 'rank',
                  width: 80,
                  render: (_, record, index) => (
                    <div style={{ 
                      width: '32px', 
                      height: '32px', 
                      borderRadius: '50%', 
                      background: index < 3 ? (index === 0 ? '#faad14' : index === 1 ? '#8c8c8c' : '#d4b106') : '#f0f0f0',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      color: index < 3 ? 'white' : '#8c8c8c',
                      fontSize: '14px',
                      fontWeight: 'bold'
                    }}>
                      {index + 1}
                    </div>
                  )
                },
                {
                  title: '职位名称',
                  dataIndex: 'position',
                  key: 'position',
                  render: (text: string) => <Text strong>{text}</Text>
                },
                {
                  title: '应聘人数',
                  dataIndex: 'applications',
                  key: 'applications',
                  render: (value: number) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <UserOutlined style={{ color: '#1890ff' }} />
                      <Text strong style={{ color: '#1890ff' }}>{value}</Text>
                    </div>
                  )
                },
                {
                  title: '平均薪资',
                  dataIndex: 'avgSalary',
                  key: 'avgSalary',
                  render: (value: string) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <DollarOutlined style={{ color: '#52c41a' }} />
                      <Text style={{ color: '#52c41a' }}>{value}</Text>
                    </div>
                  )
                },
                {
                  title: '竞争程度',
                  dataIndex: 'competition',
                  key: 'competition',
                  render: (level: string) => {
                    const color = getCompetitionColor(level);
                    const text = getCompetitionText(level);
                    const icon = level === 'high' ? <FireOutlined /> : 
                                level === 'medium' ? <ThunderboltOutlined /> : 
                                <StarOutlined />;
                    
                    return (
                      <Tag color={color} icon={icon}>
                        {text}
                      </Tag>
                    );
                  }
                }
              ]}
            />
          </Card>
        </TabPane>
      </Tabs>
    </div>
  );
};

export default DataAnalytics;
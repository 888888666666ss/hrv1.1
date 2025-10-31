import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, Space, Button, message, Modal, Row, Col, Statistic, Input, Select } from 'antd';
import { PlusOutlined, EyeOutlined, EditOutlined, DeleteOutlined, SearchOutlined, UserOutlined, FileTextOutlined, CheckCircleOutlined, ClockCircleOutlined } from '@ant-design/icons';
import axios from 'axios';
import JobFormModal from './JobFormModal';
import JobDetailModal from './JobDetailModal';

// 定义职位数据类型
interface Job {
  id: number;
  title: string;
  description: string;
  department: string;
  location: string;
  salaryMin: number;
  salaryMax: number;
  employmentType: string;
  status: string;
  recruiterName: string;
  recruiterId: number;
  requirements: string;
  benefits: string;
  applicationDeadline: string | null;
  createdAt: string;
  updatedAt: string;
}

const JobList: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [filteredJobs, setFilteredJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(false);
  const [formModalVisible, setFormModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [editingJob, setEditingJob] = useState<Job | null>(null);
  const [selectedJob, setSelectedJob] = useState<Job | null>(null);
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [typeFilter, setTypeFilter] = useState<string>('');
  const [statistics, setStatistics] = useState({
    total: 0,
    active: 0,
    paused: 0,
    closed: 0
  });

  // 获取职位列表
  const fetchJobs = async () => {
    setLoading(true);
    try {
      // 获取token
      const token = localStorage.getItem('token');
      const config = token ? {
        headers: { 'Authorization': `Bearer ${token}` }
      } : {};

      const response = await axios.get('http://localhost:9999/api/jobs', config);
      if (response.data.code === 200) {
        const jobsData = response.data.data.content || response.data.data;
        setJobs(jobsData);
        setFilteredJobs(jobsData);
        
        // 计算统计数据
        const stats = {
          total: jobsData.length,
          active: jobsData.filter((job: Job) => job.status === 'ACTIVE').length,
          paused: jobsData.filter((job: Job) => job.status === 'PAUSED').length,
          closed: jobsData.filter((job: Job) => job.status === 'CLOSED').length
        };
        setStatistics(stats);
      } else {
        message.error('获取职位列表失败');
      }
    } catch (error) {
      console.error('获取职位列表错误:', error);
      message.error('获取职位列表失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs();
  }, []);

  // 搜索和过滤功能
  useEffect(() => {
    let filtered = jobs;

    // 文本搜索
    if (searchText) {
      filtered = filtered.filter(job => 
        job.title.toLowerCase().includes(searchText.toLowerCase()) ||
        job.department.toLowerCase().includes(searchText.toLowerCase()) ||
        job.location.toLowerCase().includes(searchText.toLowerCase()) ||
        job.recruiterName.toLowerCase().includes(searchText.toLowerCase())
      );
    }

    // 状态过滤
    if (statusFilter) {
      filtered = filtered.filter(job => job.status === statusFilter);
    }

    // 类型过滤
    if (typeFilter) {
      filtered = filtered.filter(job => job.employmentType === typeFilter);
    }

    setFilteredJobs(filtered);
  }, [jobs, searchText, statusFilter, typeFilter]);

  // 表格列定义
  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: '职位标题',
      dataIndex: 'title',
      key: 'title',
      render: (text: string) => <strong>{text}</strong>,
    },
    {
      title: '部门',
      dataIndex: 'department',
      key: 'department',
    },
    {
      title: '工作地点',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: '薪资范围',
      key: 'salary',
      render: (record: Job) => (
        <span>{record.salaryMin} - {record.salaryMax} 元</span>
      ),
    },
    {
      title: '雇佣类型',
      dataIndex: 'employmentType',
      key: 'employmentType',
      render: (type: string) => {
        const typeMap: { [key: string]: { color: string; text: string } } = {
          FULL_TIME: { color: 'blue', text: '全职' },
          PART_TIME: { color: 'orange', text: '兼职' },
          CONTRACT: { color: 'purple', text: '合同工' },
          INTERN: { color: 'green', text: '实习' },
        };
        const typeInfo = typeMap[type] || { color: 'default', text: type };
        return <Tag color={typeInfo.color}>{typeInfo.text}</Tag>;
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const statusMap: { [key: string]: { color: string; text: string } } = {
          ACTIVE: { color: 'green', text: '招聘中' },
          PAUSED: { color: 'orange', text: '暂停' },
          CLOSED: { color: 'red', text: '已关闭' },
          CANCELLED: { color: 'gray', text: '已取消' },
        };
        const statusInfo = statusMap[status] || { color: 'default', text: status };
        return <Tag color={statusInfo.color}>{statusInfo.text}</Tag>;
      },
    },
    {
      title: '负责人',
      dataIndex: 'recruiterName',
      key: 'recruiterName',
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => new Date(date).toLocaleString('zh-CN'),
    },
    {
      title: '操作',
      key: 'action',
      render: (record: Job) => (
        <Space size="middle">
          <Button 
            type="link" 
            icon={<EyeOutlined />}
            onClick={() => handleView(record)}
          >
            查看
          </Button>
          <Button 
            type="link" 
            icon={<EditOutlined />}
            onClick={() => handleEdit(record)}
          >
            编辑
          </Button>
          <Button 
            type="link" 
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record)}
          >
            删除
          </Button>
        </Space>
      ),
    },
  ];

  const handleView = (job: Job) => {
    setSelectedJob(job);
    setDetailModalVisible(true);
  };

  const handleEdit = (job: Job) => {
    setEditingJob(job);
    setFormModalVisible(true);
  };

  const handleDelete = async (job: Job) => {
    Modal.confirm({
      title: '确认删除',
      content: `确定要删除职位 "${job.title}" 吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        try {
          const token = localStorage.getItem('token');
          if (!token) {
            message.error('请先登录');
            return;
          }

          const config = {
            headers: { 'Authorization': `Bearer ${token}` }
          };

          await axios.delete(`http://localhost:9999/api/jobs/${job.id}`, config);
          message.success('职位删除成功');
          fetchJobs(); // 重新加载列表
        } catch (error) {
          console.error('删除职位失败:', error);
          message.error('删除职位失败');
        }
      }
    });
  };

  const handleCreate = () => {
    setEditingJob(null);
    setFormModalVisible(true);
  };

  const handleFormSuccess = () => {
    fetchJobs(); // 重新加载列表
  };

  const handleFormCancel = () => {
    setFormModalVisible(false);
    setEditingJob(null);
  };

  const handleDetailCancel = () => {
    setDetailModalVisible(false);
    setSelectedJob(null);
  };

  return (
    <div style={{ 
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      padding: '24px'
    }}>
      {/* 统计卡片 */}
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="总职位数"
              value={statistics.total}
              prefix={<FileTextOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="招聘中"
              value={statistics.active}
              prefix={<CheckCircleOutlined />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="暂停招聘"
              value={statistics.paused}
              prefix={<ClockCircleOutlined />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="已关闭"
              value={statistics.closed}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#f5222d' }}
            />
          </Card>
        </Col>
      </Row>

      {/* 主要内容卡片 */}
      <Card 
        style={{
          boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
          borderRadius: '12px',
          background: 'rgba(255,255,255,0.95)',
          backdropFilter: 'blur(10px)'
        }}
        title={
          <div style={{ 
            fontSize: '20px', 
            fontWeight: 'bold',
            background: 'linear-gradient(45deg, #667eea, #764ba2)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent'
          }}>
            职位管理
          </div>
        }
        extra={
          <Button 
            type="primary" 
            icon={<PlusOutlined />}
            onClick={handleCreate}
            style={{
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              border: 'none',
              borderRadius: '8px',
              height: '40px',
              fontSize: '14px',
              fontWeight: '500'
            }}
          >
            新建职位
          </Button>
        }
      >
        {/* 搜索和过滤区域 */}
        <Row gutter={[16, 16]} style={{ marginBottom: '16px' }}>
          <Col xs={24} sm={12} md={8}>
            <Input
              placeholder="搜索职位标题、部门、地点、负责人"
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              style={{ borderRadius: '8px' }}
            />
          </Col>
          <Col xs={24} sm={6} md={4}>
            <Select
              placeholder="筛选状态"
              value={statusFilter}
              onChange={setStatusFilter}
              allowClear
              style={{ width: '100%', borderRadius: '8px' }}
            >
              <Select.Option value="ACTIVE">招聘中</Select.Option>
              <Select.Option value="PAUSED">暂停</Select.Option>
              <Select.Option value="CLOSED">已关闭</Select.Option>
              <Select.Option value="CANCELLED">已取消</Select.Option>
            </Select>
          </Col>
          <Col xs={24} sm={6} md={4}>
            <Select
              placeholder="筛选类型"
              value={typeFilter}
              onChange={setTypeFilter}
              allowClear
              style={{ width: '100%', borderRadius: '8px' }}
            >
              <Select.Option value="FULL_TIME">全职</Select.Option>
              <Select.Option value="PART_TIME">兼职</Select.Option>
              <Select.Option value="CONTRACT">合同工</Select.Option>
              <Select.Option value="INTERN">实习</Select.Option>
            </Select>
          </Col>
        </Row>

        {/* 职位表格 */}
        <Table
          columns={columns}
          dataSource={filteredJobs}
          rowKey="id"
          loading={loading}
          pagination={{
            total: filteredJobs.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条记录`,
            style: { marginTop: '16px' }
          }}
          scroll={{ x: 1200 }}
          style={{
            background: 'white',
            borderRadius: '8px'
          }}
        />
      </Card>

      {/* 模态框 */}
      <JobFormModal
        visible={formModalVisible}
        onCancel={handleFormCancel}
        onSuccess={handleFormSuccess}
        editingJob={editingJob}
      />

      <JobDetailModal
        visible={detailModalVisible}
        onCancel={handleDetailCancel}
        job={selectedJob}
      />
    </div>
  );
};

export default JobList;
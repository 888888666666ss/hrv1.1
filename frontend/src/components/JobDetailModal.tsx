import React from 'react';
import { Modal, Descriptions, Tag, Typography } from 'antd';
import dayjs from 'dayjs';

const { Paragraph } = Typography;

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

interface JobDetailModalProps {
  visible: boolean;
  onCancel: () => void;
  job: Job | null;
}

const JobDetailModal: React.FC<JobDetailModalProps> = ({ visible, onCancel, job }) => {
  if (!job) return null;

  const typeMap: { [key: string]: { color: string; text: string } } = {
    FULL_TIME: { color: 'blue', text: '全职' },
    PART_TIME: { color: 'orange', text: '兼职' },
    CONTRACT: { color: 'purple', text: '合同工' },
    INTERN: { color: 'green', text: '实习' },
  };

  const statusMap: { [key: string]: { color: string; text: string } } = {
    ACTIVE: { color: 'green', text: '招聘中' },
    PAUSED: { color: 'orange', text: '暂停' },
    CLOSED: { color: 'red', text: '已关闭' },
    CANCELLED: { color: 'gray', text: '已取消' },
  };

  const typeInfo = typeMap[job.employmentType] || { color: 'default', text: job.employmentType };
  const statusInfo = statusMap[job.status] || { color: 'default', text: job.status };

  return (
    <Modal
      title={`职位详情 - ${job.title}`}
      open={visible}
      onCancel={onCancel}
      footer={null}
      width={800}
    >
      <Descriptions column={2} bordered>
        <Descriptions.Item label="职位ID" span={1}>
          {job.id}
        </Descriptions.Item>
        <Descriptions.Item label="状态" span={1}>
          <Tag color={statusInfo.color}>{statusInfo.text}</Tag>
        </Descriptions.Item>
        
        <Descriptions.Item label="职位标题" span={2}>
          <strong style={{ fontSize: '16px' }}>{job.title}</strong>
        </Descriptions.Item>
        
        <Descriptions.Item label="部门" span={1}>
          {job.department || '-'}
        </Descriptions.Item>
        <Descriptions.Item label="工作地点" span={1}>
          {job.location || '-'}
        </Descriptions.Item>
        
        <Descriptions.Item label="薪资范围" span={1}>
          ¥{job.salaryMin.toLocaleString()} - ¥{job.salaryMax.toLocaleString()}
        </Descriptions.Item>
        <Descriptions.Item label="雇佣类型" span={1}>
          <Tag color={typeInfo.color}>{typeInfo.text}</Tag>
        </Descriptions.Item>
        
        <Descriptions.Item label="负责人" span={1}>
          {job.recruiterName}
        </Descriptions.Item>
        <Descriptions.Item label="申请截止时间" span={1}>
          {job.applicationDeadline ? dayjs(job.applicationDeadline).format('YYYY-MM-DD HH:mm') : '无限期'}
        </Descriptions.Item>
        
        <Descriptions.Item label="创建时间" span={1}>
          {dayjs(job.createdAt).format('YYYY-MM-DD HH:mm')}
        </Descriptions.Item>
        <Descriptions.Item label="最后更新" span={1}>
          {dayjs(job.updatedAt).format('YYYY-MM-DD HH:mm')}
        </Descriptions.Item>
        
        <Descriptions.Item label="职位描述" span={2}>
          <Paragraph style={{ marginBottom: 0, whiteSpace: 'pre-wrap' }}>
            {job.description || '-'}
          </Paragraph>
        </Descriptions.Item>
        
        <Descriptions.Item label="任职要求" span={2}>
          <Paragraph style={{ marginBottom: 0, whiteSpace: 'pre-wrap' }}>
            {job.requirements || '-'}
          </Paragraph>
        </Descriptions.Item>
        
        <Descriptions.Item label="福利待遇" span={2}>
          <Paragraph style={{ marginBottom: 0, whiteSpace: 'pre-wrap' }}>
            {job.benefits || '-'}
          </Paragraph>
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
};

export default JobDetailModal;
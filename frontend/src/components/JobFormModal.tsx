import React, { useState, useEffect } from 'react';
import { Modal, Form, Input, InputNumber, Select, DatePicker, message } from 'antd';
import axios from 'axios';
import dayjs from 'dayjs';

const { TextArea } = Input;
const { Option } = Select;

interface Job {
  id?: number;
  title: string;
  description: string;
  department: string;
  location: string;
  salaryMin: number;
  salaryMax: number;
  employmentType: string;
  requirements: string;
  benefits: string;
  applicationDeadline?: string | null;
}

interface JobFormModalProps {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
  editingJob?: Job | null;
}

const JobFormModal: React.FC<JobFormModalProps> = ({ 
  visible, 
  onCancel, 
  onSuccess, 
  editingJob 
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible && editingJob) {
      // 编辑模式，填充表单数据
      form.setFieldsValue({
        ...editingJob,
        applicationDeadline: editingJob.applicationDeadline ? dayjs(editingJob.applicationDeadline) : null,
      });
    } else if (visible) {
      // 新建模式，重置表单
      form.resetFields();
    }
  }, [visible, editingJob, form]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      setLoading(true);

      // 格式化数据
      const jobData = {
        ...values,
        applicationDeadline: values.applicationDeadline ? values.applicationDeadline.toISOString() : null,
      };

      // 获取token
      const token = localStorage.getItem('token');
      if (!token) {
        message.error('请先登录');
        return;
      }

      const config = {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      };

      if (editingJob?.id) {
        // 编辑职位
        await axios.put(`http://localhost:9999/api/jobs/${editingJob.id}`, jobData, config);
        message.success('职位更新成功');
      } else {
        // 创建职位
        await axios.post('http://localhost:9999/api/jobs', jobData, config);
        message.success('职位创建成功');
      }

      onSuccess();
      onCancel();
    } catch (error: any) {
      console.error('提交失败:', error);
      if (error.response?.data?.error) {
        message.error(error.response.data.error);
      } else {
        message.error('操作失败，请重试');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal
      title={editingJob ? '编辑职位' : '创建职位'}
      open={visible}
      onCancel={onCancel}
      onOk={handleSubmit}
      confirmLoading={loading}
      width={800}
      destroyOnClose
    >
      <Form
        form={form}
        layout="vertical"
        requiredMark={false}
      >
        <Form.Item
          name="title"
          label="职位标题"
          rules={[{ required: true, message: '请输入职位标题' }]}
        >
          <Input placeholder="请输入职位标题" />
        </Form.Item>

        <Form.Item
          name="description"
          label="职位描述"
          rules={[{ required: true, message: '请输入职位描述' }]}
        >
          <TextArea rows={3} placeholder="请输入职位描述" />
        </Form.Item>

        <div style={{ display: 'flex', gap: '16px' }}>
          <Form.Item
            name="department"
            label="部门"
            style={{ flex: 1 }}
          >
            <Input placeholder="请输入部门" />
          </Form.Item>

          <Form.Item
            name="location"
            label="工作地点"
            style={{ flex: 1 }}
          >
            <Input placeholder="请输入工作地点" />
          </Form.Item>
        </div>

        <div style={{ display: 'flex', gap: '16px' }}>
          <Form.Item
            name="salaryMin"
            label="最低薪资"
            rules={[{ required: true, message: '请输入最低薪资' }]}
            style={{ flex: 1 }}
          >
            <InputNumber
              min={0}
              style={{ width: '100%' }}
              placeholder="请输入最低薪资"
              formatter={(value) => value ? `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',') : ''}
              parser={(value: any) => value ? parseFloat(value.replace(/¥\s?|(,*)/g, '')) || 0 : 0 as any}
            />
          </Form.Item>

          <Form.Item
            name="salaryMax"
            label="最高薪资"
            rules={[{ required: true, message: '请输入最高薪资' }]}
            style={{ flex: 1 }}
          >
            <InputNumber
              min={0}
              style={{ width: '100%' }}
              placeholder="请输入最高薪资"
              formatter={(value) => value ? `¥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',') : ''}
              parser={(value: any) => value ? parseFloat(value.replace(/¥\s?|(,*)/g, '')) || 0 : 0 as any}
            />
          </Form.Item>
        </div>

        <Form.Item
          name="employmentType"
          label="雇佣类型"
          initialValue="FULL_TIME"
        >
          <Select placeholder="请选择雇佣类型">
            <Option value="FULL_TIME">全职</Option>
            <Option value="PART_TIME">兼职</Option>
            <Option value="CONTRACT">合同工</Option>
            <Option value="INTERN">实习</Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="applicationDeadline"
          label="申请截止时间"
        >
          <DatePicker 
            style={{ width: '100%' }}
            placeholder="请选择申请截止时间"
            showTime
          />
        </Form.Item>

        <Form.Item
          name="requirements"
          label="职位要求"
        >
          <TextArea rows={3} placeholder="请输入职位要求" />
        </Form.Item>

        <Form.Item
          name="benefits"
          label="福利待遇"
        >
          <TextArea rows={2} placeholder="请输入福利待遇" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default JobFormModal;
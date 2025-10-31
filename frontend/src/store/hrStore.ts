import { create } from 'zustand';

// 数据类型定义
interface Job {
  id: string;
  title: string;
  department: string;
  location: string;
  salaryRange: string;
  status: 'active' | 'paused' | 'closed';
  priority: 'high' | 'medium' | 'low';
  description: string;
  requirements: string;
  applicants: number;
  createdAt: string;
  updatedAt: string;
}

interface Candidate {
  id: string;
  name: string;
  email: string;
  phone: string;
  jobId: string;
  jobTitle: string;
  status: 'pending' | 'screening' | 'interviewing' | 'offered' | 'hired' | 'rejected';
  aiScore: number;
  skills: string[];
  experience: string;
  education: string;
  resumeUrl?: string;
  notes: string;
  source: string;
  appliedAt: string;
}

interface Referral {
  id: string;
  referrerId: string;
  referrerName: string;
  candidateId: string;
  candidateName: string;
  jobId: string;
  jobTitle: string;
  status: 'pending' | 'approved' | 'hired' | 'rejected';
  reward: number;
  submittedAt: string;
  notes: string;
}

// Store 接口定义
interface HRStore {
  // 数据状态
  jobs: Job[];
  candidates: Candidate[];
  referrals: Referral[];
  loading: boolean;
  
  // 筛选状态
  jobFilters: {
    search: string;
    department: string;
    status: string;
  };
  candidateFilters: {
    search: string;
    status: string;
    jobTitle: string;
  };
  
  // Actions
  setLoading: (loading: boolean) => void;
  
  // Job Actions
  addJob: (job: Omit<Job, 'id' | 'createdAt' | 'updatedAt'>) => void;
  updateJob: (id: string, updates: Partial<Job>) => void;
  deleteJob: (id: string) => void;
  setJobFilters: (filters: Partial<HRStore['jobFilters']>) => void;
  getFilteredJobs: () => Job[];
  
  // Candidate Actions
  addCandidate: (candidate: Omit<Candidate, 'id' | 'appliedAt'>) => void;
  updateCandidate: (id: string, updates: Partial<Candidate>) => void;
  deleteCandidate: (id: string) => void;
  setCandidateFilters: (filters: Partial<HRStore['candidateFilters']>) => void;
  getFilteredCandidates: () => Candidate[];
  
  // Referral Actions
  addReferral: (referral: Omit<Referral, 'id' | 'submittedAt'>) => void;
  updateReferral: (id: string, updates: Partial<Referral>) => void;
  
  // Statistics
  getStats: () => {
    totalJobs: number;
    activeJobs: number;
    totalCandidates: number;
    pendingCandidates: number;
    interviewingCandidates: number;
    avgAIScore: number;
    totalReferrals: number;
    successfulReferrals: number;
  };
}

// 创建Zustand Store
export const useHRStore = create<HRStore>((set, get) => ({
  // 初始数据
  jobs: [
    {
      id: '1',
      title: '高级前端开发工程师',
      department: '技术部',
      location: '北京',
      salaryRange: '20K-35K',
      status: 'active',
      priority: 'high',
      description: '负责公司前端产品开发，参与架构设计和技术选型',
      requirements: '5年以上前端开发经验，熟练掌握React、TypeScript等技术栈',
      applicants: 128,
      createdAt: '2024-10-25',
      updatedAt: '2024-10-30'
    },
    {
      id: '2',
      title: 'AI算法工程师',
      department: '技术部',
      location: '上海',
      salaryRange: '35K-60K',
      status: 'active',
      priority: 'high',
      description: '负责AI算法研发，机器学习模型设计与优化',
      requirements: '计算机相关专业硕士以上，熟悉深度学习框架',
      applicants: 89,
      createdAt: '2024-10-20',
      updatedAt: '2024-10-28'
    },
    {
      id: '3',
      title: '产品经理',
      department: '产品部',
      location: '深圳',
      salaryRange: '25K-40K',
      status: 'paused',
      priority: 'medium',
      description: '负责产品规划设计，用户需求分析',
      requirements: '3年以上产品经验，有B端产品经验',
      applicants: 56,
      createdAt: '2024-10-15',
      updatedAt: '2024-10-25'
    }
  ],
  
  candidates: [
    {
      id: '1',
      name: '张小明',
      email: 'zhang.xiaoming@email.com',
      phone: '138****1234',
      jobId: '1',
      jobTitle: '高级前端开发工程师',
      status: 'interviewing',
      aiScore: 92,
      skills: ['React', 'TypeScript', 'Node.js', 'Vue'],
      experience: '5年',
      education: '本科',
      notes: '技术能力强，有大厂经验，沟通能力良好',
      source: '智联招聘',
      appliedAt: '2024-10-28'
    },
    {
      id: '2',
      name: '李小红',
      email: 'li.xiaohong@email.com',
      phone: '139****5678',
      jobId: '2',
      jobTitle: 'AI算法工程师',
      status: 'offered',
      aiScore: 88,
      skills: ['Python', 'TensorFlow', '机器学习', '深度学习'],
      experience: '3年',
      education: '硕士',
      notes: '算法基础扎实，学习能力强',
      source: 'Boss直聘',
      appliedAt: '2024-10-25'
    },
    {
      id: '3',
      name: '王小强',
      email: 'wang.xiaoqiang@email.com',
      phone: '137****9012',
      jobId: '3',
      jobTitle: '产品经理',
      status: 'rejected',
      aiScore: 65,
      skills: ['产品设计', '用户调研', 'Axure', 'Figma'],
      experience: '2年',
      education: '本科',
      notes: '经验略显不足，但思路清晰',
      source: '拉勾网',
      appliedAt: '2024-10-22'
    }
  ],
  
  referrals: [
    {
      id: '1',
      referrerId: 'E001',
      referrerName: '张三',
      candidateId: '4',
      candidateName: '赵六',
      jobId: '1',
      jobTitle: '高级前端开发工程师',
      status: 'pending',
      reward: 5000,
      submittedAt: '2024-10-28',
      notes: '前同事推荐，技术能力很强'
    }
  ],
  
  loading: false,
  
  jobFilters: {
    search: '',
    department: '',
    status: ''
  },
  
  candidateFilters: {
    search: '',
    status: '',
    jobTitle: ''
  },
  
  // Actions
  setLoading: (loading) => set({ loading }),
  
  // Job Actions
  addJob: (jobData) => set((state) => ({
    jobs: [...state.jobs, {
      ...jobData,
      id: Date.now().toString(),
      createdAt: new Date().toISOString().split('T')[0],
      updatedAt: new Date().toISOString().split('T')[0],
      applicants: 0
    }]
  })),
  
  updateJob: (id, updates) => set((state) => ({
    jobs: state.jobs.map(job => 
      job.id === id 
        ? { ...job, ...updates, updatedAt: new Date().toISOString().split('T')[0] }
        : job
    )
  })),
  
  deleteJob: (id) => set((state) => ({
    jobs: state.jobs.filter(job => job.id !== id)
  })),
  
  setJobFilters: (filters) => set((state) => ({
    jobFilters: { ...state.jobFilters, ...filters }
  })),
  
  getFilteredJobs: () => {
    const { jobs, jobFilters } = get();
    return jobs.filter(job => {
      const matchesSearch = !jobFilters.search || 
        job.title.toLowerCase().includes(jobFilters.search.toLowerCase()) ||
        job.department.toLowerCase().includes(jobFilters.search.toLowerCase());
      
      const matchesDepartment = !jobFilters.department || job.department === jobFilters.department;
      const matchesStatus = !jobFilters.status || job.status === jobFilters.status;
      
      return matchesSearch && matchesDepartment && matchesStatus;
    });
  },
  
  // Candidate Actions
  addCandidate: (candidateData) => set((state) => ({
    candidates: [...state.candidates, {
      ...candidateData,
      id: Date.now().toString(),
      appliedAt: new Date().toISOString().split('T')[0]
    }]
  })),
  
  updateCandidate: (id, updates) => set((state) => ({
    candidates: state.candidates.map(candidate => 
      candidate.id === id ? { ...candidate, ...updates } : candidate
    )
  })),
  
  deleteCandidate: (id) => set((state) => ({
    candidates: state.candidates.filter(candidate => candidate.id !== id)
  })),
  
  setCandidateFilters: (filters) => set((state) => ({
    candidateFilters: { ...state.candidateFilters, ...filters }
  })),
  
  getFilteredCandidates: () => {
    const { candidates, candidateFilters } = get();
    return candidates.filter(candidate => {
      const matchesSearch = !candidateFilters.search || 
        candidate.name.toLowerCase().includes(candidateFilters.search.toLowerCase()) ||
        candidate.email.toLowerCase().includes(candidateFilters.search.toLowerCase());
      
      const matchesStatus = !candidateFilters.status || candidate.status === candidateFilters.status;
      const matchesJobTitle = !candidateFilters.jobTitle || candidate.jobTitle === candidateFilters.jobTitle;
      
      return matchesSearch && matchesStatus && matchesJobTitle;
    });
  },
  
  // Referral Actions
  addReferral: (referralData) => set((state) => ({
    referrals: [...state.referrals, {
      ...referralData,
      id: Date.now().toString(),
      submittedAt: new Date().toISOString().split('T')[0]
    }]
  })),
  
  updateReferral: (id, updates) => set((state) => ({
    referrals: state.referrals.map(referral => 
      referral.id === id ? { ...referral, ...updates } : referral
    )
  })),
  
  // Statistics
  getStats: () => {
    const { jobs, candidates, referrals } = get();
    
    return {
      totalJobs: jobs.length,
      activeJobs: jobs.filter(job => job.status === 'active').length,
      totalCandidates: candidates.length,
      pendingCandidates: candidates.filter(c => c.status === 'pending' || c.status === 'screening').length,
      interviewingCandidates: candidates.filter(c => c.status === 'interviewing').length,
      avgAIScore: Math.round(candidates.reduce((sum, c) => sum + c.aiScore, 0) / candidates.length),
      totalReferrals: referrals.length,
      successfulReferrals: referrals.filter(r => r.status === 'hired').length
    };
  }
}));
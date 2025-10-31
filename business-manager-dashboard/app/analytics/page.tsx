import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import {
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  CartesianGrid,
  ResponsiveContainer,
} from "recharts"

export default function AnalyticsPage() {
  // Recruitment channels data
  const channelData = [
    { name: "校招网站", value: 45, color: "#8b5cf6" },
    { name: "社交媒体", value: 30, color: "#06b6d4" },
    { name: "其他", value: 25, color: "#ec4899" },
  ]

  // Achievement trend data
  const achievementData = [
    { month: "1月", value: 55 },
    { month: "2月", value: 58 },
    { month: "3月", value: 62 },
    { month: "4月", value: 60 },
    { month: "5月", value: 65 },
    { month: "6月", value: 68 },
  ]

  // Quality metrics data
  const qualityData = [
    { month: "1月", value: 35 },
    { month: "2月", value: 38 },
    { month: "3月", value: 42 },
    { month: "4月", value: 45 },
    { month: "5月", value: 48 },
  ]

  // Internal referral data
  const referralData = [
    { category: "人员推荐量", value: 85 },
    { category: "人员到岗数", value: 65 },
  ]

  // Headcount execution data
  const executionData = [
    { month: "1月", value: 45 },
    { month: "2月", value: 52 },
    { month: "3月", value: 58 },
    { month: "4月", value: 62 },
    { month: "5月", value: 65 },
  ]

  // Offer data
  const offerData = [
    { month: "1月", value: 250 },
    { month: "2月", value: 270 },
    { month: "3月", value: 290 },
    { month: "4月", value: 310 },
    { month: "5月", value: 340 },
  ]

  // Turnover data
  const turnoverData = [
    { month: "1月", value: 12 },
    { month: "2月", value: 15 },
    { month: "3月", value: 18 },
    { month: "4月", value: 14 },
    { month: "5月", value: 10 },
  ]

  // Confirmed employees data
  const confirmedData = [
    { month: "1月", value: 250 },
    { month: "2月", value: 270 },
    { month: "3月", value: 290 },
    { month: "4月", value: 310 },
    { month: "5月", value: 326 },
  ]

  // Headcount data
  const headcountData = [
    { month: "1月", value: 180 },
    { month: "2月", value: 195 },
    { month: "3月", value: 210 },
    { month: "4月", value: 225 },
    { month: "5月", value: 240 },
  ]

  // Funnel data
  const funnelData = [
    { stage: "简历投递", value: 10000, color: "#06b6d4" },
    { stage: "简历筛选", value: 5000, color: "#3b82f6" },
    { stage: "初试", value: 2000, color: "#8b5cf6" },
    { stage: "复试", value: 800, color: "#a855f7" },
    { stage: "Offer", value: 340, color: "#c084fc" },
    { stage: "入职", value: 120, color: "#e9d5ff" },
  ]

  // Cycle data
  const cycleData = [
    { month: "1月", interview: 65, probation: 220 },
    { month: "2月", interview: 68, probation: 225 },
    { month: "3月", interview: 70, probation: 230 },
  ]

  // Onboarding trend data
  const onboardingTrendData = [
    { month: "1月", value: 45 },
    { month: "2月", value: 48 },
    { month: "3月", value: 50 },
    { month: "4月", value: 52 },
    { month: "5月", value: 50 },
  ]

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      {/* Header */}
      <div className="mb-6 flex items-center gap-2">
        <div className="flex gap-1.5">
          <div className="h-3 w-3 rounded-full bg-red-500" />
          <div className="h-3 w-3 rounded-full bg-yellow-500" />
          <div className="h-3 w-3 rounded-full bg-green-500" />
        </div>
        <h1 className="ml-4 text-xl font-medium text-gray-900">招聘数据分析</h1>
      </div>

      {/* Dashboard Grid */}
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
        {/* Recruitment Channels */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">招聘渠道</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">人事服务部数据</div>
              <div className="text-2xl font-semibold">招聘网站</div>
              <div className="mt-1 flex gap-4 text-sm">
                <span className="text-gray-600">累计入职人数</span>
                <span className="font-medium">1500</span>
              </div>
              <div className="flex gap-4 text-sm">
                <span className="text-gray-600">占比</span>
                <span className="font-medium">45%</span>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={150}>
              <PieChart>
                <Pie
                  data={channelData}
                  cx="50%"
                  cy="50%"
                  innerRadius={40}
                  outerRadius={60}
                  paddingAngle={2}
                  dataKey="value"
                >
                  {channelData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Recruitment Achievement */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">招聘达成</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">招聘完成率</div>
              <div className="text-3xl font-semibold">65%</div>
              <div className="mt-1 flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">累计入职人数</span>
                  <span className="ml-2 font-medium">1300</span>
                </div>
              </div>
              <div className="flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">累计需求人数</span>
                  <span className="ml-2 font-medium">2000</span>
                </div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <LineChart data={achievementData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Line type="monotone" dataKey="value" stroke="#3b82f6" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Recruitment Quality */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">招聘质量</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">累计已邀约人数</div>
              <div className="text-3xl font-semibold">48</div>
              <div className="mt-1 text-sm text-gray-600">
                <span>占比</span>
                <span className="ml-2 font-medium">20%</span>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={qualityData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Internal Referrals */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">内部推荐</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">招聘完成率</div>
              <div className="text-3xl font-semibold">65%</div>
              <div className="mt-1 flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">人员推荐量</span>
                  <span className="ml-2 font-medium">240</span>
                </div>
              </div>
              <div className="flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">人员到岗数</span>
                  <span className="ml-2 font-medium">156</span>
                </div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={referralData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="category" tick={{ fontSize: 11 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Headcount Execution */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">编制执行率</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-3xl font-semibold">65%</div>
              <div className="mt-1 flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">编制数</span>
                  <span className="ml-2 font-medium">3000</span>
                </div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={executionData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#f97316" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Offers Issued */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">发放Offer数</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-3xl font-semibold">340</div>
              <div className="mt-1 flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">到岗offer</span>
                  <span className="ml-2 font-medium">80</span>
                </div>
              </div>
              <div className="flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">未到岗offer</span>
                  <span className="ml-2 font-medium">260</span>
                </div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={offerData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#f97316" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Probation Turnover */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">试用期员工流失率</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">试用期员工流失率</div>
            </div>
            <ResponsiveContainer width="100%" height={150}>
              <BarChart data={turnoverData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Confirmed Employees */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">累计转正人数</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-3xl font-semibold">326</div>
              <div className="mt-1 text-sm">
                <span className="text-gray-600">转正占比</span>
                <span className="ml-2 font-medium">80%</span>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={confirmedData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Headcount */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">提头</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">人事服务部数据</div>
              <div className="mt-1 flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">人员推荐量</span>
                  <span className="ml-2 font-medium">240</span>
                </div>
              </div>
              <div className="flex gap-4 text-sm">
                <div>
                  <span className="text-gray-600">人员到岗数</span>
                  <span className="ml-2 font-medium">156</span>
                </div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={headcountData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Onboarding Conversion */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">入职转化率</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-3xl font-semibold">0.12%</div>
              <div className="mt-1 text-sm text-gray-600">入职人数 / 简历投递数</div>
            </div>
            <div className="space-y-2">
              {funnelData.map((item, index) => (
                <div key={index} className="flex items-center gap-3">
                  <div className="w-20 text-xs text-gray-600">{item.stage}</div>
                  <div className="flex-1">
                    <div
                      className="h-6 rounded"
                      style={{
                        backgroundColor: item.color,
                        width: `${(item.value / funnelData[0].value) * 100}%`,
                      }}
                    />
                  </div>
                  <div className="w-16 text-right text-xs font-medium">{item.value}</div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Average Recruitment Cycle */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">平均招聘周期</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4 space-y-2">
              <div>
                <div className="text-sm text-gray-600">平均面试周期</div>
                <div className="text-2xl font-semibold">70天</div>
              </div>
              <div>
                <div className="text-sm text-gray-600">试用期通过周期</div>
                <div className="text-2xl font-semibold">230</div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={cycleData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="interview" fill="#06b6d4" radius={[4, 4, 0, 0]} />
                <Bar dataKey="probation" fill="#0891b2" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Onboarding Trend Analysis */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base font-medium">到岗周期趋势分析</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="mb-4">
              <div className="text-sm text-gray-600">到岗周期</div>
              <div className="text-3xl font-semibold">50%</div>
            </div>
            <ResponsiveContainer width="100%" height={120}>
              <BarChart data={onboardingTrendData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="month" tick={{ fontSize: 12 }} />
                <Bar dataKey="value" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
